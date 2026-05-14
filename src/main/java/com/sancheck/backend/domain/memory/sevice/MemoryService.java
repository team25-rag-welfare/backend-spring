package com.sancheck.backend.domain.memory.sevice;

import com.sancheck.backend.domain.chat.document.ChatMessage;
import com.sancheck.backend.domain.memory.dto.response.MemoryResponseDto;
import com.sancheck.backend.domain.memory.entity.Memory;
import com.sancheck.backend.domain.memory.repository.MemoryRepository;
import com.sancheck.backend.domain.user.entity.User;
import com.sancheck.backend.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoryService {

    private final MemoryRepository memoryRepository;
    private final UserRepository userRepository;

    //user의 과거 memory만 뽑아주는 기능
    @Transactional(readOnly = true)
    public List<String> getMemoryContents(User user){
        return memoryRepository.findByUser(user).stream()
            .map(Memory::getContent)
            .toList();
    }

    //AI server에서 포착한 새로운 메모리를 MySQL에 저장
    @Transactional
    public void saveNewMemory(User user, List<String> extractedMemories){
        if (extractedMemories != null && !extractedMemories.isEmpty()){
            for (String memory : extractedMemories){
                if (memory != null && !memory.trim().isEmpty()){
                    Memory newMemory = new Memory();
                    newMemory.setUser(user);
                    newMemory.setContent(memory);
                    memoryRepository.save(newMemory);
                    System.out.println("user의 새로운 메모리 저장 완료" + memory);
                }
            }

        }
    }

    @Transactional(readOnly = true)
    public List<MemoryResponseDto> getUserMemories(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        // repository가 IS NULL 조건을 알아서 붙여서 가져옵니다.
        return memoryRepository.findByUser(user).stream()
            .map(m -> MemoryResponseDto.builder()
                .memoryId(m.getId())
                .content(m.getContent())
                .createdAt(m.getCreatedAt())
                .build())
            .toList();
    }

    @Transactional
    public void deleteMemory(Long memoryId) {
        if (!memoryRepository.existsById(memoryId)) {
            throw new IllegalArgumentException("존재하지 않는 기억입니다.");
        }
        memoryRepository.deleteById(memoryId); // soft Delete
    }
}
