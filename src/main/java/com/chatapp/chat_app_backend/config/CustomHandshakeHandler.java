//After HttpHandshakeInterceptor is used to grab username from the URL query param, CustomHandshakeHandler is used to assign that username as the Principal. Now Spring can correctly route messages to the right user
package com.chatapp.chat_app_backend.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String username = (String) attributes.get("username");
        if (username == null) return null;
        return () -> username;  // Return a Principal with the username
    }
}