package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ProfitReportDao;
import vn.edu.nlu.fit.thuctapltw.model.ProfitDailyReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitProductReport;
import vn.edu.nlu.fit.thuctapltw.model.ProfitSummary;

import java.time.LocalDate;
import java.util.List;

public class ProfitReportService {
    private final ProfitReportDao profitReportDao = new ProfitReportDao();

    public ProfitSummary getSummary(LocalDate fromDate, LocalDate toDate) {
        return profitReportDao.getSummary(fromDate, toDate);
    }

    public List<ProfitDailyReport> getDailyReports(LocalDate fromDate, LocalDate toDate) {
        return profitReportDao.getDailyReports(fromDate, toDate);
    }

    public List<ProfitProductReport> getProductReports(LocalDate fromDate, LocalDate toDate) {
        return profitReportDao.getProductReports(fromDate, toDate);
    }
}
