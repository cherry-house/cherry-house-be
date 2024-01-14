package com.cherryhouse.server.post;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.Cursor;
import com.cherryhouse.server.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostResponse.PostDto getPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionCode.POST_NOT_FOUND));
        return new PostResponse.PostDto(post);
    }

    public PostResponse.PostsDto getPosts(Cursor cursor){
        List<Post> postList = getPostList(cursor);
        Long lastKey = getLastKey(postList);
        return new PostResponse.PostsDto(new Cursor(lastKey, postList.size()), postList);
    }

    private List<Post> getPostList(Cursor cursor){
        Pageable pageable = PageRequest.of(0, cursor.getSize());
        if(cursor.hasKey()) return postRepository.findAllByIdLessThanOrderOrderByCreatedDate(cursor.getKey(), pageable);
        else return postRepository.findAllOrderByCreatedDate(pageable);
    }

    private Long getLastKey(List<Post> postList){
        return postList.isEmpty() ? -1L : postList.get(postList.size() - 1).getId();
    }
}
