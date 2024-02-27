package main;

import menu.Command;
import menu.usermenu.ShowMenu;

import user.ClientHolder;


public class Main {
    public static void main(String[] args) {
        Command showMenu = new ShowMenu();
        showMenu.execute();
        ClientHolder.deleteFile();
    }
}