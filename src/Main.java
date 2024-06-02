import gui.SimpleGUI;
import javax.swing.*;
import java.io.FileNotFoundException;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // 在事件分派线程中运行GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimpleGUI();
            }
        });

    }
}