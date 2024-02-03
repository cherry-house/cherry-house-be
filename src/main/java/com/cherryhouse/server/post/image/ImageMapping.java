package com.cherryhouse.server.post.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageMapping {

    private Long postId;
    private List<String> imgUrls;

    public static List<String> getImgUrlsByPostId(List<ImageMapping> imageMappingList, Long postId){
        return imageMappingList.stream()
                .filter(postTagMapping -> postTagMapping.getPostId().equals(postId))
                .findFirst()
                .map(ImageMapping::getImgUrls)
                .orElseThrow();
    }
}