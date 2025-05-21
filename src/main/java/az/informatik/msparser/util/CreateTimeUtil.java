package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateTimeUtil {

  public static LocalTime getCreateTime(Document doc) {
    Element titleBlock = doc.selectFirst(".eTitle > div[style*=float:right]");
    if (titleBlock == null) return null;

    String timeText = titleBlock.text().trim(); // e.g., "18:22"
    return LocalTime.parse(timeText);
  }

}
