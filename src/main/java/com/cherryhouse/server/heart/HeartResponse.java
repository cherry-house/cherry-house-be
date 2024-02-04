package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.posttag.PostTagMapping;

import java.util.List;

public class HeartResponse {

    public record HeartDto(
            PageData page,
            List<PostDto> postList
    ){
        public static HeartDto of(
                PageData pageData,
                List<Post> postList,
                List<PostTagMapping.TagsDto> tagsDtoList,
                List<ImageMapping.UrlDto> urlDtoList
        ){
            return new HeartDto(
                    pageData,
                    postList.stream()
                            .map(post -> new HeartResponse.HeartDto.PostDto(
                                    post,
                                    PostTagMapping.getTagsByPostId(tagsDtoList, post.getId()),
                                    ImageMapping.getUrlByPostId(urlDtoList, post.getId())
                            ))
                            .toList()
            );
        }

        public record PostDto (
                Long id,
                String title,
                String location,
                String address,
                Integer distance,
                List<String> tags,
                String image
        ){
            //TODO: 위치 로직 추가
            public PostDto(Post post, List<String> tags, String image) {
                this(
                        post.getId(),
                        post.getTitle(),
                        null,
                        null,
                        null,
                        tags,
                        image
                );
            }
        }
    }
}
