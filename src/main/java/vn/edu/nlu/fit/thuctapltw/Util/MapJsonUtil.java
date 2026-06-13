package vn.edu.nlu.fit.thuctapltw.Util;

import java.util.Map;

public class MapJsonUtil {

    private MapJsonUtil() {}

    public static String toJsonLabels(Map<String, Double> map) {
        StringBuilder sb = new StringBuilder("[");
        for (String key : map.keySet()) {
            sb.append("\"").append(key).append("\",");
        }
        if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    public static String toJsonValues(Map<String, Double> map) {
        StringBuilder sb = new StringBuilder("[");
        for (Double val : map.values()) {
            sb.append(val).append(",");
        }
        if (sb.length() > 1) sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
