package com.edgeverve.excp;

public class CustomException extends Exception{
    private String eMessage;
    private String eCode;

    public CustomException(String eMessage, String eCode) {
        this.eMessage = eMessage;
        this.eCode = eCode;
    }

    public String geteMessage() {
        return eMessage;
    }

    public void seteMessage(String eMessage) {
        this.eMessage = eMessage;
    }

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    @Override
    public String toString() {
        return "CustomException : {" +
                "eMessage='" + eMessage + '\'' +
                ", eCode='" + eCode + '\'' +
                '}';
    }
}
