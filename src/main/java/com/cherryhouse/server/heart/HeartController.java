package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hearts")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @PostMapping
    public ResponseEntity<?> toggleHeart(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestParam("postId") Long postId){
        boolean heartAdded = heartService.toggleHeart(user.getEmail(), postId);
        String msg = heartAdded ? "좋아요 추가" : "좋아요 취소";
        return ResponseEntity.ok().body(ApiResponse.success(msg));
    }

    @GetMapping
    public ResponseEntity<?> getHearts(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable){
        HeartResponse.HeartDto response = heartService.getHearts(userPrincipal.getEmail(), pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }
}
