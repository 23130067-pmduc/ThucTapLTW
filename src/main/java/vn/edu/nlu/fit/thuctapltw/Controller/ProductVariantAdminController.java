package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ProductVariantService;
import vn.edu.nlu.fit.thuctapltw.model.ProductVariant;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductVariantAdminController", value = "/product-variant-admin")
public class ProductVariantAdminController extends HttpServlet {
    private ProductVariantService productVariantService;

    @Override
    public void init() {
        productVariantService = new ProductVariantService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdRaw = request.getParameter("productId");
        if (isBlank(productIdRaw)) {
            response.sendRedirect("product-admin");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect("product-admin");
            return;
        }

        String mode = request.getParameter("mode");

        if (isBlank(mode)) {
            loadVariantList(request, productId);
            request.getRequestDispatcher("/product-variant-admin.jsp").forward(request, response);
            return;
        }

        if ("add".equals(mode)) {
            loadFormOptions(request, productId);
            request.setAttribute("mode", "add");
            request.getRequestDispatcher("/product-variant-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            String idRaw = request.getParameter("id");
            if (isBlank(idRaw)) {
                response.sendRedirect("product-variant-admin?productId=" + productId);
                return;
            }

            int id;
            try {
                id = Integer.parseInt(idRaw);
            } catch (NumberFormatException e) {
                response.sendRedirect("product-variant-admin?productId=" + productId);
                return;
            }

            ProductVariant variant = productVariantService.getVariantById(id);
            if (variant == null) {
                response.sendRedirect("product-variant-admin?productId=" + productId);
                return;
            }

            request.setAttribute("variant", variant);
            request.setAttribute("mode", mode);
            loadFormOptions(request, variant.getProductId());
            request.getRequestDispatcher("/product-variant-form.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("product-variant-admin?productId=" + productId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (isBlank(action)) {
            response.sendRedirect("product-admin");
            return;
        }

        switch (action) {
            case "create":
                handleCreate(request, response);
                return;
            case "update":
                handleUpdate(request, response);
                return;
            case "delete":
                handleDelete(request, response);
                return;
            default:
                response.sendRedirect("product-admin");
        }
    }

    private void handleCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ProductVariant variant = new ProductVariant();
            variant.setProductId(parseRequiredInt(request.getParameter("productId")));
            variant.setSizeId(parseRequiredInt(request.getParameter("sizeId")));
            variant.setColorId(parseRequiredInt(request.getParameter("colorId")));
            variant.setStock(parseOptionalInt(request.getParameter("stock"), 0));
            variant.setPrice(parseOptionalDouble(request.getParameter("price"), 0.0));
            variant.setSalePrice(parseOptionalDouble(request.getParameter("salePrice"), 0.0));

            productVariantService.createVariant(variant);
            response.sendRedirect("product-variant-admin?productId=" + variant.getProductId());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ: " + e.getMessage());
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ProductVariant variant = new ProductVariant();
            variant.setId(parseRequiredInt(request.getParameter("id")));
            variant.setProductId(parseRequiredInt(request.getParameter("productId")));
            variant.setSizeId(parseRequiredInt(request.getParameter("sizeId")));
            variant.setColorId(parseRequiredInt(request.getParameter("colorId")));
            variant.setStock(parseOptionalInt(request.getParameter("stock"), 0));
            variant.setPrice(parseOptionalDouble(request.getParameter("price"), 0.0));
            variant.setSalePrice(parseOptionalDouble(request.getParameter("salePrice"), 0.0));

            productVariantService.updateVariant(variant);
            response.sendRedirect("product-variant-admin?productId=" + variant.getProductId());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ: " + e.getMessage());
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = parseRequiredInt(request.getParameter("id"));
            int productId = parseRequiredInt(request.getParameter("productId"));

            productVariantService.deleteVariant(id);
            response.sendRedirect("product-variant-admin?productId=" + productId);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ: " + e.getMessage());
        }
    }

    private void loadVariantList(HttpServletRequest request, int productId) {
        List<ProductVariant> variants = productVariantService.getProductVariantByProductId(productId);

        request.setAttribute("variants", variants);
        request.setAttribute("productId", productId);
        request.setAttribute("total", productVariantService.countVariant(productId));
        request.setAttribute("totalStock", productVariantService.countStock(productId));
        request.setAttribute("totalSize", productVariantService.countSize(productId));
        request.setAttribute("totalColor", productVariantService.countColor(productId));
    }

    private void loadFormOptions(HttpServletRequest request, int productId) {
        request.setAttribute("productId", productId);
        request.setAttribute("sizes", productVariantService.getAllSizes());
        request.setAttribute("colors", productVariantService.getAllColors());
    }

    private int parseRequiredInt(String value) {
        if (isBlank(value)) {
            throw new NumberFormatException("Thiếu giá trị số bắt buộc");
        }
        return Integer.parseInt(value.trim());
    }

    private int parseOptionalInt(String value, int defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    private double parseOptionalDouble(String value, double defaultValue) {
        if (isBlank(value)) {
            return defaultValue;
        }
        return Double.parseDouble(value.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
