package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthorUtil {


  public static String getAuthor(Document doc) {
    Element details = doc.selectFirst(".eDetails");
    if (details == null) return null;

    Element authorLink = details.selectFirst("a[href^=javascript:]");
    if (authorLink == null) return null;

    return authorLink.text().trim();
  }

}
