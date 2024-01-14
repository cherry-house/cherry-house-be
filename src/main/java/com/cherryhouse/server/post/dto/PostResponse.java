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
            private final List<String> tags;
            private final String content;
            private final List<String> photos;

            public PostDto(Post post) {
                this.id = post.getId();
                this.title = post.getTitle();
                this.location = null;
                this.address = null;
                this.tags = null;
                this.content = post.getContent();
                this.photos = null;
            }
        }
    }

    public class PostDto{

    }
}
