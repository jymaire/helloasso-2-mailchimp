package app;

import gui.MainWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import process.CSVprocessing;

@SpringBootApplication
public class Application {
        public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.setProperty("java.awt.headless", "false");
        MainWindow mainWindow = new MainWindow();
        mainWindow.drawWindow();
    }
}
