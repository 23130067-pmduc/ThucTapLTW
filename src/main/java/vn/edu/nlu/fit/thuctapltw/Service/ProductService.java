package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;
import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.util.Comparator;
import java.util.List;

public class ProductService {

    private final ProductDao productDao = new ProductDao();

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public Product getProductById(int id) {
        return productDao.findById(id);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDao.findByCategory(categoryId);
    }


    public List<Product> getProductsByCategories(List<Integer> categoryIds) {
        return productDao.findByCategories(categoryIds);
    }


    public List<Product> getLatestProducts(int limit) {
        return productDao.findLatest(limit);
    }


    public List<Product> searchProducts(String keyword) {
        return productDao.searchByName(keyword);
    }



    public List<Product> sortByNewest(List<Product> products) {
        products.sort(Comparator.comparing(Product::getCreated_at).reversed());
        return products;
    }



    public List<Product> sortByBestSeller(List<Product> products) {
        products.sort(Comparator.comparing(Product::getViews).reversed());
        return products;
    }


    public List<Product> sortBySale(List<Product> products) {
        products.sort(Comparator.comparing((Product p) -> p.getSale_price() > 0 ? 0 : 1)
                .thenComparing(Product::getSale_price));
        return products;
    }


    public List<Product> sortByPriceAsc(List<Product> products) {
        products.sort(Comparator.comparing(Product::getSale_price));
        return products;
    }

    public List<Product> sortByPriceDesc(List<Product> products) {
        products.sort(Comparator.comparing((Product::getSale_price))
                .reversed());

        return products;
    }

    public List<Product> getBoyProducts(int limit) {
        return productDao.findBoyProducts(limit);
    }

    public List<Product> getGirlProducts(int limit) {
        return productDao.findGirlProducts(limit);
    }

    public List<Product> getAccessoryProducts(int limit) {
        return productDao.findAccessoryProducts(limit);
    }

    public List<Product> ralatedProduct(int currentProductId, int limit){

        Product currentProduct = productDao.findById(currentProductId);

        if(currentProduct == null || currentProduct.getCategory_id() <= 0){
            return List.of();
        }

        int categoryId = currentProduct.getCategory_id();

        return productDao.getRelatedProductByCategory(categoryId, currentProductId, limit);
    }
}
