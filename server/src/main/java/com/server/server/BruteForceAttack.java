package com.server.server;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.math.BigInteger;

@Data
public class BruteForceAttack extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("RSA Brute Force Attack Time");

        NumberAxis xAxis = new NumberAxis(0, 200, 10);
        NumberAxis yAxis = new NumberAxis(0, 200, 10);
        xAxis.setLabel("bits");
        yAxis.setLabel("time (us)");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Brute Force Efficiency");

        XYChart.Series series = new XYChart.Series<Number, Number>();
        series.setName("RSA Brute Force Attack Time Vs. Key Size In Bits");
        series.setData(measureBruteForceAttackTimeForBits(10,100));

        Scene scene = new Scene(lineChart, 1500.0, 600.0);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();

    }


    private ObservableList measureBruteForceAttackTimeForBits(Integer minBits , Integer maxBits){
        XYChart.Series list = new XYChart.Series();
        list.getData().add(new XYChart.Data<>(1,1));
        list.getData().add(new XYChart.Data<>(2,2));
        list.getData().add(new XYChart.Data<>(100,100));

        return list.getData();

    }
    /**
     * @brief bruteforce attack prime factorization
     * @param n
     *
     * */

    public void attack(BigInteger n) {
        for (BigInteger i = new BigInteger("" + 2); n.compareTo(i) == 1 || n.compareTo(i) == 0; i = i.add(BigInteger.ONE)) {
            while (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                n = n.divide(i);
                System.out.println(i); //prints 2 2 3
            }
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
