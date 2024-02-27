package com.example.proposal;

import java.util.List;

public class AuthTokenPOJO {


    private String auth_token;

    public String getAuthToken() {
        return auth_token;
    }
    public void setAuthToken(String auth_token) {
        this.auth_token = auth_token;
    }
    public AuthTokenPOJO(String auth_token) {
        this.auth_token = auth_token;
    }

}
