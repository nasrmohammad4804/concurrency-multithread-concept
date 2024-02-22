package com.example.reentrantlock;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("cryptocurrency price");

        GridPane grid = createGrid();
        Map<String, Label> cryptoLabels = createCryptoPriceLabels();

        addLabelToGrid(cryptoLabels,grid);
        double width = 300;
        double height = 250;

        StackPane root = new StackPane();

        Rectangle background = createBackgroundRectangleWithAnimation(width,height);

        root.getChildren().add(background);
        root.getChildren().add(grid);

        stage.setScene(new Scene(root,width,height));

        PriceContainer container = new PriceContainer();

        PriceUpdater updater = new PriceUpdater(container);
        AnimationTimer timer= new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (container.getLock().tryLock()){
                    try {
                        cryptoLabels
                                .get("BTC")
                                .setText(String.valueOf(container.getBitcoinPrice()));

                        cryptoLabels.get("ETH")
                                .setText(String.valueOf(container.getEtherPrice()));

                        cryptoLabels.get("LTC")
                                .setText(String.valueOf(container.getLitecoinPrice()));

                        cryptoLabels.get("BCH")
                                .setText(String.valueOf(container.getBitcoinCashPrice()));

                        cryptoLabels.get("XRP")
                                .setText(String.valueOf(container.getRipplePrice()));
                    }finally {
                        container.getLock().unlock();
                    }
                }
            }
        };

        updater.start();
        timer.start();
        stage.show();
    }

    private Rectangle createBackgroundRectangleWithAnimation(double width,double height) {
        Rectangle background = new Rectangle(width, height);
        FillTransition fillTransition = new FillTransition(Duration.millis(1000), background, Color.LIGHTGREEN, Color.LIGHTBLUE);
        fillTransition.setCycleCount(Timeline.INDEFINITE);
        fillTransition.setAutoReverse(true);
        fillTransition.play();
        return background;
    }

    private void addLabelToGrid(Map<String, Label> cryptoLabels, GridPane grid) {

        int row = 0;
        for (Map.Entry<String, Label> entry : cryptoLabels.entrySet()) {
            String cryptoName = entry.getKey();
            Label nameLabel = new Label(cryptoName);
            nameLabel.setTextFill(Color.BLUE);
            nameLabel.setOnMousePressed(event -> nameLabel.setTextFill(Color.RED));
            nameLabel.setOnMouseReleased((EventHandler) event -> nameLabel.setTextFill(Color.BLUE));

            grid.add(nameLabel, 0, row);
            grid.add(entry.getValue(), 1, row);

            row++;
        }
    }

    private Map<String, Label> createCryptoPriceLabels() {
        javafx.scene.control.Label bitcoinPrice = new javafx.scene.control.Label("0");
        bitcoinPrice.setId("BTC");

        javafx.scene.control.Label etherPrice = new javafx.scene.control.Label("0");
        etherPrice.setId("ETH");

        javafx.scene.control.Label liteCoinPrice = new javafx.scene.control.Label("0");
        liteCoinPrice.setId("LTC");

        javafx.scene.control.Label bitcoinCashPrice = new javafx.scene.control.Label("0");
        bitcoinCashPrice.setId("BCH");

        javafx.scene.control.Label ripplePrice = new javafx.scene.control.Label("0");
        ripplePrice.setId("XRP");

        Map<String, javafx.scene.control.Label> cryptoLabelsMap = new HashMap<>();
        cryptoLabelsMap.put("BTC", bitcoinPrice);
        cryptoLabelsMap.put("ETH", etherPrice);
        cryptoLabelsMap.put("LTC", liteCoinPrice);
        cryptoLabelsMap.put("BCH", bitcoinCashPrice);
        cryptoLabelsMap.put("XRP", ripplePrice);

        return cryptoLabelsMap;
    }

    private GridPane createGrid() {

        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }
}