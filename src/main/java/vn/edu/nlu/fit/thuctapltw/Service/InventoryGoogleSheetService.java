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
    private static final String DEFAULT_TEMPLATE_SHEET_NAME = "Template";
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
        ensureSheetExists(config.spreadsheetId(), DEFAULT_TEMPLATE_SHEET_NAME, accessToken);
        ensureImportSheetHeader(config.spreadsheetId(), importConfig.importSheetName(), accessToken);
        ensureImportTemplateSheet(config.spreadsheetId(), accessToken);

        String readRange = quoteSheetName(importConfig.importSheetName()) + "!A2:M2000";
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
            String validationError = validateBasicImportRow(row);

            if (validationError == null) {
                VariantResolveResult resolveResult = resolveImportRow(row);
                if (resolveResult.success()) {
                    validRows.add(row.withResolvedVariant(resolveResult.variantId(), resolveResult.productId(), resolveResult.action()));
                } else {
                    resultRows.add(ImportRowResult.error(row, resolveResult.message()));
                }
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
            List<Integer> variantIds = validRows.stream().map(ImportRow::resolvedVariantId).toList();
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
                    resultRows.add(ImportRowResult.success(row,
                            describeAction(row.action()) + ". Đã nhập kho thành công. Mã phiếu: #" + transactionId));
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
        String headerRange = quoteSheetName(importSheetName) + "!A1:M1";
        List<List<Object>> values = readValues(spreadsheetId, headerRange, accessToken);

        boolean hasNewHeader = !values.isEmpty()
                && values.get(0).size() >= 10
                && "Mã biến thể".equalsIgnoreCase(cell(values.get(0), 0))
                && "Mã sản phẩm".equalsIgnoreCase(cell(values.get(0), 1))
                && "Mã nhà cung cấp".equalsIgnoreCase(cell(values.get(0), 9));

        if (hasNewHeader) {
            return;
        }

        updateValues(spreadsheetId, quoteSheetName(importSheetName) + "!A1", List.of(importHeader()), accessToken);
    }

    private void ensureImportTemplateSheet(String spreadsheetId, String accessToken) throws IOException {
        List<List<Object>> rows = new ArrayList<>();
        rows.add(List.of("MẪU NHẬP KHO HÀNG LOẠT SUNNYBEAR"));
        rows.add(List.of("Cách dùng:", "Nếu sản phẩm/biến thể đã có, chỉ cần nhập Mã biến thể + Số lượng nhập + Giá nhập + Mã nhà cung cấp."));
        rows.add(List.of("Cách dùng:", "Nếu sản phẩm chưa có, để trống Mã biến thể và điền Tên sản phẩm, Danh mục, Màu, Size, Giá bán."));
        rows.add(List.of());
        rows.add(importHeader());
        rows.add(List.of("709", "", "", "", "", "", "", "50", "170000", "NCC006", "", "Cập nhật tồn kho biến thể có sẵn", ""));
        rows.add(List.of("", "", "Áo thun bé trai test import", "Áo bé trai", "Xanh lá", "M", "169000", "20", "101400", "NCC006", "img/gau.png", "Tạo mới sản phẩm và nhập kho", "Sản phẩm tạo từ Google Sheet"));

        clearValues(spreadsheetId, quoteSheetName(DEFAULT_TEMPLATE_SHEET_NAME) + "!A:M", accessToken);
        updateValues(spreadsheetId, quoteSheetName(DEFAULT_TEMPLATE_SHEET_NAME) + "!A1", rows, accessToken);
    }

    private List<Object> importHeader() {
        return List.of(
                "Mã biến thể",
                "Mã sản phẩm",
                "Tên sản phẩm",
                "Danh mục",
                "Màu",
                "Size",
                "Giá bán",
                "Số lượng nhập",
                "Giá nhập",
                "Mã nhà cung cấp",
                "Ảnh đại diện",
                "Ghi chú",
                "Mô tả"
        );
    }

    private ImportRow parseImportRow(List<Object> row, int sheetRowNumber) {
        if (isOldCompactImportRow(row)) {
            return new ImportRow(
                    sheetRowNumber,
                    cell(row, 0),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    cell(row, 1),
                    cell(row, 2),
                    cell(row, 3),
                    "",
                    cell(row, 4),
                    "",
                    parseInteger(cell(row, 0)),
                    null,
                    parseInteger(cell(row, 1)),
                    null,
                    parseMoney(cell(row, 2)),
                    null,
                    null,
                    "Cập nhật tồn kho biến thể có sẵn",
                    true
            );
        }

        return new ImportRow(
                sheetRowNumber,
                cell(row, 0),
                cell(row, 1),
                cell(row, 2),
                cell(row, 3),
                cell(row, 4),
                cell(row, 5),
                cell(row, 6),
                cell(row, 7),
                cell(row, 8),
                cell(row, 9),
                cell(row, 10),
                cell(row, 11),
                cell(row, 12),
                parseInteger(cell(row, 0)),
                parseInteger(cell(row, 1)),
                parseInteger(cell(row, 7)),
                parseMoney(cell(row, 6)),
                parseMoney(cell(row, 8)),
                null,
                null,
                "",
                false
        );
    }

    private boolean isOldCompactImportRow(List<Object> row) {
        if (row == null || row.isEmpty()) {
            return false;
        }
        if (row.size() <= 5) {
            return true;
        }

        String maybeSupplier = normalizeSupplierCode(cell(row, 3));
        return parseInteger(cell(row, 0)) != null
                && parseInteger(cell(row, 1)) != null
                && parseMoney(cell(row, 2)) != null
                && maybeSupplier != null
                && maybeSupplier.startsWith("NCC");
    }

    private String validateBasicImportRow(ImportRow row) {
        if (!row.rawVariantId().isBlank() && row.variantId() == null) {
            return "Mã biến thể không hợp lệ. Nếu cần tạo sản phẩm mới, hãy để trống mã biến thể.";
        }
        if (!row.rawProductId().isBlank() && row.productId() == null) {
            return "Mã sản phẩm không hợp lệ.";
        }
        if (row.quantity() == null || row.quantity() <= 0) {
            return "Số lượng nhập phải lớn hơn 0.";
        }
        if (row.unitCost() == null || row.unitCost().compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá nhập phải lớn hơn 0.";
        }

        String supplierCode = normalizeSupplierCode(row.supplierCode());
        if (supplierCode == null) {
            return "Mã nhà cung cấp không được để trống.";
        }
        if (!inventoryService.isActiveSupplierCode(supplierCode)) {
            return "Không tìm thấy nhà cung cấp đang hoạt động với mã " + supplierCode + ".";
        }

        if (row.variantId() == null) {
            if (row.productId() == null && row.productName().isBlank()) {
                return "Tên sản phẩm không được để trống khi mã biến thể trống.";
            }
            if (row.colorName().isBlank()) {
                return "Màu không được để trống khi mã biến thể trống.";
            }
            if (row.sizeName().isBlank()) {
                return "Size không được để trống khi mã biến thể trống.";
            }
        }
        return null;
    }

    private VariantResolveResult resolveImportRow(ImportRow row) {
        if (row.variantId() != null) {
            if (!inventoryService.isActiveVariantForImport(row.variantId())) {
                return VariantResolveResult.error("Không tìm thấy biến thể đang bán với mã #" + row.variantId() + ".");
            }
            return VariantResolveResult.success(row.variantId(), null, "Cập nhật tồn kho biến thể có sẵn");
        }

        InventoryService.SheetVariantResolution resolution = inventoryService.resolveVariantForSheetImport(
                row.productId(),
                row.productName(),
                row.categoryName(),
                row.colorName(),
                row.sizeName(),
                row.sellingPrice(),
                row.thumbnail(),
                row.description()
        );

        if (!resolution.isSuccess()) {
            return VariantResolveResult.error(resolution.getMessage());
        }
        return VariantResolveResult.success(resolution.getVariantId(), resolution.getProductId(), resolution.getAction());
    }

    private void writeImportResult(String spreadsheetId, String resultSheetName, String accessToken,
                                   List<ImportRowResult> results) throws IOException {
        int successCount = 0;
        int errorCount = 0;
        int infoCount = 0;

        for (ImportRowResult result : results) {
            if ("Thành công".equals(result.status())) {
                successCount++;
            } else if ("Lỗi".equals(result.status())) {
                errorCount++;
            } else {
                infoCount++;
            }
        }

        List<List<Object>> rows = new ArrayList<>();
        rows.add(List.of("KẾT QUẢ NHẬP KHO TỪ GOOGLE SHEET"));
        rows.add(List.of("Lần xử lý:", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        rows.add(List.of("Tổng dòng kết quả:", results.size(), "Thành công:", successCount, "Lỗi:", errorCount, "Thông tin:", infoCount));
        rows.add(List.of());
        rows.add(List.of(
                "STT",
                "Dòng Sheet",
                "Hành động",
                "Mã sản phẩm",
                "Mã biến thể",
                "Tên sản phẩm",
                "Màu",
                "Size",
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
                    row == null ? "" : safe(row.action()),
                    row == null ? "" : firstNotBlank(row.rawProductId(), row.resolvedProductId() == null ? "" : String.valueOf(row.resolvedProductId())),
                    row == null ? "" : firstNotBlank(row.rawVariantId(), row.resolvedVariantId() == null ? "" : String.valueOf(row.resolvedVariantId())),
                    row == null ? "" : row.productName(),
                    row == null ? "" : row.colorName(),
                    row == null ? "" : row.sizeName(),
                    row == null ? "" : row.rawQuantity(),
                    row == null || row.unitCost() == null ? (row == null ? "" : row.rawUnitCost()) : moneyFormat.format(row.unitCost()),
                    row == null ? "" : row.supplierCode(),
                    result.status(),
                    result.message()
            ));
        }

        String clearRange = quoteSheetName(resultSheetName) + "!A:M";
        clearValues(spreadsheetId, clearRange, accessToken);
        updateValues(spreadsheetId, quoteSheetName(resultSheetName) + "!A1", rows, accessToken);
    }

    private String buildSupplierName(List<ImportRow> rows) {
        Set<String> supplierCodes = new LinkedHashSet<>();
        for (ImportRow row : rows) {
            String supplierCode = normalizeSupplierCode(row.supplierCode());
            if (supplierCode != null) {
                supplierCodes.add(supplierCode);
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

    private String describeAction(String action) {
        return action == null || action.isBlank() ? "Cập nhật tồn kho" : action;
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

    private String normalizeSupplierCode(String supplierCode) {
        if (supplierCode == null || supplierCode.trim().isBlank()) {
            return null;
        }
        return supplierCode.trim().toUpperCase();
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
            String rawProductId,
            String productName,
            String categoryName,
            String colorName,
            String sizeName,
            String rawSellingPrice,
            String rawQuantity,
            String rawUnitCost,
            String supplierCode,
            String thumbnail,
            String note,
            String description,
            Integer variantId,
            Integer productId,
            Integer quantity,
            BigDecimal sellingPrice,
            BigDecimal unitCost,
            Integer resolvedVariantId,
            Integer resolvedProductId,
            String action,
            boolean oldCompactFormat
    ) {
        ImportRow withResolvedVariant(Integer resolvedVariantId, Integer resolvedProductId, String action) {
            return new ImportRow(
                    sheetRowNumber,
                    rawVariantId,
                    rawProductId,
                    productName,
                    categoryName,
                    colorName,
                    sizeName,
                    rawSellingPrice,
                    rawQuantity,
                    rawUnitCost,
                    supplierCode,
                    thumbnail,
                    note,
                    description,
                    variantId,
                    productId,
                    quantity,
                    sellingPrice,
                    unitCost,
                    resolvedVariantId,
                    resolvedProductId,
                    action,
                    oldCompactFormat
            );
        }
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

    private record VariantResolveResult(boolean success, Integer variantId, Integer productId, String action, String message) {
        static VariantResolveResult success(Integer variantId, Integer productId, String action) {
            return new VariantResolveResult(true, variantId, productId, action, "");
        }

        static VariantResolveResult error(String message) {
            return new VariantResolveResult(false, null, null, "", message);
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
