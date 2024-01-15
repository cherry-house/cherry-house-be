package com.cherryhouse.server.post.dto;

import java.util.List;

public class PostRequest {

    //TODO: validation
    public record CreateDto(
        String title,
        String location,
        String category,
        List<String> tags,
        String content,
        List<String> photos
    ){}

    public record UpdateDto(
        String title,
        String location,
        String category,
        List<String> tags,
        String content,
        List<String> photos
    ){}
}
