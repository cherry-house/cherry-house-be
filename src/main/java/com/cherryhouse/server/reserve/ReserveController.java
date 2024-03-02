package com.cherryhouse.server.reserve;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @PostMapping
    public ResponseEntity<?> reserve(@AuthenticationPrincipal UserPrincipal user,
                                     @Valid @RequestBody ReserveRequest.makeReserveDto makeReserveDto){
        reserveService.reserve(user,makeReserveDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PutMapping("/change")
    public ResponseEntity<?> updateReserve(@AuthenticationPrincipal UserPrincipal user,
                                     @Valid @RequestBody ReserveRequest.changeReserveDto changeReserveDto){
        reserveService.update(user,changeReserveDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @GetMapping
    public ResponseEntity<?> getReservation(@AuthenticationPrincipal UserPrincipal user, Pageable pageable){
        ReserveResponse.ReserveDto response = reserveService.get(user.getEmail(),pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }




}
