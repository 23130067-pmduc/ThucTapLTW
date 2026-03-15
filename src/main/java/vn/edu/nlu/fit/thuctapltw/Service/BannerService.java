package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.BannerDao;
import vn.edu.nlu.fit.thuctapltw.model.Banner;

import java.util.List;

public class BannerService {
    private final BannerDao bannerDao = new BannerDao();

    public List<Banner> getActiveBanners() {
        return  bannerDao.findActiveBanners();
    }

    public List<Banner> getAllBanner() {
        return bannerDao.getAllBanner();
    }

    public void createBanner(Banner banner) {
        bannerDao.createBanner(banner);
    }


    public void updateBanner(Banner banner) {
        bannerDao.updateBanner(banner);
    }

    public void deleteBanner(int id) {
        bannerDao.deleteBanner(id , false);
    }

    public Banner getBannerById(int id) {
        return bannerDao.getBannerById(id);
    }
}
