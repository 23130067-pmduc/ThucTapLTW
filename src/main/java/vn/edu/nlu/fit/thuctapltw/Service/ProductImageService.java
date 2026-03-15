package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ProductImageDao;
import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.util.List;

public class ProductImageService {
    private final ProductImageDao productImageDao = new ProductImageDao();

    public List<ProductImage> getImageByProduct(int id) {
        return productImageDao.getImageByProduct(id);
    }

    public ProductImage getImageById(int id) {
        return productImageDao.getImageById(id);
    }

    public void createImage(ProductImage image) {
        productImageDao.insert(image);
    }

    public void updateImage(ProductImage image) {
        productImageDao.update(image);
    }

    public void deleteImage(int id) {
        productImageDao.delete(id);
    }
}
