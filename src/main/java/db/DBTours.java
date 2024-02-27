package db;

import logger.LocalLogger;
import tour.Tour;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;


public class DBTours {
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

    public static ArrayList<Tour> getAllTours() {
        ArrayList<Tour> tours = new ArrayList<>();

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Tours")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                String transport = resultSet.getString("transport");
                String meal = resultSet.getString("meal");
                int duration = resultSet.getInt("duration");
                int price = resultSet.getInt("price"); // нове поле

                Tour tour = new Tour(name, type, transport, meal, duration, price);
                tours.add(tour);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Error fetching tours from the database.", e);
        }

        return tours;
    }

    public static void addTour(Tour tour) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO Tours (name, type, transport, meal, duration, price) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, tour.getName());
                statement.setString(2, tour.getType());
                statement.setString(3, tour.getTransport());
                statement.setString(4, tour.getMeal());
                statement.setInt(5, tour.getDuration());
                statement.setInt(6, tour.getPrice()); // нове поле

                statement.executeUpdate();
                logger.logInfo("Тур успішно був доданий до бази даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при додаванні до бази даних", e);
        }
    }
    public static void deleteAllTours() {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM Tours";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
                logger.logInfo("Всі тури були успішно видалені з бази даних.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при видаленні всіх турів", e);
        }
    }

    public static Tour getTourByName(String name) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM Tours WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        // Отримати дані з результатів і створити об'єкт Tour
                        String typeName = resultSet.getString("type");
                        String transport = resultSet.getString("transport");
                        String meal = resultSet.getString("meal");
                        int duration = resultSet.getInt("duration");
                        int price = resultSet.getInt("price");

                        return new Tour(name, typeName, transport, meal, duration, price);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при отриманні туру за назвою", e);
        }

        // Якщо не знайдено тур за заданою назвою, можна повернути null або обробити інакше.
        return null;
    }

    public static boolean doesTourExist(String tourName) {
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

    public static void refactourTourByName(Tour tour, String newName) {
        if (!doesTourExist(newName)) {
            logger.logWarning("Тур з таким ім'ям не існує");
            return;
        }

        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET name=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, newName);  // нова назва туру
                statement.setString(2, tour.getName());  // стара назва туру

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }

    public static void refactourTourByType(Tour tour, String type) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET type=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, type);
                statement.setString(2, tour.getName());

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }
    public static void refactourTourByTransport(Tour tour, String transport) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET transport=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, transport);
                statement.setString(2, tour.getName());

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }
    public static void refactourTourByMeal(Tour tour, String meal) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET meal=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, meal);
                statement.setString(2, tour.getName());

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }
    public static void refactourTourByDuration(Tour tour, int duration) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET duration=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, duration);
                statement.setString(2, tour.getName());

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }

    public static void refactourTourByPrice(Tour tour, int price) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE Tours SET price=? WHERE name=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, price);
                statement.setString(2, tour.getName());

                statement.executeUpdate();
                logger.logInfo("Тур успішно був апдейтнутий в базі даних");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при апдейті туру", e);
        }
    }

    public static ArrayList<Tour> searchToursByName(String name) {
        return searchTours("name", name);
    }

    public static ArrayList<Tour> searchToursByType(String type) {
        return searchTours("type", type);
    }

    public static ArrayList<Tour> searchToursByTransport(String transport) {
        return searchTours("transport", transport);
    }

    public static ArrayList<Tour> searchToursByMeal(String meal) {
        return searchTours("meal", meal);
    }

    public static ArrayList<Tour> searchToursByDuration(int duration) {
        return searchTours("duration", String.valueOf(duration));
    }

    public static ArrayList<Tour> searchToursByPrice(int price) {
        return searchTours("price", String.valueOf(price));
    }

    private static ArrayList<Tour> searchTours(String columnName, String value) {
        ArrayList<Tour> tours = new ArrayList<>();

        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM Tours WHERE " + columnName + "=?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, value);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String transport = resultSet.getString("transport");
                        String meal = resultSet.getString("meal");
                        int duration = resultSet.getInt("duration");
                        int price = resultSet.getInt("price"); // нове поле

                        Tour tour = new Tour(name, type, transport, meal, duration, price);
                        tours.add(tour);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при пошуку турів за " + columnName, e);
        }

        return tours;
    }

    public static ArrayList<Tour> searchToursByCriteria(String name, String type, String transport, String meal, int duration, int price) {
        ArrayList<Tour> tours = new ArrayList<>();

        try (Connection connection = getConnection()) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Tours WHERE 1=1");

            if (name != null) {
                sqlBuilder.append(" AND name=?");
            }

            if (type != null) {
                sqlBuilder.append(" AND type=?");
            }

            if (transport != null) {
                sqlBuilder.append(" AND transport=?");
            }

            if (meal != null) {
                sqlBuilder.append(" AND meal=?");
            }

            if (duration != 0) {
                sqlBuilder.append(" AND duration=?");
            }

            if (price != 0) {
                sqlBuilder.append(" AND price=?");
            }

            try (PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString())) {
                int parameterIndex = 1;

                if (name != null) {
                    statement.setString(parameterIndex++, name);
                }

                if (type != null) {
                    statement.setString(parameterIndex++, type);
                }

                if (transport != null) {
                    statement.setString(parameterIndex++, transport);
                }

                if (meal != null) {
                    statement.setString(parameterIndex++, meal);
                }

                if (duration != 0) {
                    statement.setInt(parameterIndex++, duration);
                }

                if (price != 0) {
                    statement.setInt(parameterIndex++, price);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String tourName = resultSet.getString("name");
                        String tourType = resultSet.getString("type");
                        String tourTransport = resultSet.getString("transport");
                        String tourMeal = resultSet.getString("meal");
                        int tourDuration = resultSet.getInt("duration");
                        int tourPrice = resultSet.getInt("price");

                        Tour tour = new Tour(tourName, tourType, tourTransport, tourMeal, tourDuration, tourPrice);
                        tours.add(tour);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.logError("Помилка при комбінованому пошуку турів", e);
        }

        return tours;
    }
}
