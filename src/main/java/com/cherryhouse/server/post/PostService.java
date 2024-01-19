package com.cherryhouse.server.post;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.dto.PostRequest;
import com.cherryhouse.server.post.dto.PostResponse;
import com.cherryhouse.server.posttag.PostTagMapping;
import com.cherryhouse.server.tag.TagService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserService userService;

    @Transactional
    public void create(PostRequest.CreateDto createDto){
        if(isNotCategoryValid(createDto.category())) {
    public void create(PostRequest.CreateDto createDto, String email){
        if(isNotValid(createDto.category())) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "카테고리 입력이 올바르지 않습니다.");
        }

        //TODO: 위치, 사진 로직 추가
        Post post = save(createDto);
        tagService.create(post, createDto.tags());
    }

    private Post save(PostRequest.CreateDto createDto) {
        User user = userService.findByEmail(email);

        //TODO: 위치, 태그, 사진 로직 추가
        Post post = Post.builder()
                .user(user)
                .title(createDto.title())
                .category(createDto.category())
                .content(createDto.content())
                .build();

        postRepository.save(post);
        tagService.create(post, createDto.tags());
    }

    @Transactional
    public void update(Long postId, PostRequest.UpdateDto updateDto){
        if(isNotCategoryValid(updateDto.category())) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "카테고리 입력이 올바르지 않습니다.");
        }

        Post post = getPostById(postId);
        post.update(updateDto.title(), updateDto.content(), updateDto.category());
        if(!updateDto.tags().isEmpty()){
            tagService.update(post, updateDto.tags());
        }
        //TODO: 위치, 사진 추가
    }

    @Transactional
    public void delete(Long postId, String email){
        getPostById(postId);
        tagService.delete(postId);
        if (postRepository.findByIdAndUserEmail(postId,email).isEmpty() ){
            throw new ApiException(ExceptionCode.POST_NOT_FOUND , "해당 작성자가 작성한 글이 아닙니다.");
        }
        postRepository.deleteById(postId);
    }

    public PostResponse.PostDto getPost(Long postId){
        Post post = getPostById(postId);
        List<String> tags = tagService.getTags(postId);
        return new PostResponse.PostDto(post, tags);
    }

    public PostResponse.PostsDto getPosts(Pageable pageable){
        Page<Post> postList = postRepository.findAllOrderByCreatedDate(pageable);
        PageData pageData = getPageData(postList);
        List<PostTagMapping> postTagMappings = postList.stream() //post id 마다 tags 를 일급 클래스에 담아서 가지고 오기
                .map(post -> new PostTagMapping(post.getId(), tagService.getTags(post.getId())))
                .toList();
        return PostResponse.PostsDto.of(pageData, postList.getContent(), postTagMappings);
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionCode.POST_NOT_FOUND));
    }

    private boolean isNotCategoryValid(Category category) {
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
