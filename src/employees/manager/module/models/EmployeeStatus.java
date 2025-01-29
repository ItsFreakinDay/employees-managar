package employees.manager.module.models;

import java.util.Date;

public class EmployeeStatus {
    private int id;
    private int employeeId; // ID сотрудника
    private String fullName; // ФИО сотрудника
    private String status; // Статус
    private String position; // Должность
    private String department; // Отдел
    private Date startDate; // Дата начала
    private Date endDate; // Дата окончания

    // Конструктор
    public EmployeeStatus(int employeeId, String status, String position, String department, Date startDate, Date endDate) {
        this.employeeId = employeeId;
        this.status = status;
        this.position = position;
        this.department = department;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Пустой конструктор
    public EmployeeStatus() {}

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
