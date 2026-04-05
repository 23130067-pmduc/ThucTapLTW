package vn.edu.nlu.fit.thuctapltw.Service;

import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.DAO.SearchHistoryDao;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchHistoryService {
    private static final int MAX_HISTORY = 6;
    private final SearchHistoryDao searchHistoryDao = new SearchHistoryDao();

    public void saveHistory(HttpSession session, User user, String keyword) {
        if (user != null) {
            saveToDb(session, user, keyword);
        } else {
            saveToSession(session, keyword);
        }
    }

    private void saveToDb(HttpSession session, User user, String keyword) {
        searchHistoryDao.saveOrUpdate(user.getId(), keyword);
        List<String> dbHistory = searchHistoryDao.getRecentKeywordsByUser(user.getId(), MAX_HISTORY);
        session.setAttribute("searchHistory", dbHistory);
    }

    @SuppressWarnings("unchecked")
    private void saveToSession(HttpSession session, String keyword) {
        List<String> searchHistory = (List<String>) session.getAttribute("searchHistory");

        if (searchHistory == null) {
            searchHistory = new LinkedList<>();
        }

        searchHistory.removeIf(item -> item.equalsIgnoreCase(keyword));
        searchHistory.add(0, keyword);

        if (searchHistory.size() > MAX_HISTORY) {
            searchHistory = new ArrayList<>(searchHistory.subList(0, MAX_HISTORY));
        }

        session.setAttribute("searchHistory", searchHistory);
    }
}