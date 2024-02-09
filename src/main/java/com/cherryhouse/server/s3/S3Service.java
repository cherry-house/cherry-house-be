package com.cherryhouse.server.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> upload(List<MultipartFile> multipartFiles, String folderName){
        List<String> fileNameList = new ArrayList<>();

        multipartFiles.forEach(file -> {
            String objectKey = uploadS3(file,folderName);
            fileNameList.add(objectKey);
        });
        return fileNameList;
    }

    public String uploadOne(MultipartFile file, String folderName){
        return uploadS3(file,folderName);
    }

    private String uploadS3(MultipartFile file, String folderName){
        String objectKey = createFilePathName(file.getOriginalFilename(),folderName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try(InputStream inputStream = file.getInputStream()){
            amazonS3.putObject(new PutObjectRequest(bucket,objectKey,inputStream,objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        }catch (IOException e){
            throw new ApiException(ExceptionCode.INTERNAL_SERVER_ERROR,"이미지 업로드에 실패했습니다.");
        }
        return objectKey;
    }

    private String createFilePathName(String originalFilename, String folderName){
        String filename = UUID.randomUUID().toString().concat(getFileExtension(originalFilename));
        String path = folderName + "/" + filename;
        return path;
    }

    private String getFileExtension(String originalFilename){
        try {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA,"잘못된 형식의 파일입니다 : "+originalFilename);
        }
    }

    public void delete(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket,fileName));
    }
}
