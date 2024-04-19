package com.mini.advice_park.domain.openAI;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatResponse {

    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
    }

}
