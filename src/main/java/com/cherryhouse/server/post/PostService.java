package com.cherryhouse.server.post;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.dto.PostRequest;
import com.cherryhouse.server.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void create(PostRequest.CreateDto createDto){
        if(isNotValid(createDto.category())) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "카테고리 입력이 올바르지 않습니다.");
        }

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
        if(isNotValid(updateDto.category())) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "카테고리 입력이 올바르지 않습니다.");
        }

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

    public PostResponse.PostsDto getPosts(Pageable pageable){
        Page<Post> postList = postRepository.findAllOrderByCreatedDate(pageable);
        PageData pageData = getPageData(postList);
        return PostResponse.PostsDto.of(pageData, postList.getContent());
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionCode.POST_NOT_FOUND));
    }

    private boolean isNotValid(Category category) {
        return Arrays.stream(Category.values())
                .noneMatch(enumValue -> enumValue.name().equals(category.name().toUpperCase()));
    }

    private PageData getPageData(Page<Post> postList) {
        return new PageData(
                postList.getTotalElements(),
                postList.getTotalPages(),
                postList.isLast(),
                postList.isFirst(),
                postList.getNumber(),
                postList.getSize()
        );
    }
}
