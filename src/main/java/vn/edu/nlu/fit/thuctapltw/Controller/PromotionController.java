package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.DAO.ProductDao;
import vn.edu.nlu.fit.thuctapltw.Service.VoucherService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/khuyen-mai")
public class PromotionController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao productDao = new ProductDao();
        VoucherService voucherService = new VoucherService();

        List<Map<String, String>> promotionEvents = List.of(
                Map.of(
                        "icon", "fa-child",
                        "title", "Tết thiếu nhi",
                        "description", "Ưu đãi quần áo và phụ kiện đáng yêu cho bé",
                        "tag", "Giảm đến 40%"
                ),
                Map.of(
                        "icon", "fa-school",
                        "title", "Back to school",
                        "description", "Đồ đi học, balo và outfit năng động cho bé",
                        "tag", "Combo tiết kiệm"
                ),
                Map.of(
                        "icon", "fa-sun",
                        "title", "Đồ hè cho bé",
                        "description", "Chất liệu thoáng mát, phù hợp ngày nắng nóng",
                        "tag", "Mua nhiều giảm thêm"
                ),
                Map.of(
                        "icon", "fa-bolt",
                        "title", "Flash sale cuối tuần",
                        "description", "Săn deal giá tốt trong thời gian giới hạn",
                        "tag", "Số lượng có hạn"
                )
        );

        request.setAttribute("vouchers", voucherService.getActiveOrderAndProductVouchers());
        request.setAttribute("promotionEvents", promotionEvents);
        request.setAttribute("flashSaleProducts", productDao.findFlashSaleProducts(4));
        request.setAttribute("discountProducts", productDao.findDiscountProducts());

        request.getRequestDispatcher("/khuyenmai.jsp")
                .forward(request, response);
    }
}
