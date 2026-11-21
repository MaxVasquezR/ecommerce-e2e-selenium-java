package com.idat.qa.models;

public record User(
        String title,
        String name,
        String email,
        String password,
        String birthDay,
        String birthMonth,
        String birthYear,
        String firstName,
        String lastName,
        String company,
        String address1,
        String address2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}
