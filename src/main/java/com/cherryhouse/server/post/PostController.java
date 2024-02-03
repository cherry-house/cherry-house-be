package com.cherryhouse.server.post;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.post.dto.PostRequest;
import com.cherryhouse.server.post.dto.PostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getPosts(@PageableDefault(size = 6) Pageable pageable){
        PostResponse.PostsDto response = postService.getPosts(pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable(name = "postId") Long postId){
        PostResponse.PostDto response = postService.getPost(postId);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @RequestPart @Valid PostRequest.CreateDto request, Errors errors,
                                    @RequestPart("images") List<MultipartFile> images){
        postService.create(request, images, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable(name = "postId") Long postId,
                                    @RequestPart @Valid PostRequest.UpdateDto request, Errors errors,
                                    @RequestPart("images") List<MultipartFile> images){
        postService.update(postId, request, images, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                    @PathVariable(name = "postId")  Long postId){
        postService.delete(postId, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
