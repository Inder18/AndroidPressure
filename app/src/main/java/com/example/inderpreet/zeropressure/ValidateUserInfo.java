package com.example.inderpreet.zeropressure;

/**
 * Created by INDERPREET on 16-06-2016.
 */
public class ValidateUserInfo {
    public static boolean isEmailValid(String email) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPasswordValid(String password) {
        //TODO change for your own logic
        return password.length() > 4;
    }

    public static boolean isUserNameValid(String username) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z][a-zA-Z0-9.,$;]+$";
        java.util.regex.Pattern a = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher b = a.matcher(username);
        return b.matches();
    }

    public static boolean isMobileValid(String mobile) {
        //TODO change for your own logic
        return mobile.length() == 10;
    }






}
