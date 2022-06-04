package app;

import app.gui.MainWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
        public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.setProperty("java.awt.headless", "false");
        MainWindow mainWindow = new MainWindow();
        mainWindow.drawWindow();
    }
}
