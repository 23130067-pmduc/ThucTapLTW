package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.fit.thuctapltw.Service.AddressService;
import vn.edu.nlu.fit.thuctapltw.model.Address;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/dia-chi")
public class AddressController extends HttpServlet {

    private AddressService addressService;

    @Override
    public void init() {
        addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        List<Address> addressList = addressService.getAddressesByUser(user.getId());
        request.setAttribute("addressList", addressList);

        String success = request.getParameter("success");
        if (success != null && !success.isBlank()) {
            request.setAttribute("success", success);
        }

        String error = request.getParameter("error");
        if (error != null && !error.isBlank()) {
            request.setAttribute("error", error);
        }

        request.setAttribute("pageCss", "diachi.css");
        request.setAttribute("pageTitle", "Địa chỉ của tôi");
        request.getRequestDispatcher("/diachi.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        User user = getLoggedInUser(request, response);
        if (user == null) {
            return;
        }

        String action = request.getParameter("action");

        try {
            switch (action == null ? "" : action) {
                case "add" -> handleAdd(request, user);
                case "update" -> handleUpdate(request, user);
                case "setDefault" -> handleSetDefault(request, user);
                case "delete" -> handleDelete(request, user);
                default -> throw new RuntimeException("Thao tác không hợp lệ");
            }

            String redirectTo = request.getParameter("redirectTo");
            String basePath = (redirectTo != null && !redirectTo.isBlank()) ? (request.getContextPath() + redirectTo) : (request.getContextPath() + "/dia-chi");

            redirectWithMessage(response, basePath, "success", getSuccessMessage(action));
        } catch (RuntimeException e) {
            String redirectTo = request.getParameter("redirectTo");
            String basePath = (redirectTo != null && !redirectTo.isBlank()) ? (request.getContextPath() + redirectTo) : (request.getContextPath() + "/dia-chi");

            redirectWithMessage(response, basePath, "error", e.getMessage());
        }
    }

    private void handleAdd(HttpServletRequest request, User user) {
        Address address = buildAddressFromRequest(request, user.getId());
        addressService.addAddress(address);
    }

    private void handleUpdate(HttpServletRequest request, User user) {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            throw new RuntimeException("Thiếu mã địa chỉ cần cập nhật");
        }

        Address address = buildAddressFromRequest(request, user.getId());
        address.setId(Integer.parseInt(idParam));
        addressService.updateAddress(address);
    }

    private void handleSetDefault(HttpServletRequest request, User user) {
        int addressId = parseAddressId(request.getParameter("id"));
        addressService.setDefaultAddress(addressId, user.getId());
    }

    private void handleDelete(HttpServletRequest request, User user) {
        int addressId = parseAddressId(request.getParameter("id"));
        addressService.deleteAddress(addressId, user.getId());
    }

    private Address buildAddressFromRequest(HttpServletRequest request, int userId) {
        Address address = new Address();
        address.setUserId(userId);
        address.setReceiverName(trim(request.getParameter("receiverName")));
        address.setPhone(trim(request.getParameter("phone")));
        address.setCity(trim(request.getParameter("city")));
        address.setDistrict(trim(request.getParameter("district")));
        address.setWard(trim(request.getParameter("ward")));
        address.setDetailAddress(trim(request.getParameter("detailAddress")));
        address.setDefaultAddress(request.getParameter("isDefault") != null);
        address.setActive(true);
        return address;
    }

    private int parseAddressId(String idParam) {
        if (idParam == null || idParam.isBlank()) {
            throw new RuntimeException("Thiếu mã địa chỉ");
        }
        try {
            return Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Mã địa chỉ không hợp lệ");
        }
    }

    private User getLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userlogin") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return (User) session.getAttribute("userlogin");
    }

    private void redirectWithMessage(HttpServletResponse response, String baseUrl, String key, String message)
            throws IOException {
        response.sendRedirect(baseUrl + "?" + key + "=" + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }

    private String getSuccessMessage(String action) {
        return switch (action) {
            case "add" -> "Thêm địa chỉ thành công";
            case "update" -> "Cập nhật địa chỉ thành công";
            case "setDefault" -> "Đã đặt địa chỉ mặc định";
            case "delete" -> "Xóa địa chỉ thành công";
            default -> "Thao tác thành công";
        };
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
