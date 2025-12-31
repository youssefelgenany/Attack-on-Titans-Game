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
import javafx.scene.control.ScrollPane;
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
    private Map<Integer, HBox> weaponAreas;
    private Map<Titan, Double> titanXPositions;
    
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
        startLayout.setPadding(new Insets(0));
        Scene startScene = new Scene(startLayout, 1000, 700);
        startLayout.setStyle(
            "-fx-background-image: url('/start_bg.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;" +
            "-fx-background-repeat: no-repeat;"
        );

        VBox titleBox = new VBox(15);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(80, 20, 50, 20));
        titleBox.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.4);" +
            "-fx-background-radius: 20px;"
        );
        titleBox.setMaxWidth(700);
        titleBox.setMaxHeight(200);
        
        Text title = new Text("ATTACK ON TITAN");
        title.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 56));
        title.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #e8e8e8 0%, #ffffff 50%, #c0c0c0 100%);" +
            "-fx-stroke: #1a1a1a;" +
            "-fx-stroke-width: 3px;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.8), 15, 0.8, 0, 0);"
        );
        
        Text subtitle = new Text("UTOPIA");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        subtitle.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #ff8c00 0%, #ffa500 50%, #ff6b00 100%);" +
            "-fx-stroke: #2a1a00;" +
            "-fx-stroke-width: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.9), 10, 0.7, 0, 0);"
        );
        
        titleBox.getChildren().addAll(title, subtitle);
        
        StackPane titleContainer = new StackPane();
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(40, 0, 0, 0));
        titleContainer.getChildren().add(titleBox);
        startLayout.setTop(titleContainer);

        VBox buttonContainer = new VBox(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(50, 20, 80, 20));

        Button easyModeBtn = createStyledButton("EASY MODE", "#2d5016", "#3d7026", "#4a8a2e");
        Button hardModeBtn = createStyledButton("HARD MODE", "#5a1a1a", "#7a2a2a", "#9a3a3a");
        Button instructionsBtn = createStyledButton("INSTRUCTIONS", "#1a3a4a", "#2a5a6a", "#3a7a9a");

        easyModeBtn.setOnAction(e -> startGame(3, 250));
        hardModeBtn.setOnAction(e -> startGame(5, 125));
        instructionsBtn.setOnAction(e -> showInstructions());

        buttonContainer.getChildren().addAll(easyModeBtn, hardModeBtn, instructionsBtn);
        startLayout.setCenter(buttonContainer);

        primaryStage.setScene(startScene);
    }

    private Button createStyledButton(String text, String darkColor, String baseColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setPrefWidth(320);
        btn.setPrefHeight(70);
        btn.setFont(Font.font("Arial Black", FontWeight.BOLD, 20));
        btn.setStyle(
            "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, " + baseColor + " 0%, " + darkColor + " 100%);" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-radius: 8px;" +
            "-fx-border-color: #1a1a1a;" +
            "-fx-border-width: 3px;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 15, 0.5, 0, 4);" +
            "-fx-cursor: hand;"
        );
        
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, " + hoverColor + " 0%, " + baseColor + " 100%);" +
                "-fx-text-fill: #ffffff;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-radius: 8px;" +
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 3px;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.6), 20, 0.7, 0, 0), " +
                             "dropshadow(gaussian, rgba(0, 0, 0, 0.9), 15, 0.5, 0, 4);" +
                "-fx-cursor: hand;"
            );
        });
        
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, " + baseColor + " 0%, " + darkColor + " 100%);" +
                "-fx-text-fill: #ffffff;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-radius: 8px;" +
                "-fx-border-color: #1a1a1a;" +
                "-fx-border-width: 3px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 15, 0.5, 0, 4);" +
                "-fx-cursor: hand;"
            );
        });
        
        btn.setOnMousePressed(e -> {
            btn.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, " + darkColor + " 0%, " + darkColor + " 100%);" +
                "-fx-text-fill: #ffffff;" +
                "-fx-background-radius: 8px;" +
                "-fx-border-radius: 8px;" +
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 3px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.3, 0, 2);" +
                "-fx-cursor: hand;"
            );
        });
        
        return btn;
    }

    private void showInstructions() {
        Stage instructionsStage = new Stage();
        instructionsStage.setTitle("Game Instructions");
        instructionsStage.initOwner(primaryStage);
        instructionsStage.setResizable(false);
        instructionsStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        
        BorderPane instructionsLayout = new BorderPane();
        instructionsLayout.setStyle(
            "-fx-background-color: rgba(20, 20, 25, 0.95);" +
            "-fx-background-radius: 15px;"
        );
        instructionsLayout.setPadding(new Insets(30));
        
        Scene instructionsScene = new Scene(instructionsLayout, 700, 600);
        instructionsStage.setScene(instructionsScene);
        
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        
        Text title = new Text("GAME INSTRUCTIONS");
        title.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 32));
        title.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #e8e8e8 0%, #ffffff 50%, #c0c0c0 100%);" +
            "-fx-stroke: #1a1a1a;" +
            "-fx-stroke-width: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.8), 12, 0.8, 0, 0);"
        );
        
        titleBox.getChildren().add(title);
        instructionsLayout.setTop(titleBox);
        
        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.TOP_LEFT);
        
        VBox modesBox = createInstructionSection(
            "GAME MODES",
            new String[]{
                "EASY MODE: 3 lanes, 250 resources per lane",
                "HARD MODE: 5 lanes, 125 resources per lane"
            }
        );
        
        VBox objectiveBox = createInstructionSection(
            "OBJECTIVE",
            new String[]{
                "Protect your walls from incoming titans",
                "Purchase and deploy weapons strategically",
                "Survive as long as possible"
            }
        );
        
        VBox gameplayBox = createInstructionSection(
            "HOW TO PLAY",
            new String[]{
                "1. Select a weapon from the shop",
                "2. Choose a lane to deploy it",
                "3. Click 'Pass Turn' to advance",
                "4. Monitor your resources and wall health",
                "5. Plan ahead to maximize survival"
            }
        );
        
        contentBox.getChildren().addAll(modesBox, objectiveBox, gameplayBox);
        
        instructionsLayout.setCenter(contentBox);
        
        Button closeBtn = createStyledButton("CLOSE", "#3a1a1a", "#5a2a2a", "#7a3a3a");
        closeBtn.setPrefWidth(200);
        closeBtn.setOnAction(e -> instructionsStage.close());
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.getChildren().add(closeBtn);
        instructionsLayout.setBottom(buttonBox);
        
        instructionsStage.showAndWait();
    }
    
    private VBox createInstructionSection(String sectionTitle, String[] items) {
        VBox section = new VBox(12);
        section.setPadding(new Insets(15));
        section.setStyle(
            "-fx-background-color: rgba(40, 40, 50, 0.7);" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: #ff8c00;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;"
        );
        
        Text title = new Text(sectionTitle);
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 18));
        title.setStyle(
            "-fx-fill: #ff8c00;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0.5, 0, 1);"
        );
        
        VBox itemsBox = new VBox(8);
        for (String item : items) {
            Text itemText = new Text("• " + item);
            itemText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            itemText.setStyle(
                "-fx-fill: #e0e0e0;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 3, 0.3, 0, 1);"
            );
            itemText.setWrappingWidth(600);
            itemsBox.getChildren().add(itemText);
        }
        
        section.getChildren().addAll(title, itemsBox);
        return section;
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
        gameField.prefWidthProperty().bind(primaryStage.widthProperty().subtract(340));
        gameField.prefHeightProperty().bind(primaryStage.heightProperty());

        lanesContainer = new HBox(20);
        lanesContainer.setPadding(new Insets(20));
        lanesContainer.prefWidthProperty().bind(gameField.prefWidthProperty().subtract(40));

        gameField.getChildren().add(lanesContainer);
        mainLayout.setCenter(gameField);

        infoPanel = new VBox(15);
        infoPanel.setPadding(new Insets(20));
        infoPanel.setPrefWidth(320);
        infoPanel.setStyle(
            "-fx-background-color: rgba(20, 20, 25, 0.9);" +
            "-fx-border-color: #ff8c00;" +
            "-fx-border-width: 3px 0px 0px 3px;"
        );
        mainLayout.setRight(infoPanel);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
    }

    private void setupLanes() {
        laneInfoLabels = new Label[numberOfLanes];
        laneGamePanes   = new Pane[numberOfLanes];
        weaponAreas = new HashMap<>();
        titanXPositions = new HashMap<>();
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
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 3px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 10, 0.5, 0, 2);"
            );

            Label laneInfo = new Label("LANE " + (i + 1));
            laneInfo.setFont(Font.font("Arial Black", FontWeight.BOLD, 14));
            laneInfo.setStyle(
                "-fx-background-color: rgba(40, 40, 50, 0.9);" +
                "-fx-text-fill: #ff8c00;" +
                "-fx-padding: 8px;" +
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 2px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0.5, 0, 1);"
            );
            laneInfo.setAlignment(Pos.CENTER);
            laneInfo.setMaxWidth(Double.MAX_VALUE);
            laneInfoLabels[i] = laneInfo;

            VBox laneContainer = new VBox(0);
            laneContainer.prefWidthProperty().bind(laneBox.prefWidthProperty());
            laneContainer.setPrefHeight(laneHeight);

            HBox weaponArea = new HBox(8);
            weaponArea.setAlignment(Pos.CENTER);
            weaponArea.setPrefHeight(60);
            weaponArea.prefWidthProperty().bind(laneBox.prefWidthProperty());
            weaponArea.setStyle(
                "-fx-background-color: rgba(40, 40, 50, 0.8);" +
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 0px 0px 2px 0px;" +
                "-fx-padding: 5px;"
            );

            Pane lanePane = new Pane();
            lanePane.prefWidthProperty().bind(laneBox.prefWidthProperty());
            lanePane.setPrefHeight(laneHeight - 60); 
            lanePane.setStyle("-fx-background-color: rgba(0,0,0,0.3);");

            laneContainer.getChildren().addAll(weaponArea, lanePane);

            laneGamePanes[i] = lanePane;
            laneBox.getChildren().addAll(laneInfo, laneContainer);
            lanesContainer.getChildren().add(laneBox);
            
            weaponAreas.put(i, weaponArea);
        }
    }

    private void setupInfoPanel() {
        infoPanel.getChildren().clear();

        VBox statsBox = new VBox(12);
        statsBox.setPadding(new Insets(10));
        statsBox.setStyle(
            "-fx-background-color: rgba(40, 40, 50, 0.7);" +
            "-fx-background-radius: 10px;" +
            "-fx-border-color: #ff8c00;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;"
        );

        Text statsTitle = new Text("BATTLE STATUS");
        statsTitle.setFont(Font.font("Arial Black", FontWeight.BOLD, 16));
        statsTitle.setStyle(
            "-fx-fill: #ff8c00;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0.5, 0, 1);"
        );

        phaseLabel     = new Label("Phase: Early");
        scoreLabel     = new Label("Score: 0");
        turnLabel      = new Label("Turn: 1");
        resourcesLabel = new Label("Resources: 0");

        phaseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        phaseLabel.setStyle("-fx-text-fill: #ffffff;");
        scoreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        scoreLabel.setStyle("-fx-text-fill: #e0e0e0;");
        turnLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        turnLabel.setStyle("-fx-text-fill: #e0e0e0;");
        resourcesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        resourcesLabel.setStyle("-fx-text-fill: #ff8c00;");

        statsBox.getChildren().addAll(statsTitle, phaseLabel, scoreLabel, turnLabel, resourcesLabel);

        VBox shopBox = new VBox(15);
        shopBox.setPadding(new Insets(10, 0, 0, 0));

        Text shopLabel = new Text("WEAPON SHOP");
        shopLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 18));
        shopLabel.setStyle(
            "-fx-fill: #ff8c00;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0.5, 0, 1);"
        );

        weaponShopBox = new VBox(12);
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            WeaponRegistry wr = battle.getWeaponFactory().getWeaponShop().get(i + 1);
            Weapon w = wr.buildWeapon();
            
            VBox weaponContainer = new VBox(0);
            weaponContainer.setPrefWidth(280);
            weaponContainer.setPrefHeight(110);
            weaponContainer.setStyle(
                "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #3a3a4a 0%, #2a2a3a 100%);" +
                "-fx-background-radius: 8px;" +
                "-fx-border-color: #ff8c00;" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 8px;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 8, 0.4, 0, 2);" +
                "-fx-padding: 10px;"
            );
            
            Text nameText = new Text(wr.getName().toUpperCase());
            nameText.setFont(Font.font("Arial Black", FontWeight.BOLD, 14));
            nameText.setStyle(
                "-fx-fill: #ff8c00;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 3, 0.5, 0, 1);"
            );
            
            VBox detailsBox = new VBox(4);
            detailsBox.setPadding(new Insets(5, 0, 0, 0));
            
            Label typeLabel = createWeaponDetailLabel("TYPE", w.getClass().getSimpleName());
            Label priceLabel = createWeaponDetailLabel("PRICE", String.valueOf(wr.getPrice()));
            Label damageLabel = createWeaponDetailLabel("DAMAGE", String.valueOf(wr.getDamage()));
            
            detailsBox.getChildren().addAll(typeLabel, priceLabel, damageLabel);
            
            weaponContainer.getChildren().addAll(nameText, detailsBox);
            
            weaponContainer.setOnMouseEntered(e -> {
                weaponContainer.setStyle(
                    "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #4a4a5a 0%, #3a3a4a 100%);" +
                    "-fx-background-radius: 8px;" +
                    "-fx-border-color: #ffa500;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.4), 12, 0.6, 0, 0), " +
                                 "dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.4, 0, 2);" +
                    "-fx-padding: 10px;"
                );
            });
            
            weaponContainer.setOnMouseExited(e -> {
                weaponContainer.setStyle(
                    "-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #3a3a4a 0%, #2a2a3a 100%);" +
                    "-fx-background-radius: 8px;" +
                    "-fx-border-color: #ff8c00;" +
                    "-fx-border-width: 2px;" +
                    "-fx-border-radius: 8px;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 8, 0.4, 0, 2);" +
                    "-fx-padding: 10px;"
                );
            });
            
            weaponContainer.setOnMouseClicked(e -> {
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
            
            weaponShopBox.getChildren().add(weaponContainer);
        }

        shopBox.getChildren().addAll(shopLabel, weaponShopBox);
        infoPanel.getChildren().addAll(statsBox, shopBox);
    }
    
    private Label createWeaponDetailLabel(String label, String value) {
        Label detailLabel = new Label(label + ": " + value);
        detailLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        detailLabel.setStyle(
            "-fx-text-fill: #c0c0c0;"
        );
        return detailLabel;
    }

    private void setupBottomControls() {
        HBox bottomBox = new HBox(30);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        bottomBox.setStyle(
            "-fx-background-color: rgba(20, 20, 25, 0.8);" +
            "-fx-border-color: #ff8c00;" +
            "-fx-border-width: 3px 0px 0px 0px;"
        );

        passTurnButton = createStyledButton("PASS TURN", "#2d5016", "#3d7026", "#4a8a2e");
        passTurnButton.setPrefWidth(180);
        passTurnButton.setPrefHeight(50);
        passTurnButton.setOnAction(this::handlePassTurn);

        restartButton = createStyledButton("RESTART", "#5a1a1a", "#7a2a2a", "#9a3a3a");
        restartButton.setPrefWidth(180);
        restartButton.setPrefHeight(50);
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
            lanePane.getChildren().removeIf(node -> node instanceof StackPane);

            Lane lane = lanes.get(i);
            laneInfoLabels[i].setText(
                "LANE " + (i+1) +
                "\nDANGER: " + lane.getDangerLevel() +
                "\nWALL HP: " + lane.getLaneWall().getCurrentHealth()
            );

            for (Iterator<Titan> iter = lane.getTitans().iterator(); iter.hasNext(); ) {
                Titan t = iter.next();
                if (t.getCurrentHealth() <= 0 || t.getDistance() >= lanePane.getPrefHeight()) {
                    titanXPositions.remove(t); 
                    iter.remove();
                }
            }

            if (!lane.isLaneLost()) {
                HBox weaponArea = weaponAreas.get(i);
                if (weaponArea != null) {
                    weaponArea.getChildren().clear();
                    for (Weapon w : lane.getWeapons()) {
                        ImageView iv = createImageView(weaponImage(w), 40, 40);
                        iv.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0.5, 0, 1);");
                        weaponArea.getChildren().add(iv);
                    }
                }

                double laneWidth = lanePane.getWidth();
                
                for (Titan t : lane.getTitans()) {
                    ImageView titanIV = titanImageView(t, 60, 60);
                    double titanWidth = titanIV.getFitWidth();
                    double maxX = Math.max(0, laneWidth - titanWidth);
                    
                    Double storedX = titanXPositions.get(t);
                    double xPos = 0.0; 
                    
                    if (storedX == null || storedX < 0 || storedX > maxX) {
                        boolean positionFound = false;
                        int attempts = 0;
                        double minSpacing = titanWidth + 5; 
                        
                        while (!positionFound && attempts < 50) {
                            double testX = Math.random() * maxX;
                            
                            boolean overlaps = false;
                            for (Titan otherTitan : lane.getTitans()) {
                                if (otherTitan != t) {
                                    Double otherX = titanXPositions.get(otherTitan);
                                    if (otherX != null && Math.abs(testX - otherX) < minSpacing) {
                                        overlaps = true;
                                        break;
                                    }
                                }
                            }
                            
                            if (!overlaps) {
                                positionFound = true;
                                xPos = testX;
                                titanXPositions.put(t, xPos);
                            } else {
                                attempts++;
                            }
                        }
                        
                        if (!positionFound) {
                            xPos = Math.random() * maxX;
                            titanXPositions.put(t, xPos);
                        }
                    } else {
                        xPos = Math.min(maxX, Math.max(0, storedX));
                    }
                    
                    Label healthLabel = new Label("" + t.getCurrentHealth());
                    healthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                    healthLabel.setStyle(
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-color: rgba(200, 0, 0, 0.8);" +
                        "-fx-padding: 2px 4px;" +
                        "-fx-background-radius: 3px;"
                    );
                    
                    StackPane titanPane = new StackPane(titanIV, healthLabel);
                    StackPane.setAlignment(healthLabel, Pos.BOTTOM_CENTER);
                    
                    double maxDistance = 300; 
                    double distancePercent = Math.min(1.0, Math.max(0.0, t.getDistance() / maxDistance));
                    double yPos = distancePercent * (lanePane.getPrefHeight() - titanIV.getFitHeight());
                    
                    titanPane.setLayoutX(xPos);
                    titanPane.setLayoutY(yPos);
                    titanPane.setStyle("-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.6, 0, 2);");
                    
                    lanePane.getChildren().add(titanPane);
                }
                
                titanXPositions.entrySet().removeIf(entry -> !lane.getTitans().contains(entry.getKey()));
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
        over.setPadding(new Insets(0));
        over.setStyle(
            "-fx-background-image: url('/main_bg.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center center;" +
            "-fx-background-repeat: no-repeat;"
        );

        VBox titleBox = new VBox(15);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(100, 20, 60, 20));
        titleBox.setStyle(
            "-fx-background-color: rgba(0, 0, 0, 0.6);" +
            "-fx-background-radius: 20px;"
        );
        titleBox.setMaxWidth(600);
        titleBox.setMaxHeight(400);
        
        Text title = new Text("GAME OVER");
        title.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 64));
        title.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #ff4444 0%, #ff6666 50%, #cc2222 100%);" +
            "-fx-stroke: #1a1a1a;" +
            "-fx-stroke-width: 3px;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 0.9), 20, 0.9, 0, 0);"
        );
        
        Text subtitle = new Text("The Titans Have Breached The Wall");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        subtitle.setStyle(
            "-fx-fill: #e0e0e0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.6, 0, 1);"
        );
        
        Text scoreText = new Text("Final Score: " + battle.getScore());
        scoreText.setFont(Font.font("Arial Black", FontWeight.BOLD, 24));
        scoreText.setStyle(
            "-fx-fill: #ff8c00;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 8, 0.6, 0, 1);"
        );
        
        titleBox.getChildren().addAll(title, subtitle, scoreText);
        
        StackPane centerContainer = new StackPane();
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.getChildren().add(titleBox);
        over.setCenter(centerContainer);

        Button home = createStyledButton("RETURN TO HOMEPAGE", "#3a1a1a", "#5a2a2a", "#7a3a3a");
        home.setPrefWidth(250);
        home.setPrefHeight(55);
        home.setOnAction(e -> {
            titanXPositions.clear(); 
            showStartScreen();
        });

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40, 0, 60, 0));
        box.getChildren().add(home);
        over.setBottom(box);

        double w = primaryStage.getWidth();
        double h = primaryStage.getHeight();
        Scene scene = new Scene(over, w, h);

        primaryStage.setScene(scene);
    }

    private int inputLaneNumber() {
        Stage laneSelectStage = new Stage();
        laneSelectStage.setTitle("Select Lane");
        laneSelectStage.initOwner(primaryStage);
        laneSelectStage.setResizable(false);
        laneSelectStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        
        BorderPane laneSelectLayout = new BorderPane();
        laneSelectLayout.setStyle(
            "-fx-background-color: rgba(20, 20, 25, 0.95);" +
            "-fx-background-radius: 15px;"
        );
        laneSelectLayout.setPadding(new Insets(30));
        
        int windowHeight = Math.max(300, 280 + (numberOfLanes * 60));
        Scene laneSelectScene = new Scene(laneSelectLayout, 450, windowHeight);
        laneSelectStage.setScene(laneSelectScene);
        
        VBox titleBox = new VBox(10);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        
        Text title = new Text("SELECT LANE");
        title.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 24));
        title.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #e8e8e8 0%, #ffffff 50%, #c0c0c0 100%);" +
            "-fx-stroke: #1a1a1a;" +
            "-fx-stroke-width: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 140, 0, 0.8), 10, 0.8, 0, 0);"
        );
        
        Text subtitle = new Text("Choose the lane to deploy your weapon");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        subtitle.setStyle(
            "-fx-fill: #c0c0c0;"
        );
        
        titleBox.getChildren().addAll(title, subtitle);
        laneSelectLayout.setTop(titleBox);
        
        VBox lanesBox = new VBox(12);
        lanesBox.setAlignment(Pos.CENTER);
        lanesBox.setPadding(new Insets(20));
        
        final int[] selectedLane = {-1};
        
        for (int i = 1; i <= numberOfLanes; i++) {
            final int laneNum = i;
            Button laneBtn = createStyledButton("LANE " + laneNum, "#2a4a5a", "#3a6a7a", "#4a8a9a");
            laneBtn.setPrefWidth(200);
            laneBtn.setPrefHeight(45);
            laneBtn.setOnAction(e -> {
                selectedLane[0] = laneNum;
                laneSelectStage.close();
            });
            lanesBox.getChildren().add(laneBtn);
        }
        
        if (numberOfLanes > 3) {
            ScrollPane scrollPane = new ScrollPane(lanesBox);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background: transparent;"
            );
            scrollPane.setPadding(new Insets(10));
            laneSelectLayout.setCenter(scrollPane);
        } else {
            laneSelectLayout.setCenter(lanesBox);
        }
        
        Button cancelBtn = createStyledButton("CANCEL", "#5a1a1a", "#7a2a2a", "#9a3a3a");
        cancelBtn.setPrefWidth(150);
        cancelBtn.setPrefHeight(40);
        cancelBtn.setOnAction(e -> {
            selectedLane[0] = -1;
            laneSelectStage.close();
        });
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().add(cancelBtn);
        laneSelectLayout.setBottom(buttonBox);
        
        laneSelectStage.showAndWait();
        return selectedLane[0];
    }

    private void showError(String msg) {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");
        errorStage.initOwner(primaryStage);
        errorStage.setResizable(false);
        errorStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        
        BorderPane errorLayout = new BorderPane();
        errorLayout.setStyle(
            "-fx-background-color: rgba(20, 20, 25, 0.95);" +
            "-fx-background-radius: 15px;"
        );
        errorLayout.setPadding(new Insets(30));
        
        Scene errorScene = new Scene(errorLayout, 500, 250);
        errorStage.setScene(errorScene);
        
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        
        Text title = new Text("ERROR");
        title.setFont(Font.font("Arial Black", FontWeight.EXTRA_BOLD, 28));
        title.setStyle(
            "-fx-fill: linear-gradient(from 0% 0% to 100% 100%, #ff4444 0%, #ff6666 50%, #cc2222 100%);" +
            "-fx-stroke: #1a1a1a;" +
            "-fx-stroke-width: 2px;" +
            "-fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 0.8), 10, 0.8, 0, 0);"
        );
        
        titleBox.getChildren().add(title);
        errorLayout.setTop(titleBox);
        
        Text errorText = new Text(msg);
        errorText.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
        errorText.setStyle(
            "-fx-fill: #e0e0e0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.6), 3, 0.3, 0, 1);"
        );
        errorText.setWrappingWidth(440);
        
        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setPadding(new Insets(10));
        messageBox.getChildren().add(errorText);
        errorLayout.setCenter(messageBox);
        
        Button okBtn = createStyledButton("OK", "#5a1a1a", "#7a2a2a", "#9a3a3a");
        okBtn.setPrefWidth(120);
        okBtn.setPrefHeight(40);
        okBtn.setOnAction(e -> errorStage.close());
        
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.getChildren().add(okBtn);
        errorLayout.setBottom(buttonBox);
        
        errorStage.showAndWait();
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
