module org.uclouvain.visualsearchtree {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

//    opens org.uclouvain.visualsearchtree to javafx.fxml;
//    opens org.uclouvain.visualsearchtree.tree to javafx.fxml;
//    opens org.uclouvain.visualsearchtree.bridge to javafx.fxml;
//    opens org.uclouvain.visualsearchtree.server to javafx.fxml;
//    opens org.uclouvain.visualsearchtree.examples to javafx.fxml;

    opens org.uclouvain.visualsearchtree to javafx.graphics;
    opens org.uclouvain.visualsearchtree.tree to javafx.graphics;
    opens org.uclouvain.visualsearchtree.bridge to javafx.graphics;
    opens org.uclouvain.visualsearchtree.server to javafx.graphics;
    opens org.uclouvain.visualsearchtree.examples to javafx.graphics;

    exports org.uclouvain.visualsearchtree;
    exports org.uclouvain.visualsearchtree.tree;
    exports org.uclouvain.visualsearchtree.bridge;
    exports org.uclouvain.visualsearchtree.server;
    exports org.uclouvain.visualsearchtree.examples;
}