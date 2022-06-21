module com.practice.practice {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.practice.chat.client to javafx.fxml;

    exports com.practice.chat.client;
    exports com.practice.chat.server;
    exports com.practice.chat.database;
}