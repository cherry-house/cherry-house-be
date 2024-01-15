package com.cherryhouse.server.post;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.Cursor;
import com.cherryhouse.server.post.dto.PostRequest;
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

    @Transactional
    public void create(PostRequest.CreateDto createDto){
        //TODO: 위치, 태그, 사진 로직 추가
        Post post = Post.builder()
                .title(createDto.title())
                .category(createDto.category())
                .content(createDto.content())
                .build();
        postRepository.save(post);
    }

    @Transactional
    public void update(Long postId, PostRequest.UpdateDto updateDto){
        Post post = getPostById(postId);
        post.update(updateDto.title(), updateDto.content(), updateDto.category());
        //TODO: 위치, 태그, 사진 추가
    }

    @Transactional
    public void delete(Long postId){
        getPostById(postId);
        postRepository.deleteById(postId);
    }

    public PostResponse.PostDto getPost(Long postId){
        Post post = getPostById(postId);
        return new PostResponse.PostDto(post);
    }

    public PostResponse.PostsDto getPosts(Cursor cursor){
        List<Post> postList = getPostList(cursor);
        Long lastKey = getLastKey(postList);
        return PostResponse.PostsDto.of(new Cursor(lastKey, postList.size()), postList);
    }

    private List<Post> getPostList(Cursor cursor){
        Pageable pageable = PageRequest.ofSize(cursor.getSize());
        return cursor.hasKey() ? postRepository.findAllByIdLessThanOrderOrderByCreatedDate(cursor.getKey(), pageable)
                : postRepository.findAllOrderByCreatedDate(pageable);
    }

    private Long getLastKey(List<Post> postList){
        return postList.isEmpty() ? -1L : postList.get(postList.size() - 1).getId();
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionCode.POST_NOT_FOUND));
    }
}
