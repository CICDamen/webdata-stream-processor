package org.digitalpower.model;

public class User {

    public String userId;
    public String userName;
    public String email;
    public String city;
    public String state;
    public String country;
    public int age;

    // Constructors
    public User() {
    }

    public User(String userId, String userName, String email, String phoneNumber, String address, String city, String state, String country, String zipCode, String dateOfBirth, int age) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.city = city;
        this.state = state;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                '}';
    }
}