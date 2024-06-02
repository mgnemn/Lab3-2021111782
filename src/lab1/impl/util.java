package lab1.impl;

import entity.MyGraph;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class util {
    public static MyGraph getGraphFromFile(File file) throws FileNotFoundException {
        System.setProperty("org.graphstream.ui", "swing");

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
        } catch (IOException e) {
            // Ignored
        }

        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(content.toString());

        while (matcher.find()) {
            if (!result.isEmpty()) {
                result.append(" ");
            }
            result.append(matcher.group().toLowerCase());
        }

        MyGraph graph = new MyGraph("graph1");
        String[] words = result.toString().split(" ");
        Set<String> existNode = new HashSet<>();
        for (String word : words) {
            if (!existNode.contains(word)) {
                graph.addNode(word).setAttribute("ui.label", word);
                existNode.add(word);
            }
        }

        Set<String> existEdge = new HashSet<>();
        for (int i = 0; i < words.length - 1; i++) {
            String edgeKey = words[i] + "_" + words[i + 1];
            if (!existEdge.contains(edgeKey)) {
                graph.addEdge(edgeKey, words[i], words[i + 1], true).setAttribute("ui.label", 1);
                existEdge.add(edgeKey);
            } else {
                int weight = (int) graph.getEdge(edgeKey).getAttribute("ui.label");
                graph.getEdge(edgeKey).setAttribute("ui.label", weight + 1);
            }
        }
        return graph;
    }
}

