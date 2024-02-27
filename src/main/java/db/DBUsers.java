package db;

import logger.LocalLogger;
import user.Client;

import java.sql.*;
import java.util.logging.Logger;

public class DBUsers {
    private static LocalLogger logger = new LocalLogger();
    private static final String JDBC_URL = "jdbc:sqlite:D:/sqlite/booking.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Failed to connect to the database.", e);
            return null;
        }
    }

    public static int getClientBalance(String email) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT balance FROM Clients WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при отриманні балансу клієнта.", e);
        }
        return 0; // або інше значення за замовчуванням, якщо баланс не знайдено
    }

    public static void setClientBalance(String email, int newBalance) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Clients SET balance = ? WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, newBalance);
                statement.setString(2, email);
                statement.executeUpdate();
                logger.logInfo("Баланс користувача успішно оновлено в базі даних.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при оновленні балансу користувача.", e);
        }
    }

    public static void saveClient(Client client) {
        String email = client.getEmail();
        String password = client.getPassword();

        try (Connection connection = getConnection()) {
            // Перевірка наявності клієнта з таким же імейлом

            // Збереження нового клієнта
            String sql = "INSERT INTO Clients (email, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                statement.setString(2, password);
                statement.executeUpdate();
                logger.logInfo("Клієнт успішно збережений в базі даних.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при збереженні клієнта.", e);
        }
    }

    // Метод для перевірки наявності клієнта з заданим імейлом
    public static boolean doesClientEmailExist(String email) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM Clients WHERE email = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при перевірці наявності клієнта.", e);
        }
        return false;
    }

    public static boolean doesClientExist(String email, String password) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Clients WHERE email = ? AND password = ?")) {

            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при перевірці наявності клієнта.", e);
        }
        return false;
    }
}