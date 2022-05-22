package com.chat.chatwithrsa;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SenderController implements Initializable {
    private int counter = 0;
    private String messageString;
    PrintWriter out;
    BufferedReader in;
    private RSA rsa;
    @FXML
    private TextField messageSend;
    @FXML
    private TextArea textArea;

    @FXML
    private void sendMyMessage(ActionEvent actionEvent) {
        messageString = messageSend.getText();
        if (messageString.isEmpty()) return;
        counter++;

        String prevText;

        prevText = textArea.getText();
        if (counter == 1) {
            prevText = "~~ Your Messages";
        }
        prevText += "\n";
        textArea.setText(prevText + messageString);
        messageSend.setText("");
        try {
            if (out != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String str = in.readLine();
                            if (!str.isEmpty()) {
                                String splitted[] = str.split(",");
                                rsa.setPublicKey(new BigInteger("" + splitted[0]));
                                rsa.setN(new BigInteger("" + splitted[1]));
                            }
                        }catch (Exception e){
                            System.err.println(e.getMessage());
                        }
                    }
                }).start();

                String encrypted = rsa.encrypt(messageString);
                if(encrypted != null)
                    out.println(encrypted);
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("your message can't be send by this n please choose larger n");
                    alert.showAndWait();
                    return;
                }

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            rsa = new RSA();

            System.out.println("Am i Initializelation Function");
            /*
             * This line establishes a connection with the server with IP address
             * "localhost" or "127.0.0.1" and port 6666, it is the same port that will be
             * used in the server. Note that: "localhost" refers to the local computer that
             * a program is running on. The local machine is defined as "localhost," which
             * gives it an IP address of 127.0. 0.1.
             */
            System.out.println("Hi I am the new client");
            Socket socket = new Socket("localhost", 6666);

            /* Don't forget to set the autoflush parameter of the PrintWriter object as true */
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String str = in.readLine();
                if (!str.isEmpty()) {
                    String splitted[] = str.split(",");
                    rsa.setPublicKey(new BigInteger("" + splitted[0]));
                    rsa.setN(new BigInteger("" + splitted[1]));


                    break;
                }

            }
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.print("out: ");
            System.out.println(this.out);


//
//            out.close();
//            socket.close();

        } catch (Exception e) {
            System.out.println(e);

        }
    }


}