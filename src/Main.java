import gui.SimpleGui;
import javax.swing.SwingUtilities;

/**
 * Main class to run the Simple GUI.
 */
public class Main { public static void main(String[] args) {
    // Run GUI on Event Dispatch Thread
    SwingUtilities.invokeLater(SimpleGui::new);
  }
}
