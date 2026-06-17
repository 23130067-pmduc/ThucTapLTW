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
import java.util.List;
import java.util.Map;

public class InventoryGoogleSheetService {
    private static final String SHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets";
    private static final String DEFAULT_SHEET_NAME = "TonKho";
    private static final String DEFAULT_SPREADSHEET_ID = "1H0is6rV0zZ-d3IBhpakRK9YdeJXY4L-xY8ajBBthNnE";
    private static final int LOW_STOCK_THRESHOLD = 10;

    private final InventoryService inventoryService = new InventoryService();
    private final Gson gson = new Gson();

    public int updateInventoryReport(ServletContext context) throws IOException {
        GoogleSheetConfig config = loadConfig(context);
        GoogleCredentials credentials = loadCredentials(context).createScoped(List.of(SHEETS_SCOPE));
        credentials.refreshIfExpired();

        AccessToken token = credentials.getAccessToken();
        if (token == null || token.getTokenValue() == null || token.getTokenValue().isBlank()) {
            credentials.refresh();
            token = credentials.getAccessToken();
        }

        String accessToken = token.getTokenValue();
        List<InventoryItem> items = inventoryService.getInventoryItemsForGoogleSheet();
        List<List<Object>> values = buildSheetValues(items);

        String clearRange = quoteSheetName(config.sheetName()) + "!A:Q";
        String updateRange = quoteSheetName(config.sheetName()) + "!A1";

        clearValues(config.spreadsheetId(), clearRange, accessToken);
        updateValues(config.spreadsheetId(), updateRange, values, accessToken);

        return items.size();
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

    private record GoogleSheetConfig(String spreadsheetId, String sheetName) {
    }
}
