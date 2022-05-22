package com.server.server;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class EncryptionTime extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("RSA Encryption Time");

        NumberAxis xAxis = new NumberAxis(0, 20000, 1000);
        NumberAxis yAxis = new NumberAxis(0, 300, 10);
        xAxis.setLabel("bits");
        yAxis.setLabel("time (us)");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Encryption Efficiency");

        XYChart.Series series = new XYChart.Series<Number, Number>();
        series.setName("RSA Encryption Time Vs. Key Size In Bits");
        series.setData(measureEncryptionTimeForBits("CMP23 Will Graduate At 2023 whit RSA Encryption Time Vs. Key Size In Bits Assignment Grade Excellent"));

        Scene scene = new Scene(lineChart, 1500.0, 600.0);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();

    }


    private ObservableList measureEncryptionTimeForBits(String message){
        XYChart.Series list = new XYChart.Series();
        RSA rsa = new RSA();
        BigInteger m = rsa.asBase256Number(message);
        int startWithN = rsa.generatRSAParamsWithMessage(m).bitLength();

        for (int i = startWithN+1; i < startWithN * 5 ; i+=1){
            rsa.generatRSAParamsWithN(i);
            Instant start = Instant.now();
            rsa.encrypt(message);
            Instant end = Instant.now();
            list.getData().add(new XYChart.Data<>(i,(float) Duration.between(start, end).toNanos()/1000));

        }

        return list.getData();

    }



    public static void main(String[] args) {
        launch();
    }
}
