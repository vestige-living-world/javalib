package io.vestige.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard API error response body.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    @JsonProperty("error")
    private ErrorDetail error;

    public ErrorDetail getError() { return error; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorDetail {
        @JsonProperty("type")
        private String type;

        @JsonProperty("message")
        private String message;

        @JsonProperty("code")
        private String code;

        public String getType() { return type; }
        public String getMessage() { return message; }
        public String getCode() { return code; }
    }
}
