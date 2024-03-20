package com.cherryhouse.server.heart;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "HEART", description = "좋아요(저장) API 입니다.")
@RestController
@RequestMapping("/hearts")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    @Operation(summary = "좋아요 클릭", description = "사용자가 좋아요(저장)를 클릭합니다.")
    @PostMapping
    public ResponseEntity<?> toggleHeart(@AuthenticationPrincipal UserPrincipal user,
                                         @RequestParam("postId") Long postId){
        boolean heartAdded = heartService.toggleHeart(user.getEmail(), postId);
        String msg = heartAdded ? "좋아요 추가" : "좋아요 취소";
        return ResponseEntity.ok().body(ApiResponse.success(msg));
    }

    @Operation(summary = "좋아요 목록 조회", description = "사용자가 좋아요(저장) 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getHearts(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable){
        HeartResponse.HeartDto response = heartService.getHearts(userPrincipal.getEmail(), pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }
}
