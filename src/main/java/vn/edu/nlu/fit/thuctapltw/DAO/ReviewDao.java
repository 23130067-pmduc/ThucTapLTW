package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Review;

import java.util.List;

public class ReviewDao extends BaseDao {
    public Review findByProductAndUser(int productId, int userId) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
            SELECT * 
            FROM product_reviews
            WHERE product_id = :pid AND user_id = :uid
        """)
                        .bind("pid", productId)
                        .bind("uid", userId)
                        .mapToBean(Review.class)
                        .findOne()
                        .orElse(null)
        );
    }


    public void insert(Review review) {
        getJdbi().useHandle(handle -> handle.createUpdate(
                                """
                                        INSERT INTO product_reviews(product_id, user_id, rating, comment, created_at)
                                        VALUES (:pid, :cid, :rating, :comment, NOW())
                                    """
                        ).bind("pid", review.getProductId())
                        .bind("cid", review.getUserId())
                        .bind("rating", review.getRating())
                        .bind("comment", review.getComment())
                        .execute()
        );

    }

    public void update(Review review) {
        getJdbi().useHandle(handle ->
                handle.createUpdate("""
            UPDATE product_reviews
            SET rating = :rating,
                comment = :comment,
                created_at = NOW()
            WHERE product_id = :pid AND user_id = :uid
        """)
                        .bind("pid", review.getProductId())
                        .bind("uid", review.getUserId())
                        .bind("rating", review.getRating())
                        .bind("comment", review.getComment())
                        .execute()
        );
    }



    public List<Review> findByProductID(int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery(
                        """
                                SELECT r.* , u.username
                                FROM product_reviews r
                                JOIN users u ON r.user_id = u.id
                                WHERE product_id = :productId
                                ORDER BY created_at DESC
                                """
                ).bind("productId", productId)
                .mapToBean(Review.class)
                .list());
    }


    public double getAvgRating(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT AVG(rating)
                FROM product_reviews
                WHERE product_id = :id
                """).bind("id", id)
                .mapTo(double.class)
                .one());

    }

    public int getTotalReviews(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM product_reviews
                WHERE product_id = :id
                """).bind("id", id)
                .mapTo(int.class)
                .one());
    }

    public Review getReviewByUserID(int userId, int productId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM product_reviews
                WHERE user_id = :userId AND product_id =:productId""")
                .bind("userId", userId)
                .bind("productId", productId)
                .mapToBean(Review.class)
                .findOne()
                .orElse(null));
    }
}
