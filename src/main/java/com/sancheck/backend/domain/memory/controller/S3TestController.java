package com.sancheck.backend.domain.memory.controller;

import com.sancheck.backend.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
public class S3TestController {

    private final S3Service s3Service;

    /**
     * S3 파일 업로드 테스트 API
     *
     * @param file    업로드할 파일
     * @param dirName 저장할 버킷 내 디렉토리명 (기본값: test)
     * @return 업로드된 파일의 S3 URL
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "dir", defaultValue = "test") String dirName) {
        String fileUrl = s3Service.uploadFile(file, dirName);
        return ResponseEntity.ok(fileUrl);
    }

    /**
     * S3 파일 삭제 테스트 API
     *
     * @param fileUrl 삭제할 파일의 S3 URL
     * @return 삭제 결과 메세지
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("url") String fileUrl) {
        s3Service.deleteFile(fileUrl);
        return ResponseEntity.ok("파일 삭제가 완료되었습니다: " + fileUrl);
    }
}
