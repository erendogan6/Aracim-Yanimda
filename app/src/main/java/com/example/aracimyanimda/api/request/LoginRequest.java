package com.example.aracimyanimda.api.request;

public class LoginRequest {
    private final String ePosta;
    private final String sifre;

    public LoginRequest(String ePosta, String sifre) {
        this.ePosta = ePosta;
        this.sifre = sifre;
    }

}
