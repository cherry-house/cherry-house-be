package com.cherryhouse.server.post.dto;

import com.cherryhouse.server._core.util.Cursor;
import com.cherryhouse.server.post.Post;
import lombok.Getter;

import java.util.List;

public class PostResponse {

    @Getter
    public static class PostsDto{
        private final Cursor cursor;
        private final List<Post> postList;

        public PostsDto(Cursor cursor, List<Post> postList) {
            this.cursor = cursor;
            this.postList = postList;
        }

        @Getter
        public class PostDto{
            private final Long id;
            private final String title;
            private final String location;
            private final String address;
            private final Integer distance;
            private final List<String> tags;
            private final String content;
            private final List<String> photos;

            public PostDto(Post post) { //TODO: 위치, 태그, 사진 로직 추가
                this.id = post.getId();
                this.title = post.getTitle();
                this.location = null;
                this.address = null;
                this.distance = null;
                this.tags = null;
                this.content = post.getContent();
                this.photos = null;
            }
        }
    }

    @Getter
    public static class PostDto{
        private final Long id;
        private final String title;
        private final AuthorDto author;
        private final String location;
        private final String address;
        private final Integer distance;
        private final List<String> tags;
        private final String content;
        private final List<String> photos;

        public PostDto(Post post) { //TODO: 위치, 태그, 사진 로직 추가
            this.id = post.getId();
            this.title = post.getTitle();
            this.author = new AuthorDto();
            this.location = null;
            this.address = null;
            this.distance = null;
            this.tags = null;
            this.content = post.getContent();
            this.photos = null;
        }

        @Getter
        public class AuthorDto{
            private final String name;
            private final String image;

            public AuthorDto() { //TODO: 이름, 이미지 로직 추가
                this.name = null;
                this.image = null;
            }
        }
    }
}
