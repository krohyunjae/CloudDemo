package org.repoapi.domain;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;


@Service
public class FileServiceImpl implements FileService {
    @Override
    public void upload(String path, String fileName, Optional<Map<String, String>> optionalMetaData, InputStream inputStream) {

    }

    @Override
    public byte[] download(String path, String key) {
        return new byte[0];
    }
}
