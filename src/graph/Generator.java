package graph;

/**
 * This interface defines methods for operations on directed graphs,
 * including displaying the graph, querying bridge words, generating new text,
 * calculating shortest paths, and performing random walks.
 */

public interface Generator {
  void showDirectedGraph();

  String queryBridgeWords(String word1, String word2);
  // 根据bridge word生成新文本

  String generateNewText(String inputText);
  // 计算两个单词之间的最短路径

  String calcShortestPath(String word1, String word2);
  // 随机游走

  void randomWalk();
}
