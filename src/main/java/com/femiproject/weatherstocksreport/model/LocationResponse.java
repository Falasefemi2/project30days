package com.femiproject.weatherstocksreport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationResponse {
    public String status;
    public String country;
    public String city;
    public double lat;
    public double lon;
    public String countryCode;
    public String region;
    public String regionName;
    public String zip;
    public String timezone;
    public String isp;
    public String org;
    public String as;
    public String query;
}
