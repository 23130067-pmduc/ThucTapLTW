package vn.edu.nlu.fit.thuctapltw.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.DAO.CartDao;
import vn.edu.nlu.fit.thuctapltw.DAO.CartItemDao;
import vn.edu.nlu.fit.thuctapltw.DAO.SearchHistoryDao;
import vn.edu.nlu.fit.thuctapltw.Service.UserService;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "loginController", value = "/login")
public class loginController extends HttpServlet {
    private UserService userService;
    private CartDao cartDao;
    private static final String CLIENT_ID =
            "1001120059484-ffncp4n4pvstlq3v1q4gtlu0hprkcedd.apps.googleusercontent.com";

    @Override
    public void init() {
        userService = new UserService();
        cartDao = new CartDao();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String credential = request.getParameter("credential");

        if (credential != null && !credential.trim().isEmpty()) {
            handleGoogleLogin(request, response, credential);
            return;
        }



        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null
                || username.trim().isEmpty()
                || password.trim().isEmpty()) {

            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        User user = userService.login(username, password);

        if (user == null) {
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (user.getStatus() == null) {
            request.setAttribute("error", "Tài khoản chưa được kích hoạt");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
            request.setAttribute("error", "Tài khoản đã bị khóa");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (user.getIsActive() == 0) {
            request.setAttribute("error", "Tài khoản chưa xác nhận OTP. Vui lòng kiểm tra email.");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        createUserSession(request, user);

        if ("admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/trang-chu");
        }
    }

    private void createUserSession(HttpServletRequest request, User user){
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userlogin", user);

        Integer cartId = cartDao.findCartIdByUser(user.getId());
        if (cartId == null) {cartId = cartDao.createCart(user.getId());}
        session.setAttribute("cartId", cartId);
        int cartSize = new CartItemDao().countTotalQuantity(cartId);
        session.setAttribute("cartSize", cartSize);
        List<String> dbHistory = new SearchHistoryDao().getRecentKeywordsByUser(user.getId(), 6);
        session.setAttribute("searchHistory", dbHistory);
    }

    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response, String credential)
            throws ServletException, IOException {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList(CLIENT_ID)).build();

            GoogleIdToken idToken = verifier.verify(credential);

            if (idToken == null) {
                request.setAttribute("error", "Đăng nhập Google thất bại");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");


            User user = userService.findByEmail(email);


            if (user == null) {

                String userName = email.split("@")[0];
                user = new User();
                user.setUsername(userName);
                user.setEmail(email);
                user.setFullName(name);
                user.setRole("user");
                user.setStatus("ACTIVE");
                user.setIsActive(1);


                int newUserId = userService.createGoogleUser(user);

                user = userService.findById(newUserId);
            }

            if (user.getStatus() == null) {
                request.setAttribute("error", "Tài khoản chưa được kích hoạt");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            if ("BLOCKED".equalsIgnoreCase(user.getStatus())) {
                request.setAttribute("error", "Tài khoản Google này đã bị khóa");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            if (user.getIsActive() == 0) {
                request.setAttribute("error", "Tài khoản chưa xác nhận kích hoạt");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            createUserSession(request, user);

            if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/trang-chu");
            }

        } catch (GeneralSecurityException e) {
            throw new ServletException("Lỗi xác thực Google token", e);
        }
    }
}