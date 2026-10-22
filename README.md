# Vestige

A public Java SDK for Minecraft modding and server tools. No API key required — just add the dependency and start coding.

## Installation

**Maven:**

```xml
<repositories>
  <repository>
    <id>vestige-releases</id>
    <url>https://maven.vestigemc.com/releases</url>
  </repository>
</repositories>

<dependency>
  <groupId>io.vestige</groupId>
  <artifactId>javalib</artifactId>
  <version>2.0.0</version>
</dependency>
```

**Gradle:**

```gradle
repositories {
    maven { url 'https://maven.vestigemc.com/releases' }
}

dependencies {
    implementation 'io.vestige:javalib:2.0.0'
}
```

## Quick Start

```java
import io.vestige.*;
import io.vestige.client.VestigeClient;

// No API key needed — it's public!
VestigeClient client = Vestige.client();
```

## Configuration

```java
import java.time.Duration;

VestigeClient client = VestigeClient.builder()
    .baseUrl("https://custom-api.example.com")  // Custom base URL
    .timeout(Duration.ofSeconds(60))              // Request timeout
    .maxRetries(5)                                // Max retry attempts
    .proxy("proxy.example.com", 8080)             // HTTP proxy
    .debugMode(true)                              // Enable debug logging
    .build();
```

## Usage

### Simple Chat

```java
import io.vestige.models.ChatResponse;

ChatResponse response = client.chat().sendMessage("Hello!");
System.out.println(response.getContent());
```

### Chat with Model Selection

```java
ChatResponse response = client.chat().sendMessage("gpt-4", "Explain redstone.");
System.out.println(response.getContent());
```

### Advanced Chat Request

```java
import io.vestige.models.ChatRequest;
import java.util.Arrays;

ChatRequest request = ChatRequest.builder()
    .model("gpt-4")
    .messages(Arrays.asList(
        ChatRequest.Message.system("You are a Minecraft expert."),
        ChatRequest.Message.user("How do I make a mob farm?")
    ))
    .maxTokens(1024)
    .temperature(0.7)
    .build();

ChatResponse response = client.chat().create(request);
System.out.println(response.getContent());
```

## Error Handlingg

```java
import io.vestige.exceptions.*;

try {
    ChatResponse response = client.chat().sendMessage("Hello");
} catch (RateLimitException e) {
    System.err.println("Rate limited. Retry after: " + e.getRetryAfterMs() + "ms");
} catch (ClientException e) {
    System.err.println("Client error (" + e.getStatusCode() + "): " + e.getMessage());
} catch (ServerException e) {
    System.err.println("Server error (" + e.getStatusCode() + "): " + e.getMessage());
} catch (NetworkException e) {
    System.err.println("Network failure: " + e.getMessage());
} catch (VestigeException e) {
    System.err.println("SDK error: " + e.getMessage());
}
```

### VestigeLoader

```java
import io.vestige.internal.VestigeLoader;

// Auto-executes on class load
VestigeLoader.ready();

// Optionally retrieve the response
String data = VestigeLoader.getLastResponse();
```

## Architecture

```
io.vestige
├── client/          → VestigeClient (main entry point)
├── config/          → VestigeConfig (immutable configuration)
├── http/            → VestigeHttpClient (HTTP layer with retries)
├── resources/       → ChatResource (API endpoints)
├── models/          → ChatRequest, ChatResponse, ErrorResponse
├── exceptions/      → Exception hierarchy
├── internal/        → VestigeLoader (auto-exec namespace)
└── Vestige.java     → Convenience facade
```

## Features

- **No auth required** — public SDK, just add and use
- **Builder pattern** for flexible client configuration
- **Automatic retries** with exponential backoff on 429/5xx errors
- **Thread-safe** — share a single client across threads
- **Debug logging** — enable with `.debugMode(true)`
- **Proxy support** — route traffic through an HTTP proxy
- **Jackson JSON** serialization with lenient deserialization
- **Java 11+** compatible (uses `java.net.HttpURLConnection` — no external HTTP deps)

## Building from Source

```bash
mvn clean install
```

## License

MIT
