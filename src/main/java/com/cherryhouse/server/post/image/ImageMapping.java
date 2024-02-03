package com.cherryhouse.server.post.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageMapping {

    private Long postId;
    private List<ImageDto> images;

    public record ImageDto (
            Long id,
            String url
    ){}

    public static List<ImageDto> getImgUrlsByPostId(List<ImageMapping> imageMappingList, Long postId){
        return imageMappingList.stream()
                .filter(imageMapping -> imageMapping.getPostId().equals(postId))
                .findFirst()
                .map(ImageMapping::getImages)
                .orElseThrow();
    }
}