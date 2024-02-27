package menu.adminmenu;

import db.DBBooking;
import logger.LocalLogger;
import menu.Command;

import java.util.Scanner;
import java.util.logging.Logger;

public class ShowEnterAdminMenu implements Command {
    private static LocalLogger logger = new LocalLogger();

    @Override
    public void execute(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t--------------------------------------");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t| Введіть дані для входу до адмінки: |");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t| 1. Пароль                          |");
        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t--------------------------------------");

        String password = scanner.nextLine();

        if (password.equals("123")){
            Command showAdminMenu = new ShowAdminMenu();
            showAdminMenu.execute();
        }
        else{
            logger.logWarning("Невірний пароль");
            execute();
        }
    }
}
