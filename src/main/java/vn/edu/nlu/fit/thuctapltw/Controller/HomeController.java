package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.BannerService;
import vn.edu.nlu.fit.thuctapltw.Service.ProductService;

import java.io.IOException;

@WebServlet(name = "HomeController", value = "/trang-chu")
public class HomeController extends HttpServlet {

    private ProductService productService;
    private BannerService bannerService;

    @Override
    public void init() {
        productService = new ProductService();
        bannerService = new BannerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("banners", bannerService.getActiveBanners());


        request.setAttribute("latestProducts",
                productService.getLatestProducts(8));

        request.setAttribute("boyProducts",
                productService.getBoyProducts(8));

        request.setAttribute("girlProducts",
                productService.getGirlProducts(8));

        request.setAttribute("accessoryProducts",
                productService.getAccessoryProducts(8));


        request.getRequestDispatcher("/trangchu.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}