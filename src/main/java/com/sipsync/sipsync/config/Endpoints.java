package com.sipsync.sipsync.config;

public final class Endpoints{
    public final static String LOGIN = "/api/login";
    public final static String SIGNUP = "/api/signup";
    public final static String VERIFICATION_EMAIL = "/email";

    // request verification code
    public final static String PASSWORD_RESET_EMAIL = "/password/reset/code";

    // compare codes
    public final static String PASSWORD_RESET_CODE = "/password/reset/verify";

    // change password
    public final static String PASSWORD_RESET_CHANGE = "/password/reset/change";


    private Endpoints(){}
}