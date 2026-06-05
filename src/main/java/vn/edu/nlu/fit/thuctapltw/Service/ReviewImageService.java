package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ReviewImageDao;

import java.util.List;

public class ReviewImageService {
    private final ReviewImageDao reviewImageDao = new ReviewImageDao();

    public void saveImage(int reviewId, String imageUrl){
        reviewImageDao.insert(reviewId, imageUrl);
    }

    public List<String> getImagesByReviewId(int reviewId){
        return reviewImageDao.getImagesByReviewId(reviewId);
    }
}
