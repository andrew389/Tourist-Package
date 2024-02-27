package menu.adminmenu;

import db.DBBooking;
import db.DBUsers;
import logger.LocalLogger;
import menu.usermenu.ShowMenu;
import menu.Command;
import menu.adminmenu.service.AddTour;
import menu.adminmenu.service.RefactourTour;
import menu.adminmenu.service.ShowAllToursFromDB;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class ShowAdminMenu implements Command {
    private static LocalLogger logger = new LocalLogger();
    private final Map<Integer, Command> commands;
    private final Connection connection = DBUsers.getConnection();;

    public ShowAdminMenu() {
        commands = new HashMap<>();
        commands.put(1, new RefactourTour(connection));
        commands.put(2, new ShowAllToursFromDB(connection));
        commands.put(3, new AddTour(connection));
        commands.put(4, new ShowMenu());
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n---------Адмінка---------");
            System.out.println(" Виберіть опцію: ");
            System.out.println(" 1. Змінити тур");
            System.out.println(" 2. Вивести всі тури");
            System.out.println(" 3. Додати тур");
            System.out.println(" 4. Вийти");
            System.out.println("---------Адмінка---------");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очищення буфера

            Command selectedCommand = commands.get(choice);

            if (selectedCommand != null) {
                selectedCommand.execute();
            } else {
                logger.logWarning("Невірний вибір. Спробуйте ще раз.");
            }
        }
    }
}
