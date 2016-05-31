package pl.pw.edu.fizyka.pojava.lama;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

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
    ComboBox<String> coverNumberComboBox;
    @FXML
    ComboBox<String> coverDepthComboBox;
    @FXML
    LineChart<Double, Double> lineChart;
    @FXML
    RadioButton linearScaleButton;
    @FXML
    RadioButton loggharytmicScaleButton;

    double coverDepth;
    double miFactor;
    double coverNumber;
    double startGamma;  // początkowa liczba kwantów gamma
    final List<String> plutoniumLabel = Collections.unmodifiableList(Arrays.asList("Pluton (Pu)", "Plutonium (Pu)"));
    final List<String> uraniumLabel = Collections.unmodifiableList(Arrays.asList("Uran (U)", "Uranium (U)"));
    final List<String> cobaltLabel = Collections.unmodifiableList(Arrays.asList("Kobalt (Co)", "Cobalt (Co)"));
    final List<String> leadLabel = Collections.unmodifiableList(Arrays.asList("Ołów (Pb)", "Lead"));
    final List<String> copperLabel = Collections.unmodifiableList(Arrays.asList("Copper", "Miedź (Cu)"));
    final List<String> aluminumLabel = Collections.unmodifiableList(Arrays.asList("Aluminum", "Aluminium (Al)"));
    double numberSteps;
    WritableImage image;
    File file;
    ToggleGroup groupButtons;
    Line coverLine;
    Timeline loopBeforeCover;

    ObservableList<String> optionsSourceMaterials;
    ObservableList<String> optionsCoverMaterials;
    ObservableList<String> optionsCoverDepth;
    ObservableList<String> optionsCoverNumber;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        groupButtons = new ToggleGroup();
        linearScaleButton.setToggleGroup(groupButtons);
        loggharytmicScaleButton.setToggleGroup(groupButtons);
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
        optionsCoverNumber = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        coverNumberComboBox.setItems(optionsCoverNumber);
        sourceMaterialComboBox.setItems(optionsSourceMaterials);
        coverDepthComboBox.setPromptText("Grubość przesłony");
        coverNumberComboBox.setPromptText("Liczba przesłon");
        coverMaterialComboBox.setPromptText("Materiał przesłony");
        sourceMaterialComboBox.setPromptText("Materiał źródła");
        lineChart.setTitle("Osłabienie promieniowania gamma");
        linearScaleButton.setText("Skala liniowa");
        loggharytmicScaleButton.setText("Skala logarytmiczna");
        exportButton.setText("Eksportuj");
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
        optionsCoverNumber = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        coverDepthComboBox.setItems(optionsCoverDepth);
        sourceMaterialComboBox.setItems(optionsSourceMaterials);
        coverNumberComboBox.setItems(optionsCoverNumber);
        coverDepthComboBox.setPromptText("Size of screen");
        coverNumberComboBox.setPromptText("Number of screens");
        coverMaterialComboBox.setPromptText("Material of screen");
        sourceMaterialComboBox.setPromptText("Material of source");
        lineChart.setTitle("The weakening of gamma radiation");
        linearScaleButton.setText("Linear scale");
        loggharytmicScaleButton.setText("Logharytmic scale");
        exportButton.setText("Export");
        refreshChartButton.setText("Refresh chart");
    }

    @FXML
    void startAction() {
        for(int s=0; s<20; s++) {
            animationGamma.getChildren().clear();
            beforeCoverAnimation();
            afterCoverAnimation();
        }
    }

    void beforeCoverAnimation() {
        numberSteps=0;
        //wstawienie bariery
        animationGamma.getChildren().remove(coverLine);
        coverLine = new Line(100, 5, 100, 250);
        coverLine.setStrokeWidth(coverDepth);
        animationGamma.getChildren().add(coverLine);
        while (numberSteps < 1000) {
            //kulki - poczatkowa liczba 15 - rozpatrujemy jedna paczke kwantowa
            Circle circle = new Circle(1, Color.BLUE);
            circle.relocate(10*Math.random(), Math.random()*250);
            numberSteps ++;
            animationGamma.getChildren().add(circle);
            loopBeforeCover = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent t) {
                    if (circle.getLayoutX() < 95) {
                        circle.setLayoutX(circle.getLayoutX() + +Math.random() + 0.6);
                        circle.setLayoutY(circle.getLayoutY() + Math.random() - 0.5);
                    }
                    else
                        circle.setVisible(false);
                }
            }));
           // loopBeforeCover.setDelay(Duration.millis(50));
            loopBeforeCover.setCycleCount(Timeline.INDEFINITE);
            loopBeforeCover.play();}
    }

    void afterCoverAnimation() {
        numberSteps=0;
        while (numberSteps < 1000*Math.exp((-1)*miFactor*coverDepth)) {
            //kulki - poczatkowa liczba 15 - rozpatrujemy jedna paczke kwantowa
            Circle circle = new Circle(1, Color.BLUE);
            circle.setVisible(false);
            circle.relocate(91 + coverDepth + Math.random(), Math.random()*250);
            numberSteps ++;
            animationGamma.getChildren().addAll(circle);
            Timeline loopAfterCover = new Timeline(new KeyFrame(Duration.millis(30), new EventHandler<ActionEvent>() {

                @Override
                public void handle(final ActionEvent t) {
                    if (circle.getLayoutX() > 91+coverDepth && circle.getLayoutX() < 400 ) {
                        circle.setVisible(true);
                        circle.setLayoutX(circle.getLayoutX() + + Math.random() + 0.6);
                        circle.setLayoutY(circle.getLayoutY() + Math.random()-0.5);
                    } else
                        circle.setVisible(false);
                }
            }));
            loopAfterCover.setCycleCount(Timeline.INDEFINITE);
            loopAfterCover.setDelay(Duration.millis(2300));
            loopAfterCover.play();}
    }

    @FXML
    void stopAction() {
        //Stop Animacja
    }

    @FXML
    void exportAction() {
        image = lineChart.snapshot(new SnapshotParameters(), null);
        file = new File("chart.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
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
        if (leadLabel.contains(coverMaterialComboBox.getValue()))
            miFactor = 0.15;
        if (copperLabel.contains(coverMaterialComboBox.getValue()))
            miFactor = 0.093;
        if (aluminumLabel.contains(coverMaterialComboBox.getValue()))
            miFactor = 0.049;
    }

    @FXML
    void chooseNumberCovers() {
        if (coverNumberComboBox.getValue() != null){
            if (coverNumberComboBox.getValue().equals("1"))
                coverNumber = 1;
            if (coverNumberComboBox.getValue().equals("2"))
                coverNumber = 2;
            if (coverNumberComboBox.getValue().equals("3"))
                coverNumber = 3;
            if (coverNumberComboBox.getValue().equals("4"))
                coverNumber = 4;
            if (coverNumberComboBox.getValue().equals("5"))
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
        double dii = 0.01; //krok dodawania do x
        double I_o = startGamma; //wartosc poczatkowa natezenia (przykladowa)
        double x=0;
        double y=1;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

        while (x <= coverDepth*coverNumber) {

            double argument=((-1)*miFactor*x);
            y = I_o * Math.exp(argument);
            lineChartData.add(new LineChart.Data(x,y));
            x += dii;
        }
        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);
        lineChart.setCreateSymbols(false);
    }

    @FXML
    void logarithmicScale(){
        double dii = 0.01; //krok dodawania do x
        double I_o = startGamma; //wartosc poczatkowa natezenia (przykladowa)
        double x=0;
        double y=0;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;

        lineChartSeries= FXCollections.observableArrayList();

        while (x <= coverDepth * coverNumber) {
            {
                if ( y>=0 ) {
                    double argument = (-1) * miFactor  * x;
                    y = Math.log(I_o * Math.exp(argument));
                    lineChartData.add(new LineChart.Data(x, y));
                    x += dii;
                }
                else
                    break;
            }
        }
        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);

        lineChart.setCreateSymbols(false);
    }

    @FXML
    void refreshChart() {
        if (linearScaleButton.isSelected()) { linearScale(); }
        if (loggharytmicScaleButton.isSelected()) { logarithmicScale(); }
    }
}
