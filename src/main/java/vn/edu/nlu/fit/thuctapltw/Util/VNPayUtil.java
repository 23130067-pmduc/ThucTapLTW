package vn.edu.nlu.fit.thuctapltw.Util;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class VNPayUtil {

    public static String buildPaymentUrl(int orderId, long amount, String ipAddr, String returnUrl) {
        String vnpTxnRef = generateTxnRef(orderId);
        String vnpAmount = String.valueOf(amount * 100);

        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setTimeZone(tz);

        String vnpCreateDate = sdf.format(new Date());

        Calendar cal = Calendar.getInstance(tz);
        cal.add(Calendar.MINUTE, 15);
        String vnpExpireDate = sdf.format(cal.getTime());

        String orderInfo = "Thanh toan don hang " + orderId;

        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", VNPayConfig.VNPAY_VERSION);
        params.put("vnp_Command", VNPayConfig.VNPAY_COMMAND);
        params.put("vnp_TmnCode", VNPayConfig.VNPAY_TMN_CODE);
        params.put("vnp_Amount", vnpAmount);
        params.put("vnp_CurrCode", VNPayConfig.VNPAY_CURR_CODE);
        params.put("vnp_TxnRef", vnpTxnRef);
        params.put("vnp_OrderInfo", orderInfo);
        params.put("vnp_OrderType", VNPayConfig.VNPAY_ORDER_TYPE);
        params.put("vnp_Locale", VNPayConfig.VNPAY_LOCALE);
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_CreateDate", vnpCreateDate);
        params.put("vnp_ExpireDate", vnpExpireDate);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryStr = new StringBuilder();

        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null || value.isBlank()) {
                continue;
            }

            if (!first) {
                hashData.append('&');
                queryStr.append('&');
            }
            String encodedValue = URLEncoder.encode(value, StandardCharsets.US_ASCII);

            hashData.append(key).append('=').append(encodedValue);
            queryStr.append(URLEncoder.encode(key, StandardCharsets.US_ASCII))
                    .append('=')
                    .append(encodedValue);
            first = false;
        }

        String secureHash = hmacSHA512(VNPayConfig.VNPAY_HASH_SECRET, hashData.toString());

        queryStr.append("&vnp_SecureHash=").append(secureHash);

        return VNPayConfig.VNPAY_URL + "?" + queryStr;
    }

    public static String buildReturnUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        boolean defaultPort = ("http".equals(scheme) && serverPort == 80)
                || ("https".equals(scheme) && serverPort == 443);

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (!defaultPort) {
            url.append(':').append(serverPort);
        }

        url.append(request.getContextPath()).append("/vnpay-return");
        return url.toString();
    }

    public static String generateTxnRef(int orderId) {
        return "OD" + orderId + "T" + System.currentTimeMillis();
    }

    public static Integer extractOrderIdFromTxnRef(String txnRef) {
        if (txnRef == null || txnRef.isBlank()) {
            return null;
        }

        if (txnRef.matches("\\d+")) {
            try {
                return Integer.parseInt(txnRef);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }

        if (txnRef.startsWith("OD")) {
            int markerIndex = txnRef.indexOf('T', 2);
            if (markerIndex > 2) {
                try {
                    return Integer.parseInt(txnRef.substring(2, markerIndex));
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }

        return null;
    }

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");

            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            mac.init(secretKey);

            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Loi tinh HMAC-SHA512", e);
        }
    }

    public static boolean verifyReturnUrl(Map<String, String[]> params) {
        String vnpSecureHash = getSingleParam(params, "vnp_SecureHash");
        if (vnpSecureHash == null || vnpSecureHash.isBlank()) {
            return false;
        }

        Map<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String key = entry.getKey();
            if (!"vnp_SecureHash".equals(key) && !"vnp_SecureHashType".equals(key)) {
                String[] values = entry.getValue();
                if (values != null && values.length > 0) {
                    sortedParams.put(key, values[0]);
                }
            }
        }

        StringBuilder hashData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (!first) {
                hashData.append('&');
            }
            hashData.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
            first = false;
        }

        String computedHash = hmacSHA512(VNPayConfig.VNPAY_HASH_SECRET, hashData.toString());
        return computedHash.equalsIgnoreCase(vnpSecureHash);
    }

    public static String getSingleParam(Map<String, String[]> params, String key) {
        String[] values = params.get(key);
        return values != null && values.length > 0 ? values[0] : null;
    }

}
