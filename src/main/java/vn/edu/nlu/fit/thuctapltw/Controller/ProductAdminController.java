package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.nlu.fit.thuctapltw.DAO.CategoryDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductImageDao;
import vn.edu.nlu.fit.thuctapltw.Service.ProductImageService;
import vn.edu.nlu.fit.thuctapltw.Service.ProductService;
import vn.edu.nlu.fit.thuctapltw.Util.CloudinaryUtil;
import vn.edu.nlu.fit.thuctapltw.model.Category;
import vn.edu.nlu.fit.thuctapltw.model.Product;
import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductAdminController", value = "/product-admin")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 60 * 1024 * 1024
)
public class ProductAdminController extends HttpServlet {

    private ProductDao productDao;
    private CategoryDao categoryDao;
    private ProductService productService;
    private ProductImageService productImageService;

    @Override
    public void init() {
        productDao = new ProductDao();
        categoryDao = new CategoryDao();
        productService = new ProductService();
        productImageService = new ProductImageService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");


        String mode = request.getParameter("mode");

        String ajax = request.getParameter("ajax");

        if ("search".equals(ajax)) {
            response.setContentType("application/json;charset=UTF-8");


            String categoryParam = request.getParameter("categoryId");
            int categoryId = 0;

            if (categoryParam != null && !categoryParam.isEmpty() ) {
                try {
                    categoryId = Integer.parseInt(categoryParam);
                } catch (NumberFormatException e) {
                    categoryId = 0;
                }
            }

            String status = request.getParameter("status");

            if (status == null){
                status = "";
            }

            status = status.trim();

            int pageSize = 20;
            int currentPage = 1;

            String pageParam = request.getParameter("page");
            String keyword = request.getParameter("keyword");

            if (keyword == null) {
                keyword = "";
            }

            keyword = keyword.trim();

            if (pageParam != null) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            if (currentPage < 1) {
                currentPage = 1;
            }


            int total = productService.countProductByFilter(keyword , categoryId, status);

            int totalPages = (int) Math.ceil((double) total / pageSize);

            if (totalPages == 0){
                totalPages = 1;
            }

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * pageSize;

            List<Product> products = productService.searchProductByFilter(keyword, categoryId, status, pageSize, offset);


            Map<String, Object> result = new HashMap<>();
            result.put("products", products);
            result.put("currentPage", currentPage);
            result.put("totalPages", totalPages);
            result.put("keyword", keyword);

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)
                            (src, typeOfSrc, context) -> new JsonPrimitive(src.toString()))
                    .create();

            String json = gson.toJson(result);
            response.getWriter().write(json);
            return;
        }

        if (mode == null) {

            int pageSize = 20;
            int currentPage = 1;

            String pageParam = request.getParameter("page");

            if (pageParam != null){
                try {
                    currentPage = Integer.parseInt(pageParam);
                } catch (NumberFormatException e){
                    currentPage = 1;
                }
            }

            if (currentPage < 1){
                currentPage = 1;
            }

            int totalProducts = productService.countProducts();

            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

            if (totalPages == 0){
                totalPages = 1;
            }

            if (currentPage > totalPages){
                currentPage = totalPages;
            }

            int offset = (currentPage - 1) * pageSize;


            List<Product> products = productService.getProductByPage(pageSize, offset);
            int activeProducts = productService.countActiveProduct();
            int inactiveProducts = productService.countInactiveProducts();
            int newProductThisWeek = productService.countNewProductThisWeek();

            List<Category> categories = categoryDao.findAll();

            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("pageSize", pageSize);

            request.setAttribute("products", products);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("activeProducts",activeProducts);
            request.setAttribute("inactiveProducts",inactiveProducts);
            request.setAttribute("newProductThisWeek",newProductThisWeek);

            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/product-admin.jsp").forward(request, response);
            return;
        }
        String keyword = request.getParameter("keyword");
        List<Product> allProducts;

        if (keyword != null && !keyword.trim().isEmpty()) {
            allProducts = productDao.searchByName(keyword.trim());
            request.setAttribute("keyword", keyword.trim());
        } else {
            allProducts = productDao.findAll();
        }
        if ("add".equals(mode)) {
            List<Category> categories = categoryDao.findAll();
            request.setAttribute("categories", categories);
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productDao.findById(id);
            List<Category> categories = categoryDao.findAll();

            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.setAttribute("mode", mode);
            request.getRequestDispatcher("/product-form.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceRaw = request.getParameter("price");
        String categoryIdRaw = request.getParameter("category_id");
        String status = request.getParameter("status");

        double price = (priceRaw != null && !priceRaw.isEmpty()) ? Double.parseDouble(priceRaw) : 0;
        int categoryId = (categoryIdRaw != null && !categoryIdRaw.isEmpty()) ? Integer.parseInt(categoryIdRaw) : 0;

        if ("updateStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));

            productService.updateProductStatus(id, status);
            response.sendRedirect("product-admin");
            return;
        }

        if ("create".equals(action) || "add".equals(action)) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setDescription(description);
            product.setCategory_id(categoryId);
            product.setStatus(status);

            String imageUrl = CloudinaryUtil.uploadImage(
                    request.getPart("imageFile"),
                    "sunnybear/products"
            );

            if (imageUrl != null && !imageUrl.isBlank()) {
                product.setThumbnail(imageUrl);
            }

            int productId = productDao.insert(product);

            if (imageUrl != null && !imageUrl.isBlank()) {
                ProductImage productImage = new ProductImage();
                productImage.setProductId(productId);
                productImage.setImageUrl(imageUrl);
                productImage.setMain(true);
                productImage.setStatus(ProductImageDao.STATUS_VISIBLE);

                productImageService.createImage(productImage);
            }

            for (Part part : request.getParts()) {
                if (!"subImageFiles".equals(part.getName())) {
                    continue;
                }

                if (part.getSubmittedFileName() == null
                        || part.getSubmittedFileName().isBlank()
                        || part.getSize() == 0) {
                    continue;
                }

                String subImageUrl = CloudinaryUtil.uploadImage(part, "sunnybear/products");

                if (subImageUrl != null && !subImageUrl.isBlank()) {
                    ProductImage subImage = new ProductImage();
                    subImage.setProductId(productId);
                    subImage.setImageUrl(subImageUrl);
                    subImage.setMain(false);
                    subImage.setStatus(ProductImageDao.STATUS_VISIBLE);

                    productImageService.createImage(subImage);
                }
            }

            response.sendRedirect("product-admin");
            return;
        }

        if ("update".equals(action)) {
            String idRaw = request.getParameter("id");

            if (idRaw != null && !idRaw.isEmpty()) {
                int id = Integer.parseInt(idRaw);

                Product product = new Product();
                product.setId(id);
                product.setName(name);
                product.setPrice(price);
                product.setCategory_id(categoryId);
                product.setStatus(status);

                String imageUrl = CloudinaryUtil.uploadImage(
                        request.getPart("imageFile"),
                        "sunnybear/products"
                );

                if (imageUrl != null && !imageUrl.isBlank()) {
                    product.setThumbnail(imageUrl);
                } else {
                    Product oldProduct = productDao.findById(id);
                    if (oldProduct != null) {
                        product.setThumbnail(oldProduct.getThumbnail());
                    }
                }

                productDao.update(product);

                if (imageUrl != null && !imageUrl.isBlank()) {
                    ProductImage productImage = new ProductImage();
                    productImage.setProductId(id);
                    productImage.setImageUrl(imageUrl);
                    productImage.setMain(true);
                    productImage.setStatus(ProductImageDao.STATUS_VISIBLE);

                    productImageService.createImage(productImage);
                }
            }

            response.sendRedirect("product-admin");
            return;
        }
    }

}