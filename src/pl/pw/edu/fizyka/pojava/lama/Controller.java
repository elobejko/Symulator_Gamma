package pl.pw.edu.fizyka.pojava.lama;

import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

public class Controller {

    @FXML
    public SwingNode gammaChart;

    @FXML
    public void startAction(){

    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                swingNode.setContent(new ChartPanel(
                        generateGammaChart()
                ));
            }

        });
    }
    private JFreeChart generateGammaChart() {

            JFreeChart w;
            double grubosc = 20;
            double wspolczynnik_mi = 1.5;

            double dii=0.0001; //krok dodawania do x
            double I_o = 1000; //wartosc poczatkowa natezenia (przykladowa)

            XYSeries series = new XYSeries("Wartość osłabienia promieniowania gamma");
            for(double ii=0; ii<=grubosc; ii+=dii) { //grubosc to zmienna lokalna, wlasnie z comboboxa moznaby ja zmieniac
                double argument=((-1)*wspolczynnik_mi*ii); //wzor na wartosc y w przypadku konkretnego wspolczynnika oslabienia, zmiana rowniez z comboboxa
                double y = I_o * Math.exp(argument);
                series.add(ii, y); //dodanie serii do wykresu
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(series);
            w= ChartFactory.createXYLineChart( //okreslenie osi, grubosci i ilosci zyjacych czastek
                    "Oslabienie promieniowania gamma", "Grubosc absorbentu [mm]", "Ilosc żyjących czastek gamma", dataset,
                    PlotOrientation.VERTICAL, true, true, false);
        return w;
    }

    public Controller(){
        gammaChart = new SwingNode();
        createSwingContent(gammaChart);
    }
}
