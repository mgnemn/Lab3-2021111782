package graph.impl;

import entity.MyGraph;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 黑盒检验生成新文本.
 */

public class MyGraphBlackBoxTest {
  private GeneratorImpl generator;

  /**
   * 生成图.
   */
  @BeforeEach
  public void setUp() throws IOException {
    // 假设 'Util.getGraphFromFile' 是正确实现的，从一个真实的文件中读取图。
    File file = new File("C:\\Users\\Administrator\\Desktop\\test1.txt");  // 替换为实际文件路径
    MyGraph graph = Util.getGraphFromFile(file);
    generator = new GeneratorImpl(graph);
  }

  @Test
  public void testGenerateNewTextWithBridges() {
    // 假设输入文本的单词在图中，并且存在桥接词
    String inputText = "explore new";
    String expectedResult = "explore strange new"; // 假设 'bridgeWord' 是存在的桥接词
    String result = generator.generateNewText(inputText);
    Assertions.assertTrue(result.contains("strange"),
        "Generated text should contain the bridge word.");
  }

  @Test
  public void testGenerateNewTextWithoutBridges() {
    // 输入文本的单词在图中，但没有桥接词
    String inputText = "explore worlds";  // 假设没有直接的桥接词
    String expectedResult = "explore worlds";
    String result = generator.generateNewText(inputText);
    Assertions.assertEquals(expectedResult, result,
        "Generated text should not contain any bridge words.");
  }

  @Test
  public void testGenerateNewTextWithNonexistentWords() {
    // 输入文本中的单词不存在于图中
    String inputText = "nonexistent1 nonexistent2";
    String expectedResult = "nonexistent1 nonexistent2";
    String result = generator.generateNewText(inputText);
    Assertions.assertEquals(expectedResult, result, "Generated text should remain unchanged.");
  }
}

