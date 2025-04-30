package game.gui;

import game.engine.Battle;
import game.engine.lanes.Lane;
import game.engine.titans.Titan;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class gameGui extends Application {

    private Battle battle;
    private Stage primaryStage;

    private Pane gameField;
    private HBox lanesContainer;

    private VBox infoPanel;
    private Label phaseLabel, scoreLabel, turnLabel, resourcesLabel;
    private VBox weaponShopBox;

    private Button passTurnButton, restartButton;

    private int numberOfLanes;

    private Label[] laneInfoLabels;
    private Pane[] laneGamePanes;

    private Map<String, Image> imageCache = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Attack on Titan: Utopia");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        showStartScreen();
        primaryStage.show();
    }

    private void showStartScreen() {
        BorderPane startLayout = new BorderPane();
        startLayout.setPadding(new Insets(20));
        Scene startScene = new Scene(startLayout, 600, 500);
        startLayout.setStyle(
            "-fx-background-image: url('/start_bg.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;" +
            "-fx-background-repeat: no-repeat;"
        );

        Button easyModeBtn = new Button("Easy Mode");
        Button hardModeBtn = new Button("Hard Mode");
        Button instructionsBtn = new Button("Instructions");

        easyModeBtn.setOnAction(e -> startGame(3, 250));
        hardModeBtn.setOnAction(e -> startGame(5, 125));
        instructionsBtn.setOnAction(e -> showInstructions());

        HBox bottomBox = new HBox(40, easyModeBtn, hardModeBtn, instructionsBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10));
        startLayout.setBottom(bottomBox);

        primaryStage.setScene(startScene);
    }

    private void showInstructions() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Instructions");
        alert.setHeaderText("Game Instructions");
        alert.setContentText(
            "Easy Mode: 3 lanes, 250 resources per lane.\n" +
            "Hard Mode: 5 lanes, 125 resources per lane.\n" +
            "Protect your walls from the incoming titans by purchasing and deploying weapons."
        );
        alert.showAndWait();
    }

    private void startGame(int lanes, int initialResourcesPerLane) {
        this.numberOfLanes = lanes;
        try {
            this.battle = new Battle(1, 0, 300, lanes, initialResourcesPerLane);
        } catch (IOException e) {
            showError(e.getMessage());
            return;
        }

        setupGameLayout();
        setupLanes();
        setupInfoPanel();
        setupBottomControls();
        try {
            updateBattleInfo();
            updateGameField();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void setupGameLayout() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle(
            "-fx-background-image: url('/main_bg.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;" +
            "-fx-background-repeat: no-repeat;"
        );

        gameField = new Pane();
        gameField.prefWidthProperty().bind(primaryStage.widthProperty().subtract(300));
        gameField.prefHeightProperty().bind(primaryStage.heightProperty());

        lanesContainer = new HBox(20);
        lanesContainer.setPadding(new Insets(20));
        lanesContainer.prefWidthProperty().bind(gameField.prefWidthProperty());

        gameField.getChildren().add(lanesContainer);
        mainLayout.setCenter(gameField);

        infoPanel = new VBox(15);
        infoPanel.setPadding(new Insets(15));
        infoPanel.setPrefWidth(300);
        infoPanel.setStyle("-fx-background-color: rgba(224,224,224,0.8);");
        mainLayout.setRight(infoPanel);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
    }

    private void setupLanes() {
        laneInfoLabels = new Label[numberOfLanes];
        laneGamePanes   = new Pane[numberOfLanes];
        lanesContainer.getChildren().clear();

        int laneHeight = 400;  

        for (int i = 0; i < numberOfLanes; i++) {
            VBox laneBox = new VBox(5);
            laneBox.setAlignment(Pos.TOP_CENTER);
            laneBox.prefWidthProperty().bind(
                lanesContainer.widthProperty()
                    .subtract((numberOfLanes - 1) * 20)
                    .divide(numberOfLanes)
            );
            laneBox.setStyle(
                "-fx-background-image: url('/lane_bg.png');" +
                "-fx-background-size: cover;" +
                "-fx-background-position: center center;" +
                "-fx-border-color: black; -fx-border-width: 2;"
            );

            Label laneInfo = new Label("Lane " + (i + 1));
            laneInfo.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            laneInfo.setStyle("-fx-background-color: beige;");
            laneInfoLabels[i] = laneInfo;

            Pane lanePane = new Pane();
            lanePane.prefWidthProperty().bind(laneBox.prefWidthProperty());
            lanePane.setPrefHeight(laneHeight);
            lanePane.setStyle("-fx-background-color: rgba(255,255,255,0.5);");

            laneGamePanes[i] = lanePane;
            laneBox.getChildren().addAll(laneInfo, lanePane);
            lanesContainer.getChildren().add(laneBox);
        }
    }

    private void setupInfoPanel() {
        infoPanel.getChildren().clear();

        phaseLabel     = new Label("Phase: Early");
        scoreLabel     = new Label("Score: 0");
        turnLabel      = new Label("Turn: 1");
        resourcesLabel = new Label("Resources: 0");

        phaseLabel.setFont(Font.font("Verdana", FontWeight.BOLD,   14));
        scoreLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
        turnLabel.setFont(Font.font("Verdana", FontWeight.NORMAL,  14));
        resourcesLabel.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

        Label shopLabel = new Label("Weapon Shop");
        shopLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        weaponShopBox = new VBox(10);
        for (int i = 0; i < 4; i++) {
            Button weaponBtn = new Button();
            weaponBtn.setPrefWidth(250);
            final int idx = i;
            WeaponRegistry wr = battle.getWeaponFactory().getWeaponShop().get(i + 1);
            Weapon w = wr.buildWeapon();
            weaponBtn.setText(
                "Name: " + wr.getName() +
                "\nType: " + w.getClass().getSimpleName() +
                "\nPrice: " + wr.getPrice() +
                "\nDamage: " + wr.getDamage()
            );
            weaponBtn.setOnAction(e -> {
                int laneNo = inputLaneNumber();
                if (laneNo > 0 && laneNo <= battle.getOriginalLanes().size()) {
                    try {
                        battle.purchaseWeapon(idx + 1, battle.getOriginalLanes().get(laneNo - 1));
                        updateBattleInfo();
                        updateGameField();
                    } catch (Exception ex) {
                        showError(ex.getMessage());
                    }
                } else {
                    showError("Invalid lane number!");
                }
            });
            weaponShopBox.getChildren().add(weaponBtn);
        }

        infoPanel.getChildren().addAll(
            phaseLabel, scoreLabel, turnLabel, resourcesLabel,
            shopLabel, weaponShopBox
        );
    }

    private void setupBottomControls() {
        HBox bottomBox = new HBox(30);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15));

        passTurnButton = new Button("Pass Turn");
        passTurnButton.setPrefWidth(120);
        passTurnButton.setOnAction(this::handlePassTurn);

        restartButton = new Button("Restart Game");
        restartButton.setPrefWidth(120);
        restartButton.setOnAction(e -> showStartScreen());

        bottomBox.getChildren().addAll(passTurnButton, restartButton);
        ((BorderPane)primaryStage.getScene().getRoot()).setBottom(bottomBox);
    }

    private void updateBattleInfo() {
        phaseLabel.setText("Phase: " + battle.getBattlePhase());
        scoreLabel.setText("Score: " + battle.getScore());
        turnLabel.setText("Turn: " + battle.getNumberOfTurns());
        resourcesLabel.setText("Resources: " + battle.getResourcesGathered());
    }

    private void updateGameField() {
        ArrayList<Lane> lanes = battle.getOriginalLanes();
        for (int i = 0; i < numberOfLanes; i++) {
            Pane lanePane = laneGamePanes[i];
            lanePane.getChildren().removeIf(node -> node instanceof StackPane || node instanceof HBox);

            Lane lane = lanes.get(i);
            laneInfoLabels[i].setText(
                "Lane " + (i+1) +
                "\nDanger: " + lane.getDangerLevel() +
                "\nWall HP: " + lane.getLaneWall().getCurrentHealth()
            );

            for (Iterator<Titan> iter = lane.getTitans().iterator(); iter.hasNext(); ) {
                Titan t = iter.next();
                if (t.getCurrentHealth() <= 0 || t.getDistance() >= lanePane.getPrefHeight()) {
                    iter.remove();
                }
            }

            if (!lane.isLaneLost()) {
                HBox weaponsBox = new HBox(5);
                for (Weapon w : lane.getWeapons()) {
                    ImageView iv = createImageView(weaponImage(w), 45, 45);
                    weaponsBox.getChildren().add(iv);
                }
                lanePane.getChildren().add(weaponsBox);

                for (Titan t : lane.getTitans()) {
                    ImageView titanIV = titanImageView(t, 50, 50);
                    StackPane titanPane = new StackPane(
                        titanIV,
                        new Text("" + t.getCurrentHealth())
                    );
                    double maxX = lanePane.getWidth() - titanIV.getFitWidth();
                    if (maxX < 0) maxX = 0;
                    titanPane.setLayoutX(Math.random() * maxX);
                    titanPane.setLayoutY(t.getDistance());
                    lanePane.getChildren().add(titanPane);
                }
            }
        }
    }

    private void handlePassTurn(ActionEvent e) {
        try {
            battle.passTurn();
            updateBattleInfo();
            updateGameField();
            if (isGameOver()) {
                showGameOverScreen();
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private boolean isGameOver() {
        for (Lane lane : battle.getOriginalLanes()) {
            if (lane.isLaneLost()) {
                return true;
            }
        }
        return false;
    }

    private void showGameOverScreen() {
        BorderPane over = new BorderPane();
        over.setPadding(new Insets(30));
        over.setStyle(
            "-fx-background-image: url('/main_bg.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;" +
            "-fx-background-repeat: no-repeat;"
        );

        Label msg = new Label("GAME OVER");
        msg.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 48));
        over.setCenter(msg);
        BorderPane.setAlignment(msg, Pos.CENTER);

        Button home = new Button("Return to Homepage");
        home.setOnAction(e -> showStartScreen());

        HBox box = new HBox(home);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        over.setBottom(box);

        double w = primaryStage.getWidth();
        double h = primaryStage.getHeight();
        Scene scene = new Scene(over, w, h);

        primaryStage.setScene(scene);
    }

    private int inputLaneNumber() {
        TextInputDialog dlg = new TextInputDialog();
        dlg.setTitle("Select Lane");
        dlg.setHeaderText("Choose the lane number to deploy the weapon:");
        dlg.setContentText("Lane number:");
        Optional<String> res = dlg.showAndWait();
        if (res.isPresent()) {
            try { return Integer.parseInt(res.get()); }
            catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private ImageView createImageView(String path, double w, double h) {
        Image img = imageCache.computeIfAbsent(path, p -> new Image(getClass().getResourceAsStream(p)));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(w);
        iv.setFitHeight(h);
        return iv;
    }

    private String weaponImage(Weapon w) {
        if (w instanceof game.engine.weapons.PiercingCannon)     return "/Volley.png";
        if (w instanceof game.engine.weapons.SniperCannon)      return "/Cannonn.png";
        if (w instanceof game.engine.weapons.WallTrap)          return "/Cannonnn.png";
        return "/Cannonnnn.png";
    }

    private ImageView titanImageView(Titan t, double w, double h) {
        String p;
        if (t instanceof game.engine.titans.ArmoredTitan)        p = "/Armored titan.jpeg";
        else if (t instanceof game.engine.titans.AbnormalTitan) p = "/Abnormal.png";
        else if (t instanceof game.engine.titans.PureTitan)     p = "/Pure.jpg";
        else                                                     p = "/Colossal.png";
        return createImageView(p, w, h);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
