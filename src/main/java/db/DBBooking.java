package db;

import java.sql.*;
import java.util.ArrayList;

import logger.LocalLogger;
import tour.Tour;

public class DBBooking {
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

    public static void bookTour(String clientEmail, String tourName) {
        try (Connection connection = getConnection()) {
            // Перевірка, чи тур існує
            if (!doesTourExist(tourName)) {
                logger.logWarning("Такого туру не існує.");
                return;
            }

            // Перевірка, чи тур ще не був заброньований
            if (doesClientHaveTour(clientEmail, tourName)) {
                logger.logInfo("Цей тур вже заброньовано.");
                return;
            }

            // Збереження нового бронювання
            String sql = "INSERT INTO Bookings (client_email, tour_name) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, clientEmail);
                statement.setString(2, tourName);
                statement.executeUpdate();
                logger.logInfo("Тур успішно заброньовано.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при бронюванні туру.", e);
        }
    }

    private static boolean doesTourExist(String tourName) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM Tours WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, tourName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при перевірці наявності туру.", e);
        }
        return false;
    }

    private static boolean doesClientHaveTour(String clientEmail, String tourName) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM Bookings WHERE client_email=? AND tour_name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, clientEmail);
                statement.setString(2, tourName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при перевірці наявності туру у клієнта.", e);
        }
        return false;
    }

    public static ArrayList<Tour> getBookedToursByEmail(String clientEmail) {
        ArrayList<Tour> bookedTours = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String sql = "SELECT Tours.* FROM Tours " +
                    "JOIN Bookings ON Tours.name = Bookings.tour_name " +
                    "WHERE Bookings.client_email=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, clientEmail);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String transport = resultSet.getString("transport");
                        String meal = resultSet.getString("meal");
                        int duration = resultSet.getInt("duration");
                        int price = resultSet.getInt("price");

                        Tour tour = new Tour(name, type, transport, meal, duration, price);
                        bookedTours.add(tour);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при отриманні заброньованих турів з бази даних", e);
        }

        return bookedTours;
    }
}
