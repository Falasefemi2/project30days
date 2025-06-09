package com.femiproject.passwordgenerator;

public class Password {

    public String text;
    public int textLength;

    public Password(String text) {
        this.text = text;
        this.textLength = text.length();
    }

    public int charType(char words) {
        if (Character.isUpperCase(words)) {
            return 1;
        }
        if (Character.isLowerCase(words)) {
            return 2;
        }
        if (Character.isDigit(words)) {
            return 3;
        } else {
            return 4;
        }
    }

    public int checkPasswordStrength() {
        String word = this.text;

        boolean upper = false;
        boolean lower = false;
        boolean numbers = false;
        boolean symbols = false;

        int score = 0;

        for (int i = 0; i < word.length(); i++) {
            int type = charType(word.charAt(i));

            switch (type) {
                case 1 -> {
                    upper = true;
                }
                case 2 -> {
                    lower = true;
                }
                case 3 -> {
                    numbers = true;
                }
                case 4 -> {
                    symbols = true;
                }
                default -> {
                    throw new AssertionError();
                }
            }
        }

        if (upper)
            score += 1;
        if (lower)
            score += 1;
        if (numbers)
            score += 1;
        if (symbols)
            score += 1;

        return score;
    }

    public PasswordMessage calculatePassword() {
        int score = checkPasswordStrength();

        if (score == 6) {
            return new PasswordMessage("This is a very strong password", "Strong");
        } else if (score >= 4) {
            return new PasswordMessage("This is a good password", "Good");
        } else if (score >= 3) {
            return new PasswordMessage("This is a medium password", "Medium");
        } else {
            return new PasswordMessage("This is a weak password", "Weak");
        }
    }

    @Override
    public String toString() {
        return "Password [text=" + text + "]";
    }
}
