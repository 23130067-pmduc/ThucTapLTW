package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.*;
import vn.edu.nlu.fit.thuctapltw.model.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "QuickAddInfoController", value = "/quick-add-info")
public class QuickAddInfoController extends HttpServlet {

    private ProductService productService;
    private ProductImageService productImageService;
    private ColorService colorService;
    private SizeService sizeService;
    private ReviewService reviewService;
    private ProductVariantService productVariantService;

    @Override
    public void init(){
        productService = new ProductService();
        productImageService = new ProductImageService();
        productVariantService = new ProductVariantService();
        colorService = new ColorService();
        sizeService = new SizeService();
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String idRaw = request.getParameter("id");

        if(idRaw == null || idRaw.isEmpty()){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int id = Integer.parseInt(idRaw);

            Product product = productService.getProductById(id);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }


            List<ProductImage> listImage = productImageService.getImageByProduct(id);

            String mainImage = null;
            if (listImage != null) {
                for (ProductImage img : listImage) {
                    if (img.getMain()) {
                        mainImage = img.getImageUrl();
                        break;
                    }
                }

                if (mainImage == null && !listImage.isEmpty()) {
                    mainImage = listImage.get(0).getImageUrl();
                }
            }

            List<Color> listColor = colorService.getColorByProductId(id);
            List<Size> listSize = sizeService.getSizeByProductId(id);
            List<ProductVariant> listVariant = productVariantService.getVariantByProductId(id);

            Map<String, Object> data = new HashMap<>();
            data.put("name", product.getName());
            data.put("price", product.getPrice());
            data.put("image", mainImage);
            data.put("colors", listColor);
            data.put("sizes", listSize);
            data.put("variants", listVariant);

            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(data));

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}