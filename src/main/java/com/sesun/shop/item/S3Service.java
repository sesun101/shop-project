package com.sesun.shop.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3Service {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final ItemRepository itemRepository;
    String createPresignedUrl(String path) {
        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();
        var preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .putObjectRequest(putObjectRequest)
                .build();
        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }
    private String extractKeyFromUrl(String url) {
        try {
            var u = java.net.URI.create(url);
            String path = u.getPath();        // 예) /folder/file.png
            if (path == null || path.length() <= 1) return null;
            return path.substring(1);         // 선행 '/' 제거 → folder/file.png
        } catch (Exception e) {
            return null;
        }
    }
    @Transactional
    public void deleteItemImage(Long id) {
        Item item = itemRepository.findById(id).orElseThrow();

        String url = item.getImg_url();
        if (url == null || url.isBlank()) return;

        String key = extractKeyFromUrl(url);
        if (key == null) {
            return;
        }

        try {
            s3Client.deleteObject(b -> b.bucket(bucket).key(key));
            item.setImg_url(null);
            // itemRepository.save(item); // 영속 상태면 생략 가능 (Dirty Checking)
        } catch (software.amazon.awssdk.services.s3.model.S3Exception e) {
            // log.warn("S3 삭제 실패 bucket={}, key={}, code={}", bucket, key, e.awsErrorDetails().errorCode(), e);
            throw e; // 필요 시 사용자 메시지로 변환
        }
    }
}
