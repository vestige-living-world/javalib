package io.vestige.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Request payload for the chat endpoint.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatRequest {

    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @JsonProperty("temperature")
    private Double temperature;

    @JsonProperty("metadata")
    private Map<String, String> metadata;

    private ChatRequest() {
    }

    private ChatRequest(Builder builder) {
        this.model = builder.model;
        this.messages = builder.messages;
        this.maxTokens = builder.maxTokens;
        this.temperature = builder.temperature;
        this.metadata = builder.metadata;
    }

    public String getModel() { return model; }
    public List<Message> getMessages() { return messages; }
    public Integer getMaxTokens() { return maxTokens; }
    public Double getTemperature() { return temperature; }
    public Map<String, String> getMetadata() { return metadata; }

    public static Builder builder() { return new Builder(); }

    public static ChatRequest ofMessage(String content) {
        return builder()
                .messages(Collections.singletonList(Message.user(content)))
                .build();
    }

    public static final class Builder {
        private String model;
        private List<Message> messages;
        private Integer maxTokens;
        private Double temperature;
        private Map<String, String> metadata;

        private Builder() {}

        public Builder model(String model) { this.model = model; return this; }
        public Builder messages(List<Message> messages) { this.messages = messages; return this; }
        public Builder maxTokens(int maxTokens) { this.maxTokens = maxTokens; return this; }
        public Builder temperature(double temperature) { this.temperature = temperature; return this; }
        public Builder metadata(Map<String, String> metadata) { this.metadata = metadata; return this; }

        public ChatRequest build() { return new ChatRequest(this); }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {
        @JsonProperty("role")
        private String role;

        @JsonProperty("content")
        private String content;

        public Message() {}

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public static Message user(String content) { return new Message("user", content); }
        public static Message system(String content) { return new Message("system", content); }
        public static Message assistant(String content) { return new Message("assistant", content); }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }
}
