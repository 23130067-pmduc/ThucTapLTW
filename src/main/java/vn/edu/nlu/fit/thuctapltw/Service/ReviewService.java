package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.OrderDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ReviewDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ReviewImageDao;
import vn.edu.nlu.fit.thuctapltw.model.Review;

import java.util.List;

public class ReviewService {
    private final ReviewDao reviewDao = new ReviewDao();
    private final OrderDao orderDao = new OrderDao();
    private final ReviewImageDao reviewImageDao = new ReviewImageDao();

    public void addOrUpdateReview(Review review) {
        Review exist = reviewDao.findByProductAndUser(
                review.getProductId(),
                review.getUserId()
        );

        if (exist == null) {
            reviewDao.insert(review);
        } else {
            reviewDao.update(review);
        }
    }

    public void addReview(Review review) {
        reviewDao.insert(review);
    }

    public int addReviewAndReturnId(Review review) {
        return reviewDao.insertAndReturnId(review);
    }

    public List<Review> getReviewByProductID(int productID){
        return reviewDao.findByProductID(productID);
    }

    public List<Review> getReviewByProductID(int productId, String sortRating) {
        return reviewDao.getReviewByProductID(productId, sortRating);
    }

    public double getAvgRating(int id) {
        return reviewDao.getAvgRating(id);
    }

    public int getTotalReviews(int id) {
        return reviewDao.getTotalReviews(id);
    }

    public Review getReviewByUserID(int userId, int productId) {
        return reviewDao.getReviewByUserID(userId, productId);
    }

    public int getRemainingReviewTimes(int userId, int productId) {
        int purchaseCount = orderDao.countCompletePurchaseByUserAndProduct(userId, productId);
        int reviewCount = reviewDao.countReviewByUserAndProduct(userId, productId);

        return purchaseCount - reviewCount;

    }

    public List<Review> getReviewByProductIDWithImages(int productId) {
        List<Review> reviews = reviewDao.findByProductID(productId);

        for (Review review : reviews) {
            review.setImages(reviewImageDao.getImagesByReviewId(review.getId()));
        }

        return reviews;
    }

    public List<Review> getReviewByProductIDWithImages(int productId, String sortRating) {
        List<Review> reviews = reviewDao.getReviewByProductID(productId, sortRating);

        for (Review review : reviews) {
            review.setImages(reviewImageDao.getImagesByReviewId(review.getId()));
        }

        return reviews;
    }

}
