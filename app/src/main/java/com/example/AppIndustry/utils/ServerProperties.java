package com.example.AppIndustry.utils;

public interface ServerProperties {
    final int SERVER_PORT = 8888;
    final String SERVER_IP = "10.0.2.2";
    final String SERVER_URIs = "ws://" + SERVER_IP + ":" + SERVER_PORT;
    final int SERVER_QUERY_DELAY = 1000;
}
