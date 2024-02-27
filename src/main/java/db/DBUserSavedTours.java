package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import logger.LocalLogger;
import tour.Tour;

public class DBUserSavedTours {
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

    public static Tour getSavedTourByNameAndEmail(String tourName, String userEmail) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT Tours.* FROM Tours " +
                    "JOIN UserSavedTours ON Tours.name = UserSavedTours.tour_name " +
                    "WHERE UserSavedTours.user_email=? AND UserSavedTours.tour_name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userEmail);
                statement.setString(2, tourName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String transport = resultSet.getString("transport");
                        String meal = resultSet.getString("meal");
                        int duration = resultSet.getInt("duration");
                        int price = resultSet.getInt("price");

                        return new Tour(name, type, transport, meal, duration, price);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при отриманні збереженого туру з бази даних", e);
        }

        // Якщо тур не знайдено, можна повернути null або обробити інакше.
        return null;
    }

    public static ArrayList<Tour> getSavedToursByEmail(String userEmail) {
        ArrayList<Tour> savedTours = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String sql = "SELECT Tours.* FROM Tours " +
                    "JOIN UserSavedTours ON Tours.name = UserSavedTours.tour_name " +
                    "WHERE UserSavedTours.user_email=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userEmail);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String transport = resultSet.getString("transport");
                        String meal = resultSet.getString("meal");
                        int duration = resultSet.getInt("duration");
                        int price = resultSet.getInt("price");

                        Tour tour = new Tour(name, type, transport, meal, duration, price);
                        savedTours.add(tour);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при отриманні збережених турів з бази даних", e);
        }

        return savedTours;
    }

    public static void addUserSavedTour(String userEmail, String tourName) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO UserSavedTours (user_email, tour_name) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, userEmail);
                statement.setString(2, tourName);

                statement.executeUpdate();
                logger.logInfo("Збережений тур успішно додано до бази даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при додаванні збереженого туру до бази даних", e);
        }
    }
}