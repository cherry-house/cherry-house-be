package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostRepository;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.posttag.PostTagMapping;
import com.cherryhouse.server.tag.TagService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HeartService {

    private final UserService userService;
    private final PostService postService;
    private final HeartRepository heartRepository;
    private final TagService tagService;

    @Transactional
    public boolean toggleHeart(String email, Long postId) {
        User user = userService.findByEmail(email);

        if (heartExists(user,postId)) {           //하트가 이미 존재하면 하트 삭제
            heartRepository.deleteByUserAndPostId(user, postId);
            return false;
        }else{                                  //하트가 없으면 하트 추가
            Post post = postService.getPostById(postId);
            Heart heart = Heart.builder()
                    .user(user)
                    .post(post)
                    .build();

            heartRepository.save(heart);

            return true;
        }
    }

    public boolean heartExists(User user, Long postId){
        return heartRepository.existsByUserAndPost(user.getEmail(), postId);
    }

    public HeartResponse.HeartDto getHearts(String email, Pageable pageable) {

        User user = userService.findByEmail(email);
        Page<Post> postList = heartRepository.findPostByUserEmail(email, pageable);

        PageData pageData = PageData.getPageData(postList);
        List<PostTagMapping> postTagMappings = postList.stream()
                .map(post -> new PostTagMapping(post.getId(), tagService.getTags(post.getId())))
                .toList();
        return HeartResponse.HeartDto.of(pageData, postList.getContent(), postTagMappings);
    }


}












