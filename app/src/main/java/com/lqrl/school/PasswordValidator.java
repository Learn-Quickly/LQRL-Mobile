package com.lqrl.school;

import android.content.Context;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {

    public enum PasswordStrength {
        WEAK,
        NORMAL,
        STRONG
    }

    public static PasswordStrength validatePassword(Context context, String password) {
        if (password == null || password.length() < 8) {
            return PasswordStrength.WEAK;
        }

        int score = 0;

        // Длина пароля
        if (password.length() >= 14) {
            score += 2;
        } else if(password.length() >= 8 && password.length() <= 12){
            score++;
        } else return PasswordStrength.WEAK;

        List<String> commonPasswords = new CSVPasswordReader(context).readCSVFile("10K-most-common.txt");
        boolean matchesWeak = commonPasswords.stream().anyMatch(password::contains);
        if(matchesWeak) return PasswordStrength.WEAK;

        // Проверка наличия заглавных букв
        Pattern upperCasePattern = Pattern.compile("[A-Z]");
        Matcher upperCaseMatcher = upperCasePattern.matcher(password);
        if (upperCaseMatcher.find()) {
            score++;
        }

        // Проверка наличия строчных букв
        Pattern lowerCasePattern = Pattern.compile("[a-z]");
        Matcher lowerCaseMatcher = lowerCasePattern.matcher(password);
        if (lowerCaseMatcher.find()) {
            score++;
        }

        // Проверка наличия цифр
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(password);
        if (digitMatcher.find()) {
            score++;
        }

        // Проверка наличия специальных символов
        Pattern specialCharPattern = Pattern.compile("[@#$%^&+=]");
        Matcher specialCharMatcher = specialCharPattern.matcher(password);
        if (specialCharMatcher.find()) {
            score++;
        }

        // Определение силы пароля
        switch (score) {
            case 6:
            case 5:
                return PasswordStrength.STRONG;
            case 3:
            case 4:
                return PasswordStrength.NORMAL;
            default:
                return PasswordStrength.WEAK;
        }
    }
}
