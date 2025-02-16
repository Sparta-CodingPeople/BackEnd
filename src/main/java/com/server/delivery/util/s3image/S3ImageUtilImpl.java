package com.server.delivery.util.s3image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3ImageUtilImpl implements S3ImageUtil {

    private final AmazonS3 amazonS3Client;
    static List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    @Override
    public String uploadImageToS3(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            log.warn("Image upload failed: File is empty");
            throw new S3Exception(ExceptionCode.FILE_IS_EMPTY);
        }
        log.trace("Start image upload : {}", imageFile.getOriginalFilename());
        String originalFilename = imageFile.getOriginalFilename();
        String extension = validateImageFileExtension(originalFilename);

        String createdUUIDName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;
        log.trace("Generated S3 File Name : {}", createdUUIDName);
        try {
            InputStream is = imageFile.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(bytes.length);

            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
                log.info("Uploading file to S3 bucket: {}, file name: {}", bucket, createdUUIDName);
                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(bucket, createdUUIDName, byteArrayInputStream, metadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead);
                amazonS3Client.putObject(putObjectRequest);
                log.info("File successfully uploaded to S3: {}", createdUUIDName);            } catch (IOException e) {
                log.error("S3 Upload Image Failed (ByteArrayInputStream) : {}", e.getMessage());
                throw new S3Exception(ExceptionCode.PUT_OBJECT_EXCEPTION);
            }

        } catch (IOException e) {
            log.error("S3 Upload Image Failed (File Read) : {}", e.getMessage());
            throw new S3Exception(ExceptionCode.FILE_READ_FAILED);
        }

        return amazonS3Client.getUrl(bucket,createdUUIDName).toString();
    }

    @Override
    public void deleteImageFromS3(String imageAddress) {
        String decodkingKey = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3Client.deleteObject(bucket, decodkingKey);
        }catch (Exception e){
            throw new S3Exception(ExceptionCode.FILE_ON_IMAGE_DELETE);
        }
    }

    @Override
    public String readImageFromS3(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String validateImageFileExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex == -1) {
            log.warn("validation failed : No File Extension Found in {}", originalFilename);
            throw new S3Exception(ExceptionCode.FILE_MISSING_EXTENSION);
        }
        String extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        if (!allowedExtensionList.contains(extension)) {
            log.warn("validation failed : File Extension Not Allowed : {}", extension);
            throw new S3Exception(ExceptionCode.INVALID_FILE_EXTENSION);
        }
        log.trace("File Extension Validated : {}", extension);
        return extension;
    }

    private String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1);
        } catch (MalformedURLException e) {
            throw new S3Exception(ExceptionCode.FILE_ON_DECODING_KEY);
        }
    }
}
