package employees.manager.module;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conn {
    private static final String URL = "jdbc:mysql://localhost:3307/mng_db?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Conn instance;
    private Connection connection;

    private Conn() {
        try {
            // Регистрация драйвера
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Установление соединения
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Соединение с базой данных установлено успешно.");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка подключения к базе данных", e);
        }
    }

    public static Conn getInstance() {
        if (instance == null) {
            synchronized (Conn.class) {
                if (instance == null) {
                    instance = new Conn();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        // Проверка, если соединение закрыто, то переподключаем
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Переподключение к базе данных.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при попытке переподключения.");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Соединение с базой данных закрыто.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Ошибка при закрытии соединения с базой данных.");
            }
        }
    }
}
