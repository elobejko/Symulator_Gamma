package pl.pw.edu.fizyka.pojava.lama;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

public class Controller {

    double coverDepth ;
    double miFactor ;
    double numberScreens;
    WritableImage image;
    File file;
    ObservableList<String> optionsCoverMaterials;
    ObservableList<String> optionsCoverDepth;
    ObservableList<String> optionsNumber;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        optionsCoverMaterials =
                FXCollections.observableArrayList(
                        "Miedź (Cu)",
                        "Aluminium (Al)",
                        "Ołów (Pb)"
                );
        coverMaterialComboBox.setItems(optionsCoverMaterials);
        optionsCoverDepth =
                FXCollections.observableArrayList(
                        "2 mm", "5 mm", "7 mm","10 mm", "12 mm", "15 mm", "17 mm", "20 mm"
                );
        coverDepthComboBox.setItems(optionsCoverDepth);
        optionsNumber = FXCollections.observableArrayList ("2","3","4","5");
        numberComboBox.setItems(optionsNumber);
        coverDepthComboBox.setPromptText("Grubość przesłony");
        numberComboBox.setPromptText("Liczba przesłon");
        coverMaterialComboBox.setPromptText("Materiał przesłony");
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
        coverMaterialComboBox.setPromptText("Material of screen");
        optionsCoverDepth =
                FXCollections.observableArrayList(
                        "2 mm", "5 mm", "7 mm","10 mm", "12 mm", "15 mm", "17 mm", "20 mm"
                );
        coverDepthComboBox.setItems(optionsCoverDepth);
        coverDepthComboBox.setPromptText("Size of screen");
        numberComboBox.setPromptText("Number of screens");
        lineChart.setTitle("The weakening of gamma radiation");
        //setTitle("Simulation - Gamma");
        linearScaleButton.setText("Linear scale");
        loggharytmicScaleButton.setText("Logharytmic scale");
        exportButton.setText("Export");

    }

    @FXML
    ComboBox<String> coverMaterialComboBox;
    @FXML
    Button exportButton;
    @FXML
    ComboBox<String> numberComboBox;
    @FXML
    ComboBox<String> coverDepthComboBox;
    @FXML
    LineChart<Double,Double> lineChart;
    @FXML
    RadioButton linearScaleButton;
    @FXML
    RadioButton loggharytmicScaleButton;

    @FXML
    void startAction() {
        //Start animacji

    }
    @FXML
    void stopAction(){
        //Stop Animacja
    }
    @FXML
    void exportAction(){
        image = lineChart.snapshot(new SnapshotParameters(), null);
        file = new File("chart.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            // TODO: handle exception here
        }
    }

    @FXML
    void chooseCoverMaterial(){
        if (coverMaterialComboBox.getValue().equals("Ołów (Pb)"))
            miFactor = 0.15;
        if (coverMaterialComboBox.getValue().equals("Miedź (Cu)"))
            miFactor = 0.093;
        if (coverMaterialComboBox.getValue().equals("Aluminium (Al)"))
            miFactor = 0.049;
    }
    @FXML
    void chooseNumberScreens(){
        if (numberComboBox.getValue().equals("2"))
            numberScreens = 2;
        if (numberComboBox.getValue().equals("3"))
            numberScreens = 3;
        if (numberComboBox.getValue().equals("4"))
            numberScreens = 4;
        if (numberComboBox.getValue().equals("5"))
            numberScreens = 5;
    }
    @FXML
    void chooseCoverDepth() {
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

    @FXML
    void linearScale(){
        double dii = 0.01; //krok dodawania do x
        double I_o = 1000; //wartosc poczatkowa natezenia (przykladowa)
        double x=0;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

         while (x <= coverDepth) {
           double argument=((-1)*miFactor*numberScreens*x);
           double y = I_o * Math.exp(argument);
             lineChartData.add(new LineChart.Data(x,y));
             x += dii;
        }
        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);
    }

    @FXML
    void logarithmicScale(){
        double dii = 0.01; //krok dodawania do x
        double I_o = 1000; //wartosc poczatkowa natezenia (przykladowa)
        double x=0;
        double y=0;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();
        while ( x <= coverDepth ) {
            //while ( y>=0 ) {
                double argument = (-1) * miFactor * numberScreens * x;
                y = Math.log(I_o * Math.exp(argument));
                lineChartData.add(new LineChart.Data(x, y));
                x += dii;
           // }
        }
        lineChartSeries.add(new LineChart.Series(lineChartData));
        lineChart.setData(lineChartSeries);
    }
}




