package com.cherryhouse.server.user.dto;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.posttag.PostTagMapping;
import com.cherryhouse.server.style.Style;
import com.cherryhouse.server.user.User;

import java.util.List;

public class UserResponse {

    public record UsersDto(
            String email,
            String username,
            String profileImg
    ){
        public UsersDto(User user){
            this(
                    user.getEmail(),
                    user.getName(),
                    user.getProfileImage());
        }
    }

    public record UserDto(
            PageData pageData,
            String username,
            String profileImg,
            String introduction,
            List<StyleDto> styleList,
            List<PostDto> postList

    ){
        public static UserDto of(PageData pageData,
                                 User user,
                                 List<Style> styleList,
                                 List<Post> postList,
                                 List<PostTagMapping.TagsDto> tagsDtoList
        ){
            return new UserDto(
                    pageData,
                    user.getName(),
                    user.getProfileImage(),
                    user.getIntroduction(),
                    styleList.stream()
                            .map(StyleDto::new)
                            .toList(),
                    postList.stream()
                            .map(post -> new PostDto(post, PostTagMapping.getTagsByPostId(tagsDtoList, post.getId())))
                            .toList()

            );
        }

        public record StyleDto(
                Long id,
                String img
        ){
            public StyleDto(Style style){
                this(
                        style.getId(),
                        style.getImgUrl()
                );
            }
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
            public PostDto(Post post, List<String> tags) {
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
