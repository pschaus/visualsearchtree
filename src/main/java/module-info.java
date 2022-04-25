module org.uclouvain.visualsearchtree {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.uclouvain.visualsearchtree to javafx.fxml;
    exports org.uclouvain.visualsearchtree;
}