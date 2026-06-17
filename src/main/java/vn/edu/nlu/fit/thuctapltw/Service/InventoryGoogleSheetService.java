package vn.edu.nlu.fit.thuctapltw.Service;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import vn.edu.nlu.fit.thuctapltw.model.InventoryItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InventoryGoogleSheetService {
    private static final String SHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";
    private static final String DEFAULT_SHEET_NAME = "TonKho";
    private static final String DEFAULT_IMPORT_SHEET_NAME = "NhapKho";
    private static final String DEFAULT_IMPORT_RESULT_SHEET_NAME = "KetQuaNhap";
    private static final String DEFAULT_SPREADSHEET_ID = "1H0is6rV0zZ-d3IBhpakRK9YdeJXY4L-xY8ajBBthNnE";
    private static final int LOW_STOCK_THRESHOLD = 10;

    private final InventoryService inventoryService = new InventoryService();
    private final InventoryTransactionService transactionService = new InventoryTransactionService();
    private final Gson gson = new Gson();

    public int updateInventoryReport(ServletContext context) throws IOException {
        GoogleSheetConfig config = loadConfig(context);
        String accessToken = getAccessToken(context);
        ensureSheetExists(config.spreadsheetId(), config.sheetName(), accessToken);

        List<InventoryItem> items = inventoryService.getInventoryItemsForGoogleSheet();
        List<List<Object>> values = buildSheetValues(items);

        String clearRange = quoteSheetName(config.sheetName()) + "!A:Q";
        String updateRange = quoteSheetName(config.sheetName()) + "!A1";

        clearValues(config.spreadsheetId(), clearRange, accessToken);
        updateValues(config.spreadsheetId(), updateRange, values, accessToken);

        return items.size();
    }

    public ImportSyncResult importInventoryFromSheet(ServletContext context, Integer createdBy) throws IOException {
        GoogleSheetConfig config = loadConfig(context);
        GoogleSheetImportConfig importConfig = loadImportConfig(context);
        String accessToken = getAccessToken(context);

        ensureSheetExists(config.spreadsheetId(), importConfig.importSheetName(), accessToken);
        ensureSheetExists(config.spreadsheetId(), importConfig.resultSheetName(), accessToken);
        ensureImportSheetHeader(config.spreadsheetId(), importConfig.importSheetName(), accessToken);

        String readRange = quoteSheetName(importConfig.importSheetName()) + "!A2:E2000";
        List<List<Object>> sheetRows = readValues(config.spreadsheetId(), readRange, accessToken);

        List<ImportRow> validRows = new ArrayList<>();
        List<ImportRowResult> resultRows = new ArrayList<>();

        int totalDataRows = 0;
        int sheetRowNumber = 2;
        for (List<Object> sheetRow : sheetRows) {
            if (isBlankRow(sheetRow)) {
                sheetRowNumber++;
                continue;
            }

            totalDataRows++;
            ImportRow row = parseImportRow(sheetRow, sheetRowNumber);
            String validationError = validateImportRow(row);

            if (validationError == null) {
                validRows.add(row);
            } else {
                resultRows.add(ImportRowResult.error(row, validationError));
            }
            sheetRowNumber++;
        }

        if (totalDataRows == 0) {
            writeImportResult(config.spreadsheetId(), importConfig.resultSheetName(), accessToken,
                    List.of(ImportRowResult.info("Chưa có dữ liệu trong tab " + importConfig.importSheetName()
                            + ". Hãy nhập từ dòng 2 rồi bấm đồng bộ lại.")));
            return new ImportSyncResult(0, 0, 0, 0,
                    "Đã tạo/kiểm tra tab " + importConfig.importSheetName()
                            + ". Hãy nhập dữ liệu từ dòng 2 rồi bấm đồng bộ lại.");
        }

        if (!validRows.isEmpty()) {
            List<Integer> variantIds = validRows.stream().map(ImportRow::variantId).toList();
            List<Integer> quantities = validRows.stream().map(ImportRow::quantity).toList();
            List<BigDecimal> unitCosts = validRows.stream().map(ImportRow::unitCost).toList();

            String supplierName = buildSupplierName(validRows);
            String note = "Nhập kho hàng loạt từ Google Sheet - "
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            int transactionId = transactionService.createTransaction(
                    "IMPORT",
                    supplierName,
                    note,
                    createdBy,
                    variantIds,
                    quantities,
                    unitCosts
            );

            String status = transactionService.updateStatusIfPending(transactionId, "COMPLETED");
            if ("SUCCESS".equals(status)) {
                for (ImportRow row : validRows) {
                    resultRows.add(ImportRowResult.success(row, "Đã nhập kho thành công. Mã phiếu: #" + transactionId));
                }
            } else {
                for (ImportRow row : validRows) {
                    resultRows.add(ImportRowResult.error(row,
                            "Không thể hoàn tất phiếu nhập. Mã lỗi: " + status + ". Kiểm tra lại dữ liệu và lịch sử nhập xuất."));
                }
                validRows.clear();
            }
        }

        writeImportResult(config.spreadsheetId(), importConfig.resultSheetName(), accessToken, resultRows);

        int successRows = validRows.size();
        int errorRows = totalDataRows - successRows;
        String message = "Đồng bộ nhập kho từ Google Sheet xong: "
                + successRows + " dòng thành công, " + errorRows + " dòng lỗi.";

        return new ImportSyncResult(totalDataRows, successRows, errorRows, resultRows.size(), message);
    }

    public boolean isConfigured(ServletContext context) {
        try {
            loadConfig(context);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public String getSheetUrl(ServletContext context) {
        String configuredUrl = firstNotBlank(
                getContextParam(context, "googleInventorySheetUrl"),
                System.getenv("GOOGLE_INVENTORY_SHEET_URL")
        );
        if (configuredUrl != null) {
            return configuredUrl;
        }

        try {
            GoogleSheetConfig config = loadConfig(context);
            return "https://docs.google.com/spreadsheets/d/" + config.spreadsheetId() + "/edit";
        } catch (IllegalStateException e) {
            return "";
        }
    }

    public String getImportSheetUrl(ServletContext context) {
        String configuredUrl = firstNotBlank(
                getContextParam(context, "googleInventoryImportSheetUrl"),
                System.getenv("GOOGLE_INVENTORY_IMPORT_SHEET_URL")
        );
        if (configuredUrl != null) {
            return configuredUrl;
        }
        return getSheetUrl(context);
    }

    private List<List<Object>> buildSheetValues(List<InventoryItem> items) {
        List<List<Object>> rows = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        rows.add(List.of("BÁO CÁO TỒN KHO SẢN PHẨM"));
        rows.add(List.of("Ngày báo cáo:", LocalDate.now().format(dateFormatter)));
        rows.add(List.of("Lần cập nhật gần nhất:", LocalDateTime.now().format(dateTimeFormatter)));
        rows.add(List.of());
        rows.add(List.of(
                "STT",
                "Mã sản phẩm",
                "Mã biến thể",
                "Tên sản phẩm",
                "Danh mục",
                "Màu",
                "Size",
                "Giá bán",
                "Giá nhập gần nhất",
                "Tồn kho hiện tại",
                "Tồn theo lô",
                "Cảnh báo",
                "Đã bán hiện tại",
                "Tổng nhập kho",
                "Tổng xuất kho",
                "Trạng thái bán",
                "Cập nhật kho gần nhất"
        ));

        int index = 1;
        for (InventoryItem item : items) {
            BigDecimal latestUnitCost = item.getLatestUnitCost() == null ? BigDecimal.ZERO : item.getLatestUnitCost();
            double sellingPrice = item.getSalePrice() > 0 ? item.getSalePrice() : item.getPrice();

            rows.add(List.of(
                    index++,
                    item.getProductId(),
                    item.getVariantId(),
                    safe(item.getProductName()),
                    safe(item.getCategoryName()),
                    safe(item.getColorName()),
                    safe(item.getSizeName()),
                    sellingPrice,
                    latestUnitCost,
                    item.getStock(),
                    item.getRemainingBatchQuantity(),
                    getStockWarning(item.getStock()),
                    item.getSoldQuantity(),
                    item.getImportedQuantity(),
                    item.getExportedQuantity(),
                    safe(item.getProductStatus()),
                    getLastUpdateText(item)
            ));
        }

        return rows;
    }

    private String getStockWarning(int stock) {
        if (stock <= 0) {
            return "Đã hết hàng";
        }
        if (stock <= LOW_STOCK_THRESHOLD) {
            return "Sắp hết hàng";
        }
        return "Còn hàng";
    }

    private String getLastUpdateText(InventoryItem item) {
        String importDate = safe(item.getLatestImportDateText());
        String batchCode = safe(item.getLatestBatchCode());

        if (importDate.isBlank() && batchCode.isBlank()) {
            return "";
        }
        if (batchCode.isBlank()) {
            return importDate;
        }
        if (importDate.isBlank()) {
            return batchCode;
        }
        return importDate + " - " + batchCode;
    }

    private void ensureImportSheetHeader(String spreadsheetId, String importSheetName, String accessToken) throws IOException {
        String headerRange = quoteSheetName(importSheetName) + "!A1:E1";
        List<List<Object>> values = readValues(spreadsheetId, headerRange, accessToken);

        if (!values.isEmpty() && !values.get(0).isEmpty()
                && "Mã biến thể".equalsIgnoreCase(cell(values.get(0), 0))) {
            return;
        }

        List<List<Object>> header = List.of(List.of(
                "Mã biến thể",
                "Số lượng nhập",
                "Giá nhập",
                "Mã nhà cung cấp",
                "Ghi chú"
        ));
        updateValues(spreadsheetId, quoteSheetName(importSheetName) + "!A1", header, accessToken);
    }

    private ImportRow parseImportRow(List<Object> row, int sheetRowNumber) {
        return new ImportRow(
                sheetRowNumber,
                cell(row, 0),
                cell(row, 1),
                cell(row, 2),
                cell(row, 3),
                cell(row, 4),
                parseInteger(cell(row, 0)),
                parseInteger(cell(row, 1)),
                parseMoney(cell(row, 2))
        );
    }

    private String validateImportRow(ImportRow row) {
        if (row.variantId() == null || row.variantId() <= 0) {
            return "Mã biến thể không hợp lệ.";
        }
        if (!inventoryService.isActiveVariantForImport(row.variantId())) {
            return "Không tìm thấy biến thể đang bán với mã #" + row.variantId() + ".";
        }
        if (row.quantity() == null || row.quantity() <= 0) {
            return "Số lượng nhập phải lớn hơn 0.";
        }
        if (row.unitCost() == null || row.unitCost().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá nhập phải lớn hơn 0.";
        }
        return null;
    }

    private void writeImportResult(String spreadsheetId, String resultSheetName, String accessToken,
                                   List<ImportRowResult> results) throws IOException {
        List<List<Object>> rows = new ArrayList<>();
        rows.add(List.of("KẾT QUẢ NHẬP KHO TỪ GOOGLE SHEET"));
        rows.add(List.of("Lần xử lý:", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        rows.add(List.of());
        rows.add(List.of(
                "STT",
                "Dòng Sheet",
                "Mã biến thể",
                "Số lượng nhập",
                "Giá nhập",
                "Mã nhà cung cấp",
                "Trạng thái",
                "Thông báo"
        ));

        int index = 1;
        DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");
        for (ImportRowResult result : results) {
            ImportRow row = result.row();
            rows.add(List.of(
                    index++,
                    row == null ? "" : row.sheetRowNumber(),
                    row == null ? "" : row.rawVariantId(),
                    row == null ? "" : row.rawQuantity(),
                    row == null || row.unitCost() == null ? (row == null ? "" : row.rawUnitCost()) : moneyFormat.format(row.unitCost()),
                    row == null ? "" : row.supplierCode(),
                    result.status(),
                    result.message()
            ));
        }

        String clearRange = quoteSheetName(resultSheetName) + "!A:H";
        clearValues(spreadsheetId, clearRange, accessToken);
        updateValues(spreadsheetId, quoteSheetName(resultSheetName) + "!A1", rows, accessToken);
    }

    private String buildSupplierName(List<ImportRow> rows) {
        Set<String> supplierCodes = new LinkedHashSet<>();
        for (ImportRow row : rows) {
            if (row.supplierCode() != null && !row.supplierCode().isBlank()) {
                supplierCodes.add(row.supplierCode().trim());
            }
        }

        if (supplierCodes.isEmpty()) {
            return "Google Sheet";
        }

        String joined = String.join(", ", supplierCodes);
        if (joined.length() > 240) {
            joined = joined.substring(0, 240) + "...";
        }
        return "Google Sheet: " + joined;
    }

    private boolean isBlankRow(List<Object> row) {
        if (row == null || row.isEmpty()) {
            return true;
        }
        for (Object cell : row) {
            if (cell != null && !cell.toString().trim().isBlank()) {
                return false;
            }
        }
        return true;
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String normalized = value.trim()
                    .replace("#", "")
                    .replace(",", "")
                    .replace(" ", "");

            if (normalized.endsWith(".0")) {
                normalized = normalized.substring(0, normalized.length() - 2);
            }

            return Integer.parseInt(normalized);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseMoney(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            String normalized = value.trim()
                    .replace("₫", "")
                    .replace("đ", "")
                    .replace("Đ", "")
                    .replace(",", "")
                    .replace(" ", "");
            return new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String cell(List<Object> row, int index) {
        if (row == null || index < 0 || index >= row.size() || row.get(index) == null) {
            return "";
        }
        return row.get(index).toString().trim();
    }

    private String getAccessToken(ServletContext context) throws IOException {
        GoogleCredentials credentials = loadCredentials(context).createScoped(List.of(SHEETS_SCOPE));
        credentials.refreshIfExpired();

        AccessToken token = credentials.getAccessToken();
        if (token == null || token.getTokenValue() == null || token.getTokenValue().isBlank()) {
            credentials.refresh();
            token = credentials.getAccessToken();
        }

        if (token == null || token.getTokenValue() == null || token.getTokenValue().isBlank()) {
            throw new IOException("Không lấy được access token từ Google credentials.");
        }
        return token.getTokenValue();
    }

    private void ensureSheetExists(String spreadsheetId, String sheetName, String accessToken) throws IOException {
        if (getSheetNames(spreadsheetId, accessToken).contains(sheetName)) {
            return;
        }

        Map<String, Object> body = Map.of(
                "requests", List.of(Map.of(
                        "addSheet", Map.of(
                                "properties", Map.of("title", sheetName)
                        )
                ))
        );

        String url = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId + ":batchUpdate";
        executeJsonRequest("POST", url, accessToken, gson.toJson(body));
    }

    private Set<String> getSheetNames(String spreadsheetId, String accessToken) throws IOException {
        String url = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId
                + "?fields=sheets(properties(title))";
        String responseBody = executeGetRequest(url, accessToken);
        SpreadsheetResponse response = gson.fromJson(responseBody, SpreadsheetResponse.class);
        Set<String> names = new LinkedHashSet<>();

        if (response != null && response.sheets != null) {
            for (SheetEntry sheet : response.sheets) {
                if (sheet != null && sheet.properties != null && sheet.properties.title != null) {
                    names.add(sheet.properties.title);
                }
            }
        }

        return names;
    }

    private List<List<Object>> readValues(String spreadsheetId, String range, String accessToken) throws IOException {
        String url = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId
                + "/values/" + encodePath(range);
        String responseBody = executeGetRequest(url, accessToken);
        ValuesResponse response = gson.fromJson(responseBody, ValuesResponse.class);

        if (response == null || response.values == null) {
            return List.of();
        }
        return response.values;
    }

    private void clearValues(String spreadsheetId, String range, String accessToken) throws IOException {
        String url = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId
                + "/values/" + encodePath(range) + ":clear";
        executeJsonRequest("POST", url, accessToken, "{}");
    }

    private void updateValues(String spreadsheetId, String range, List<List<Object>> values, String accessToken) throws IOException {
        String url = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId
                + "/values/" + encodePath(range) + "?valueInputOption=USER_ENTERED";

        Map<String, Object> body = Map.of(
                "range", range,
                "majorDimension", "ROWS",
                "values", values
        );

        executeJsonRequest("PUT", url, accessToken, gson.toJson(body));
    }

    private String executeGetRequest(String urlString, String accessToken) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Accept", "application/json");

        int statusCode = connection.getResponseCode();
        String body = statusCode >= 200 && statusCode < 300
                ? readResponseBody(connection)
                : readResponseBody(connection);

        connection.disconnect();

        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("Google Sheet trả về lỗi " + statusCode + ": " + body);
        }

        return body;
    }

    private void executeJsonRequest(String method, String urlString, String accessToken, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        connection.getOutputStream().write(payload);

        int statusCode = connection.getResponseCode();
        if (statusCode < 200 || statusCode >= 300) {
            String errorBody = readResponseBody(connection);
            throw new IOException("Google Sheet trả về lỗi " + statusCode + ": " + errorBody);
        }

        connection.disconnect();
    }

    private String readResponseBody(HttpURLConnection connection) {
        try (InputStream stream = connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream()) {
            if (stream == null) {
                return "Không có nội dung lỗi";
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "Không đọc được nội dung lỗi: " + e.getMessage();
        }
    }

    private GoogleCredentials loadCredentials(ServletContext context) throws IOException {
        String credentialJson = firstNotBlank(
                getContextParam(context, "googleSheetsCredentialsJson"),
                System.getenv("GOOGLE_SHEETS_CREDENTIALS_JSON")
        );

        if (credentialJson != null) {
            return GoogleCredentials.fromStream(new ByteArrayInputStream(credentialJson.getBytes(StandardCharsets.UTF_8)));
        }

        String credentialFile = firstNotBlank(
                getContextParam(context, "googleSheetsCredentialsFile"),
                System.getenv("GOOGLE_SHEETS_CREDENTIALS_FILE"),
                System.getenv("GOOGLE_APPLICATION_CREDENTIALS")
        );

        if (credentialFile == null) {
            throw new IllegalStateException("Thiếu cấu hình Google Sheet credentials. Cần GOOGLE_APPLICATION_CREDENTIALS hoặc GOOGLE_SHEETS_CREDENTIALS_JSON.");
        }

        Path path = Path.of(credentialFile);
        if (!Files.exists(path)) {
            throw new IllegalStateException("Không tìm thấy file credentials: " + credentialFile);
        }

        try (InputStream stream = Files.newInputStream(path)) {
            return GoogleCredentials.fromStream(stream);
        }
    }

    private GoogleSheetConfig loadConfig(ServletContext context) {
        String spreadsheetId = firstNotBlank(
                getContextParam(context, "googleInventorySpreadsheetId"),
                getContextParam(context, "googleInventorySheetId"),
                System.getenv("GOOGLE_INVENTORY_SPREADSHEET_ID"),
                System.getenv("GOOGLE_INVENTORY_SHEET_ID"),
                DEFAULT_SPREADSHEET_ID
        );

        if (spreadsheetId == null) {
            throw new IllegalStateException("Thiếu GOOGLE_INVENTORY_SPREADSHEET_ID hoặc googleInventorySpreadsheetId.");
        }

        String sheetName = firstNotBlank(
                getContextParam(context, "googleInventorySheetName"),
                System.getenv("GOOGLE_INVENTORY_SHEET_NAME"),
                DEFAULT_SHEET_NAME
        );

        return new GoogleSheetConfig(spreadsheetId, sheetName);
    }

    private GoogleSheetImportConfig loadImportConfig(ServletContext context) {
        String importSheetName = firstNotBlank(
                getContextParam(context, "googleInventoryImportSheetName"),
                System.getenv("GOOGLE_INVENTORY_IMPORT_SHEET_NAME"),
                DEFAULT_IMPORT_SHEET_NAME
        );

        String resultSheetName = firstNotBlank(
                getContextParam(context, "googleInventoryImportResultSheetName"),
                System.getenv("GOOGLE_INVENTORY_IMPORT_RESULT_SHEET_NAME"),
                DEFAULT_IMPORT_RESULT_SHEET_NAME
        );

        return new GoogleSheetImportConfig(importSheetName, resultSheetName);
    }

    private String getContextParam(ServletContext context, String key) {
        if (context == null) {
            return null;
        }
        String value = context.getInitParameter(key);
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String firstNotBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private String quoteSheetName(String sheetName) {
        return "'" + sheetName.replace("'", "''") + "'";
    }

    private String encodePath(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public static class ImportSyncResult {
        private final int totalRows;
        private final int successRows;
        private final int errorRows;
        private final int resultRows;
        private final String message;

        public ImportSyncResult(int totalRows, int successRows, int errorRows, int resultRows, String message) {
            this.totalRows = totalRows;
            this.successRows = successRows;
            this.errorRows = errorRows;
            this.resultRows = resultRows;
            this.message = message;
        }

        public int getTotalRows() {
            return totalRows;
        }

        public int getSuccessRows() {
            return successRows;
        }

        public int getErrorRows() {
            return errorRows;
        }

        public int getResultRows() {
            return resultRows;
        }

        public String getMessage() {
            return message;
        }
    }

    private record GoogleSheetConfig(String spreadsheetId, String sheetName) {
    }

    private record GoogleSheetImportConfig(String importSheetName, String resultSheetName) {
    }

    private record ImportRow(
            int sheetRowNumber,
            String rawVariantId,
            String rawQuantity,
            String rawUnitCost,
            String supplierCode,
            String note,
            Integer variantId,
            Integer quantity,
            BigDecimal unitCost
    ) {
    }

    private record ImportRowResult(ImportRow row, String status, String message) {
        static ImportRowResult success(ImportRow row, String message) {
            return new ImportRowResult(row, "Thành công", message);
        }

        static ImportRowResult error(ImportRow row, String message) {
            return new ImportRowResult(row, "Lỗi", message);
        }

        static ImportRowResult info(String message) {
            return new ImportRowResult(null, "Thông tin", message);
        }
    }

    private static class ValuesResponse {
        List<List<Object>> values;
    }

    private static class SpreadsheetResponse {
        List<SheetEntry> sheets;
    }

    private static class SheetEntry {
        SheetProperties properties;
    }

    private static class SheetProperties {
        String title;
    }
}
