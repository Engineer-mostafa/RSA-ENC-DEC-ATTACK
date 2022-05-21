package com.server.server;


import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;

@Data
public class RSA {

    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger n;
    private BigInteger p;
    private BigInteger q;

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

    public String dencrypt(ArrayList<BigInteger> cipherTexts) {
        int arrayListLength = cipherTexts.toArray().length;
        String message = "";
        for (int i = 0; i < arrayListLength; i++) {
            BigInteger plainValue = cipherTexts.get(i).modPow(privateKey, n);
            int ascii = plainValue.intValue();
            System.out.println(Character.toString((char) ascii));
            message += Character.toString((char) ascii);
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
}
