package az.informatik.msparser.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileUtil {

    public static List<String> getFileUrls(Element mainBlock) {
        return mainBlock.select("img").stream()
                .map(img -> img.attr("src"))
                .toList();
    }

    public static List<String> getSavedFileNames(List<String> fileUrls) {
        List<String> savedFilenames = new ArrayList<>();
        for (String url : fileUrls) {
            String savedFile = downloadAndSaveFile(url);
            if (savedFile != null) {
                savedFilenames.add(savedFile);
            }
        }

        return savedFilenames;
    }

    private static String downloadAndSaveFile(String relativeUrl) {
        String baseUrl = "https://www.informatik.az";
        String fullUrl = relativeUrl.startsWith("http") ? relativeUrl : "https://www.informatik.az" + relativeUrl;

        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(relativeUrl);
        String filename = uuid + extension;
        Path targetPath = Paths.get("files", filename);

        try (InputStream in = new URL(fullUrl).openStream()) {
            Files.createDirectories(targetPath.getParent());
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("✅ File saved: {}", targetPath);
            return filename;
        } catch (IOException e) {
            log.error("❌ Failed to download: {}", fullUrl, e);
            return null;
        }
    }

    private static String getFileExtension(String url) {
        String path = url.toLowerCase();
        return path.contains(".") ? path.substring(path.lastIndexOf(".")) : "";
    }

}
