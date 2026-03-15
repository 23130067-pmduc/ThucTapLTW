package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.ProductService;
import vn.edu.nlu.fit.thuctapltw.model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductController", value = "/san-pham")
public class ProductController extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String group = request.getParameter("group");
        String category = request.getParameter("category");
        String sort = request.getParameter("sort");

        List<Product> products;

        Integer categoryId = null;
        if (category != null && !category.isEmpty()) {
            try{
                categoryId = Integer.parseInt(category);
            } catch (NumberFormatException e){}

        }

        if (categoryId != null){
            products = productService.getProductsByCategory(categoryId);



        } else if ("betrai".equals(group)) {
            products = productService.getProductsByCategories(List.of(1, 2, 3, 9));
        } else if ("begai".equals(group)) {
            products = productService.getProductsByCategories(List.of(4, 5, 6, 7, 9));
        } else if ("phukien".equals(group)) {
            products = productService.getProductsByCategories(List.of(8,9, 10));

        } else {
            products = productService.getAllProducts();
        }


        if ( sort != null && !sort.isEmpty()){
            switch (sort) {
                case "new":
                    products = productService.sortByNewest(products);
                    break;


                case "best":
                    products = productService.sortByBestSeller(products);
                    break;


                case "sale":
                    products = productService.sortBySale(products);
                    break;


                case "price_asc":
                    products = productService.sortByPriceAsc(products);
                    break;


                case "price_desc":
                    products = productService.sortByPriceDesc(products);
                    break;
            }


        }

        request.setAttribute("list", products);
        request.getRequestDispatcher("/sanpham.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}