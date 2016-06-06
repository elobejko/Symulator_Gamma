package pl.pw.edu.fizyka.pojava.lama;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.animation.KeyFrame;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

public class Controller {

    @FXML
    ComboBox<String> sourceMaterialComboBox;
    @FXML
    ComboBox<String> coverMaterialComboBox;
    @FXML
    HBox simulationPane;
    @FXML
    Pane animationGamma;
    @FXML
    Button exportButton;
    @FXML
    Button refreshChartButton;
    @FXML
    Button animationButton;
    @FXML
    ComboBox<String> coverNumberComboBox;
    @FXML
    ComboBox<String> coverDepthComboBox;
    @FXML
    LineChart<Double, Double> lineChart;
    @FXML
    RadioButton linearScaleButton;
    @FXML
    RadioButton logarithmicScaleButton;

    double coverDepth;
    double miFactor;
    double coverNumber;
    double startGamma;
    String colorCover;
    final List<String> plutoniumLabel = Collections.unmodifiableList(Arrays.asList("Pluton (Pu)", "Plutonium (Pu)"));
    final List<String> uraniumLabel = Collections.unmodifiableList(Arrays.asList("Uran (U)", "Uranium (U)"));
    final List<String> cobaltLabel = Collections.unmodifiableList(Arrays.asList("Kobalt (Co)", "Cobalt (Co)"));
    final List<String> leadLabel = Collections.unmodifiableList(Arrays.asList("Ołów (Pb)", "Lead"));
    final List<String> copperLabel = Collections.unmodifiableList(Arrays.asList("Copper", "Miedź (Cu)"));
    final List<String> aluminumLabel = Collections.unmodifiableList(Arrays.asList("Aluminum", "Aluminium (Al)"));
    ToggleGroup groupButtonsChart;
    ObservableList<String> optionsSourceMaterials;
    ObservableList<String> optionsCoverMaterials;
    ObservableList<String> optionsCoverDepth;
    ObservableList<String> optionsCoverNumber;
    Timeline loopBeforeCover = new Timeline();
    Timeline loopAfterCover = new Timeline();
    Random generator = new Random();
    boolean visibilityGamma;
    WritableImage image;
    File file;

    @FXML
    void initialize() {
        colorCover = "black";
        groupButtonsChart = new ToggleGroup();
        linearScaleButton.setToggleGroup(groupButtonsChart);
        logarithmicScaleButton.setToggleGroup(groupButtonsChart);
        optionsSourceMaterials =
                FXCollections.observableArrayList(
                        "Kobalt (Co)",
                        "Pluton (Pu)",
                        "Uran (U)"
                );
        optionsCoverMaterials =
                FXCollections.observableArrayList(
                        "Miedź (Cu)",
                        "Aluminium (Al)",
                        "Ołów (Pb)"
                );
        coverMaterialComboBox.setItems(optionsCoverMaterials);
        optionsCoverDepth =
                FXCollections.observableArrayList(
                        "2 mm", "5 mm", "7 mm", "10 mm", "12 mm", "15 mm", "17 mm", "20 mm"
                );
        coverDepthComboBox.setItems(optionsCoverDepth);
        optionsCoverNumber = FXCollections.observableArrayList
                ("1 przesłona", "2 przesłony", "3 przesłony", "4 przesłony", "5 przesłon");
        coverNumberComboBox.setItems(optionsCoverNumber);
        sourceMaterialComboBox.setItems(optionsSourceMaterials);
        coverDepthComboBox.setPromptText("Grubość przesłony");
        coverNumberComboBox.setPromptText("Liczba przesłon");
        coverMaterialComboBox.setPromptText("Materiał przesłony");
        sourceMaterialComboBox.setPromptText("Materiał źródła");
        lineChart.setTitle("Osłabienie promieniowania gamma");
        linearScaleButton.setText("Skala liniowa");
        logarithmicScaleButton.setText("Skala logarytmiczna");
        exportButton.setText("Eksportuj");
        refreshChartButton.setText("Odśwież wykres");
        animationButton.setText("Animacja");
    }

    @FXML
    void initializeEnglish() {
        optionsCoverMaterials =
                FXCollections.observableArrayList(
                        "Copper",
                        "Aluminum",
                        "Lead"
                );
        coverMaterialComboBox.setItems(optionsCoverMaterials);
        optionsSourceMaterials =
                FXCollections.observableArrayList(
                        "Cobalt (Co)",
                        "Plutonium (Pu)",
                        "Uranium (U)"
                );
        optionsCoverDepth =
                FXCollections.observableArrayList(
                        "2 mm", "5 mm", "7 mm", "10 mm", "12 mm", "15 mm", "17 mm", "20 mm"
                );
        optionsCoverNumber = FXCollections.observableArrayList("1 cover", "2 covers", "3 covers", "4 covers", "5 covers");
        coverNumberComboBox.setItems(optionsCoverNumber);
        coverDepthComboBox.setItems(optionsCoverDepth);
        sourceMaterialComboBox.setItems(optionsSourceMaterials);
        coverNumberComboBox.setItems(optionsCoverNumber);
        animationButton.setText("Animation");
        coverDepthComboBox.setPromptText("Size of screen");
        coverNumberComboBox.setPromptText("Number of screens");
        coverMaterialComboBox.setPromptText("Material of screen");
        sourceMaterialComboBox.setPromptText("Material of source");
        lineChart.setTitle("The weakening of gamma radiation");
        linearScaleButton.setText("Linear scale");
        logarithmicScaleButton.setText("Logharitmic scale");
        exportButton.setText("Export");
        refreshChartButton.setText("Refresh chart");
    }

    @FXML
    void startAction() {
        animationGamma.getChildren().clear();
        Group coversGroup = new Group();
        for(double z=0; z<coverNumber; z++) {
            Rectangle cover = new Rectangle((95+coverDepth*z), 0, coverDepth, 255);
            cover.setStroke(Color.web("black"));
            cover.setStrokeWidth(1.5);
            cover.setFill(Color.web(colorCover));
            coversGroup.getChildren().addAll(cover);
        }
            animationGamma.getChildren().setAll(coversGroup);
            beforeAbsorption();
            afterAbsorption();
    }

    void beforeAbsorption() {
        int numberSteps = 0;
        while (numberSteps < 200) {
            Circle gammaPortion = new Circle(2, Color.BLUE);
            gammaPortion.relocate(10 * Math.random(), Math.random() * 250);
            animationGamma.getChildren().add(gammaPortion);
            loopBeforeCover = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent t) {
                    if (gammaPortion.getLayoutX() < 95) {
                        gammaPortion.setLayoutX(gammaPortion.getLayoutX() + Math.random() + 0.6);
                        gammaPortion.setLayoutY(gammaPortion.getLayoutY() + Math.random() - 0.5);
                    } else if (gammaPortion.getLayoutX() > 95 && gammaPortion.getLayoutX() < (95 + coverDepth * coverNumber)) {
                        visibilityGamma = generator.nextBoolean();
                        gammaPortion.setVisible(visibilityGamma);
                        gammaPortion.setLayoutX(gammaPortion.getLayoutX() + Math.random() + 0.6);
                        gammaPortion.setLayoutY(gammaPortion.getLayoutY() + Math.random() - 0.5);
                    } else if (gammaPortion.getLayoutX() == (95 + coverDepth * coverNumber)) {
                        gammaPortion.setVisible(false);
                    } else {
                        gammaPortion.setVisible(false);
                    }
                }
            }));
            loopBeforeCover.setCycleCount(Timeline.INDEFINITE);
            loopBeforeCover.play();
            numberSteps++;
        }
    }

    void afterAbsorption() {
        int numberSteps=0;
        while( numberSteps < 200*(Math.exp((-1)*miFactor*coverNumber*coverDepth))) {
                Circle gammaPortionSurvivor = new Circle(2, Color.BLUE);
                gammaPortionSurvivor.setVisible(false);
                gammaPortionSurvivor.relocate(95 + coverDepth * coverNumber, Math.random() * 250);
                animationGamma.getChildren().add(gammaPortionSurvivor);
                loopAfterCover = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent t) {
                        if ((gammaPortionSurvivor.getLayoutX() > (95 + coverDepth * coverNumber))
                                && gammaPortionSurvivor.getLayoutX() < 350) {
                            gammaPortionSurvivor.setVisible(true);
                            gammaPortionSurvivor.setLayoutX(gammaPortionSurvivor.getLayoutX() + Math.random() + 0.6);
                            gammaPortionSurvivor.setLayoutY(gammaPortionSurvivor.getLayoutY() + Math.random() - 0.5);
                        }
                        else
                            gammaPortionSurvivor.setVisible(false);
                    }
                }));
                loopAfterCover.setCycleCount(Timeline.INDEFINITE);
                loopAfterCover.setDelay(Duration.millis(2100+20*coverNumber*coverDepth));
                loopAfterCover.play();
                numberSteps++;
                }
        }

    @FXML
    void exportAction() {
        image = lineChart.snapshot(new SnapshotParameters(), null);
        file = new File("chart.png");
        try {

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

        } catch (IOException e) {

        }
    }

    @FXML
    void chooseSourceMaterial() {

        if (cobaltLabel.contains(sourceMaterialComboBox.getValue()))
            startGamma = 40000000000000d;

        if (plutoniumLabel.contains(sourceMaterialComboBox.getValue()))
            startGamma = 2000000000;

        if (uraniumLabel.contains(sourceMaterialComboBox.getValue()))
            startGamma = 40000;

    }

    @FXML
    void chooseCoverMaterial() {
        if (leadLabel.contains(coverMaterialComboBox.getValue())) {
            miFactor = 0.15;
            colorCover = "gray";
        }
        if (copperLabel.contains(coverMaterialComboBox.getValue())) {
            miFactor = 0.093;
            colorCover = "orange";
        }
        if (aluminumLabel.contains(coverMaterialComboBox.getValue())) {
            miFactor = 0.049;
            colorCover = "silver";
        }
    }

    @FXML
    void chooseNumberCovers() {
        if (coverNumberComboBox.getValue() != null){
            if (coverNumberComboBox.getValue().equals("1 przesłona")
                    || coverNumberComboBox.getValue().equals("1 cover"))
                coverNumber = 1;
            if (coverNumberComboBox.getValue().equals("2 przesłony")
                    || coverNumberComboBox.getValue().equals("2 covers"))
                coverNumber = 2;
            if (coverNumberComboBox.getValue().equals("3 przesłony")
                    || coverNumberComboBox.getValue().equals("3 covers"))
                coverNumber = 3;
            if (coverNumberComboBox.getValue().equals("4 przesłony")
                    || coverNumberComboBox.getValue().equals("4 covers"))
                coverNumber = 4;
            if (coverNumberComboBox.getValue().equals("5 przesłon")
                    || coverNumberComboBox.getValue().equals("5 covers"))
                coverNumber = 5;
        }
    }

    @FXML
    void chooseCoverDepth() {
        if(coverDepthComboBox.getValue() != null){
            if (coverDepthComboBox.getValue().equals("2 mm"))
                coverDepth = 2;
            if (coverDepthComboBox.getValue().equals("5 mm"))
                coverDepth = 5;
            if (coverDepthComboBox.getValue().equals("7 mm"))
                coverDepth = 7;
            if (coverDepthComboBox.getValue().equals("10 mm"))
                coverDepth = 10;
            if (coverDepthComboBox.getValue().equals("12 mm"))
                coverDepth = 12;
            if (coverDepthComboBox.getValue().equals("15 mm"))
                coverDepth = 15;
            if (coverDepthComboBox.getValue().equals("17 mm"))
                coverDepth = 17;
            if (coverDepthComboBox.getValue().equals("20 mm"))
                coverDepth = 20;
        }
    }

    @FXML
    void linearScale(){
        double dx = 0.01;
        double I_o = startGamma;
        double x=0;
        double y;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

        while (x <= coverDepth*coverNumber) {
            double argument=((-1)*miFactor*coverNumber*x);
            y = I_o * Math.exp(argument);
            lineChartData.add(new LineChart.Data(x,y));
            x += dx;
        }

        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);
        lineChart.setCreateSymbols(false);
    }

    @FXML
    void logarithmicScale(){
        double dx = 0.01;
        double I_o = startGamma;
        double x=0;
        double y=0;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

        while (x <= coverDepth * coverNumber) {
                if ( y>=0 ) {
                    double argument = (-1) * miFactor  * x;
                    y = Math.log(I_o * Math.exp(argument));
                    lineChartData.add(new LineChart.Data(x, y));
                    x += dx;
                }
                else
                    break;
        }

        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);
        lineChart.setCreateSymbols(false);
    }

    @FXML
    void refreshChart() {
        if (linearScaleButton.isSelected()) { linearScale(); }
        if (logarithmicScaleButton.isSelected()) { logarithmicScale(); }
    }
}
