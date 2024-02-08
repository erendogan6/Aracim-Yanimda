package com.example.aracimyanimda.api.request;

public class RegisterFirstRequest {
    private String eposta;
    public RegisterFirstRequest(String eposta) {
        this.eposta = eposta;
    }
    public String getEposta() {
        return eposta;
    }
    public void setEposta(String eposta) {
        this.eposta = eposta;
    }
}
