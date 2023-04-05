package org.repoapi.domain;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileService {
    void upload(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream);
    byte[] download(String path, String key);
}
