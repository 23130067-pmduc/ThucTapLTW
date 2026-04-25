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

    public ShippingQuote calculateFee(Address address) {
        if (address == null) {
            return ShippingQuote.failure("Khong tim thay dia chi giao hang.");
        }

        if (!isConfigured()) {
            return ShippingQuote.success(0, "Chua cau hinh GHN, tam tinh phi ship = 0.");
        }

        if (isBlank(address.getCity()) || isBlank(address.getDistrict()) || isBlank(address.getWard())) {
            return ShippingQuote.failure("Dia chi giao hang chua day du de tinh phi van chuyen.");
        }

        try {
            Integer districtId = findDistrictId(address.getDistrict(), address.getCity());
            if (districtId == null) {
                return ShippingQuote.failure("Khong tim thay ma quan/huyen tren GHN.");
            }

            String wardCode = findWardCode(districtId, address.getWard());
            if (wardCode == null) {
                return ShippingQuote.failure("Khong tim thay ma phuong/xa tren GHN.");
            }

            return ShippingQuote.success(0, "Da map duoc dia chi GHN, se tinh phi o commit tiep theo.");
        } catch (Exception e) {
            return ShippingQuote.failure("Khong xu ly duoc dia chi GHN: " + e.getMessage());
        }
    }

    public boolean isConfigured() {
        return token != null && !token.isBlank() && shopId > 0 && fromDistrictId > 0;
    }

    private Integer findDistrictId(String districtName, String cityName) throws IOException, InterruptedException {
        JsonArray provinces = getAndReadDataArray("/master-data/province");

        Integer provinceId = null;
        String normalizedCity = normalizeAdministrativeUnit(cityName);

        for (JsonElement item : provinces) {
            JsonObject province = item.getAsJsonObject();
            String ghnCityName = province.get("ProvinceName").getAsString();
            String normalizedGhnCity = normalizeAdministrativeUnit(ghnCityName);

            if (normalizedGhnCity.equals(normalizedCity)
                    || normalizedGhnCity.contains(normalizedCity)
                    || normalizedCity.contains(normalizedGhnCity)) {
                provinceId = province.get("ProvinceID").getAsInt();
                break;
            }
        }

        if (provinceId == null) {
            return null;
        }

        JsonArray districts = getAndReadDataArray("/master-data/district?province_id=" + provinceId);
        String normalizedDistrict = normalizeAdministrativeUnit(districtName);

        for (JsonElement item : districts) {
            JsonObject district = item.getAsJsonObject();
            String ghnDistrictName = district.get("DistrictName").getAsString();
            String normalizedGhnDistrict = normalizeAdministrativeUnit(ghnDistrictName);

            if (normalizedGhnDistrict.equals(normalizedDistrict)
                    || normalizedGhnDistrict.contains(normalizedDistrict)
                    || normalizedDistrict.contains(normalizedGhnDistrict)) {
                return district.get("DistrictID").getAsInt();
            }
        }

        return null;
    }

    private String findWardCode(int districtId, String wardName) throws IOException, InterruptedException {
        JsonArray wards = getAndReadDataArray("/master-data/ward?district_id=" + districtId);
        String normalizedWard = normalize(wardName);

        for (JsonElement item : wards) {
            JsonObject ward = item.getAsJsonObject();
            String ghnWardName = ward.get("WardName").getAsString();
            String normalizedGhnWard = normalize(ghnWardName);

            if (normalizedGhnWard.equals(normalizedWard)
                    || normalizedGhnWard.contains(normalizedWard)
                    || normalizedWard.contains(normalizedGhnWard)) {
                return ward.get("WardCode").getAsString();
            }
        }

        return null;
    }

    private JsonArray getAndReadDataArray(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GHN_BASE_URL + path))
                .header("Content-Type", "application/json")
                .header("Token", token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject root = JsonParser.parseString(response.body()).getAsJsonObject();

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("HTTP " + response.statusCode());
        }

        return root.getAsJsonArray("data");
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

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return normalized.toLowerCase(Locale.ROOT)
                .replace("đ", "d")
                .trim();
    }

    private static String normalizeAdministrativeUnit(String value) {
        String normalized = normalize(value);
        normalized = normalized
                .replace("thanh pho ", "")
                .replace("tp ", "")
                .replace("quan ", "")
                .replace("huyen ", "")
                .replace("thi xa ", "")
                .replace("thi tran ", "")
                .replace("phuong ", "")
                .replace("xa ", "");
        return normalized.replaceAll("\\s+", " ").trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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
