package com.miracle.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyMetricsService {

    private final JdbcTemplate jdbcTemplate;

    // ── LOAD CATEGORIES WITH ITEMS AND EXISTING VALUES ────────────────────────
    public List<Map<String, Object>> getCategoriesWithItems(String categoryType, String entryDate) {
        List<Map<String, Object>> categories = jdbcTemplate.queryForList(
            "SELECT categoryId, categoryName, categoryType FROM metric_categories " +
            "WHERE categoryType = ? AND categoryStatus = 1 ORDER BY sortOrder", categoryType);

        for (Map<String, Object> cat : categories) {
            int categoryId = (int) cat.get("categoryId");
            List<Map<String, Object>> items = jdbcTemplate.queryForList(
                "SELECT i.itemId, i.itemName, i.itemType, " +
                "COALESCE(e.valueNumeric, '') as valueNumeric, " +
                "COALESCE(e.valueText, '') as valueText " +
                "FROM metric_items i " +
                "LEFT JOIN daily_metric_entries e ON e.metricItemId = i.itemId AND e.entryDate = ? " +
                "WHERE i.categoryId = ? AND i.itemStatus = 1 ORDER BY i.sortOrder",
                entryDate, categoryId);

            // Set existingValue for JSP
            for (Map<String, Object> item : items) {
                String numVal = item.get("valueNumeric").toString();
                String txtVal = item.get("valueText").toString();
                item.put("existingValue", !numVal.isEmpty() ? numVal : txtVal);
            }
            cat.put("items", items);
        }
        return categories;
    }

    // ── SAVE METRICS ──────────────────────────────────────────────────────────
    public Map<String, Object> saveMetrics(String entryDate, List<Map<String, Object>> entries, String enteredBy) {
        Map<String, Object> response = new HashMap<>();
        try {
            for (Map<String, Object> entry : entries) {
                int itemId = Integer.parseInt(entry.get("itemId").toString());
                String value = entry.get("value").toString().trim();

                // Get item type
                List<Map<String, Object>> items = jdbcTemplate.queryForList(
                    "SELECT itemType FROM metric_items WHERE itemId = ?", itemId);
                if (items.isEmpty()) continue;
                String itemType = items.get(0).get("itemType").toString();

                // Upsert
                int existing = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM daily_metric_entries WHERE metricItemId = ? AND entryDate = ?",
                    Integer.class, itemId, entryDate);

                if (existing > 0) {
                    if ("number".equals(itemType)) {
                        jdbcTemplate.update(
                            "UPDATE daily_metric_entries SET valueNumeric = ?, valueText = NULL, enteredBy = ? " +
                            "WHERE metricItemId = ? AND entryDate = ?",
                            Double.parseDouble(value), enteredBy, itemId, entryDate);
                    } else {
                        jdbcTemplate.update(
                            "UPDATE daily_metric_entries SET valueText = ?, valueNumeric = NULL, enteredBy = ? " +
                            "WHERE metricItemId = ? AND entryDate = ?",
                            value, enteredBy, itemId, entryDate);
                    }
                } else {
                    if ("number".equals(itemType)) {
                        jdbcTemplate.update(
                            "INSERT INTO daily_metric_entries (metricItemId, entryDate, valueNumeric, enteredBy) VALUES (?,?,?,?)",
                            itemId, entryDate, Double.parseDouble(value), enteredBy);
                    } else {
                        jdbcTemplate.update(
                            "INSERT INTO daily_metric_entries (metricItemId, entryDate, valueText, enteredBy) VALUES (?,?,?,?)",
                            itemId, entryDate, value, enteredBy);
                    }
                }
            }
            response.put("status", "success");
            response.put("message", "Metrics saved successfully");
        } catch (Exception e) {
            log.error("saveMetrics error: {}", e.getMessage(), e);
            response.put("status", "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ── GET REPORT DATA ───────────────────────────────────────────────────────
    public List<Map<String, Object>> getReportData(String fromDate, String toDate) {
        try {
            return jdbcTemplate.queryForList(
                "SELECT e.entryDate, c.categoryName, c.categoryType, i.itemName, " +
                "e.valueNumeric, e.valueText, e.enteredBy " +
                "FROM daily_metric_entries e " +
                "JOIN metric_items i ON e.metricItemId = i.itemId " +
                "JOIN metric_categories c ON i.categoryId = c.categoryId " +
                "WHERE e.entryDate BETWEEN ? AND ? " +
                "ORDER BY e.entryDate DESC, c.sortOrder, i.sortOrder",
                fromDate, toDate);
        } catch (Exception e) {
            log.error("getReportData error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ── GET STATUTORY COMPLIANCES ─────────────────────────────────────────────
    public List<Map<String, Object>> getStatutoryCompliances() {
        try {
            return jdbcTemplate.queryForList(
                "SELECT complianceName, frequency, dueDescription FROM statutory_compliance " +
                "WHERE complianceStatus = 1 ORDER BY sortOrder");
        } catch (Exception e) {
            log.error("getStatutoryCompliances error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ── REPORT SUMMARY ────────────────────────────────────────────────────────
    public Map<String, Object> getReportSummary(String fromDate, String toDate) {
        Map<String, Object> summary = new HashMap<>();
        try {
            Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM daily_metric_entries WHERE entryDate BETWEEN ? AND ?",
                Integer.class, fromDate, toDate);
            Integer days = jdbcTemplate.queryForObject(
                "SELECT COUNT(DISTINCT entryDate) FROM daily_metric_entries WHERE entryDate BETWEEN ? AND ?",
                Integer.class, fromDate, toDate);
            String latest = jdbcTemplate.queryForObject(
                "SELECT MAX(entryDate) FROM daily_metric_entries WHERE entryDate BETWEEN ? AND ?",
                String.class, fromDate, toDate);
            summary.put("totalEntries", total != null ? total : 0);
            summary.put("daysCovered", days != null ? days : 0);
            summary.put("latestEntry", latest != null ? latest : "N/A");
        } catch (Exception e) {
            log.error("getReportSummary error: {}", e.getMessage(), e);
            summary.put("totalEntries", 0);
            summary.put("daysCovered", 0);
            summary.put("latestEntry", "N/A");
        }
        return summary;
    }

    // ── CSV EXPORT ────────────────────────────────────────────────────────────
    public String generateCsv(String fromDate, String toDate) {
        List<Map<String, Object>> data = getReportData(fromDate, toDate);
        StringBuilder csv = new StringBuilder("Date,Department,Type,Metric,Value,Entered By\n");
        for (Map<String, Object> row : data) {
            String value = row.get("valueNumeric") != null && !row.get("valueNumeric").toString().isEmpty()
                ? row.get("valueNumeric").toString() : row.get("valueText") != null ? row.get("valueText").toString() : "";
            csv.append(row.get("entryDate")).append(",")
               .append(row.get("categoryName")).append(",")
               .append(row.get("categoryType")).append(",")
               .append("\"").append(row.get("itemName").toString().replace("\"", "\"\"")).append("\",")
               .append("\"").append(value.replace("\"", "\"\"")).append("\",")
               .append(row.get("enteredBy") != null ? row.get("enteredBy") : "Admin").append("\n");
        }
        return csv.toString();
    }
    public Map<String, Map<String, Object>> getGroupedReportData(String fromDate, String toDate) {
    List<Map<String, Object>> flat = getReportData(fromDate, toDate);
    Map<String, Map<String, Object>> grouped = new LinkedHashMap<>();
    for (Map<String, Object> row : flat) {
        String date = row.get("entryDate").toString();
        String catName = row.get("categoryName").toString();
        String displayDate = formatDisplayDate(date);
        grouped.computeIfAbsent(displayDate, k -> {
            Map<String, Object> dayMap = new LinkedHashMap<>();
            dayMap.put("editDate", date);
            dayMap.put("totalCount", 0);
            dayMap.put("byCategory", new LinkedHashMap<String, List<Map<String, Object>>>());
            return dayMap;
        });
        Map<String, Object> dayMap = grouped.get(displayDate);
        dayMap.put("totalCount", (int) dayMap.get("totalCount") + 1);
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> byCategory =
            (Map<String, List<Map<String, Object>>>) dayMap.get("byCategory");
        byCategory.computeIfAbsent(catName, k -> new ArrayList<>()).add(row);
    }
    return grouped;
}

private String formatDisplayDate(String dateStr) {
    try {
        java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
        String day = String.format("%02d", date.getDayOfMonth());
        String month = date.getMonth().getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
        String year = String.valueOf(date.getYear());
        String dayOfWeek = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);
        return day + " " + month + " " + year + " (" + dayOfWeek + ")";
    } catch (Exception e) {
        return dateStr;
    }
}
}
