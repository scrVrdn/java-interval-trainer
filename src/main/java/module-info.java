module io.github.scrvrdn {
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;

    opens io.github.scrvrdn to javafx.fxml;
    exports io.github.scrvrdn;
}
