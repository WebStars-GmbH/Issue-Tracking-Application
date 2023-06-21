package com.example.application.data.service;

import java.util.regex.Pattern;

public class EmailCheckUtility {
    public static boolean isValid(String email)
    {
        //This code was brutally stolen from geeksforgeeks.com ;-)
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
