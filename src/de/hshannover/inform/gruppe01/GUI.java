package de.hshannover.inform.gruppe01;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GUI extends Application {

    static PropLoader pl;

    ImageView imageView;
    Label desc;
    Button startButton;
    GridPane gameInfoPane;
    HBox currentlyPlaying;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        pl = new PropLoader();
        pl.loadConf(("data/conf"));

        primaryStage.setTitle("Spielesammlung - Gotta Study Fast");

        VBox root = new VBox();
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.getScene().getStylesheets().add("style.css");
        root.getStyleClass().add("background");


        StackPane titlePane = new StackPane();
        HBox gamesPane = new HBox();
        VBox gamePickPane = new VBox();
        gameInfoPane = new GridPane();
        currentlyPlaying = new HBox();

        root.getChildren().add(titlePane);
        root.getChildren().add(gamesPane);
        gamesPane.getChildren().addAll(gamePickPane, gameInfoPane, currentlyPlaying);

        initTitlePane(titlePane);
        initGamesPane(gamesPane);
        initGamePickPane(gamePickPane);
        initGameInfoPane(gameInfoPane);
        initCurrentlyPlaying(currentlyPlaying);

        updateInfoTo("MATZE");
        switchMode(false);

        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void initCurrentlyPlaying(HBox root) {
        Label label = new Label("Spiel läuft gerade..");
        root.getChildren().add(label);
        label.getStyleClass().add("currentlyPlayingLabel");
    }

    private void initGameInfoPane(GridPane root) throws FileNotFoundException {

        imageView = new ImageView(new Image(new FileInputStream(getFullPathToData(pl.author.get("MATZE").get(PropLoader.KEY_IMAGE)))));
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(500);

        StackPane imageWrapper = new StackPane(imageView);
        imageWrapper.getStyleClass().add("previewImage");

        desc = new Label();
        desc.setText("");
        desc.getStyleClass().add("desc");
        desc.setMaxWidth(600);
        desc.setPrefWidth(300);
        double padding = 20;
        desc.setPadding(new Insets(20, 0, 20, 0));

        startButton = new Button("SPIEL STARTEN");
        startButton.getStyleClass().add("startButton");

        HBox startBtnHolder = new HBox(startButton);
        startBtnHolder.setAlignment(Pos.CENTER_RIGHT);

        root.add(imageWrapper, 1, 1);
        root.add(desc, 1, 2);
        root.add(startBtnHolder, 1, 3);

    }

    private void initGamePickPane(VBox root) {

        root.setSpacing(20);


        int padVert = 0;
        int padHor = 10;
        root.setPadding(new Insets(padVert, padHor, padVert, padHor));

        Button btn1 = new Button("STUDY RACE");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateInfoTo("MATZE");
            }
        });

        Button btn2 = new Button("SPEED STUDYING");
        btn2.setOnAction(actionEvent -> updateInfoTo("JANNIS"));

        Button btn3 = new Button("JÄGER DER VERLORENEN SCHEINE");
        btn3.setOnAction(actionEvent -> updateInfoTo("MARIUS"));

        Button btn4 = new Button("CATCH THE KLAUSUREN");
        btn4.setOnAction(actionEvent -> updateInfoTo("NIKLAS"));

        Button btn5 = new Button("SPEED RACE STUDY");
        btn5.setOnAction(actionEvent -> updateInfoTo("JULIAN"));

        btn1.getStyleClass().add("buttonGamePick");
        btn2.getStyleClass().add("buttonGamePick");
        btn3.getStyleClass().add("buttonGamePick");
        btn4.getStyleClass().add("buttonGamePick");
        btn5.getStyleClass().add("buttonGamePick");

        double prefHeight = 50;

        btn1.setMaxWidth(Double.MAX_VALUE);
        btn2.setMaxWidth(Double.MAX_VALUE);
        btn3.setMaxWidth(Double.MAX_VALUE);
        btn4.setMaxWidth(Double.MAX_VALUE);
        btn5.setMaxWidth(Double.MAX_VALUE);
        btn1.setPrefHeight(prefHeight);
        btn2.setPrefHeight(prefHeight);
        btn3.setPrefHeight(prefHeight);
        btn4.setPrefHeight(prefHeight);
        btn5.setPrefHeight(prefHeight);


        root.getChildren().addAll(btn1, btn2, btn3, btn4, btn5);
    }

    private void initGamesPane(HBox root) {
        root.setFillHeight(true);
        root.setSpacing(40);
        root.setPadding(new Insets(0, 40, 0, 40));
    }

    private void initTitlePane(StackPane root) {

        int vertPadding = 50;
        root.setPadding(new Insets(vertPadding, 0, vertPadding,0));

        Label title = new Label("Gotta Study Fast");
        title.getStyleClass().add("titleFont");

        root.getChildren().add(title);

    }

    private void setDesc(String s) {
        desc.setText(s);
    }

    private void setImageView(String url) {
        try {
            imageView.setImage(new Image(new FileInputStream(url)));
        } catch(FileNotFoundException e) {
            System.out.println("Image file not found.");
            imageView.setImage(null);
        }
    }

    private void updateStartButton(String pathToJar) {
        if( !pathToJar.contains(".jar") ) {
            System.out.println("Jar-path not correctly given..");
            startButton.setText("SPIEL NICHT GEFUNDEN");
            startButton.setDisable(true);
            return;
        }

        startButton.setText("SPIEL STARTEN");
        startButton.setDisable(false);

        ProcessManager pm = new ProcessManager(this);
        pm.setJar(pathToJar);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if( pm.run() ) {
                    switchMode(true);
                } else {
                    System.out.println("Not started..");
                    switchMode(false);
                }
            }
        });
    }

    public void switchMode(boolean gameRunning) {
        currentlyPlaying.setVisible(gameRunning);
        currentlyPlaying.setManaged(gameRunning);
        gameInfoPane.setVisible(!gameRunning);
        gameInfoPane.setManaged(!gameRunning);
    }

    public void gameFinished() {
        switchMode(false);
    }

    private void updateInfoTo(String authorName) {
        setDesc(pl.author.get(authorName).get(PropLoader.KEY_DESC));
        setImageView(getFullPathToData(pl.author.get(authorName).get(PropLoader.KEY_IMAGE)));
        updateStartButton(getFullPathToData(pl.author.get(authorName).get(PropLoader.KEY_JAR)));
    }

    private String getFullPathToData(String relative) {
        return "data/" +relative;
    }
}
