package com.cherryhouse.server.posttag;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostTagMapping {

    //일급 클래스에 담아서 반환하기

    public record TagsDto (
            Long postId,
            List<String> tags
    ){}

    //response 에서 사용하는 메소드로 response dto 형태로 변환
    public static List<String> getTagsByPostId(List<PostTagMapping.TagsDto> tagsDtoList, Long postId){
        return tagsDtoList.stream()
                .filter(tagsDto -> tagsDto.postId.equals(postId))
                .findFirst()
                .map(TagsDto::tags)
                .orElseThrow();
    }
}