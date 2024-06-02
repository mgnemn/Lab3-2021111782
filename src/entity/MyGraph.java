package entity;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swing_viewer.SwingViewer;//asuidhu
import org.graphstream.ui.view.Viewer;//JSJansdjksad

import java.awt.*;//jasidjaisdjas

public class MyGraph extends SingleGraph {

    public MyGraph(String id) {
        super(id);
    }

    public void myDisplay(Graph graph) {
        graph.setAttribute("ui.stylesheet",
                "node {" +
                        "   fill-color: black;" +
                        "   size: 20px;" +
                        "   text-alignment: above;" +
                        "   text-size: 20;" +
                        "   text-color: black;" +
                        "   text-style: bold;" +
                        "}" +
                        "edge {" +
                        "   fill-color: black;" +
                        "   size: 3px;" +
                        "   text-size: 20;" +
                        "   text-color: black;" +
                        "   text-style: bold;" +
                        "   text-alignment: along;" +
                        "   text-offset: 0px, 10px;" +
                        "   arrow-size: 20px, 10px;" +
                        "}");

        // 显示图形
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        javax.swing.JFrame frame = new javax.swing.JFrame("GraphStream");
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 800));
        javax.swing.JPanel panel = (javax.swing.JPanel) viewer.addDefaultView(false);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
