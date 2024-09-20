package org.digitalpower.model;

public class User {

    private String userId;
    private String userName;
    private String email;
    private String city;
    private String state;
    private String country;
    private int age;

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

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAge(int age) {
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
                ", age='" + age + '\'' +
                '}';
    }

}
