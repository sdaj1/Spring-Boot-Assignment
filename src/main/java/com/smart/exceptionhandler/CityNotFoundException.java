package com.smart.exceptionhandler;

public class CityNotFoundException extends Throwable {
    public CityNotFoundException(String cityNotFound) {
        super(cityNotFound);
    }
}
