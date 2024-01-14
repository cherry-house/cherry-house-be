package com.cherryhouse.server.post.dto;

import lombok.Getter;

import java.util.List;

public class PostRequest {

    //TODO: validation
    @Getter
    public static class CreateDto{
        private String title;
        private String location;
        private String category;
        private List<String> tags;
        private String content;
        private List<String> photos;
    }

    @Getter
    public static class UpdateDto{
        private String title;
        private String location;
        private String category;
        private List<String> tags;
        private String content;
        private List<String> photos;
    }
}
