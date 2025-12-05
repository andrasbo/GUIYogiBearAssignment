package controller;

import view.MainWindow;

/**
 *
 * @author Andras Boromissza [hyczbo]
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainWindow gameGUI = new MainWindow();
        Controller controller = new Controller(gameGUI);
    }
    
}
