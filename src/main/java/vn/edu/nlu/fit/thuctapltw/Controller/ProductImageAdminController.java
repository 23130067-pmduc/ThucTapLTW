package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.edu.nlu.fit.thuctapltw.Service.ProductImageService;
import vn.edu.nlu.fit.thuctapltw.model.ProductImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductImageAdminController", value = "/product-image-admin")
@MultipartConfig(
        maxFileSize = 10 * 1024 * 1024,
        maxRequestSize = 50 * 1024 * 1024
)
public class ProductImageAdminController extends HttpServlet {
    private static final String REDIRECT_LIST = "product-image-admin?productId=";
    private static final String PRODUCT_ADMIN_PAGE = "product-admin";
    private static final String IMAGE_ADMIN_PAGE = "/product-image-admin.jsp";
    private static final String IMAGE_FORM_PAGE = "/product-image-form.jsp";
    private static final String MODE_ADD = "add";
    private static final String MODE_EDIT = "edit";
    private static final String MODE_VIEW = "view";
    private static final String ACTION_CREATE = "create";
    private static final String ACTION_UPDATE = "update";
    private static final String ACTION_DELETE = "delete";
    private static final String IMAGE_FOLDER = "img";

    private ProductImageService productImageService;

    @Override
    public void init() {
        productImageService = new ProductImageService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Integer productId = parseInteger(request.getParameter("productId"));
        if (productId == null) {
            response.sendRedirect(PRODUCT_ADMIN_PAGE);
            return;
        }

        String mode = trimToEmpty(request.getParameter("mode"));
        if (mode.isEmpty()) {
            showImageList(request, response, productId);
            return;
        }

        switch (mode) {
            case MODE_ADD -> showAddForm(request, response, productId);
            case MODE_EDIT -> showImageForm(request, response, productId, MODE_EDIT);
            case MODE_VIEW -> showImageForm(request, response, productId, MODE_VIEW);
            default -> response.sendRedirect(REDIRECT_LIST + productId + "&error=invalid_id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        Integer productId = parseInteger(request.getParameter("productId"));
        if (productId == null) {
            response.sendRedirect(PRODUCT_ADMIN_PAGE);
            return;
        }

        String action = trimToEmpty(request.getParameter("action"));

        try {
            switch (action) {
                case ACTION_CREATE -> createImage(request, response, productId);
                case ACTION_UPDATE -> updateImage(request, response, productId);
                case ACTION_DELETE -> deleteImage(request, response, productId);
                default -> response.sendRedirect(REDIRECT_LIST + productId + "&error=invalid_id");
            }
        } catch (Exception exception) {
            log("Error while handling product image action: " + action, exception);
            response.sendRedirect(REDIRECT_LIST + productId + "&error=true");
        }
    }

    private void showImageList(HttpServletRequest request, HttpServletResponse response, int productId)
            throws ServletException, IOException {
        List<ProductImage> images = productImageService.getImageByProduct(productId);
        request.setAttribute("images", images);
        request.setAttribute("productId", productId);
        request.setAttribute("totalImages", images.size());
        request.getRequestDispatcher(IMAGE_ADMIN_PAGE).forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response, int productId)
            throws ServletException, IOException {
        request.setAttribute("mode", MODE_ADD);
        request.setAttribute("productId", productId);
        request.getRequestDispatcher(IMAGE_FORM_PAGE).forward(request, response);
    }

    private void showImageForm(HttpServletRequest request, HttpServletResponse response, int productId, String mode)
            throws ServletException, IOException {
        Integer imageId = parseInteger(request.getParameter("id"));
        if (imageId == null) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=invalid_id");
            return;
        }

        ProductImage image = productImageService.getImageById(imageId);
        if (image == null || image.getProductId() != productId) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=not_found");
            return;
        }

        request.setAttribute("image", image);
        request.setAttribute("productId", productId);
        request.setAttribute("mode", mode);
        request.getRequestDispatcher(IMAGE_FORM_PAGE).forward(request, response);
    }

    private void createImage(HttpServletRequest request, HttpServletResponse response, int productId)
            throws ServletException, IOException {
        String imagePath = handleFileUpload(request, "imageFile");
        if (imagePath == null || imagePath.isBlank()) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=no_image");
            return;
        }

        ProductImage image = new ProductImage();
        image.setProductId(productId);
        image.setImageUrl(imagePath);
        image.setMain(isMainSelected(request));

        productImageService.createImage(image);
        response.sendRedirect(REDIRECT_LIST + productId);
    }

    private void updateImage(HttpServletRequest request, HttpServletResponse response, int productId)
            throws ServletException, IOException {
        Integer imageId = parseInteger(request.getParameter("id"));
        if (imageId == null) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=invalid_id");
            return;
        }

        ProductImage image = productImageService.getImageById(imageId);
        if (image == null || image.getProductId() != productId) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=not_found");
            return;
        }

        image.setMain(isMainSelected(request));

        String newImagePath = handleFileUpload(request, "imageFile");
        if (newImagePath != null && !newImagePath.isBlank()) {
            image.setImageUrl(newImagePath);
        }

        productImageService.updateImage(image);
        response.sendRedirect(REDIRECT_LIST + productId);
    }

    private void deleteImage(HttpServletRequest request, HttpServletResponse response, int productId)
            throws IOException {
        Integer imageId = parseInteger(request.getParameter("id"));
        if (imageId == null) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=invalid_id");
            return;
        }

        ProductImage image = productImageService.getImageById(imageId);
        if (image == null || image.getProductId() != productId) {
            response.sendRedirect(REDIRECT_LIST + productId + "&error=not_found");
            return;
        }

        productImageService.deleteImage(imageId);
        response.sendRedirect(REDIRECT_LIST + productId);
    }

    private boolean isMainSelected(HttpServletRequest request) {
        return "true".equals(request.getParameter("isMain"));
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String handleFileUpload(HttpServletRequest request, String fieldName)
            throws IOException, ServletException {
        Part filePart = request.getPart(fieldName);
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String submittedFileName = filePart.getSubmittedFileName();
        if (submittedFileName == null || submittedFileName.isBlank()) {
            return null;
        }

        String extension = extractFileExtension(submittedFileName);
        String storedFileName = buildStoredFileName(extension);

        String uploadPath = request.getServletContext().getRealPath("") + File.separator + IMAGE_FOLDER;
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists() && !uploadDirectory.mkdirs()) {
            throw new IOException("Cannot create upload directory: " + uploadPath);
        }

        String filePath = uploadPath + File.separator + storedFileName;
        filePart.write(filePath);
        return IMAGE_FOLDER + "/" + storedFileName;
    }

    private String extractFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex < 0 || lastDotIndex == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(lastDotIndex).replaceAll("[^a-zA-Z0-9.]", "");
    }

    private String buildStoredFileName(String extension) {
        return ("product_img_" + System.currentTimeMillis() + extension)
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
