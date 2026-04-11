package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.ReviewService;
import vn.edu.nlu.fit.thuctapltw.model.Review;
import vn.edu.nlu.fit.thuctapltw.model.User;


import java.io.IOException;

@WebServlet("/review")
public class ReviewController extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init(){
        reviewService = new ReviewService();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        User user = (User) request.getSession().getAttribute("userlogin");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int productId = Integer.parseInt(request.getParameter("product_id"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        int userId = user.getId();

        Review review = new Review();
        review.setProductId(productId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);

        reviewService.addOrUpdateReview(review);


        response.sendRedirect("chi-tiet-san-pham?id=" + productId);
    }
}