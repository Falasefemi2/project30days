package com.femiproject.hotelmanagement;

public class NotAvailable extends Exception {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NotAvailable{");
        sb.append('}');
        return sb.toString();
    }
}
