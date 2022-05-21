module com.chat.chatwithrsa {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens com.chat.chatwithrsa to javafx.fxml;
    exports com.chat.chatwithrsa;
}