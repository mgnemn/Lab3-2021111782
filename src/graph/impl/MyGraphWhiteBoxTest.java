package graph.impl;



import static org.junit.Assert.assertEquals;

import graph.Generator;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;

/**
 * 白盒测试桥接词.
 */
public class MyGraphWhiteBoxTest {
  String filepath = "C:\\Users\\Administrator\\Desktop\\test1.txt";

  Generator graph = new GeneratorImpl(Util.getGraphFromFile(new File(filepath)));

  public MyGraphWhiteBoxTest() throws FileNotFoundException {
  }

  @Test
  public void testQueryBridgeWords1() {
    String result = graph.queryBridgeWords("nonexistent", "life");
    assertEquals("No nonexistent or life in the graph!", result);
  }

  @Test
  public void testQueryBridgeWords2() {
    String result = graph.queryBridgeWords("seek", "new");
    assertEquals("The bridge words from seek to new are: out.", result);
  }

  @Test
  public void testQueryBridgeWords3() {
    String result = graph.queryBridgeWords("new", "seek");
    assertEquals("No bridge words from new to seek!", result);
  }

  @Test
  public void testQueryBridgeWords4() {
    String result = graph.queryBridgeWords("life", "nonexistent");
    assertEquals("No life or nonexistent in the graph!", result);
  }
}
