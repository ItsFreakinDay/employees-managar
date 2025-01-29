package employees.manager.module.dao;

import employees.manager.module.Conn;
import employees.manager.module.models.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {
    private final Connection connection;

    public DepartmentDao() {
        connection = Conn.getInstance().getConnection();
    }

    // Метод для получения всех отделов
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM Departments ORDER BY Name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                departments.add(new Department(rs.getInt("ID"), rs.getString("Name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    // Метод для добавления нового отдела
    public boolean addDepartment(String name) {
        String query = "INSERT INTO Departments (Name) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для обновления отдела
    public boolean updateDepartment(int id, String name) {
        String query = "UPDATE Departments SET Name = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для удаления отдела
    public boolean deleteDepartment(int id) {
        String query = "DELETE FROM Departments WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
