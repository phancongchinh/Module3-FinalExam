package com.codegym.dao.category;

import com.codegym.dao.DBConnection;
import com.codegym.model.Category;
import com.codegym.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements ICategoryDAO {

    private static final Connection CONNECTION = DBConnection.getConnection();
    private static final String SQL_GET_ALL = "SELECT * FROM category";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM category WHERE id=?";

    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_GET_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Category category = new Category(id, name);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public boolean save(Category category) {
        return false;
    }

    @Override
    public boolean update(int id, Category category) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Category findById(int id) {
        Category category = null;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                category = new Category(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
