package com.server.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ReceiverController implements Initializable {
    private BufferedReader in;
    private RSA rsa;
    Socket socket;
    PrintWriter out;

    @FXML
    private TextArea textAreaField;

    @FXML
    private Button receiveButton;

    @FXML
    private TextField p_value;

    @FXML
    private TextField q_value;

    @FXML
    private TextField e_value;

    @FXML
    private CheckBox checkBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rsa = new RSA();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket ss = new ServerSocket(6666);

                    System.out.println("Server is started.\n");
                    System.out.println("I am waiting for a client to connect.");
                    socket = ss.accept();
                    receiveButton.setVisible(true);
                    System.out.println("A client sent request and connection is established.");

                    out = new PrintWriter(socket.getOutputStream(), true);

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    public void receiveNow(ActionEvent actionEvent) {

        String p = p_value.getText();
        String q = q_value.getText();
        String e = e_value.getText();
        Boolean checkBoxIsSelected = checkBox.isSelected();
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (checkBoxIsSelected && !p.isEmpty() && !q.isEmpty()) {
            BigInteger pB = new BigInteger("" + p);
            BigInteger qB = new BigInteger("" + q);
            if (!rsa.isEmpty(pB) && !rsa.isEmpty(qB)) {
                if (qB.equals(pB)) {
                    alert.setContentText("P should be not equal q");
                    alert.showAndWait();
                    return;
                } else if (!rsa.isPrime(pB)) {
                    alert.setContentText("P should be Prime");
                    alert.showAndWait();
                    return;
                } else if (!rsa.isPrime(qB)) {
                    alert.setContentText("q should be Prime");
                    alert.showAndWait();
                    return;
                } else if (!rsa.getNValue(pB, qB)) {
                    alert.setContentText("Your p and q is too small");
                    alert.showAndWait();
                    return;
                }
            }


            boolean eIsGood = rsa.computeRSA(p, q, e, true);
            if (eIsGood) {
                out.println(rsa.getPublicKey() + "," + rsa.getN());
            } else {
                alert.setContentText("Your e is not co-prime with phi please change it");
                alert.showAndWait();
                return;
            }


        } else {
            boolean eIsGood = rsa.computeRSA(p, q, e, checkBoxIsSelected);
            if (eIsGood) {
                out.println(rsa.getPublicKey() + "," + rsa.getN());
            } else {
                alert.setContentText("Your e is not co-prime with phi please change it");
                alert.showAndWait();
                return;
            }
        }
        try {
            /*
             * waiting for a client to send a request. When the connection request
             * reaches, it establishes a connection with the client and returns the socket
             * object that will be used for communication with the client.
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // While there is still a connection with the server, continue to listen for messages on a separate thread.
                        while (socket.isConnected()) {
                            try {
                                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                String prevText;


                                String str = null;
                                while ((str = in.readLine()) != null) {
                                    String splitted[] = str.split(",");
                                    ArrayList<BigInteger> ciphers = new ArrayList<BigInteger>();

                                    for (int i = 0; i < splitted.length; i++) {
                                        ciphers.add(new BigInteger("" + splitted[i]));
                                    }
                                    String decMessage = rsa.dencrypt(ciphers);
                                    prevText = textAreaField.getText();
                                    prevText += "\n";
                                    textAreaField.setText(prevText + decMessage);

                                }
                            } catch (Exception e) {
                                System.err.println("error");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }).start();


        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}