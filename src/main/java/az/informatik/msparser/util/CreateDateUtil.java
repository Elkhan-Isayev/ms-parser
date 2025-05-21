package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateDateUtil {

  public static LocalDate getCreateDate(Document doc) {
    Elements breadcrumbs = doc.select(".breadcrumb-item");

    if (breadcrumbs.size() < 3) {
      log.warn("❌ Breadcrumbs not enough to parse date: found {}", breadcrumbs.size());
      return null;
    }

    String yearStr = breadcrumbs.get(0).text().trim();   // 2024
    String monthStr = breadcrumbs.get(1).text().trim();  // İyun
    String dayStr = breadcrumbs.get(2).text().trim();    // 3

    return parseDateFromStrings(dayStr, monthStr, yearStr);
  }

  private static LocalDate parseDateFromStrings(String day, String monthAz, String year) {
    int dayInt = Integer.parseInt(day);
    int yearInt = Integer.parseInt(year);

    int monthInt = switch (monthAz.toLowerCase()) {
      case "yanvar" -> 1;
      case "fevral" -> 2;
      case "mart" -> 3;
      case "aprel" -> 4;
      case "may" -> 5;
      case "iyun" -> 6;
      case "iyul" -> 7;
      case "avqust" -> 8;
      case "sentyabr" -> 9;
      case "oktyabr" -> 10;
      case "noyabr" -> 11;
      case "dekabr" -> 12;
      default -> 1;
    };

    return LocalDate.of(yearInt, monthInt, dayInt);
  }

}
