package app.gui;

import javax.swing.*;

public class Popup extends JFrame {
    static JFrame frame;
    public void actionPerformed(String retour) {
        JDialog d = new JDialog(frame, "Résultat");
        JLabel l = new JLabel(retour);
        d.add(l);
        d.setSize(200, 100);
        d.setVisible(true);

    }
}
