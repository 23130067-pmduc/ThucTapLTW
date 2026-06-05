package vn.edu.nlu.fit.thuctapltw.DAO;

import java.util.List;

public class ReviewImageDao extends BaseDao {
    public void insert(int reviewId, String imageUrl) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
            INSERT INTO product_review_images(review_id, image_url)
            VALUES(:reviewId, :imageUrl)""")
                .bind("reviewId", reviewId)
                .bind("imageUrl", imageUrl)
                .execute());
    }

    public List<String> getImagesByReviewId(int reviewId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                    SELECT image_url
                    FROM product_review_images
                    WHERE review_id = :reviewId
                    """)
                    .bind("reviewId", reviewId)
                    .mapTo(String.class)
                    .list()
        );
    }
}
