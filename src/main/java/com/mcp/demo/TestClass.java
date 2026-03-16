package com.mcp.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@RestController
public class TestClass {

    private final ToolCallbackProvider toolCallbackProvider;
    private final ChatClient client;

    public TestClass(ToolCallbackProvider toolCallbackProvider,
                     ChatClient client) {
        this.toolCallbackProvider = toolCallbackProvider;
        this.client = client;
    }

    @PostMapping("/")
    public String connect(@RequestBody com.mcp.demo.RequestBody requestBody){
        System.out.println(requestBody.url); // http://localhost:8081/sse used sse endpoint to connect YAYYYYYY
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(requestBody.url);
        McpClientTransport transport = HttpClientSseClientTransport.builder(requestBody.url).build();

        McpSyncClient mcpClient = McpClient.sync(transport)
                .build();

        mcpClient.initialize();

        System.out.println(mcpClient.listTools());
        return client.prompt()
                .user("What tools do you have")
                .toolCallbacks(SyncMcpToolCallbackProvider.syncToolCallbacks(List.of(mcpClient))).
                call()
                .content();
    }

    private void p(ToolCallbackProvider provider){
        System.out.println(Arrays.toString(provider.getToolCallbacks()));
    }

}
