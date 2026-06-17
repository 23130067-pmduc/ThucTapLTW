package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.CategoryDao;
import vn.edu.nlu.fit.thuctapltw.model.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDao categoryDao = new CategoryDao();

    public List<Category> findAllI() {
        return categoryDao.findAll();
    }

    public int getCategoryActive() {
        return categoryDao.getCategoryActive();
    }

    public Category findById(int id) {
        return categoryDao.findById(id);
    }

    public List<Category> findAllWithCountProduct() {
        return  categoryDao.findAllWithCountProduct();
    }


    public void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    public void create(Category category) {
        categoryDao.create(category);
    }

    public void deleteCategory(int id) {
        categoryDao.deleteCategory(id);
    }

    public int countAllCategory() {
        return categoryDao.countAllCategory();
    }

    public List<Category> getCategoryByPage(int pageSize, int offset) {
        return categoryDao.getCategoryByPage(pageSize, offset);
    }

    public int countCategoryByKeyword(String keyword) {
        return categoryDao.countCategoryByKeyword(keyword);
    }

    public List<Category> searchCategoryByPage(String keyword, int pageSize, int offset) {
        return categoryDao.searchCategoryByPage(keyword,pageSize,offset);
    }

    public void updateStatus(int id, int status) {
        categoryDao.updateStatus(id, status);
    }
}
