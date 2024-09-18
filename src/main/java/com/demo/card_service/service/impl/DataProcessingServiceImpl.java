package com.demo.card_service.service.impl;

import com.demo.card_service.dto.CardRangeUpdateRequestDto;
import com.demo.card_service.service.DataProcessingService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    @Value("${zip.api-url}")
    private String apiUrl;

    @Value("${zip.temp-dir}")
    private String tempDir;

    @Value(value = "${zip.name}")
    private String zipName;

    @Value(value = "${zip.file-name}")
    private String zipFileName;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    public DataProcessingServiceImpl(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CardRangeUpdateRequestDto> fetchData() throws IOException {
        try {
            ResponseEntity<byte[]> response = restTemplate.getForEntity(apiUrl, byte[].class);
            byte[] zipFileBytes = response.getBody();
            String jsonFilePath = unzipFile(zipFileBytes);
            return cleanupData(objectMapper.readValue(new File(jsonFilePath), new TypeReference<List<CardRangeUpdateRequestDto>>() {
            }));
        } finally {
            cleanupFiles();
        }
    }

    private String unzipFile(byte[] zipFileBytes) throws IOException {
        Path tempDirPath = Paths.get(tempDir);
        if (!Files.exists(tempDirPath)) {
            Files.createDirectory(tempDirPath);
        }


        String zipFilePath = tempDir + zipName;
        Files.write(Paths.get(zipFilePath), zipFileBytes);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null && zipEntry.getName().equals(zipFileName)) {
                String fileName = zipEntry.getName();
                File newFile = new File(tempDir + fileName);

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }
        return tempDir + zipFileName;
    }

    private List<CardRangeUpdateRequestDto> cleanupData(List<CardRangeUpdateRequestDto> cardRanges) {
        return cardRanges.stream().filter(cardRangeUpdateRequestDto -> cardRangeUpdateRequestDto.getBankName() != null).toList();
    }

    private void cleanupFiles() throws IOException {
        Files.walk(Paths.get(tempDir))
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
