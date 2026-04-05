package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.SearchHistory;

import java.util.List;
import java.util.stream.Collectors;

public class SearchHistoryDao extends BaseDao {

    public void saveOrUpdate(int userId, String keyword) {
        String findSql = """
            SELECT id
            FROM search_history
            WHERE user_id = :userId AND keyword = :keyword
            LIMIT 1
        """;

        Integer existingId = getJdbi().withHandle(handle -> handle.createQuery(findSql).bind("userId", userId).bind("keyword", keyword).mapTo(Integer.class).findOne().orElse(null)
        );

        if (existingId != null) {
            String updateSql = """
                UPDATE search_history
                SET searched_at = CURRENT_TIMESTAMP
                WHERE id = :id
            """;

            getJdbi().withHandle(handle -> handle.createUpdate(updateSql).bind("id", existingId).execute()
            );
        } else {
            String insertSql = """
                INSERT INTO search_history(user_id, keyword, searched_at)
                VALUES (:userId, :keyword, CURRENT_TIMESTAMP)
            """;

            getJdbi().withHandle(handle ->
                    handle.createUpdate(insertSql).bind("userId", userId).bind("keyword", keyword).execute()
            );
        }
    }

    public List<SearchHistory> getRecentByUser(int userId, int limit) {
        String sql = """
            SELECT *
            FROM search_history
            WHERE user_id = :userId
            ORDER BY searched_at DESC
            LIMIT :limit
        """;

        return getJdbi().withHandle(handle ->
                handle.createQuery(sql).bind("userId", userId).bind("limit", limit).mapToBean(SearchHistory.class).list()
        );
    }

    public List<String> getRecentKeywordsByUser(int userId, int limit) {
        return getRecentByUser(userId, limit).stream().map(SearchHistory::getKeyword).collect(Collectors.toList());
    }
    public void deleteAllByUser(int userId) {
        String sql = """
        DELETE FROM search_history
        WHERE user_id = :userId
        """;
        getJdbi().withHandle(handle -> handle.createUpdate(sql).bind("userId", userId).execute()
        );
    }
}