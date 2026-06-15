package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ProductImageDao;
import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.util.List;

public class ProductImageService {
    private final ProductImageDao productImageDao = new ProductImageDao();

    public List<ProductImage> getImageByProduct(int id) {
        return productImageDao.getImageByProduct(id);
    }

    public List<ProductImage> getVisibleImagesByProduct(int id) {
        return productImageDao.getVisibleImagesByProduct(id);
    }

    public ProductImage getImageById(int id) {
        return productImageDao.getImageById(id);
    }

    public int countVisibleImages(int productId) {
        return productImageDao.countImagesByStatus(productId, ProductImageDao.STATUS_VISIBLE);
    }

    public int countHiddenImages(int productId) {
        return productImageDao.countImagesByStatus(productId, ProductImageDao.STATUS_HIDDEN);
    }

    public void createImage(ProductImage image) {
        if (image.getStatus() == null || image.getStatus().isBlank()) {
            image.setStatus(ProductImageDao.STATUS_VISIBLE);
        }
        productImageDao.insert(image);
    }

    public void updateImage(ProductImage image) {
        productImageDao.update(image);
    }

    public void updateImageStatus(int id, String status) {
        productImageDao.updateStatus(id, status);
    }
}
