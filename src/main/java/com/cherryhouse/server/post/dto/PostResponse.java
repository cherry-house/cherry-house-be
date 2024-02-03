package com.cherryhouse.server.post.dto;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.posttag.PostTagMapping;
import com.cherryhouse.server.user.User;

import java.util.List;

public class PostResponse {

    public record PostsDto(
            PageData pageData,
            List<PostDto> postList
    ) {
        public static PostsDto of(
                PageData pageData,
                List<Post> postList,
                List<PostTagMapping> postTagMappingList,
                List<ImageMapping> imageMappingList
        ){
            return new PostsDto(
                    pageData,
                    postList.stream()
                            .map(post -> new PostDto(
                                    post,
                                    PostTagMapping.getTagsByPostId(postTagMappingList, post.getId()),
                                    ImageMapping.getImgUrlsByPostId(imageMappingList, post.getId())
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
                String content,
                List<ImageMapping.ImageDto> images
        ){
            public PostDto(Post post, List<String> tags, List<ImageMapping.ImageDto> images) { //TODO: 위치 로직 추가
                this(
                        post.getId(),
                        post.getTitle(),
                        null,
                        null,
                        null,
                        tags,
                        post.getContent(),
                        images
                );
            }
        }
    }

    public record PostDto (
            Long id,
            String title,
            AuthorDto author,
            String location,
            String address,
            Integer distance,
            List<String> tags,
            String content,
            List<ImageMapping.ImageDto> images
    ){
        public PostDto(Post post, List<String> tags, List<ImageMapping.ImageDto> images, User user) { //TODO: 위치 로직 추가
            this(
                    post.getId(),
                    post.getTitle(),
                    new AuthorDto(user),
                    null,
                    null,
                    null,
                    tags,
                    post.getContent(),
                    images
            );
        }

        public record AuthorDto(
                String name,
                String image
        ){
            public AuthorDto(User user){
                this(
                        user.getName(),
                        user.getProfileImage()
                );
            }
        }
    }
}
