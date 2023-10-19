package com.compound_calculator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20));
        VBox vBox = new VBox(20);

        vBox.getChildren().addAll(new InputSection(), new Table());
        hBox.getChildren().addAll(vBox);

        root.setTop(new Bar());
        root.setCenter(hBox);

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Compound Interest Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}