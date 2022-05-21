module com.server.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.server.server to javafx.fxml;
    exports com.server.server;
}