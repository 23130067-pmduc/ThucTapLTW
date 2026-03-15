package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.BannerService;
import vn.edu.nlu.fit.thuctapltw.model.Banner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebServlet(name = "BannerAdminController", value = "/banner-admin")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class BannerAdminController extends HttpServlet {
    private BannerService bannerService;

    @Override
    public void init(){
        bannerService = new BannerService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");

        if (mode == null){
            List<Banner> banners = bannerService.getAllBanner();

            int total = banners.size();

            int totalActive = bannerService.getActiveBanners().size();

            request.setAttribute("banners", banners);
            request.setAttribute("total", total);
            request.setAttribute("totalActive", totalActive);

            request.getRequestDispatcher("/banner-admin.jsp").forward(request,response);
            return;
        }

        if ("edit".equals(mode)){
            int id = Integer.parseInt(request.getParameter("id"));
            Banner banner = bannerService.getBannerById(id);

            request.setAttribute("banner", banner);
            request.setAttribute("mode", mode);

            request.getRequestDispatcher("banner-form.jsp").forward(request,response);
            return;
        }

        if ("add".equals(mode)){
            request.setAttribute("mode", mode);

            request.getRequestDispatcher("banner-form.jsp").forward(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("block".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            bannerService.deleteBanner(id);

            response.sendRedirect("banner-admin");
            return;
        }



        String uploadDir = getServletContext().getRealPath("/uploads/shopquanao/banner/");

        File dir = new File(uploadDir);
        if (!dir.exists()){
            dir.mkdirs();
        }

        Part filePart = request.getPart("imageFile");
        String fileName = "";

        if (filePart != null && filePart.getSubmittedFileName() != null) {
            fileName = Path.of(filePart.getSubmittedFileName())
                    .getFileName().toString();
        }

        if ("create".equals(action)){
            String imageUrl = null;
            if (!fileName.isEmpty()) {
                filePart.write(uploadDir + fileName);
                imageUrl = "./uploads/shopquanao/banner/" + fileName;
            }

            Banner banner = new Banner();

            banner.setImageUrl(imageUrl);
            banner.setTitle(request.getParameter("title"));
            banner.setNavigateTo(request.getParameter("navigateTo"));

            int status = Integer.parseInt(request.getParameter("status"));

            banner.setStatus(status == 1);

            bannerService.createBanner(banner);

            response.sendRedirect("banner-admin");
            return;
        }

        if ("update".equals(action)){
            int id = Integer.parseInt(request.getParameter("id"));

            Banner oldBanner = bannerService.getBannerById(id);
            String imageUrl = oldBanner.getImageUrl();

            if (!fileName.isEmpty()) {
                filePart.write(uploadDir + fileName);
                imageUrl = "./uploads/shopquanao/banner/" + fileName;
            }

            Banner banner = new Banner();

            banner.setId(id);
            banner.setImageUrl(imageUrl);
            banner.setTitle(request.getParameter("title"));
            banner.setNavigateTo(request.getParameter("navigateTo"));

            int status = Integer.parseInt(request.getParameter("status"));

            banner.setStatus(status == 1);

            bannerService.updateBanner(banner);

            response.sendRedirect("banner-admin");
            return;
        }
    }
}