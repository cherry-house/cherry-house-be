package com.cherryhouse.server.posttag;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostTagMapping {

    private Long postId;
    private List<String> tags;

    public static List<String> getTagsByPostId(List<PostTagMapping> postTagMappingList, Long postId){
        return postTagMappingList.stream()
                .filter(postTagMapping -> postTagMapping.getPostId().equals(postId))
                .findFirst()
                .map(PostTagMapping::getTags)
                .orElseThrow();
    }
}
