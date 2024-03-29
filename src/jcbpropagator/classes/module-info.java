

module jcbpropagator {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    
    requires java.prefs;
   
    requires java.base;
    requires java.desktop;
    requires java.logging;
    requires java.net.http;
    requires jdk.httpserver;
    
    opens jcbpropagator to javafx.fxml,javafx.base,java.prefs;
    exports jcbpropagator;
    
    
    opens cbm to java.base, java.desktop,java.logging;
    exports cbm;
    
    opens cliente to java.base,java.net.http;
    exports cliente;
    
    opens servidor to java.base,jdk.httpserver;
    exports servidor;
}
