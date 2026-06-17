package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductImageDao;
import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.util.List;

public class ProductImageService {
    private final ProductImageDao productImageDao = new ProductImageDao();
    private final ProductDao productDao = new ProductDao();

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


    public void updateImage(ProductImage image) {
        if (image.getMain()) {
            productImageDao.clearMainByProduct(image.getProductId());
        }

        productImageDao.update(image);

        if (image.getMain()) {
            productDao.updateThumbnail(image.getProductId(), image.getImageUrl());
        }
    }


    public void updateImageStatus(int id, String status) {
        ProductImage image = productImageDao.getImageById(id);

        if (image == null) {
            return;
        }

        boolean wasMainImage = image.getMain();

        productImageDao.updateStatus(id, status);

        if (ProductImageDao.STATUS_HIDDEN.equals(status) && wasMainImage) {
            ProductImage newMainImage = productImageDao.getFirstVisibleImageByProduct(image.getProductId());

            if (newMainImage != null) {
                productImageDao.clearMainByProduct(image.getProductId());
                productImageDao.updateMain(newMainImage.getId(), true);
                productDao.updateThumbnail(image.getProductId(), newMainImage.getImageUrl());
            } else {
                productDao.updateThumbnail(image.getProductId(), null);
            }
        }
    }
    public void createImage(ProductImage image) {
        if (image.getStatus() == null || image.getStatus().isBlank()) {
            image.setStatus(ProductImageDao.STATUS_VISIBLE);
        }

        if (image.getMain()) {
            productImageDao.clearMainByProduct(image.getProductId());
        }

        productImageDao.insert(image);

        if (image.getMain()) {
            productDao.updateThumbnail(image.getProductId(), image.getImageUrl());
        }
    }


}
