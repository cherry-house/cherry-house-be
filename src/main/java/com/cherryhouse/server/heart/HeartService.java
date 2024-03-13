package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.post.image.ImageService;
import com.cherryhouse.server.post.posttag.PostTagMapping;
import com.cherryhouse.server.post.tag.TagService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartService {

    private final UserService userService;
    private final PostService postService;
    private final TagService tagService;
    private final ImageService imageService;
    private final HeartRepository heartRepository;

    @Transactional
    public boolean toggleHeart(String email, Long postId) {
        User user = userService.findByEmail(email);

        if (heartExists(user, postId)) {
            //하트가 이미 존재하면 하트 삭제
            heartRepository.deleteByUserAndPostId(user, postId);
            return false;
        }else{
            //하트가 없으면 하트 추가
            Post post = postService.getPostById(postId);
            Heart heart = Heart.builder()
                    .user(user)
                    .post(post)
                    .build();

            heartRepository.save(heart);
            return true;
        }
    }

    private boolean heartExists(User user, Long postId){
        return heartRepository.existsByUserAndPost(user.getEmail(), postId);
    }

    public HeartResponse.HeartDto getHearts(String email, Pageable pageable) {
        userService.findByEmail(email);

        Page<Post> postList = heartRepository.findPostByUserEmail(email, pageable);

        PageData pageData = PageData.getPageData(postList);
        List<PostTagMapping.TagsDto> tagsDtoList = tagService.getTagsDtoList(postList.getContent());
        List<ImageMapping.UrlDto> urlDtoList = imageService.getUrlDtoList(postList.getContent());

        return HeartResponse.HeartDto.of(pageData, postList.getContent(), tagsDtoList, urlDtoList);
    }
}