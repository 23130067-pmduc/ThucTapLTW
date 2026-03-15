package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;

import java.io.IOException;

@WebServlet("/khuyen-mai")
public class PromotionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao productDao = new ProductDao();

        request.setAttribute(
                "flashSaleProducts",
                productDao.findFlashSaleProducts(4)
        );

        request.setAttribute(
                "discountProducts",
                productDao.findDiscountProducts()
        );

        request.getRequestDispatcher("/khuyenmai.jsp")
                .forward(request, response);
    }

}
