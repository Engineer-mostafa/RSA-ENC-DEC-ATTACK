package com.chat.chatwithrsa;

import lombok.Data;

import java.math.BigInteger;
import java.util.ArrayList;

@Data
public class RSA {

    private BigInteger publicKey;
    private BigInteger n;

    public RSA() {


    }
    
    public String encrypt(String message){

        byte[] plainTxtByte = message.getBytes();

        ArrayList partialMessages = splitMessage(message , n);
        if(partialMessages == null ) return null;
        String cipherTexts="";

        for(int i = 0; i < partialMessages.size();i++){
            BigInteger partial = cipherTexts. partialMessages[i];
            BigInteger convertedVal = new BigInteger(""+asciValue);
            if(i==0)
                cipherTexts+=(convertedVal.modPow(publicKey,n));
            else
                cipherTexts+=","+(convertedVal.modPow(publicKey,n));

        }
        return cipherTexts;

    }
    private ArrayList<BigInteger> splitMessage(String message , BigInteger n){
        int len = message.length() ;

        return null;
    }


}
