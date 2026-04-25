package vn.edu.nlu.fit.thuctapltw.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import vn.edu.nlu.fit.thuctapltw.model.Address;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Normalizer;
import java.util.Locale;

public class GhnShippingService {
    private static final String GHN_BASE_URL = "https://online-gateway.ghn.vn/shiip/public-api";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String token = readConfig("GHN_API_TOKEN");
    private final int shopId = readIntConfig("GHN_SHOP_ID", 0);
    private final int fromDistrictId = readIntConfig("GHN_FROM_DISTRICT_ID", 0);
    private final int defaultWeight = readIntConfig("GHN_DEFAULT_WEIGHT", 500);
    private final int defaultLength = readIntConfig("GHN_DEFAULT_LENGTH", 20);
    private final int defaultWidth = readIntConfig("GHN_DEFAULT_WIDTH", 20);
    private final int defaultHeight = readIntConfig("GHN_DEFAULT_HEIGHT", 10);

    public ShippingQuote calculateFee(Address address) {
        if (address == null) {
            return ShippingQuote.failure("Khong tim thay dia chi giao hang.");
        }

        if (!isConfigured()) {
            return ShippingQuote.success(0, "Chua cau hinh GHN, tam tinh phi ship = 0.");
        }

        try {
            Integer toDistrictId = findDistrictId(address.getDistrict(), address.getCity());
            if (toDistrictId == null) {
                return ShippingQuote.failure("Khong tim thay ma quan/huyen tren GHN.");
            }

            String wardCode = findWardCode(toDistrictId, address.getWard());
            if (wardCode == null) {
                return ShippingQuote.failure("Khong tim thay ma phuong/xa tren GHN.");
            }

            Integer serviceId = findServiceId(toDistrictId);
            if (serviceId == null) {
                return ShippingQuote.failure("Khong tim thay dich vu giao hang phu hop.");
            }

            long fee = requestShippingFee(serviceId, toDistrictId, wardCode);
            return ShippingQuote.success(fee, null);
        } catch (Exception e) {
            return ShippingQuote.failure("Khong tinh duoc phi van chuyen GHN: " + e.getMessage());
        }
    }

    public boolean isConfigured() {
        return token != null && !token.isBlank() && shopId > 0 && fromDistrictId > 0;
    }

    private Integer findDistrictId(String districtName, String cityName) throws IOException, InterruptedException {
        JsonArray provinces = getAndReadDataArray("/master-data/province", false);

        Integer toProvinceId = null;
        String normalizedCity = normalizeAdministrativeUnit(cityName);

        for (JsonElement item : provinces) {
            JsonObject province = item.getAsJsonObject();
            JsonElement pNameElem = province.get("ProvinceName");
            if (pNameElem == null || pNameElem.isJsonNull()) continue;

            String ghnCityName = pNameElem.getAsString();
            String normalizedGhnCity = normalizeAdministrativeUnit(ghnCityName);

            if (normalizedGhnCity.equals(normalizedCity) || normalizedGhnCity.contains(normalizedCity) || normalizedCity.contains(normalizedGhnCity)) {
                toProvinceId = province.get("ProvinceID").getAsInt();
                break;
            }
        }

        if (toProvinceId == null) {
            return null;
        }

        JsonArray districts = getAndReadDataArray("/master-data/district?province_id=" + toProvinceId, false);

        String normalizedDistrict = normalizeAdministrativeUnit(districtName);

        for (JsonElement item : districts) {
            JsonObject district = item.getAsJsonObject();
            JsonElement dNameElem = district.get("DistrictName");
            if (dNameElem == null || dNameElem.isJsonNull()) continue;

            String ghnDistrictName = dNameElem.getAsString();
            String normalizedGhnDistrict = normalizeAdministrativeUnit(ghnDistrictName);

            if (normalizedGhnDistrict.equals(normalizedDistrict)) {
                return district.get("DistrictID").getAsInt();
            }
        }

        for (JsonElement item : districts) {
            JsonObject district = item.getAsJsonObject();
            JsonElement dNameElem = district.get("DistrictName");
            if (dNameElem == null || dNameElem.isJsonNull()) continue;

            String ghnDistrictName = dNameElem.getAsString();
            String normalizedGhnDistrict = normalizeAdministrativeUnit(ghnDistrictName);

            if (normalizedGhnDistrict.contains(normalizedDistrict) || normalizedDistrict.contains(normalizedGhnDistrict)) {
                return district.get("DistrictID").getAsInt();
            }
        }

        return null;
    }

    private String findWardCode(int districtId, String wardName) throws IOException, InterruptedException {
        JsonArray wards = getAndReadDataArray("/master-data/ward?district_id=" + districtId, false);

        String normalizedWard = normalize(wardName);

        for (JsonElement item : wards) {
            JsonObject ward = item.getAsJsonObject();
            JsonElement wNameElem = ward.get("WardName");
            if (wNameElem == null || wNameElem.isJsonNull()) continue;

            String ghnWardName = wNameElem.getAsString();
            String normalizedGhnWard = normalize(ghnWardName);
            if (normalizedGhnWard.equals(normalizedWard)) {
                return ward.get("WardCode").getAsString();
            }
        }

        for (JsonElement item : wards) {
            JsonObject ward = item.getAsJsonObject();
            JsonElement wNameElem = ward.get("WardName");
            if (wNameElem == null || wNameElem.isJsonNull()) continue;

            String ghnWardName = wNameElem.getAsString();
            String normalizedGhnWard = normalize(ghnWardName);
            if (normalizedGhnWard.contains(normalizedWard) || normalizedWard.contains(normalizedGhnWard)) {
                return ward.get("WardCode").getAsString();
            }
        }
        return null;
    }

    private Integer findServiceId(int toDistrictId) throws IOException, InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("shop_id", shopId);
        body.addProperty("from_district", fromDistrictId);
        body.addProperty("to_district", toDistrictId);

        JsonArray services = postAndReadDataArray("/v2/shipping-order/available-services", body, true);
        if (services.isEmpty()) {
            return null;
        }
        JsonObject firstService = services.get(0).getAsJsonObject();
        JsonElement sIdElem = firstService.get("service_id");
        if (sIdElem == null || sIdElem.isJsonNull()) return null;
        return sIdElem.getAsInt();
    }

    private long requestShippingFee(int serviceId, int toDistrictId, String wardCode) throws IOException, InterruptedException {
        JsonObject body = new JsonObject();
        body.addProperty("service_id", serviceId);
        body.addProperty("insurance_value", 0);
        body.addProperty("from_district_id", fromDistrictId);
        body.addProperty("to_district_id", toDistrictId);
        body.addProperty("to_ward_code", wardCode);
        body.addProperty("height", defaultHeight);
        body.addProperty("length", defaultLength);
        body.addProperty("weight", defaultWeight);
        body.addProperty("width", defaultWidth);
        body.addProperty("coupon", "");

        JsonObject response = post("/v2/shipping-order/fee", body, true);
        JsonObject data = response.getAsJsonObject("data");
        if (data == null || data.isJsonNull()) return 0;
        JsonElement totalElem = data.get("total");
        if (totalElem == null || totalElem.isJsonNull()) return 0;
        return totalElem.getAsLong();
    }

    private JsonArray postAndReadDataArray(String path, JsonObject body, boolean includeShopId)
            throws IOException, InterruptedException {
        JsonObject response = sendRequest("POST", path, body, includeShopId);
        return response.getAsJsonArray("data");
    }

    private JsonObject post(String path, JsonObject body, boolean includeShopId) throws IOException, InterruptedException {
        return sendRequest("POST", path, body, includeShopId);
    }

    private JsonArray getAndReadDataArray(String path, boolean includeShopId) throws IOException, InterruptedException {
        JsonObject response = sendRequest("GET", path, null, includeShopId);
        return response.getAsJsonArray("data");
    }

    private JsonObject sendRequest(String method, String path, JsonObject body, boolean includeShopId) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(GHN_BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Token", token);

        if (includeShopId) {
            requestBuilder.header("ShopId", String.valueOf(shopId));
        }

        if ("POST".equalsIgnoreCase(method)) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)));
        } else {
            requestBuilder.GET();
        }

        HttpResponse<String> response = httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());

        JsonObject root = null;
        try {
            root = JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception e) {
            throw new IOException("HTTP " + response.statusCode() + " - " + response.body());
        }

        int code = root.has("code") && !root.get("code").isJsonNull() ? root.get("code").getAsInt() : response.statusCode();
        if (response.statusCode() < 200 || response.statusCode() >= 300 || code != 200) {
            String message = root.has("message") && !root.get("message").isJsonNull()
                    ? root.get("message").getAsString() : "Loi khong xac dinh tu GHN";
            throw new IOException("API Error " + response.statusCode() + ": " + message);
        }
        return root;
    }

    private static String readConfig(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = System.getProperty(key);
        }
        return value;
    }

    private static int readIntConfig(String key, int defaultValue) {
        String value = readConfig(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "");

        return normalized.toLowerCase(Locale.ROOT)
                .replace("\u0111", "d")
                .replace("\u0110", "d")
                .trim();
    }

    private static String normalizeAdministrativeUnit(String value) {
        String normalized = normalize(value);
        normalized = normalized.replace("thanh pho ", "")
                .replace("tp ", "")
                .replace("quan ", "")
                .replace("huyen ", "")
                .replace("thi xa ", "")
                .replace("thi tran ", "")
                .replace("phuong ", "")
                .replace("xa ", "");
        return normalized.replaceAll("\\s+", " ").trim();
    }


    public record ShippingQuote(long fee, boolean success, String message) {
        public static ShippingQuote success(long fee, String message) {
            return new ShippingQuote(fee, true, message);
        }

        public static ShippingQuote failure(String message) {
            return new ShippingQuote(0, false, message);
        }
    }
}
