package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.model.Address;

public class GhnShippingService {
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

        return ShippingQuote.success(0, "Da san sang tinh phi GHN, se hoan thien o commit tiep theo.");
    }

    public boolean isConfigured() {
        return token != null && !token.isBlank() && shopId > 0 && fromDistrictId > 0;
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
