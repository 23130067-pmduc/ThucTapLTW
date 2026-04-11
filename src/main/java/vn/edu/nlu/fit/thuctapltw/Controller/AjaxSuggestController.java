package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.fit.thuctapltw.Service.ProductService;
import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "AjaxSuggestController", value = "/ajax-suggest")
public class AjaxSuggestController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        String keyword = request.getParameter("keyword");
        PrintWriter out = response.getWriter();

        if (keyword == null || keyword.trim().isEmpty()) {
            out.print("[]");
            return;
        }

        ProductService ps = new ProductService();
        List<Product> list = ps.searchProducts(keyword.trim());

        int limit = Math.min(list.size(), 5);

        List<Map<String, Object>> resultList = new java.util.ArrayList<>();
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));

        for (int i = 0; i < limit; i++) {
            Product p = list.get(i);
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", p.getId());
            map.put("name", p.getName());
            map.put("price", p.getPrice());
            map.put("priceFormatted", nf.format(p.getPrice()) + "đ");
            map.put("sale_price", p.getSale_price());
            map.put("salePriceFormatted", p.getSale_price() > 0 ? nf.format(p.getSale_price()) + "đ" : "");
            map.put("thumbnail", p.getThumbnail() != null ? p.getThumbnail() : "");

            resultList.add(map);
        }

        com.google.gson.Gson gson = new com.google.gson.Gson();
        out.print(gson.toJson(resultList));
        out.flush();
    }


@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}