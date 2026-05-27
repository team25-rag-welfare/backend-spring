package com.sancheck.backend.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * S3에 파일을 업로드하고 업로드된 파일의 URL을 반환합니다.
     *
     * @param file    업로드할 MultipartFile
     * @param dirName 버킷 내 저장할 디렉토리 경로 (예: "profiles", "memories")
     * @return 업로드 완료된 파일의 S3 URL
     */
    public String uploadFile(MultipartFile file, String dirName) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String s3Key = dirName + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, 
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 파일 URL 반환
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, s3Key);

        } catch (IOException e) {
            log.error("S3 파일 업로드 중 I/O 에러 발생: {}", e.getMessage());
            throw new RuntimeException("S3 파일 업로드 실패", e);
        }
    }

    /**
     * S3에서 파일을 삭제합니다.
     *
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        String key = extractKeyFromUrl(fileUrl);
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3 파일 삭제 완료: {}", key);
        } catch (Exception e) {
            log.error("S3 파일 삭제 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException("S3 파일 삭제 실패", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return fileName.substring(lastIndex);
    }

    private String extractKeyFromUrl(String fileUrl) {
        String prefix = String.format("https://%s.s3.%s.amazonaws.com/", bucket, region);
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }

        // Region 이름이 빠진 URL 또는 기타 예외 형식 대비
        String fallbackPrefix = String.format("https://%s.s3.amazonaws.com/", bucket);
        if (fileUrl.startsWith(fallbackPrefix)) {
            return fileUrl.substring(fallbackPrefix.length());
        }

        throw new IllegalArgumentException("올바르지 않은 S3 파일 URL 형식입니다.");
    }
}
