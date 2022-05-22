package com.server.server;


import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class RSA {

    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger n;
    private BigInteger p;
    private BigInteger q;

    private BigInteger _256 = new BigInteger(""+256);
    private BigInteger _0 = BigInteger.ZERO;
    public RSA() {
    }

    public boolean computeRSA(String pV, String qV, String eV, boolean manual) {
        if (!manual) {
            this.setP(BigInteger.valueOf(1000000007));
            this.setQ(BigInteger.valueOf(1000000009));
        }
        if(pV.equals(""))  this.setP(BigInteger.valueOf(1000000007));
        else this.setP(new BigInteger(""+pV));

        if(qV.equals(""))  this.setQ(BigInteger.valueOf(1000000009));
        else this.setQ(new BigInteger(""+qV));

        this.setN(p.multiply(q));

        BigInteger z = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        int count = 2;
        BigInteger eValue;
        if(eV.isEmpty()) {
            while (true) {
                BigInteger GCD = z.gcd(new BigInteger("" + count));
                if (GCD.equals(BigInteger.ONE))
                    break;
                count++;
            }
            eValue = new BigInteger(""+count);
        }
        else
        {
            eValue = new BigInteger(""+eV);
            BigInteger GCD = z.gcd(eValue);
            if (!GCD.equals(BigInteger.ONE))
                return false;

        }
        publicKey = eValue;
        privateKey = publicKey.modInverse(z);
        return true;
    }


    public BigInteger keyGenerator(int numberOfBits){
        BigInteger p_V = new BigInteger(numberOfBits/2 , ThreadLocalRandom.current()).nextProbablePrime();
        BigInteger q_V = new BigInteger(numberOfBits - p_V.bitLength() , ThreadLocalRandom.current()).nextProbablePrime();
        BigInteger n_V = p_V.multiply(q_V);
        while (n_V.bitLength() != numberOfBits){
            p_V = new BigInteger(numberOfBits/2 , ThreadLocalRandom.current()).nextProbablePrime();
            q_V = new BigInteger(numberOfBits - p_V.bitLength() , ThreadLocalRandom.current()).nextProbablePrime();
            n_V = p_V.multiply(q_V);
        }
        return n_V;
    }

    public String encrypt(String message){


        ArrayList<BigInteger> partialMessages = splitMessage(message , n);
        if(partialMessages == null ) return null;
        String cipherTexts="";

        for(int i = 0; i < partialMessages.size();i++){

            if(i==0)
                cipherTexts+=(partialMessages.get(i).modPow(publicKey,n));
            else
                cipherTexts+=","+(partialMessages.get(i).modPow(publicKey,n));

        }
        return cipherTexts;

    }

    private ArrayList<BigInteger> splitMessage(String message , BigInteger n){
        int len = message.length() ;
        int numBits = (int)(Math.log(len)/Math.log(2.0));

        if(len == 1){
            var partialsMessages = new ArrayList<BigInteger>() ;
            var partialMessage = asBase256Number(message.substring(0,1)) ;
            if (partialMessage.compareTo(n) < 0){
                partialsMessages.add(partialMessage) ;
                return partialsMessages;
            }
            return null;
        }
        else {
            var partialsMessages = new ArrayList<BigInteger>() ;
            var partialMessage = asBase256Number(message.substring(0,len)) ;
            if (partialMessage.compareTo(n) < 0)  {
                partialsMessages.add(partialMessage) ;
                return partialsMessages;
            }

        }

        for (int i = 1; i <= numBits; i++) {
            int firstSplitIndex = len/(1 << i) ;
            var partialsMessages = new ArrayList<BigInteger>() ;

            boolean fits = true ;
            for (int begin = 0; begin < len;) {
                int end = begin + firstSplitIndex ;
                if (end > len) end = len ;

                var partialMessage = asBase256Number(message.substring(begin,end)) ;
                if (partialMessage.compareTo(n) < 0) partialsMessages.add(partialMessage) ;
                else {
                    partialsMessages.clear() ;
                    fits = false ;
                    break ;
                }
                begin = end ;
            }
            if (fits) return partialsMessages ;
        }
        return null ;
    }
    public String dencrypt(ArrayList<BigInteger> cipherTexts) {

        int arrayListLength = cipherTexts.toArray().length;

        String message = "";
        //dec
        for (int i = 0; i < arrayListLength; i++) {
            cipherTexts.set(i,cipherTexts.get(i).modPow(privateKey, n));

        }
        for (int i = 0; i < arrayListLength; i++) {
          message+=toStringFromBase256Number(cipherTexts.get(i));
        }
        return message;
    }

    public boolean isEmpty(BigInteger v){
        if(v.equals("")) return true;
        return false;
    }
    public boolean isPrime(BigInteger v){
        if(v.isProbablePrime(1))return true;
        return false;
    }

    public boolean getNValue(BigInteger p , BigInteger q) {
        if(p.multiply(q).compareTo(BigInteger.valueOf(255)) != 1) return false;
        return true;
    }

    public BigInteger asBase256Number(String message) {
        BigInteger acc = _0;
        for (int i = 0; i < message.length();i++){
            int me_ASCII = (int) message.charAt(i);
            acc = acc.shiftLeft(8).add(new BigInteger(""+me_ASCII));
        }

        return acc;
    }

    //The inverse of .asBase256Number()
    public String toStringFromBase256Number(BigInteger number)   {
        String str = "";

        while (number.compareTo(_0) == 1) {
            str+=(char)Integer.parseInt(number.mod(_256).toString());
            number = number.shiftRight(8);
        }

        String reverse = new StringBuffer(str).reverse().toString();

        return reverse;
    }

    public BigInteger generatRSAParamsWithMessage(BigInteger m) {
        BigInteger firstP = m.shiftRight(m.bitLength() / 2);

        BigInteger p_V = findPrimeStartingFromValue(firstP.nextProbablePrime());
        BigInteger q_V = findPrimeStartingFromValue( p_V.nextProbablePrime().multiply(new BigInteger(""+256)));
        BigInteger z_V = p_V.subtract(BigInteger.ONE).multiply(q_V.subtract(BigInteger.ONE));
        BigInteger e_V = computeE(z_V);
        BigInteger d_V = e_V.modInverse(z_V);

        setP(p_V);
        setQ(q_V);
        setPublicKey(e_V);
        setPrivateKey(d_V);
        setN(p_V.multiply(q_V));
        return this.getN();
    }

    private BigInteger findPrimeStartingFromValue(BigInteger nextProbablePrime) {
        BigInteger value = nextProbablePrime;
        while (!value.isProbablePrime(1) == true) value = value.nextProbablePrime();

        return value;

    }

    public BigInteger computeE(BigInteger z_V){
        int count = 2;
        BigInteger eValue;
            while (true) {
                BigInteger GCD = z_V.gcd(new BigInteger("" + count));
                if (GCD.equals(BigInteger.ONE))
                    break;
                count++;
            }
            eValue = new BigInteger(""+count);
            return eValue;
    }

    public String dencryptForCCA(ArrayList<BigInteger> cipherTexts) {
        int arrayListLength = cipherTexts.toArray().length;

        String message = "";
        //dec
        for (int i = 0; i < arrayListLength; i++) {
            cipherTexts.set(i,cipherTexts.get(i).modPow(privateKey, n));

        }
        for (int i = 0; i < arrayListLength; i++) {
            message+=cipherTexts.get(i);
        }
        return message;
    }

    public BigInteger generatRSAParamsWithN(int n_Value) {

        BigInteger p_V = findPrimeStartingFromValue(BigInteger.probablePrime(n_Value, new Random()));
        BigInteger q_V = findPrimeStartingFromValue( p_V.nextProbablePrime().multiply(new BigInteger(""+256)));
        BigInteger z_V = p_V.subtract(BigInteger.ONE).multiply(q_V.subtract(BigInteger.ONE));
        BigInteger e_V = computeE(z_V);
        BigInteger d_V = e_V.modInverse(z_V);

        setP(p_V);
        setQ(q_V);
        setPublicKey(e_V);
        setPrivateKey(d_V);
        setN(p_V.multiply(q_V));
        return this.getN();
    }

    private void factorize(BigInteger n_Value) throws Exception {
        BigInteger start = BigInteger.TWO.pow(n.bitLength()/2 - 1);
        BigInteger end = n.divide(BigInteger.TWO);
        BigInteger p = start.nextProbablePrime();
        BigInteger q = n.divide(p);
        while (!(q.isProbablePrime(1) && p.multiply(q).equals(n))) {
            p = p.nextProbablePrime();
            if (p.compareTo(end) == 1) throw new Exception("Passed argument "+n+" can't be factorized into 2 primes");
            q = n.divide(p);
        }
        setQ(q);
        setP(p);
    }
}
