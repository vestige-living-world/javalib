package io.vestige.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response payload from the chat endpoint.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created")
    private long created;

    @JsonProperty("model")
    private String model;

    @JsonProperty("choices")
    private List<Choice> choices;

    @JsonProperty("usage")
    private Usage usage;

    public String getId() { return id; }
    public String getObject() { return object; }
    public long getCreated() { return created; }
    public String getModel() { return model; }
    public List<Choice> getChoices() { return choices; }
    public Usage getUsage() { return usage; }

    public String getContent() {
        if (choices != null && !choices.isEmpty()) {
            Choice first = choices.get(0);
            if (first.getMessage() != null) {
                return first.getMessage().getContent();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "ChatResponse{id='" + id + "', model='" + model + "', content='" + getContent() + "'}";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        @JsonProperty("index")
        private int index;

        @JsonProperty("message")
        private ChatRequest.Message message;

        @JsonProperty("finish_reason")
        private String finishReason;

        public int getIndex() { return index; }
        public ChatRequest.Message getMessage() { return message; }
        public String getFinishReason() { return finishReason; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;

        @JsonProperty("completion_tokens")
        private int completionTokens;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getPromptTokens() { return promptTokens; }
        public int getCompletionTokens() { return completionTokens; }
        public int getTotalTokens() { return totalTokens; }
    }
}
