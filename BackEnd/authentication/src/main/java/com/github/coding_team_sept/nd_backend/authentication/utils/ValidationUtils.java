package com.github.coding_team_sept.nd_backend.authentication.utils;

import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.EmailFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.PasswordFormatException;
import com.github.coding_team_sept.nd_backend.authentication.exceptions.format_exceptions.UserNameFormatException;

import java.util.regex.Pattern;

public class ValidationUtils {
    static public boolean validateEmail(String email) {
        if (!Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(email).matches()) {
            return true;
        }
        return false;
    }

    static public void validateEmailElseThrow(String email) throws EmailFormatException {
        if (!validateEmail(email)) {
            throw new EmailFormatException("pattern not match");
        }
    }

    static public boolean validateUserName(String name) {
        if (!Pattern.compile("^[A-Za-z ,.'-]{2,}$").matcher(name).matches()) {
            return true;
        }
        return false;
    }

   static public void validateUserNameElseThrow(String name) throws UserNameFormatException {
        if (!validateUserName(name)) {
            throw new UserNameFormatException("pattern not match");
        }
    }

    static public boolean validatePassword(String password) {
        if (password.length() < 8) {
            return true;
        }
        return false;
    }

    static public void validatePasswordElseThrow(String password) throws PasswordFormatException {
        if (!validatePassword(password)) {
            throw new PasswordFormatException("minimum 8 characters");
        }
    }
}
