package com.server.delivery.util.s3image;

import org.springframework.web.multipart.MultipartFile;

public interface S3ImageUtil {

    public String uploadImageToS3(MultipartFile file);

    public void deleteImageFromS3(String imageAddress);

    public String readImageFromS3(String fileName);
}
