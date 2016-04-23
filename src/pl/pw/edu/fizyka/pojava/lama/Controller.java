package pl.pw.edu.fizyka.pojava.lama;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

public class Controller {

    double coverDepth ;
    double miFactor ;
    ObservableList<String> optionsCoverMaterials;
    ObservableList<String> optionsCoverDepth;

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


    }

    @FXML
    ComboBox coverMaterialComboBox;
    @FXML
    ComboBox coverDepthComboBox;
    @FXML
    LineChart<Double,Double> lineChart;


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

    }
    @FXML
    void chooseCoverMaterial(ActionEvent event){
        Object source = event.getSource();
        if (source == coverMaterialComboBox) {
            if (coverMaterialComboBox.getValue().equals("Ołów (Pb)"))
                miFactor = 0.15;
            if (coverMaterialComboBox.getValue().equals("Miedź (Cu)"))
                miFactor = 0.093;
            if (coverMaterialComboBox.getValue().equals("Aluminium (Al)"))
                miFactor = 0.049;
        }

    }
    @FXML
    void chooseCoverDepth(ActionEvent event) {
        Object source = event.getSource();
        if (source == coverDepthComboBox) {
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
        double I_o = 1000; //wartosc poczatkowa natezenia (przykladowa)
        double x=0;
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

         while (x <= coverDepth) {
           double argument=((-1)*miFactor*x);
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
        ObservableList<XYChart.Data<Double, Double>> lineChartData;
        lineChartData = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Double, Double>> lineChartSeries;
        lineChartSeries= FXCollections.observableArrayList();

        while (x <= coverDepth) {
            double argument = (-1)*miFactor*x;
            double y = Math.log( I_o * Math.exp(argument));
            lineChartData.add(new LineChart.Data(x,y));
            x += dii;
        }
        lineChartSeries.add(new LineChart.Series(lineChartData));

        lineChart.setData(lineChartSeries);

    }

}




