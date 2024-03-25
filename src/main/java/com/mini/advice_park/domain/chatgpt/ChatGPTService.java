package com.mini.advice_park.domain.chatgpt;

import com.mini.advice_park.domain.chatgpt.dto.ChatCompletionDto;
import com.mini.advice_park.domain.chatgpt.dto.CompletionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ChatGPT 서비스 인터페이스
 */

@Service
public interface ChatGPTService {

    List<Map<String, Object>> modelList();
    Map<String, Object> isValidModel(String modelName);
    Map<String, Object> legacyPrompt(CompletionDto completionDto);
    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);

}
