import gui.MainWindow;
import org.springframework.boot.SpringApplication;

public class Application {

    public static void main(String[] args) {
   //     SpringApplication.run(Application.class, args);
        MainWindow mainWindow = new MainWindow();
        mainWindow.drawWindow();
    }
}
