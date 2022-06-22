package com.practice.chat;

public interface ChatInterface {
    int Port = 13000;
    String host = "localhost";

    String ApplicationName = "KUN Chat";

    String author = """
            
            """;
    enum Error {
        ConnectFailed
    }

    String charsetName = "utf-8";
}
