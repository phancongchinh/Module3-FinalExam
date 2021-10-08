package com.codegym.dao.product;

import com.codegym.dao.DBConnection;
import com.codegym.dao.category.CategoryDAO;
import com.codegym.dao.category.ICategoryDAO;
import com.codegym.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IProductDAO {

    private static final Connection CONNECTION = DBConnection.getConnection();

    private static final ICategoryDAO CATEGORY_DAO = new CategoryDAO();

    public static final String SQL_GEL_ALL = "SELECT * FROM product;";
    public static final String SQL_INSERT = "INSERT INTO product (name, price, quantity, color, description, category_id ) " +
            "VALUES (?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE product " +
            "SET name=?, price=?, quantity=?, color=?, description=?, category_id=? WHERE id=?";
    public static final String SQL_DELETE = "DELETE FROM product WHERE id=?";
    public static final String SQL_FIND_BY_ID = "SELECT * FROM product WHERE id=?";

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_GEL_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                int categoryId = resultSet.getInt("category_id");
                Product product = new Product(id, name, price, quantity, color, description, categoryId);
                product.setCategory(CATEGORY_DAO.findById(categoryId));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean save(Product product) {
        boolean isSaved = false;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_INSERT);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getColor());
            statement.setString(5, product.getDescription());
            statement.setInt(6, product.getCategoryId());
            isSaved = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSaved;
    }

    @Override
    public boolean update(int id, Product product) {
        boolean isUpdated = false;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_UPDATE);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setString(4, product.getColor());
            statement.setString(5, product.getDescription());
            statement.setInt(6, product.getCategoryId());
            statement.setInt(7, id);
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_DELETE);
            statement.setInt(1, id);
            isDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    @Override
    public Product findById(int id) {
        Product product = null;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                int categoryId = resultSet.getInt("category_id");
                product = new Product(id, name, price, quantity, color, description, categoryId);
                product.setCategory(CATEGORY_DAO.findById(categoryId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> searchByName(String keyword) {
        List<Product> products = new ArrayList<>();
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM product WHERE name LIKE ?");
            statement.setString(1, keyword);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("quantity");
                String color = resultSet.getString("color");
                String description = resultSet.getString("description");
                int categoryId = resultSet.getInt("category_id");
                Product product = new Product(id, productName, price, quantity, color, description, categoryId);
                product.setCategory(CATEGORY_DAO.findById(categoryId));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
