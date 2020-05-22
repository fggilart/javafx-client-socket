module com.fidel.clientesocketjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires data.paqueta;
    opens com.fidel.clientesocketjavafx to javafx.fxml;
    exports com.fidel.clientesocketjavafx;
}
