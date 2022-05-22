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
import java.time.Duration;
import java.time.Instant;

@Data
public class BruteForceAttack extends Application {

    RSA rsa = new RSA();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("RSA Brute Force Attack Time");

        NumberAxis xAxis = new NumberAxis(0, 200, 10);
        NumberAxis yAxis = new NumberAxis(0, 200000, 100);
        xAxis.setLabel("bits");
        yAxis.setLabel("time (us)");
        LineChart lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Brute Force Efficiency");

        XYChart.Series series = new XYChart.Series<Number, Number>();
        series.setName("RSA Brute Force Attack Time Vs. Key Size In Bits");
        series.setData(measureBruteForceAttackTimeForBits(10,60));
        System.out.println("Finish");

        Scene scene = new Scene(lineChart, 1500.0, 600.0);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();



    }


    private ObservableList measureBruteForceAttackTimeForBits(Integer minBits , Integer maxBits){
        XYChart.Series list = new XYChart.Series();
        for(int i = minBits; i <= maxBits ; i++){
            BigInteger n_V = rsa.keyGenerator(i);
            Instant start = Instant.now();
            try {
                attack(n_V);
            } catch (Exception e) {
                 System.err.println("passed value " + i );
            }
            Instant end = Instant.now();
            list.getData().add(new XYChart.Data<>(i,(int)Duration.between(start, end).toNanos() / 10000));
        }


        return list.getData();

    }

    /**
     * @brief bruteforce attack prime factorization
     * @param n
     *
     * */
    public void attack(BigInteger n) throws Exception {
        BigInteger start = BigInteger.TWO.pow(n.bitLength()/2 - 1);
        BigInteger end = n.divide(BigInteger.TWO);
        BigInteger p = start.nextProbablePrime();
        BigInteger q = n.divide(p);
        while (!(q.isProbablePrime(1) && p.multiply(q).equals(n))) {
            p = p.nextProbablePrime();
            if (p.compareTo(end) == 1) throw new Exception("Passed argument "+n+" can't be factorized into 2 primes");
            q = n.divide(p);
        }
        rsa.setQ(q);
        rsa.setP(p);
        System.out.println("P= " + p+ "\n"+"q= " + q +"\n-----\n");

    }
    public static void main(String[] args) {
        launch();
    }
}
