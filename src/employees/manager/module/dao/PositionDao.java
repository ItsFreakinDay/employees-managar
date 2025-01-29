package employees.manager.module.dao;

import employees.manager.module.Conn;
import employees.manager.module.models.Position;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionDao {
    private final Connection connection;

    public PositionDao() {
        connection = Conn.getInstance().getConnection();
    }


    // Метод для получения всех должностей с отделами
    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        String query = "SELECT p.ID, p.Name, d.Name AS DepartmentName " +
                "FROM Positions p " +
                "LEFT JOIN PositionDepartments pd ON p.ID = pd.Position_ID " +
                "LEFT JOIN Departments d ON pd.Department_ID = d.ID " +
                "ORDER BY d.Name, p.Name";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                positions.add(new Position(
                        rs.getInt("ID"),
                        rs.getString("Name"),
                        rs.getString("DepartmentName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    // Метод для добавления новой должности
    public boolean addPosition(String name, int departmentId) {
        if (isPositionExists(name)) {
            return false; // Должность с таким именем уже существует
        }
        String query = "INSERT INTO Positions (Name, Department_ID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, departmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для обновления должности
    public boolean updatePosition(int id, String name, int departmentId) {
        if (isPositionExists(name)) {
            return false; // Должность с таким именем уже существует
        }
        String query = "UPDATE Positions SET Name = ?, Department_ID = ? WHERE ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setInt(2, departmentId);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для удаления должности
    public boolean deletePosition(int id) {
        String linkQuery = "DELETE FROM PositionDepartments WHERE Position_ID = ?";
        String query = "DELETE FROM Positions WHERE ID = ?";
        try (PreparedStatement linkStmt = connection.prepareStatement(linkQuery);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            linkStmt.setInt(1, id);
            stmt.setInt(1, id);
            return linkStmt.executeUpdate() > 0 && stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Метод для получения всех отделов
//    public List<String> getAllDepartments() {
//        List<String> departments = new ArrayList<>();
//        String query = "SELECT Name FROM Departments";
//        try (PreparedStatement stmt = connection.prepareStatement(query);
//             ResultSet resultSet = stmt.executeQuery()) {
//
//            while (resultSet.next()) {
//                departments.add(resultSet.getString("Name"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return departments;
//    }
    public Map<String, Integer> getDepartmentMap() {
        Map<String, Integer> departmentMap = new HashMap<>();
        String query = "SELECT ID, Name FROM Departments"; // Убедись, что таблица Departments не пустая!
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                departmentMap.put(rs.getString("Name"), rs.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Загруженные отделы: " + departmentMap); // Проверяем, загружаются ли данные
        return departmentMap;
    }


    // Проверить, существует ли должность с таким именем
    public boolean isPositionExists(String name) {
        String query = "SELECT COUNT(*) FROM Positions WHERE Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
