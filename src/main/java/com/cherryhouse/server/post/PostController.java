package com.cherryhouse.server.post;

import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server._core.util.Cursor;
import com.cherryhouse.server.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> getPosts(Cursor cursor){
        PostResponse.PostsDto response = postService.getPosts(cursor);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(){
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PostMapping
    public ResponseEntity<?> create(){
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PutMapping
    public ResponseEntity<?> update(){
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<?> delete(){
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
