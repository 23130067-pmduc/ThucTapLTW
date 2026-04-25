package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.AddressService;
import vn.edu.nlu.fit.thuctapltw.Service.GhnShippingService;
import vn.edu.nlu.fit.thuctapltw.model.Address;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

@WebServlet(name = "GhnShippingFeeController", value = "ghn_fee")
public class GhnShippingFeeController extends HttpServlet {
    private AddressService addressService;
    private GhnShippingService ghnShippingService;

    @Override
    public void init() {
        addressService = new AddressService();
        ghnShippingService = new GhnShippingService();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            writeError(response, "Chua dang nhap.");
            return;
        }

        User user = (User) session.getAttribute("userlogin");
        Integer addressId = parseInteger(request.getParameter("addressId"));
        if (addressId == null) {
            writeError(response, "Dia chi khong hop le.");
            return;
        }

        Address address = addressService.getAddressById(addressId, user.getId());
        if (address == null) {
            writeError(response, "Khong tim thay dia chi.");
            return;
        }

        GhnShippingService.ShippingQuote quote = ghnShippingService.calculateFee(address);

        JsonObject json = new JsonObject();
        json.addProperty("success", quote.success());
        json.addProperty("configured", ghnShippingService.isConfigured());
        json.addProperty("fee", quote.fee());
        json.addProperty("formattedFee", NumberFormat.getInstance(new Locale("vi", "VN")).format(quote.fee()));
        json.addProperty("message", quote.message() == null ? "" : quote.message());

        response.getWriter().write(json.toString());
    }

    private void writeError(HttpServletResponse response, String message) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("success", false);
        json.addProperty("configured", false);
        json.addProperty("fee", 0);
        json.addProperty("formattedFee", "0");
        json.addProperty("message", message);
        response.getWriter().write(json.toString());
    }

    private Integer parseInteger(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return null;
        }
    }



    @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}