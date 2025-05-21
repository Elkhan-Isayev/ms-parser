package az.informatik.msparser.service;

import az.informatik.msparser.model.FilesEntity;
import az.informatik.msparser.model.NewsEntity;
import az.informatik.msparser.repository.FilesRepository;
import az.informatik.msparser.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static az.informatik.msparser.util.AuthorUtil.getAuthor;
import static az.informatik.msparser.util.ContentUtil.getTextContent;
import static az.informatik.msparser.util.CreateDateUtil.getCreateDate;
import static az.informatik.msparser.util.CreateTimeUtil.getCreateTime;
import static az.informatik.msparser.util.FileUtil.getFileUrls;
import static az.informatik.msparser.util.FileUtil.getSavedFileNames;
import static az.informatik.msparser.util.TitleUtil.getTitle;
import static az.informatik.msparser.util.ViewCountUtil.getViewCount;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final NewsRepository newsRepository;
    private final FilesRepository filesRepository;

    public void getNews() {

        String commonUrl = "https://www.informatik.az/news/?page";

        iterateOverPages(commonUrl, 1, 65);

    }

    @SneakyThrows
    private void iterateOverPages(String url, int startPage, int endPage) {

        for (int i = startPage; i <= endPage; i++) {
            log.info("Starting page --------------> {}", i);
            String currentPageUrl = url + i;

            // Get the HTML content of the page
            Document doc = Jsoup.connect(currentPageUrl)
                    .userAgent("Mozilla/5.0") // можно указать агент для надежности
                    .timeout(10000)
                    .get();

            // Get the news blocks
            Elements newsBlocks = doc.select("#allEntries > div[id^=entryID]");

            // Iterate over the news blocks and get the links in page
            List<String> links = iterateOverNews(newsBlocks);

            iterateOverNews(links);
        }
    }

    private List<String> iterateOverNews(Elements newsBlocks) {
        List<String> newsLinks = new ArrayList<>();
        // Iterate over the news blocks
        for (Element news : newsBlocks) {
            // Get the title and link
            Element link = news.selectFirst(".eTitle > a");
            // Get the data
            if (link != null) {
                String href = link.attr("href");

                newsLinks.add(href);
            }
        }

        return newsLinks;
    }

    @SneakyThrows
    private void iterateOverNews(List<String> links) {
        String newsCommonLink = "https://www.informatik.az";

        // Iterate over the news links
        for (String link : links) {
            log.info("Starting article ---------> {}", link);
            String currentLink = newsCommonLink + link;

            // Get the HTML content of the page
            Document doc = Jsoup.connect(currentLink)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();


            // Parse the article
            parseArticlePage(doc);
        }
    }

    private void parseArticlePage(Document doc) {
        // Get the main block
        Element mainBlock = doc.selectFirst(".eMessage");
        if (mainBlock == null) return;

        // Extract data
        List<String> fileUrls = getFileUrls(mainBlock);
        String title = getTitle(doc);
        String content = getTextContent(mainBlock);
        String author = getAuthor(doc);
        Integer views = getViewCount(doc);
        LocalDate date = getCreateDate(doc);
        LocalTime time = getCreateTime(doc);
        LocalDateTime createdAt = LocalDateTime.of(date, time);

        // Prepare saved filenames
        List<String> savedFilenames = getSavedFileNames(fileUrls);

        //        log.info("SAVED_FILENAMES: {}", savedFilenames);
        //        log.info("TITLE: {}", title);
        //        log.info("CONTENT: {}", content);
        //        log.info("DATE: {}", date);
        //        log.info("AUTHOR: {}", author);
        //        log.info("VIEWS: {}", views);
        //        log.info("CREATED_AT: {}", createdAt);

        // DB
        NewsEntity news = new NewsEntity();
        news.setTitle(title);
        news.setContent(content);
        news.setAuthor(author);
        news.setViewCount(views);
        news.setCreatedAt(createdAt);

        List<FilesEntity> fileEntities = new ArrayList<>();
        savedFilenames.forEach(savedFilename -> {
            FilesEntity file = new FilesEntity();
            file.setFileName(savedFilename);
            file.setNews(news);
            fileEntities.add(file);
        });
        news.setFiles(fileEntities);

        newsRepository.save(news);
        log.info("✅ Saved news: {}", title);
    }

}
