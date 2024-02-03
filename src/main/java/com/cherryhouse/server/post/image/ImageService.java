package com.cherryhouse.server.post.image;

import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    public final S3Service s3Service;
    private final ImageRepository imageRepository;

    public List<String> getImgUrls(Long postId){
        return imageRepository.findByPostId(postId)
                .stream()
                .map(Image::getAccessImgUrl)
                .toList();
    }

    public List<Image> getImages(Long postId){
        return imageRepository.findByPostId(postId);
    }

    @Transactional
    public void save(Post post, List<MultipartFile> images){
        List<String> imgUrls = s3Service.upload(images,"post");

        imgUrls.forEach(imgUrl -> {
            String accessImgUrl = s3Service.getAccessImgUrl(imgUrl);
            Image image = Image.builder()
                    .saveImgUrl(imgUrl)
                    .accessImgUrl(accessImgUrl)
                    .post(post)
                    .build();
            imageRepository.save(image);
        });
    }

    @Transactional
    public void update(Long postId){

    }

    @Transactional
    public void delete(Long postId){
        imageRepository.findByPostId(postId)
                .forEach(image -> {
                    s3Service.delete(image.getSaveImgUrl());
                    imageRepository.deleteByPostId(postId);
                });
    }

    public List<ImageMapping.ImageDto> getImageDtoList(Long postId) {
        return imageRepository.findByPostId(postId)
                .stream()
                .map(image -> new ImageMapping.ImageDto(image.getId(), image.getAccessImgUrl()))
                .toList();
    }
}
