package employees.manager.module.models;

public class Position {
    private int id;
    private String name;
    private String department; // Имя отдела
    private int departmentId;  // ID отдела

    // Конструкторы
    public Position() {}

    public Position(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Position(int id, String name, String department, int departmentId) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.departmentId = departmentId;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    // Метод для удобного вывода объекта Position в строку (например, для отладки)
    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
