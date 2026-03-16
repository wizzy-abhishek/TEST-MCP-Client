package com.mcp.demo;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpClientTransport;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@RestController
public class TestClass {

    private final ToolCallbackProvider toolCallbackProvider;

    public TestClass(ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @PostMapping("/")
    public String connect(@RequestBody com.mcp.demo.RequestBody requestBody){
        System.out.println(requestBody.url);
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(requestBody.url);
        McpClientTransport transport = HttpClientSseClientTransport.builder(requestBody.url).build();

        McpSyncClient mcpClient = McpClient.sync(transport)
                .build();

        mcpClient.initialize();

        System.out.println(mcpClient.listTools());
        return "Successfully connected to 8081! Tools available: " + mcpClient.listTools();
    }

    private void p(ToolCallbackProvider provider){
        System.out.println(Arrays.toString(provider.getToolCallbacks()));
    }

}
