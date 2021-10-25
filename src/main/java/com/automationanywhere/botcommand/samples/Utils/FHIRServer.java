package com.automationanywhere.botcommand.samples.Utils;

public class FHIRServer {


    public String getToken() {
        return Token;
    }

    public String getURL() {
        return Url;
    }


    String Token;
    String Url;


    public FHIRServer(String Url, String Token){
        this.Url = Url;
        this.Token = Token;
    }
}