package com.cherryhouse.server.post;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.post.image.ImageService;
import com.cherryhouse.server.post.dto.PostRequest;
import com.cherryhouse.server.post.dto.PostResponse;
import com.cherryhouse.server.posttag.PostTagMapping;
import com.cherryhouse.server.tag.TagService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.cherryhouse.server._core.util.PageData.getPageData;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final UserService userService;
    private final ImageService imageService;

    public PostResponse.PostsDto getPosts(Pageable pageable){
        Page<Post> postList = postRepository.findAllOrderByCreatedDate(pageable);

        PageData pageData = getPageData(postList);
        List<PostTagMapping> postTagMappings = postList.stream() //post id 마다 tags 를 일급 클래스에 담아서 가지고 오기
                .map(post -> new PostTagMapping(post.getId(), tagService.getTags(post.getId())))
                .toList();
        List<ImageMapping> imageMappings = postList.stream()
                .map(post -> {
                    List<ImageMapping.ImageDto> images = imageService.getImages(post.getId())
                            .stream()
                            .map(image -> new ImageMapping.ImageDto(image.getId(), image.getAccessImgUrl()))
                            .toList();
                    return new ImageMapping(post.getId(), images);
                })
                .toList();
        return PostResponse.PostsDto.of(pageData, postList.getContent(), postTagMappings, imageMappings);
    }

    public PostResponse.PostDto getPost(Long postId){
        Post post = getPostById(postId);
        List<String> tags = tagService.getTags(postId);
        List<String> imgUrls = imageService.getImgUrls(postId);
        User user = userService.findById(post.getUser().getId());
        return new PostResponse.PostDto(post, tags, imgUrls, user);
    }

    @Transactional
    public void create(PostRequest.CreateDto createDto, List<MultipartFile> images, String email){
        validateCategory(createDto.category());

        User user = userService.findByEmail(email);

        //TODO : 위치 추가
        Post post = Post.builder()
                .user(user)
                .title(createDto.title())
                .category(createDto.category())
                .content(createDto.content())
                .build();

        postRepository.save(post);
        tagService.create(post, createDto.tags());
        imageService.save(post, images);
    }

    @Transactional
    public void update(Long postId, PostRequest.UpdateDto updateDto, List<MultipartFile> images, String email){
        validateAuthor(postId, email);
        validateCategory(updateDto.category());

        Post post = getPostById(postId);

        post.update(updateDto.title(), updateDto.content(), updateDto.category());
        if (!updateDto.tags().isEmpty()){
            tagService.update(post, updateDto.tags());
        }
        //TODO : 위치 추가
    }

    @Transactional
    public void delete(Long postId, String email){
        validateAuthor(postId, email);

        Post post = getPostById(postId);

        tagService.delete(postId);
        imageService.delete(postId);
        post.deletePost();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new ApiException(ExceptionCode.POST_NOT_FOUND));
    }

    private void validateCategory(Category category) {
        if (!Arrays.asList(Category.values()).contains(category)) {
            throw new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "카테고리 입력이 올바르지 않습니다.");
        }
    }

    private void validateAuthor(Long postId, String email) {
        if (postRepository.findByIdAndUserEmail(postId, email).isEmpty()){
            throw new ApiException(ExceptionCode.POST_NOT_FOUND, "해당 작성자가 작성한 글이 아닙니다.");
        }
    }
}
