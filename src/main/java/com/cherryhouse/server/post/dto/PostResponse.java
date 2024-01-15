package com.cherryhouse.server.post.dto;

import com.cherryhouse.server._core.util.Cursor;
import com.cherryhouse.server.post.Post;

import java.util.List;

public class PostResponse {

    public record PostsDto(
            Cursor cursor,
            List<PostDto> postList
    ) {
        public static PostsDto of(Cursor cursor, List<Post> postList){
            return new PostsDto(
                    cursor,
                    postList.stream().map(PostDto::new).toList()
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
                List<String> photos
        ){
            public PostDto(Post post) { //TODO: 위치, 태그, 사진 로직 추가
                this(
                        post.getId(),
                        post.getTitle(),
                        null,
                        null,
                        null,
                        null,
                        post.getContent(),
                        null
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
            List<String> photos
    ){
        public PostDto(Post post) { //TODO: 위치, 태그, 사진 로직 추가
            this(
                    post.getId(),
                    post.getTitle(),
                    null, //TODO: User 추가
                    null,
                    null,
                    null,
                    null,
                    post.getContent(),
                    null
            );
        }

        public record AuthorDto(
                String name,
                String image
        ){} //TODO: user 엔티티 생성되면 추가하기
    }
}
