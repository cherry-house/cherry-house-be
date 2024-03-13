package com.cherryhouse.server.post.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageMapping {

    //일급 클래스에 담아서 반환하기
    public record ImageDto (
            Long id,
            String url
    ){
        public ImageDto(Image image) {
            this(
                    image.getId(),
                    image.getAccessImgUrl()
            );
        }
    }

    public record UrlDto (
            Long postId,
            String url
    ){}

    //response 에서 사용하는 메소드로 response dto 형태로 변환
    public static String getUrlByPostId(List<ImageMapping.UrlDto> urlDtoList, Long postId){
        return urlDtoList.stream()
                .filter(imagesDto -> imagesDto.postId.equals(postId))
                .findFirst()
                .orElseThrow()
                .url;
    }
}