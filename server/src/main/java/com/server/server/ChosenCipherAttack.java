package com.server.server;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class ChosenCipherAttack {

    static public void main(String args []){
        try{
            System.err.println("WE ARE ON CHOSEN CIPHER TEXT ATTACK ");
            System.out.println("Enter Your Plain Text Message To Send ");
            RSA rsa = new RSA();
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            BigInteger messageBits = rsa.asBase256Number(message);
            rsa.generatRSAParamsWithMessage(messageBits);
            String C = rsa.encrypt(message);
            // here we have encryptedMessageBits = C = pow(m,e) % n
            BigInteger encryptedMessageBits = new BigInteger(C);
            BigInteger r = rsa.computeE(rsa.getN()); // random number that gcd(n,r)=1

            BigInteger cc = (encryptedMessageBits.multiply(r.modPow(rsa.getPublicKey() , rsa.getN()))).mod(rsa.getN());
            ArrayList cTs = new ArrayList<BigInteger>();
            cTs.add(cc);
            String Y = rsa.dencryptForCCA(cTs);
            BigInteger Y_BigInteger = new BigInteger(""+Y);
            BigInteger afterAttackMessage = (Y_BigInteger.multiply(r.modInverse(rsa.getN()))).mod(rsa.getN());
            System.err.println("----------------------------------------------------------------------------------------------");
            System.err.println("--------------------------------- The Message After Attack is --------------------------------");
            System.out.println("\n");
            System.out.println("Message: " + rsa.toStringFromBase256Number(afterAttackMessage));
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }






    }
}
