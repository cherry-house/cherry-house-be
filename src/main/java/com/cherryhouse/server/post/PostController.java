package com.cherryhouse.server.post;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.post.dto.PostRequest;
import com.cherryhouse.server.post.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "POST", description = "게시글 API 입니다.")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 목록 조회", description = "게시글 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getPosts(Pageable pageable){
        PostResponse.PostsDto response = postService.getPosts(pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 내용을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable(name = "postId") Long postId){
        PostResponse.PostDto response = postService.getPost(postId);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @Operation(summary = "게시글 작성", description = "게시글을 작성합니다.")
    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @RequestPart @Valid PostRequest.CreateDto request, Errors errors,
                                    @RequestPart("images") List<MultipartFile> images){
        postService.create(request, images, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable(name = "postId") Long postId,
                                    @RequestPart @Valid PostRequest.UpdateDto request, Errors errors,
                                    @RequestPart("images") List<MultipartFile> images){
        postService.update(postId, request, images, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable(name = "postId") Long postId){
        postService.delete(postId, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
