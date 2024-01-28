package com.cherryhouse.server.user;


import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/style")
    public ResponseEntity<?> uploadStyle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestParam("file") List<MultipartFile> file){
        userService.uploadStyle(file,userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @DeleteMapping("/style")
    public ResponseEntity<?> deleteStyle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestParam("file") String filePath){
        userService.deleteStyle(filePath,userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
