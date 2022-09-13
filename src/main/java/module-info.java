module org.uclouvain.visualsearchtree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens org.uclouvain.visualsearchtree to javafx.fxml;
    exports org.uclouvain.visualsearchtree;
    exports org.uclouvain.visualsearchtree.tree;
    opens org.uclouvain.visualsearchtree.tree to javafx.fxml;
    exports org.uclouvain.visualsearchtree.bridge;
    opens org.uclouvain.visualsearchtree.bridge to javafx.fxml;
    exports org.uclouvain.visualsearchtree.server;
    opens org.uclouvain.visualsearchtree.server to javafx.fxml;
    opens org.uclouvain.visualsearchtree.examples to javafx.fxml;
    exports org.uclouvain.visualsearchtree.examples;
}