package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentUtil {

  public static String getTextContent(Element mainBlock) {
    StringBuilder contentBuilder = new StringBuilder();
    for (Element p : mainBlock.select("p")) {
      String text = p.text().trim();
      // ignore empty text
      if (!text.isEmpty()) {
        contentBuilder.append(text).append("\n");
      }
    }
    String content = contentBuilder.toString().trim();

    return content.isEmpty() ? "[NO TEXT]" : content;
  }

}
