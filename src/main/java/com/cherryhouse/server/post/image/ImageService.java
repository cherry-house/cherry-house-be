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

    @Transactional
    public void save(Post post, List<MultipartFile> photos){
        List<String> photoUrls = s3Service.upload(photos,"post");

        photoUrls.forEach(photoUrl -> {
            Image image = Image.builder()
                    .saveImgUrl(photoUrl)
                    .accessImgUrl(s3Service.getAccessImgUrl(photoUrl))
                    .post(post)
                    .build();
            imageRepository.save(image);
        });
    }

    @Transactional
    public void delete(Long postId){
        imageRepository.findByPostId(postId).forEach(image -> {
            s3Service.delete(image.getSaveImgUrl());
            imageRepository.deleteByPostId(postId);
        });
    }

    public List<String> getImgUrls(Long postId){
        return null;
    }
}
