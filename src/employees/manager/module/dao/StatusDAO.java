package employees.manager.module.dao;

import employees.manager.module.Conn;
import employees.manager.module.models.EmployeeStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {
    private final Connection connection;

    public StatusDAO() {
        connection = Conn.getInstance().getConnection();
    }

    public List<EmployeeStatus> getStatuses(String filter, String status) {
        List<EmployeeStatus> statuses = new ArrayList<>();
        String query = """
            SELECT es.ID, e.PersonnelNumber, e.FullName, sv.Name AS Status, 
                   p.Name AS Position, d.Name AS Department, 
                   es.StartDate, es.EndDate 
            FROM employeestatuses es
            JOIN employees e ON es.Employee_ID = e.ID
            JOIN statusvalues sv ON es.Status_ID = sv.ID
            LEFT JOIN positions p ON es.Position_ID = p.ID
            LEFT JOIN departments d ON es.Department_ID = d.ID
            WHERE (e.FullName LIKE ? OR e.PersonnelNumber LIKE ?) 
              AND (? IS NULL OR sv.Name = ?)
            ORDER BY es.StartDate DESC;
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + filter + "%");
            stmt.setString(2, "%" + filter + "%");
            stmt.setString(3, status);
            stmt.setString(4, status);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeStatus statusObj = new EmployeeStatus();
                    statusObj.setId(rs.getInt("ID"));
                    statusObj.setEmployeeId(rs.getInt("PersonnelNumber"));
                    statusObj.setFullName(rs.getString("FullName"));
                    statusObj.setStatus(rs.getString("Status"));
                    statusObj.setPosition(rs.getString("Position"));
                    statusObj.setDepartment(rs.getString("Department"));
                    statusObj.setStartDate(rs.getDate("StartDate"));
                    statusObj.setEndDate(rs.getDate("EndDate"));
                    statuses.add(statusObj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;
    }

    // Получение всех сотрудников, должностей и отделов
    public List<String> getEmployees() {
        List<String> employees = new ArrayList<>();
        String query = "SELECT FullName FROM employees";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(rs.getString("FullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<String> getPositions() {
        List<String> positions = new ArrayList<>();
        String query = "SELECT Name FROM positions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                positions.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return positions;
    }

    public List<String> getDepartments() {
        List<String> departments = new ArrayList<>();
        String query = "SELECT Name FROM departments";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                departments.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public boolean addStatus(EmployeeStatus status) {
        String query = "INSERT INTO employeestatuses (Employee_ID, Status_ID, Position_ID, Department_ID, StartDate, EndDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, status.getEmployeeId());
            stmt.setInt(2, getStatusId(status.getStatus()));
            stmt.setInt(3, getPositionId(status.getPosition()));
            stmt.setInt(4, getDepartmentId(status.getDepartment()));
            //stmt.setDate(5, Date.valueOf(status.getStartDate()));
            //stmt.setDate(6, status.getEndDate() != null ? Date.valueOf(status.getEndDate()) : null);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStatus(EmployeeStatus status) {
        String query = "UPDATE employeestatuses SET Status_ID = ?, Position_ID = ?, Department_ID = ?, StartDate = ?, EndDate = ? WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, getStatusId(status.getStatus()));
            stmt.setInt(2, getPositionId(status.getPosition()));
            stmt.setInt(3, getDepartmentId(status.getDepartment()));
//            stmt.setDate(4, Date.valueOf(status.getStartDate()));
//            stmt.setDate(5, status.getEndDate() != null ? Date.valueOf(status.getEndDate()) : null);
            stmt.setInt(6, status.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStatus(int id) {
        String query = "DELETE FROM employeestatuses WHERE ID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Получение ID статуса по его имени
    private int getStatusId(String status) {
        String query = "SELECT ID FROM statusvalues WHERE Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, status);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Получение ID должности по имени
    private int getPositionId(String position) {
        String query = "SELECT ID FROM positions WHERE Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, position);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Получение ID отдела по имени
    private int getDepartmentId(String department) {
        String query = "SELECT ID FROM departments WHERE Name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, department);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
