package employees.manager.module.dao;

import employees.manager.module.Conn;
import employees.manager.module.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private final Connection connection;

    public EmployeeDao() {
        connection = Conn.getInstance().getConnection();
    }

    // Получить всех сотрудников
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = """
            SELECT e.ID, e.PersonnelNumber, e.FullName, e.BirthDate, e.Email, e.Phone, e.Education,
                   p.Name AS PositionName, s.Name AS StatusName
            FROM Employees e
            LEFT JOIN EmployeeStatuses es ON e.ID = es.Employee_ID AND es.EndDate IS NULL
            LEFT JOIN Positions p ON es.Position_ID = p.ID
            LEFT JOIN StatusValues s ON es.Status_ID = s.ID
            ORDER BY e.FullName
        """;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("ID"),
                        rs.getString("PersonnelNumber"),
                        rs.getString("FullName"),
                        rs.getString("BirthDate"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Education"),
                        rs.getString("PositionName"),
                        rs.getString("StatusName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Добавить нового сотрудника
//    public boolean addEmployee(Employee employee) {
//        String query = "INSERT INTO Employees (PersonnelNumber, FullName, BirthDate, Email, Phone, Education) VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, employee.getPersonnelNumber());
//            stmt.setString(2, employee.getFullName());
//            stmt.setString(3, employee.getBirthDate());
//            stmt.setString(4, employee.getEmail());
//            stmt.setString(5, employee.getPhone());
//            stmt.setString(6, employee.getEducation());
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    public boolean addEmployee(Employee employee) {
        String addEmployeeQuery = """
        INSERT INTO Employees (PersonnelNumber, FullName, BirthDate, Email, Phone, Education) 
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        String addStatusQuery = """
        INSERT INTO EmployeeStatuses (Employee_ID, Position_ID, Status_ID, StartDate) 
        VALUES (?, 
                (SELECT ID FROM Positions WHERE Name = ?), 
                (SELECT ID FROM StatusValues WHERE Name = ?), 
                CURRENT_DATE)
    """;

        try (PreparedStatement employeeStmt = connection.prepareStatement(addEmployeeQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statusStmt = connection.prepareStatement(addStatusQuery)) {
            // Вставка в Employees
            employeeStmt.setString(1, employee.getPersonnelNumber());
            employeeStmt.setString(2, employee.getFullName());
            employeeStmt.setString(3, employee.getBirthDate());
            employeeStmt.setString(4, employee.getEmail());
            employeeStmt.setString(5, employee.getPhone());
            employeeStmt.setString(6, employee.getEducation());

            int affectedRows = employeeStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Не удалось добавить сотрудника, строки не затронуты.");
            }

            // Получение ID нового сотрудника
            try (ResultSet generatedKeys = employeeStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);

                    // Вставка в EmployeeStatuses
                    statusStmt.setInt(1, employeeId);
                    statusStmt.setString(2, employee.getCurrentPosition());
                    statusStmt.setString(3, employee.getCurrentStatus());

                    if (statusStmt.executeUpdate() > 0) {
                        return true;
                    }
                } else {
                    throw new SQLException("Не удалось получить ID нового сотрудника.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Удалить сотрудника
//
    public boolean deleteEmployee(int employeeId) {
        String deleteStatusesQuery = "DELETE FROM EmployeeStatuses WHERE Employee_ID = ?";
        String deleteEmployeeQuery = "DELETE FROM Employees WHERE ID = ?";

        try {
            connection.setAutoCommit(false); // Начинаем транзакцию

            // Удаляем записи из EmployeeStatuses
            try (PreparedStatement stmt = connection.prepareStatement(deleteStatusesQuery)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }

            // Удаляем запись из Employees
            try (PreparedStatement stmt = connection.prepareStatement(deleteEmployeeQuery)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }

            connection.commit(); // Подтверждаем изменения
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Откат в случае ошибки
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


//    public boolean updateEmployee(Employee employee) {
//        String query = """
//        UPDATE Employees
//        SET PersonnelNumber = ?, FullName = ?, BirthDate = ?, Email = ?, Phone = ?, Education = ?
//        WHERE ID = ?
//    """;
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, employee.getPersonnelNumber());
//            stmt.setString(2, employee.getFullName());
//            stmt.setString(3, employee.getBirthDate());
//            stmt.setString(4, employee.getEmail());
//            stmt.setString(5, employee.getPhone());
//            stmt.setString(6, employee.getEducation());
//            stmt.setInt(7, employee.getId());
//            return stmt.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean updateEmployee(Employee employee) {
        String updateEmployeeQuery = """
        UPDATE Employees
        SET PersonnelNumber = ?, FullName = ?, BirthDate = ?, Email = ?, Phone = ?, Education = ?
        WHERE ID = ?
    """;

        String updateStatusQuery = """
        UPDATE EmployeeStatuses
        SET Position_ID = (SELECT ID FROM Positions WHERE Name = ?),
            Status_ID = (SELECT ID FROM StatusValues WHERE Name = ?)
        WHERE Employee_ID = ? AND EndDate IS NULL
    """;

        try {
            connection.setAutoCommit(false); // Начинаем транзакцию

            // Обновляем данные сотрудника
            try (PreparedStatement stmt = connection.prepareStatement(updateEmployeeQuery)) {
                stmt.setString(1, employee.getPersonnelNumber());
                stmt.setString(2, employee.getFullName());
                stmt.setString(3, employee.getBirthDate());
                stmt.setString(4, employee.getEmail());
                stmt.setString(5, employee.getPhone());
                stmt.setString(6, employee.getEducation());
                stmt.setInt(7, employee.getId());
                stmt.executeUpdate();
            }

            // Обновляем статус и должность
            try (PreparedStatement stmt = connection.prepareStatement(updateStatusQuery)) {
                stmt.setString(1, employee.getCurrentPosition());
                stmt.setString(2, employee.getCurrentStatus());
                stmt.setInt(3, employee.getId());
                stmt.executeUpdate();
            }

            connection.commit(); // Фиксируем транзакцию
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback(); // Откатываем изменения при ошибке
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Возвращаем режим автофиксации
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }




    public List<String> getAllPositions() {
        List<String> positions = new ArrayList<>();
        String query = "SELECT Name FROM Positions";
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

    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        String query = "SELECT Name FROM StatusValues";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                statuses.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statuses;
    }
    public String getEmployeePosition(int employeeId) {
        String query = """
        SELECT p.Name 
        FROM EmployeeStatuses es
        JOIN Positions p ON es.Position_ID = p.ID
        WHERE es.Employee_ID = ? AND es.EndDate IS NULL
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEmployeeStatus(int employeeId) {
        String query = """
        SELECT sv.Name 
        FROM EmployeeStatuses es
        JOIN StatusValues sv ON es.Status_ID = sv.ID
        WHERE es.Employee_ID = ? AND es.EndDate IS NULL
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}