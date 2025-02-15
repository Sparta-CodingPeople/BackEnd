package com.server.delivery.domain.user.controller;

import com.server.delivery.util.s3image.S3ImageUtilImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final S3ImageUtilImpl s3ImageUtilImpl;

    @GetMapping("/api/v1/user")
    public ResponseEntity<Void> test(){
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/api/v1/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> testPost(
            @RequestPart("image") MultipartFile image
    ){
        String fileName = s3ImageUtilImpl.uploadImageToS3(image);
        return ResponseEntity.ok().body(fileName);
    }

    @GetMapping(value = "/api/v1/user/image")
    public ResponseEntity<String> testPost(
            @RequestParam("image") String image
    ){
        System.out.println("이미지 = " + image);
        String fileName = s3ImageUtilImpl.readImageFromS3(image);
        return ResponseEntity.ok().body(fileName);
    }

    @DeleteMapping(value = "/api/v1/user")
    public ResponseEntity<Void> testPost2(
            @RequestParam("image") String image
    ){
         s3ImageUtilImpl.deleteImageFromS3(image);
        return ResponseEntity.ok().build();
    }
}
