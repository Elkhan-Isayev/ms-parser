package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewCountUtil {

  public static Integer getViewCount(Document doc) {
    Element detailEl = doc.selectFirst(".eDetails");
    if (detailEl == null) return 0;

    String text = detailEl.text(); // Baxılıb: 626 | Əlavə edib: ...
    try {
      String[] parts = text.split("Baxılıb\\s*:?\\s*");
      if (parts.length < 2) return 0;
      String[] secondPart = parts[1].split("\\|");
      return Integer.parseInt(secondPart[0].trim());
    } catch (Exception e) {
      return 0;
    }
  }

}
