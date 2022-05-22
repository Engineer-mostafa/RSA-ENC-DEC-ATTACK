package com.chat.chatwithrsa;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

@Data
public class RSA {

    private BigInteger publicKey;
    private BigInteger n;

    private BigInteger _256 = new BigInteger(""+256);
    private BigInteger _0 = BigInteger.ZERO;
    public RSA() {


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

    // string
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

}
