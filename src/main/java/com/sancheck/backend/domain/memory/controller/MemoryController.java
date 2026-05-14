package com.sancheck.backend.domain.memory.controller;

import com.sancheck.backend.domain.memory.dto.response.MemoryResponseDto;
import com.sancheck.backend.domain.memory.sevice.MemoryService;
import com.sancheck.backend.global.security.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/memories")
@RequiredArgsConstructor
public class MemoryController {

    private final MemoryService memoryService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MemoryResponseDto>> getMyMemories(@AuthenticationPrincipal CustomUserDetails userDetails){

        Long userId = userDetails.getUser().getId();
        return ResponseEntity.ok(memoryService.getUserMemories(userId));
    }

    @DeleteMapping("/{memoryId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteMemory(@PathVariable Long memoryId) {
        memoryService.deleteMemory(memoryId);
        return ResponseEntity.noContent().build();
    }

}
