package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.*;
import vn.edu.nlu.fit.thuctapltw.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailController", value = "/chi-tiet-san-pham")
public class ProductDetailController extends HttpServlet {

    private ProductService productService;
    private ReviewService reviewService;
    private ProductImageService productImageService;
    private ColorService colorService;
    private SizeService sizeService;
    private ProductVariantService productVariantService;
    private UserService userService;

    @Override
    public void init()  {

        productService = new ProductService();
        reviewService = new ReviewService();
        productImageService = new ProductImageService();
        colorService = new ColorService();
        sizeService = new SizeService();
        productVariantService = new ProductVariantService();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        if(idRaw == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id = Integer.parseInt(idRaw);
        Product product = productService.getProductById(id);

        if(product == null){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        List<Review> reviews = reviewService.getReviewByProductID(id);


        List<Product> ralatedProducts = productService.ralatedProduct(id, 4);


        double avgRating = reviewService.getAvgRating(id);
        int totalReviews = reviewService.getTotalReviews(id);

        int displayStar = (int) Math.round(avgRating);

        List<ProductImage> listImage = productImageService.getImageByProduct(id);

        List<Color> listColor = colorService.getColorByProductId(id);

        List<Size> listSize = sizeService.getSizeByProductId(id);

        List<ProductVariant> listVariant = productVariantService.getVariantByProductId(id);

        User user = (User) request.getSession().getAttribute("userlogin");

        Review myReview = null;

        if(user != null){
            myReview = reviewService.getReviewByUserID(user.getId(), id);
        }


        request.setAttribute("variants", listVariant);
        request.setAttribute("sizes", listSize);
        request.setAttribute("colors", listColor);
        request.setAttribute("images", listImage);
        request.setAttribute("displayStar", displayStar);
        request.setAttribute("totalReviews", totalReviews);
        request.setAttribute("product", product);
        request.setAttribute("reviews",reviews);
        request.setAttribute("myReview", myReview);
        request.setAttribute("ralatedProducts",ralatedProducts);
        request.getRequestDispatcher("/chitietsanpham.jsp").forward(request, response);


    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}