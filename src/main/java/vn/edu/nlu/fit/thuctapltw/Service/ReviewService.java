package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ReviewDao;
import vn.edu.nlu.fit.thuctapltw.model.Review;

import java.util.List;

public class ReviewService {
    private final ReviewDao reviewDao = new ReviewDao();

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


    public List<Review> getReviewByProductID(int productID){
        return reviewDao.findByProductID(productID);
    }

    public double getAvgRating(int id) {
        return reviewDao.getAvgRating(id);
    }

    public int getTotalReviews(int id) {
        return reviewDao.getTotalReviews(id);
    }
}
