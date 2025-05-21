package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TitleUtil {

  public static String getTitle(Document doc) {
    Element titleElement = doc.selectFirst(".eTitle");
    if (titleElement == null) return "[NO TITLE]";
    return titleElement.ownText().trim();
  }

}
