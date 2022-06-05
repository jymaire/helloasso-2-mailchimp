package app.gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Run {

    @Autowired
    private MainWindow mainWindow;

    public Run() {
    }

    public void start() {
        mainWindow.drawWindow();
    }
}
