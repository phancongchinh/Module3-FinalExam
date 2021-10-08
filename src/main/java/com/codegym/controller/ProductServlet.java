package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.category.CategoryService;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.product.IProductService;
import com.codegym.service.product.ProductService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/products")
public class ProductServlet extends HttpServlet {

    private static final IProductService PRODUCT_SERVICE = new ProductService();

    private static int productCounter = PRODUCT_SERVICE.getAll().size();

    private static final ICategoryService CATEGORY_SERVICE = new CategoryService();

    private static final String ERROR_404_JSP = "/error-404.jsp";
    public static final String PRODUCT_LIST_JSP = "/product/list.jsp";
    public static final String PRODUCT_VIEW_JSP = "/product/view.jsp";
    private static final String PRODUCT_UPDATE_JSP = "/product/update.jsp";
    private static final String PRODUCT_DELETE_JSP = "/product/delete.jsp";
    private static final String PRODUCT_CREATE_JSP = "/product/create.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                showCreateForm(request, response);
                break;
            }
            case "view": {
                showViewForm(request, response);
                break;
            }
            case "update": {
                showEditForm(request, response);
                break;
            }
            case "delete": {
                showDeleteForm(request, response);
                break;
            }
            default: {
                showAllProduct(request, response);
                break;
            }
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = PRODUCT_SERVICE.getAll();
        List<Category> categories = CATEGORY_SERVICE.getAll();
        RequestDispatcher dispatcher = request.getRequestDispatcher(PRODUCT_CREATE_JSP);
        request.setAttribute("productCounter", productCounter);
        try {
            request.setAttribute("categories", categories);
            request.setAttribute("products", products);
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showViewForm(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = PRODUCT_SERVICE.getAll();
        int id = Integer.parseInt(request.getParameter("id"));
        Product productFound = PRODUCT_SERVICE.findById(id);
        RequestDispatcher dispatcher;
        request.setAttribute("productCounter", productCounter);
        if (productFound == null) {
            dispatcher = request.getRequestDispatcher(ERROR_404_JSP);
        } else {
            request.setAttribute("product", productFound);
            request.setAttribute("products", products);
            dispatcher = request.getRequestDispatcher(PRODUCT_VIEW_JSP);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = PRODUCT_SERVICE.getAll();
        List<Category> categories = CATEGORY_SERVICE.getAll();
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = PRODUCT_SERVICE.findById(id);
        RequestDispatcher dispatcher;
        request.setAttribute("productCounter", productCounter);
        if (product == null) {
            dispatcher = request.getRequestDispatcher(ERROR_404_JSP);
        } else {
            request.setAttribute("product", product);
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            dispatcher = request.getRequestDispatcher(PRODUCT_UPDATE_JSP);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteForm(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = PRODUCT_SERVICE.getAll();
        List<Category> categories = CATEGORY_SERVICE.getAll();
        int id = Integer.parseInt(request.getParameter("id"));
        Product oldProduct = PRODUCT_SERVICE.findById(id);
        RequestDispatcher dispatcher;
        request.setAttribute("productCounter", productCounter);
        if (oldProduct == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            request.setAttribute("product", oldProduct);
            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            dispatcher = request.getRequestDispatcher(PRODUCT_DELETE_JSP);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showAllProduct(HttpServletRequest request, HttpServletResponse response) {
        String keyword =request.getParameter("keyword");
        List<Category> categories = CATEGORY_SERVICE.getAll();
        List<Product> products;
        if (keyword == null) {
            products = PRODUCT_SERVICE.getAll();
        } else {
            products = PRODUCT_SERVICE.searchByName(keyword);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(PRODUCT_LIST_JSP);
        request.setAttribute("productCounter", productCounter);
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                createProduct(request, response);
                break;
            }
            case "update": {
                editProduct(request, response);
                break;
            }
            case "delete": {
                deleteProduct(request, response);
                break;
            }
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Product product = new Product(name, price, quantity, color, description, categoryId);
        PRODUCT_SERVICE.save(product);
        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        Product product = new Product(name, price, quantity, color, description, categoryId);
        PRODUCT_SERVICE.update(id, product);

        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        PRODUCT_SERVICE.delete(id);
        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
