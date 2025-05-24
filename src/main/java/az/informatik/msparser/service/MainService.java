package az.informatik.msparser.service;

import az.informatik.msparser.model.BlogImageEntity;
import az.informatik.msparser.model.BlogPostEntity;
import az.informatik.msparser.repository.BlogImageRepository;
import az.informatik.msparser.repository.BlogPostRepository;
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
import java.util.UUID;

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

    private final BlogPostRepository blogPostRepository;
    private final BlogImageRepository blogImageRepository;

    private static final int DEFAULT_CATEGORY_ID = 1;
    private static final int DEFAULT_AUTHOR_ID = 1;

    public void getNews() {
        String commonUrl = "https://www.informatik.az/news/?page";
        iterateOverPages(commonUrl, 1, 65);
    }

    @SneakyThrows
    private void iterateOverPages(String url, int startPage, int endPage) {
        for (int i = startPage; i <= endPage; i++) {
            log.info("Starting page --------------> {}", i);
            String currentPageUrl = url + i;

            Document doc = Jsoup.connect(currentPageUrl)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Elements newsBlocks = doc.select("#allEntries > div[id^=entryID]");
            List<String> links = iterateOverNews(newsBlocks);
            iterateOverNews(links);
        }
    }

    private List<String> iterateOverNews(Elements newsBlocks) {
        List<String> newsLinks = new ArrayList<>();
        for (Element news : newsBlocks) {
            Element link = news.selectFirst(".eTitle > a");
            if (link != null) {
                String href = link.attr("href");
                newsLinks.add(href);
            }
        }
        return newsLinks;
    }

//    @SneakyThrows
//    private void iterateOverNews(List<String> links) {
//        String newsCommonLink = "https://www.informatik.az";
//        for (String link : links) {
//            log.info("Starting article ---------> {}", link);
//            String currentLink = newsCommonLink + link;
//            Document doc = Jsoup.connect(currentLink)
//                    .userAgent("Mozilla/5.0")
//                    .timeout(10000)
//                    .get();
//            parseArticlePage(doc);
//        }
//    }

    @SneakyThrows
    private void iterateOverNews(List<String> links) {
        String newsCommonLink = "https://www.informatik.az";
        int count = 0;

        for (String link : links) {
            log.info("Starting article ---------> {}", link);
            String currentLink = newsCommonLink + link;

            try {
                Document doc = Jsoup.connect(currentLink)
                        .userAgent("Mozilla/5.0")
                        .timeout(15000)
                        .get();
                parseArticlePage(doc);
            } catch (Exception e) {
                log.warn("Failed to fetch: {} | Reason: {}", currentLink, e.getMessage());
            }

            count++;

            // Short delay between requests
            Thread.sleep(300);

            // Longer pause every 10 requests
            if (count % 10 == 0) {
                log.info("Processed {} articles. Sleeping for 1 second...", count);
                Thread.sleep(1000);
            }
        }
    }

    private void parseArticlePage(Document doc) {
        Element mainBlock = doc.selectFirst(".eMessage");
        if (mainBlock == null) return;

        List<String> fileUrls = getFileUrls(mainBlock);
        // >>> Faylları yadda saxla, adlarını al:
        List<String> savedFilenames = getSavedFileNames(fileUrls);

        String title = getTitle(doc);
        String content = getTextContent(mainBlock);
        String author = getAuthor(doc);

        Integer categoryId = getCategoryIdByTitle(title);
        Integer authorId = getOrCreateAuthorId(author);

        Integer views = getViewCount(doc);
        LocalDate date = getCreateDate(doc);
        LocalTime time = getCreateTime(doc);
        LocalDateTime createdAt = (date != null && time != null) ? LocalDateTime.of(date, time) : LocalDateTime.now();

        String excerpt = (content.length() > 300) ? content.substring(0, 300) : content;
        String image = (savedFilenames != null && !savedFilenames.isEmpty()) ? savedFilenames.get(0) : null;

        BlogPostEntity post = new BlogPostEntity();
        post.setTitle(title);
        post.setContent(content);
        post.setExcerpt(excerpt);
        post.setImage(image);
        post.setCategoryId(categoryId);
        post.setAuthorId(authorId);
        post.setCreatedAt(createdAt);
        post.setUpdatedAt(createdAt);

        List<BlogImageEntity> imageEntities = new ArrayList<>();
        if (savedFilenames != null) {
            for (int i = 0; i < savedFilenames.size(); i++) {
                BlogImageEntity img = new BlogImageEntity();
                img.setId(UUID.randomUUID().toString());
                img.setUrl(savedFilenames.get(i));
                img.setPost(post);
                img.setTitle(title);
                img.setDescription(excerpt);
                img.setOrder(i);
                img.setCreatedAt(createdAt);
                imageEntities.add(img);
            }
        }
        post.setImages(imageEntities);

        blogPostRepository.save(post);
        log.info("✅ Saved blog post: {}", title);
    }

    private Integer getCategoryIdByTitle(String title) {
        // Implement logic or always use DEFAULT_CATEGORY_ID for now
        return DEFAULT_CATEGORY_ID;
    }

    private Integer getOrCreateAuthorId(String author) {
        // Implement logic if author mapping is available
        return DEFAULT_AUTHOR_ID;
    }
}
