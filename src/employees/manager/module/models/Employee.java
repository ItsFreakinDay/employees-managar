package employees.manager.module.models;

public class Employee {
    private int id;
    private String personnelNumber; // Табельный номер
    private String fullName; // ФИО
    private String birthDate; // Дата рождения
    private String email; // Электронная почта
    private String phone; // Телефон
    private String education; // Образование
    private String currentPosition; // Текущая должность
    private String currentStatus; // Текущий статус

    // Конструкторы
    public Employee() {
    }

    public Employee(int id, String personnelNumber, String fullName, String birthDate, String email, String phone,
                    String education, String currentPosition, String currentStatus) {
        this.id = id;
        this.personnelNumber = personnelNumber;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.education = education;
        this.currentPosition = currentPosition;
        this.currentStatus = currentStatus;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(String personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}