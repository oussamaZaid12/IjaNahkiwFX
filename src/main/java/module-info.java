module ijaNahkiw {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jbcrypt;
    requires mail;
    requires protobuf.java;
    requires mysql.connector.java;

    opens javafx.fxml;

    exports Controllers;
    exports services;
    opens Controllers to javafx.fxml;
}