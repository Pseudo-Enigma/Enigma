module Enigma {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.java;
    requires org.apache.commons.codec;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires reactfx;
    opens enigma;

    exports enigma;
}