package com.cherryhouse.server.s3;


import com.cherryhouse.server._core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;


    @PostMapping("/image")
    public ResponseEntity<?> upload(@RequestParam("file") List<MultipartFile> file){
        s3Service.upload(file,"");
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @DeleteMapping("/image")
    public ResponseEntity<?> delete(@RequestParam("file")String fileName){
        s3Service.delete(fileName);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

}
