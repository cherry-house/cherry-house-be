package com.cherryhouse.server.post.image;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
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

    @Transactional
    public void create(Post post, List<MultipartFile> images){
        save(post, images);
    }

    @Transactional
    public void update(Post post, List<MultipartFile> updatedImages, List<Long> deletedImages){
        save(post, updatedImages);

        deletedImages.forEach(imageId -> {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new ApiException(ExceptionCode.IMAGE_NOT_FOUND));
            s3Service.delete(image.getSaveImgUrl());
            imageRepository.deleteById(imageId);
        });
    }

    @Transactional
    public void delete(Long postId){
        imageRepository.findByPostId(postId).forEach(image -> {
            s3Service.delete(image.getSaveImgUrl());
            imageRepository.deleteByPostId(postId);
        });
    }

    private void save(Post post, List<MultipartFile> images) {
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

    public List<ImageMapping.ImageDto> getImageDtoList(Long postId) {
        return imageRepository.findByPostId(postId)
                .stream()
                .map(image -> new ImageMapping.ImageDto(image.getId(), image.getAccessImgUrl()))
                .toList();
    }
}
