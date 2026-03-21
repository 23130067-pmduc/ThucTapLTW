package vn.edu.nlu.fit.thuctapltw.Controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ProductVariantService;
import vn.edu.nlu.fit.thuctapltw.model.ProductVariant;

@WebServlet(name = "ProductVariantAdminController", value = "/product-variant-admin")
public class ProductVariantAdminController extends HttpServlet {
    private ProductVariantService productVariantService;

    @Override
    public void init(){
        productVariantService = new ProductVariantService();
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productIdRaw = request.getParameter("productId");
        if (productIdRaw == null || productIdRaw.isEmpty()) {
            response.sendRedirect("product-admin");
            return;
        }

        String mode = request.getParameter("mode");
        int productId = Integer.parseInt(request.getParameter("productId"));

        if (mode == null) {

            List<ProductVariant> variants =
                    productVariantService.getProductVariantByProductId(productId);

            request.setAttribute("variants", variants);
            request.setAttribute("productId", productId);

            request.setAttribute("total", productVariantService.countVariant(productId));
            request.setAttribute("totalStock", productVariantService.countStock(productId));
            request.setAttribute("totalSize", productVariantService.countSize(productId));
            request.setAttribute("totalColor", productVariantService.countColor(productId));

            request.getRequestDispatcher("/product-variant-admin.jsp").forward(request, response);
            return;
        }


        if ("add".equals(mode)) {

            request.setAttribute("mode", "add");
            request.setAttribute("productId", productId);
            request.setAttribute("sizes", productVariantService.getAllSizes());
            request.setAttribute("colors", productVariantService.getAllColors());

            request.getRequestDispatcher("/product-variant-form.jsp").forward(request, response);
            return;
        }

        if ("edit".equals(mode)) {

            int id = Integer.parseInt(request.getParameter("id"));

            ProductVariant variant = productVariantService.getVariantById(id);


            request.setAttribute("variant", variant);
            request.setAttribute("productId", variant.getProductId());
            request.setAttribute("sizes", productVariantService.getAllSizes());
            request.setAttribute("colors", productVariantService.getAllColors());
            request.setAttribute("mode", "edit");

            request.getRequestDispatcher("/product-variant-form.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("create".equals(action)) {

            try {
                ProductVariant variant = new ProductVariant();

                String productIdParam = request.getParameter("productId");
                String sizeIdParam = request.getParameter("sizeId");
                String colorIdParam = request.getParameter("colorId");
                String stockParam = request.getParameter("stock");
                String priceParam = request.getParameter("price");
                String salePriceParam = request.getParameter("salePrice");

                // Debug log
                System.out.println("productId: " + productIdParam);
                System.out.println("sizeId: " + sizeIdParam);
                System.out.println("colorId: " + colorIdParam);
                System.out.println("stock: " + stockParam);
                System.out.println("price: " + priceParam);
                System.out.println("salePrice: " + salePriceParam);

                variant.setProductId(Integer.parseInt(productIdParam));
                variant.setSizeId(Integer.parseInt(sizeIdParam));
                variant.setColorId(Integer.parseInt(colorIdParam));
                variant.setStock(stockParam != null && !stockParam.isEmpty() 
                    ? Integer.parseInt(stockParam) : 0);
                variant.setPrice(priceParam != null && !priceParam.isEmpty() 
                    ? Double.parseDouble(priceParam) : 0.0);
                variant.setSalePrice(salePriceParam != null && !salePriceParam.isEmpty() 
                    ? Double.parseDouble(salePriceParam) : 0.0);

                productVariantService.createVariant(variant);

                response.sendRedirect(
                        "product-variant-admin?productId=" + variant.getProductId()
                );
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Lỗi định dạng số: " + e.getMessage());
            }
            return;
        }


        if ("update".equals(action)) {

            ProductVariant variant = new ProductVariant();

            variant.setId(Integer.parseInt(request.getParameter("id")));
            variant.setProductId(Integer.parseInt(request.getParameter("productId")));
            
            // Parse stock với xử lý null/empty
            String stockParam = request.getParameter("stock");
            variant.setStock(stockParam != null && !stockParam.isEmpty() 
                ? Integer.parseInt(stockParam) : 0);
            
            // Parse price với xử lý null/empty
            String priceParam = request.getParameter("price");
            variant.setPrice(priceParam != null && !priceParam.isEmpty() 
                ? Double.parseDouble(priceParam) : 0.0);
            
            // Parse salePrice với xử lý null/empty
            String salePriceParam = request.getParameter("salePrice");
            variant.setSalePrice(salePriceParam != null && !salePriceParam.isEmpty() 
                ? Double.parseDouble(salePriceParam) : 0.0);

            productVariantService.updateVariant(variant);

            response.sendRedirect("product-variant-admin?productId=" + variant.getProductId()
            );
            return;
        }
        if ("delete".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            int productId = Integer.parseInt(request.getParameter("productId"));

            productVariantService.deleteVariant(id);

            response.sendRedirect("product-variant-admin?productId=" + productId
            );
        }
    }
}