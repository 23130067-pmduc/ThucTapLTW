package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.nlu.fit.thuctapltw.Service.ProfitReportService;
import vn.edu.nlu.fit.thuctapltw.model.ProfitDailyReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitProductReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitSummary;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "ProfitReportExportExcelController", value = "/profit-report-export-excel")
public class ProfitReportExportExcelController extends HttpServlet {
    private ProfitReportService profitReportService;

    @Override
    public void init() {
        profitReportService = new ProfitReportService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDate today = LocalDate.now();
        LocalDate defaultFromDate = today.minusDays(29);

        LocalDate fromDate = parseDate(request.getParameter("fromDate"), defaultFromDate);
        LocalDate toDate = parseDate(request.getParameter("toDate"), today);

        if (fromDate.isAfter(toDate)) {
            LocalDate temp = fromDate;
            fromDate = toDate;
            toDate = temp;
        }

        ProfitSummary summary = profitReportService.getSummary(fromDate, toDate);
        List<ProfitDailyReport> dailyReports = profitReportService.getDailyReports(fromDate, toDate);
        List<ProfitProductReport> productReports = profitReportService.getProductReportsForExcel(fromDate, toDate);

        String fileName = "thong_ke_san_pham_ban_duoc_" + fromDate + "_" + toDate + ".xlsx";
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=UTF-8''" + encodedFileName);

        try (Workbook workbook = new XSSFWorkbook()) {
            ExcelStyles styles = createStyles(workbook);
            createSummarySheet(workbook, styles, summary, fromDate, toDate);
            createProductSheet(workbook, styles, productReports, fromDate, toDate);
            createDailySheet(workbook, styles, dailyReports, fromDate, toDate);
            workbook.write(response.getOutputStream());
        }
    }

    private void createSummarySheet(Workbook workbook, ExcelStyles styles, ProfitSummary summary, LocalDate fromDate, LocalDate toDate) {
        Sheet sheet = workbook.createSheet("TongQuan");
        int rowIndex = 0;

        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("BÁO CÁO THỐNG KÊ SẢN PHẨM BÁN ĐƯỢC");
        titleCell.setCellStyle(styles.titleStyle());
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        Row rangeRow = sheet.createRow(rowIndex++);
        writeCell(rangeRow, 0, "Khoảng thời gian", styles.labelStyle());
        writeCell(rangeRow, 1, formatDate(fromDate) + " - " + formatDate(toDate), styles.normalStyle());

        Row exportedAtRow = sheet.createRow(rowIndex++);
        writeCell(exportedAtRow, 0, "Thời gian xuất", styles.labelStyle());
        writeCell(exportedAtRow, 1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), styles.normalStyle());

        rowIndex++;
        rowIndex = writeSummaryRow(sheet, rowIndex, "Đơn hoàn thành", summary.getCompletedOrders(), styles);
        rowIndex = writeSummaryRow(sheet, rowIndex, "Số lượng xuất kho", summary.getExportedQuantity(), styles);
        rowIndex = writeSummaryMoneyRow(sheet, rowIndex, "Doanh thu sản phẩm", summary.getGrossRevenue(), styles);
        rowIndex = writeSummaryMoneyRow(sheet, rowIndex, "Giảm giá", summary.getDiscount(), styles);
        rowIndex = writeSummaryMoneyRow(sheet, rowIndex, "Doanh thu thuần", summary.getNetRevenue(), styles);
        rowIndex = writeSummaryMoneyRow(sheet, rowIndex, "Giá vốn", summary.getCostOfGoods(), styles);
        writeSummaryMoneyRow(sheet, rowIndex, "Lợi nhuận gộp", summary.getGrossProfit(), styles);

        sheet.setColumnWidth(0, 28 * 256);
        sheet.setColumnWidth(1, 24 * 256);
        sheet.setColumnWidth(2, 18 * 256);
        sheet.setColumnWidth(3, 18 * 256);
    }

    private void createProductSheet(Workbook workbook, ExcelStyles styles, List<ProfitProductReport> productReports, LocalDate fromDate, LocalDate toDate) {
        Sheet sheet = workbook.createSheet("SanPhamBanDuoc");
        int rowIndex = 0;

        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("THỐNG KÊ SẢN PHẨM BÁN ĐƯỢC");
        titleCell.setCellStyle(styles.titleStyle());
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        Row rangeRow = sheet.createRow(rowIndex++);
        writeCell(rangeRow, 0, "Từ ngày", styles.labelStyle());
        writeCell(rangeRow, 1, formatDate(fromDate), styles.normalStyle());
        writeCell(rangeRow, 2, "Đến ngày", styles.labelStyle());
        writeCell(rangeRow, 3, formatDate(toDate), styles.normalStyle());

        rowIndex++;
        Row header = sheet.createRow(rowIndex++);
        String[] headers = {
                "STT", "Mã sản phẩm", "Tên sản phẩm", "Danh mục", "Số lượng đã bán",
                "Số lượng xuất kho", "Doanh thu", "Giá vốn", "Lợi nhuận", "Tỷ suất lợi nhuận (%)", "Ghi chú"
        };
        for (int i = 0; i < headers.length; i++) {
            writeCell(header, i, headers[i], styles.headerStyle());
        }

        if (productReports == null || productReports.isEmpty()) {
            Row emptyRow = sheet.createRow(rowIndex);
            writeCell(emptyRow, 0, "Chưa có sản phẩm bán được trong khoảng thời gian này.", styles.normalStyle());
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, headers.length - 1));
        } else {
            int stt = 1;
            for (ProfitProductReport report : productReports) {
                Row row = sheet.createRow(rowIndex++);
                writeCell(row, 0, stt++, styles.numberStyle());
                writeCell(row, 1, report.getProductId(), styles.numberStyle());
                writeCell(row, 2, safe(report.getProductName()), styles.normalStyle());
                writeCell(row, 3, safe(report.getCategoryName()), styles.normalStyle());
                writeCell(row, 4, report.getSoldQuantity(), styles.numberStyle());
                writeCell(row, 5, report.getExportedQuantity(), styles.numberStyle());
                writeMoneyCell(row, 6, report.getRevenue(), styles.moneyStyle());
                writeMoneyCell(row, 7, report.getCostOfGoods(), styles.moneyStyle());
                writeMoneyCell(row, 8, report.getProfit(), report.isNegativeProfit() ? styles.negativeMoneyStyle() : styles.moneyStyle());
                writeDecimalCell(row, 9, report.getProfitMargin(), styles.percentNumberStyle());
                writeCell(row, 10, "", styles.normalStyle());
            }
        }

        int[] widths = {8, 14, 42, 24, 18, 18, 18, 18, 18, 22, 30};
        setColumnWidths(sheet, widths);
        sheet.createFreezePane(0, 4);
    }

    private void createDailySheet(Workbook workbook, ExcelStyles styles, List<ProfitDailyReport> dailyReports, LocalDate fromDate, LocalDate toDate) {
        Sheet sheet = workbook.createSheet("ThongKeTheoNgay");
        int rowIndex = 0;

        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("THỐNG KÊ DOANH THU - LỢI NHUẬN THEO NGÀY");
        titleCell.setCellStyle(styles.titleStyle());
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        Row rangeRow = sheet.createRow(rowIndex++);
        writeCell(rangeRow, 0, "Từ ngày", styles.labelStyle());
        writeCell(rangeRow, 1, formatDate(fromDate), styles.normalStyle());
        writeCell(rangeRow, 2, "Đến ngày", styles.labelStyle());
        writeCell(rangeRow, 3, formatDate(toDate), styles.normalStyle());

        rowIndex++;
        Row header = sheet.createRow(rowIndex++);
        String[] headers = {"Ngày", "Đơn hoàn thành", "SL xuất", "Doanh thu thuần", "Giá vốn", "Lợi nhuận gộp"};
        for (int i = 0; i < headers.length; i++) {
            writeCell(header, i, headers[i], styles.headerStyle());
        }

        if (dailyReports == null || dailyReports.isEmpty()) {
            Row emptyRow = sheet.createRow(rowIndex);
            writeCell(emptyRow, 0, "Chưa có dữ liệu trong khoảng thời gian này.", styles.normalStyle());
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, headers.length - 1));
        } else {
            for (ProfitDailyReport report : dailyReports) {
                Row row = sheet.createRow(rowIndex++);
                writeCell(row, 0, safe(report.getReportDate()), styles.normalStyle());
                writeCell(row, 1, report.getCompletedOrders(), styles.numberStyle());
                writeCell(row, 2, report.getExportedQuantity(), styles.numberStyle());
                writeMoneyCell(row, 3, report.getRevenue(), styles.moneyStyle());
                writeMoneyCell(row, 4, report.getCostOfGoods(), styles.moneyStyle());
                writeMoneyCell(row, 5, report.getProfit(), report.isNegativeProfit() ? styles.negativeMoneyStyle() : styles.moneyStyle());
            }
        }

        int[] widths = {18, 18, 14, 20, 20, 20};
        setColumnWidths(sheet, widths);
        sheet.createFreezePane(0, 4);
    }

    private int writeSummaryRow(Sheet sheet, int rowIndex, String label, int value, ExcelStyles styles) {
        Row row = sheet.createRow(rowIndex++);
        writeCell(row, 0, label, styles.labelStyle());
        writeCell(row, 1, value, styles.numberStyle());
        return rowIndex;
    }

    private int writeSummaryMoneyRow(Sheet sheet, int rowIndex, String label, BigDecimal value, ExcelStyles styles) {
        Row row = sheet.createRow(rowIndex++);
        writeCell(row, 0, label, styles.labelStyle());
        writeMoneyCell(row, 1, value, styles.moneyStyle());
        return rowIndex;
    }

    private void writeCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    private void writeCell(Row row, int column, int value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void writeMoneyCell(Row row, int column, BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value == null ? 0 : value.doubleValue());
        cell.setCellStyle(style);
    }

    private void writeDecimalCell(Row row, int column, BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value == null ? 0 : value.doubleValue());
        cell.setCellStyle(style);
    }

    private void setColumnWidths(Sheet sheet, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i] * 256);
        }
    }

    private ExcelStyles createStyles(Workbook workbook) {
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());

        Font labelFont = workbook.createFont();
        labelFont.setBold(true);

        Font negativeFont = workbook.createFont();
        negativeFont.setColor(IndexedColors.RED.getIndex());

        DataFormat dataFormat = workbook.createDataFormat();

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.LEFT);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        addBorder(headerStyle);

        CellStyle labelStyle = workbook.createCellStyle();
        labelStyle.setFont(labelFont);
        addBorder(labelStyle);

        CellStyle normalStyle = workbook.createCellStyle();
        normalStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        normalStyle.setWrapText(true);
        addBorder(normalStyle);

        CellStyle numberStyle = workbook.createCellStyle();
        numberStyle.setAlignment(HorizontalAlignment.RIGHT);
        numberStyle.setDataFormat(dataFormat.getFormat("#,##0"));
        addBorder(numberStyle);

        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setAlignment(HorizontalAlignment.RIGHT);
        moneyStyle.setDataFormat(dataFormat.getFormat("#,##0"));
        addBorder(moneyStyle);

        CellStyle negativeMoneyStyle = workbook.createCellStyle();
        negativeMoneyStyle.cloneStyleFrom(moneyStyle);
        negativeMoneyStyle.setFont(negativeFont);

        CellStyle percentNumberStyle = workbook.createCellStyle();
        percentNumberStyle.setAlignment(HorizontalAlignment.RIGHT);
        percentNumberStyle.setDataFormat(dataFormat.getFormat("0.00"));
        addBorder(percentNumberStyle);

        return new ExcelStyles(titleStyle, headerStyle, labelStyle, normalStyle, numberStyle, moneyStyle, negativeMoneyStyle, percentNumberStyle);
    }

    private void addBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    }

    private LocalDate parseDate(String value, LocalDate defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            return defaultValue;
        }
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private record ExcelStyles(
            CellStyle titleStyle,
            CellStyle headerStyle,
            CellStyle labelStyle,
            CellStyle normalStyle,
            CellStyle numberStyle,
            CellStyle moneyStyle,
            CellStyle negativeMoneyStyle,
            CellStyle percentNumberStyle
    ) {}
}
