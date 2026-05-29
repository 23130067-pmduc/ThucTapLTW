package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;
import vn.edu.nlu.fit.thuctapltw.Service.PromotionEventService;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;

import java.io.IOException;

@WebServlet("/khuyen-mai")
public class PromotionController extends HttpServlet {
    private ProductDao productDao;
    private VoucherService voucherService;
    private PromotionEventService promotionEventService;

    @Override
    public void init() {
        productDao = new ProductDao();
        voucherService = new VoucherService();
        promotionEventService = new PromotionEventService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("vouchers", voucherService.getActiveOrderAndProductVouchers());
        request.setAttribute("shippingVouchers", voucherService.getActiveShippingVouchers());
        request.setAttribute("promotionEvents", promotionEventService.getActiveEventsWithProducts());
        request.setAttribute("flashSaleProducts", productDao.findFlashSaleProducts(4));
        request.setAttribute("discountProducts", productDao.findDiscountProducts());

        request.getRequestDispatcher("/khuyenmai.jsp")
                .forward(request, response);
    }
}
