package graph.impl;

import entity.MyGraph;
import graph.Generator;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jdesktop.swingx.prompt.PromptSupport;

/**
 * Implementation of the Generator interface.
 */

public class GeneratorImpl implements Generator {
  private final MyGraph directedGraph;
  private Node currentNode;
  private Set<Edge> traversedEdges;

  public GeneratorImpl(MyGraph graph) {
    this.directedGraph = graph;
  }


  @Override
  public void showDirectedGraph() {
    for (Edge edge : this.directedGraph.edges().toList()) {
      String sourceNode = edge.getSourceNode().getId();
      String targetNode = edge.getTargetNode().getId();
      int edgeWeight = Integer.parseInt(edge.getAttribute("ui.label").toString());
      System.out.println(sourceNode + " -> " + targetNode + ", Weight: " + edgeWeight);
    }
    this.directedGraph.myDisplay(this.directedGraph);
  }

  @Override
  public String queryBridgeWords(String firstWord, String secondWord) {
    if (this.directedGraph.getNode(firstWord) == null
        || this.directedGraph.getNode(secondWord) == null) {
      return "No " + firstWord + " or " + secondWord + " in the graph!";
    }
    List<String> bridges = getBridgeWords(firstWord, secondWord);
    if (bridges.isEmpty()) {
      return "No bridge words from " + firstWord + " to " + secondWord + "!";
    }
    return "The bridge words from " + firstWord + " to " + secondWord + " are: "
        + String.join(", ", bridges) + ".";
  }

  private List<String> getBridgeWords(String word1, String word2) {
    List<String> bridgeWords = new LinkedList<>();
    Node node1 = this.directedGraph.getNode(word1);
    Node node2 = this.directedGraph.getNode(word2);

    if (node1 != null && node2 != null) {
      node1.leavingEdges().forEach(edge -> {
        Node intermediateNode = edge.getTargetNode();
        intermediateNode.leavingEdges().forEach(e -> {
          if (e.getTargetNode().equals(node2)) {
            bridgeWords.add(intermediateNode.getId());
          }
        });
      });
    }
    return bridgeWords;
  }

  @Override
  public String generateNewText(String inputText) {
    Random rand = new Random();
    String[] tokens = inputText.toLowerCase().split(" ");
    StringBuilder newText = new StringBuilder();

    for (int i = 0; i < tokens.length - 1; i++) {
      List<String> bridges = getBridgeWords(tokens[i], tokens[i + 1]);
      newText.append(tokens[i]).append(" ");
      if (!bridges.isEmpty()) {
        newText.append(bridges.get(rand.nextInt(bridges.size()))).append(" ");
      }
    }
    newText.append(tokens[tokens.length - 1]);
    return newText.toString().trim();
  }

  @Override
  public String calcShortestPath(String word1, String word2) {
    if (this.directedGraph.getNode(word1) == null || this.directedGraph.getNode(word2) == null) {
      return "One of the specified nodes does not exist.";
    }
    return findShortestPath(word1, word2);
  }

  private String findShortestPath(String startWord, String endWord) {
    Map<Node, Integer> nodeDistances = new HashMap<>();
    Map<Node, Node> predecessors = new HashMap<>();
    PriorityQueue<Node> nodeQueue = new PriorityQueue<>(
        Comparator.comparingInt(nodeDistances::get));

    for (Node node : this.directedGraph) {
      nodeDistances.put(node, Integer.MAX_VALUE);
      predecessors.put(node, null);
      node.setAttribute("ui.style", "fill-color: black;");
    }

    Node startNode = this.directedGraph.getNode(startWord);
    nodeDistances.put(startNode, 0);
    nodeQueue.add(startNode);

    while (!nodeQueue.isEmpty()) {
      Node currentNode = nodeQueue.poll();
      if (currentNode.getId().equals(endWord)) {
        break;
      }

      for (Edge edge : currentNode.leavingEdges().toList()) {
        Node neighborNode = edge.getOpposite(currentNode);
        int weight = Integer.parseInt(edge.getAttribute("ui.label").toString());
        int newDistance = nodeDistances.get(currentNode) + weight;

        if (newDistance < nodeDistances.get(neighborNode)) {
          nodeDistances.put(neighborNode, newDistance);
          predecessors.put(neighborNode, currentNode);
          nodeQueue.add(neighborNode);
        }
      }
    }

    if (nodeDistances.get(this.directedGraph.getNode(endWord)) == Integer.MAX_VALUE) {
      return "Unreachable";
    }

    List<Node> path = new ArrayList<>();
    for (Node at = this.directedGraph.getNode(endWord); at != null; at = predecessors.get(at)) {
      path.add(at);
    }
    Collections.reverse(path);

    for (Node node : path) {
      Node prev = predecessors.get(node);
      if (prev != null) {
        Edge edge = prev.getEdgeBetween(node);
        if (edge != null) {
          edge.setAttribute("ui.style", "fill-color: red;");
        }
      }
    }

    return "Path total weight: " + nodeDistances.get(this.directedGraph.getNode(endWord));
  }

  @Override
  public void randomWalk() {
    Random rand = new Random();
    this.currentNode = this.directedGraph.getNode(rand.nextInt(this.directedGraph.getNodeCount()));
    this.traversedEdges = new HashSet<>();
    final StringBuilder walkPath = new StringBuilder();

    JFrame frame = new JFrame("Random Walk");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new FlowLayout());
    final JPanel panel = new JPanel();
    JTextField textField1 = new JTextField(15);
    PromptSupport.setPrompt("请输入保存的文件地址:", textField1);
    PromptSupport.setFocusBehavior(PromptSupport.FocusBehavior.HIDE_PROMPT, textField1);
    PromptSupport.setForeground(Color.GRAY, textField1);
    panel.add(textField1);
    int result = JOptionPane.showConfirmDialog(frame, panel, "随机游走", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String filePath = textField1.getText();
      if (filePath != null) {
        File file = new File(filePath);
        if (!file.exists()) {
          JOptionPane.showMessageDialog(frame, "文件不存在，请更换路径或新建文件！", "错误",
              JOptionPane.ERROR_MESSAGE);
        } else {
          JButton nextButton = new JButton("Continue Walk");
          JButton stopButton = new JButton("Stop");
          JLabel pathLabel = new JLabel("Path: " + walkPath);
          pathLabel.setHorizontalAlignment(SwingConstants.CENTER);

          nextButton.addActionListener(e ->
              continueRandomWalk(filePath, walkPath, pathLabel, frame));
          stopButton.addActionListener(e -> frame.dispose());

          frame.getContentPane().add(pathLabel);
          frame.getContentPane().add(nextButton);
          frame.getContentPane().add(stopButton);
          frame.setVisible(true);
        }
      }
    }
  }

  private void continueRandomWalk(String filePath, StringBuilder path, JLabel label, JFrame frame) {
    try (FileWriter writer = new FileWriter(filePath, true)) {
      if (this.currentNode != null && this.currentNode.getOutDegree() > 0) {
        path.append(this.currentNode.getId()).append(" -> ");
        writer.write(this.currentNode.getId() + " ");
        Edge edge = this.currentNode.getLeavingEdge(new Random()
            .nextInt(this.currentNode.getOutDegree()));
        if (!this.traversedEdges.add(edge)) {
          JOptionPane.showMessageDialog(frame, "Reached an already visited edge, "
              + "stopping random walk.", "提醒", JOptionPane.INFORMATION_MESSAGE);
          frame.dispose();
        }
        this.currentNode = edge.getOpposite(this.currentNode);
        label.setText("Path: " + path);
      }

      if (this.currentNode != null && this.currentNode.getOutDegree() == 0) {
        JOptionPane.showMessageDialog(frame, "No outgoing edges from this node, "
            + "stopping walk.", "提醒", JOptionPane.INFORMATION_MESSAGE);
        path.append(this.currentNode.getId());
        writer.write(this.currentNode.getId() + "\n");
        frame.dispose();
      }

      label.setText("Current Path: " + path);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(frame, ex.getMessage());
    }
  }
}

