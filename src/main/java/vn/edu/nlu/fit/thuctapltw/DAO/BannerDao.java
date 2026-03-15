package vn.edu.nlu.fit.thuctapltw.DAO;

import org.jdbi.v3.core.Jdbi;
import vn.edu.nlu.fit.thuctapltw.model.Banner;

import java.util.List;

public class BannerDao extends BaseDao{
    public List<Banner> findActiveBanners() {
        Jdbi jdbi = getJdbi();

        return jdbi.withHandle(handle ->
                handle.createQuery(
                        "SELECT id, image_url, navigate_to, status, created_at " +
                                "FROM banners " +
                                "WHERE status = 1 " +
                                "ORDER BY id ASC"
                ).mapToBean(Banner.class).list()
        );

    }

    public List<Banner> getAllBanner() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM banners""")
                .mapToBean(Banner.class)
                .list());
    }

    public void createBanner(Banner banner) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                INSERT INTO banners(id, image_url, navigate_to, title, status, created_at)
                VALUES(:id, :imageUrl, :navigateTo, :title, :status, NOW())""")
                .bindBean(banner)
                .execute());
    }

    public Banner getBannerById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, image_url, navigate_to, title, status, created_at
                FROM banners
                WHERE id = :id""")
                .bind("id", id)
                .mapToBean(Banner.class)
                .findOne()
                .orElse(null));
    }

    public void updateBanner(Banner banner) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE banners
                SET image_url = :imageUrl,
                navigate_to = :navigateTo,
                title = :title,
                status = :status
                WHERE id = :id
                """).bind("imageUrl", banner.getImageUrl())
                .bind("navigateTo", banner.getNavigateTo())
                .bind("title", banner.getTitle())
                .bind("status", banner.isStatus())
                .bind("id", banner.getId())
                .execute());
    }


    public void deleteBanner(int id, boolean status) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE banners
                SET status =:status
                WHERE id = :id""")
                .bind("status",status)
                .bind("id",id)
                .execute());
    }
}
