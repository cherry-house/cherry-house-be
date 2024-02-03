package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.dto.PostResponse;
import com.cherryhouse.server.posttag.PostTagMapping;

import java.util.List;

import static com.cherryhouse.server.posttag.PostTagMapping.getTagsByPostId;

public class HeartResponse {

    public record HeartDto(

            PageData pageData,
            List<PostDto> postDtoList
    ){
        public static HeartDto of(PageData pageData,
                                  List<Post> postList,
                                  List<PostTagMapping> postTagMappingList
        ){
            return new HeartDto(
                    pageData,
                    postList.stream()
                            .map(post -> new HeartResponse.HeartDto.PostDto(post, getTagsByPostId(postTagMappingList, post.getId())))
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
                List<String> photos
        ){
            public PostDto(Post post, List<String> tags) { //TODO: 위치, 사진 로직 추가
                this(
                        post.getId(),
                        post.getTitle(),
                        null,
                        null,
                        null,
                        tags,
                        null
                );
            }
        }

    }
}
