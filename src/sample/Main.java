package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane root = new StackPane();
        primaryStage.setTitle("Rxx and Rxy");
        primaryStage.setScene(new Scene(root, 800, 700));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart chart = new LineChart(xAxis, yAxis, getChartData());
        chart.setTitle("Charts");

        root.getChildren().add(chart);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private ObservableList<XYChart.Series<String, Double>> getChartData() {
        Calculations c = new Calculations();
        c.freq = c.generateFrequency(c.omega, c.n);
        c.x = c.generateRandSignal(c.N, c.n, c.a, c.fi, c.freq);
        c.y = c.generateRandSignal(c.N, c.n, c.aY, c.fiY, c.freq);
        double mathExpectationY = c.findMathExpectation(c.y, c.N);
        double mathExpectation = c.findMathExpectation(c.x, c.N);
        double[] rxxValue = c.findRxx(c.N, c.x, mathExpectation);
        double[] rxyValue = c.findRxy(c.N, c.y, mathExpectation, mathExpectationY);

        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Double> rxx = new XYChart.Series<>();
        XYChart.Series<String, Double> rxy = new XYChart.Series<>();
        rxx.setName("Rxx");
        rxy.setName("Rxy");

        for (int i = 0, j = 0; i < (c.N/2)-1; i++, j++) {
            if (i != (c.N/2)-1) {
                rxx.getData().add(new XYChart.Data(Integer.toString(i), rxxValue[j]));
            }
        }

        for (int i = 0, j = 0; i < (c.N/2)-1; i++, j++) {
            if (i != (c.N/2)-1) {
                rxy.getData().add(new XYChart.Data(Integer.toString(i), rxyValue[j]));
            }
        }

        data.addAll(rxx, rxy);

        return data;
    }
}
