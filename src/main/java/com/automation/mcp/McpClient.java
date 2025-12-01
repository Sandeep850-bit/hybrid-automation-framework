package com.automation.mcp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * McpClient.java
 * Handles communication with optional MCP (Model Context Protocol) server.
 * 
 * Features:
 * - HTTP POST requests to MCP endpoint
 * - JSON payload construction
 * - Timeout handling
 * - Error logging and recovery
 * 
 * MCP Mode: If enabled in config.properties, actions are sent to MCP server
 * instead of executing locally in Playwright.
 */
public class McpClient {
    
    private static final Logger logger = LoggerFactory.getLogger(McpClient.class);
    
    private final String mcpEndpoint;
    private final int timeout;

    /**
     * Initialize MCP Client with endpoint and timeout
     * @param mcpEndpoint MCP server URL
     * @param timeout Request timeout in milliseconds
     */
    public McpClient(String mcpEndpoint, int timeout) {
        this.mcpEndpoint = mcpEndpoint;
        this.timeout = timeout;
        RestAssured.baseURI = mcpEndpoint;
        logger.info("✓ MCP Client initialized with endpoint: " + mcpEndpoint);
    }

    /**
     * Send click action to MCP server
     * @param locatorKey Locator key to click
     * @return Response from MCP server
     */
    public Response sendClick(String locatorKey) {
        JsonObject payload = new JsonObject();
        payload.addProperty("action", "click");
        payload.addProperty("locatorKey", locatorKey);
        
        return sendAction(payload);
    }

    /**
     * Send type action to MCP server
     * @param locatorKey Locator key to type into
     * @param value Text to type
     * @return Response from MCP server
     */
    public Response sendType(String locatorKey, String value) {
        JsonObject payload = new JsonObject();
        payload.addProperty("action", "type");
        payload.addProperty("locatorKey", locatorKey);
        payload.addProperty("value", value);
        
        return sendAction(payload);
    }

    /**
     * Send select action to MCP server
     * @param locatorKey Locator key for select element
     * @param value Option value to select
     * @return Response from MCP server
     */
    public Response sendSelect(String locatorKey, String value) {
        JsonObject payload = new JsonObject();
        payload.addProperty("action", "select");
        payload.addProperty("locatorKey", locatorKey);
        payload.addProperty("value", value);
        
        return sendAction(payload);
    }

    /**
     * Send wait action to MCP server
     * @param locatorKey Locator key to wait for
     * @param timeoutMs Timeout in milliseconds
     * @return Response from MCP server
     */
    public Response sendWait(String locatorKey, int timeoutMs) {
        JsonObject payload = new JsonObject();
        payload.addProperty("action", "wait");
        payload.addProperty("locatorKey", locatorKey);
        payload.addProperty("timeout", timeoutMs);
        
        return sendAction(payload);
    }

    /**
     * Send generic action payload to MCP server
     * @param payload JsonObject with action details
     * @return Response from MCP server
     */
    public Response sendAction(JsonObject payload) {
        try {
            logger.debug("Sending MCP action: " + payload.toString());
            
            Response response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(payload.toString())
                    .when()
                    .post("/action")
                    .then()
                    .extract()
                    .response();

            if (response.getStatusCode() == 200) {
                logger.info("✓ MCP action successful: " + payload.get("action").getAsString());
            } else {
                logger.warn("⚠ MCP action returned status: " + response.getStatusCode());
            }

            return response;
        } catch (Exception e) {
            logger.error("✗ MCP action failed: " + e.getMessage(), e);
            throw new RuntimeException("MCP action execution failed", e);
        }
    }

    /**
     * Check MCP server health
     * @return true if server is responding
     */
    public boolean isHealthy() {
        try {
            Response response = RestAssured
                    .given()
                    .when()
                    .get("/health")
                    .then()
                    .extract()
                    .response();

            boolean healthy = response.getStatusCode() == 200;
            logger.info("MCP Server health check: " + (healthy ? "✓ Healthy" : "✗ Unhealthy"));
            return healthy;
        } catch (Exception e) {
            logger.error("✗ MCP Server health check failed", e);
            return false;
        }
    }

    /**
     * Extract success status from MCP response
     * @param response MCP response
     * @return true if action was successful
     */
    public boolean isSuccessful(Response response) {
        try {
            JsonObject json = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
            return json.has("success") && json.get("success").getAsBoolean();
        } catch (Exception e) {
            logger.error("Error parsing MCP response", e);
            return false;
        }
    }
}
