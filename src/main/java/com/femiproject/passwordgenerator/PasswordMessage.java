package com.femiproject.passwordgenerator;

public class PasswordMessage {

    public String message;
    public String strength;

    public PasswordMessage(String message, String strength) {
        this.message = message;
        this.strength = strength;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PasswordMessage{");
        sb.append("message=").append(message);
        sb.append(", strength=").append(strength);
        sb.append('}');
        return sb.toString();
    }
}
