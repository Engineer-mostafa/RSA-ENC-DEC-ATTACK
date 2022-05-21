package com.server.server;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;

public class EncryptionTime extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("RSA Encryption Time");

        NumberAxis xAxis = new NumberAxis(0, 200, 10);
        NumberAxis yAxis = new NumberAxis(0, 200, 10);
        xAxis.setLabel("bits");
        yAxis.setLabel("time (us)");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Encryption Efficiency");

        XYChart.Series series = new XYChart.Series<Number, Number>();
        series.setName("RSA Encryption Time Vs. Key Size In Bits");
        series.setData(measureEncryptionTimeForBits(10,100));

        Scene scene = new Scene(lineChart, 1500.0, 600.0);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();

    }


    private ObservableList measureEncryptionTimeForBits(Integer minBits , Integer maxBits){
        XYChart.Series list = new XYChart.Series();
        list.getData().add(new XYChart.Data<>(1,1));
        list.getData().add(new XYChart.Data<>(2,2));
        list.getData().add(new XYChart.Data<>(100,100));

        return list.getData();

    }



    public static void main(String[] args) {
        launch();
    }
}
