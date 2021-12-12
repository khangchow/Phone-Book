package com.example.phonebook;

public class Contact {
    private int id;
    private String name;
    private String phoneNumber;
    private boolean firstLetter;

    public Contact(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        firstLetter = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(boolean firstLetter) {
        this.firstLetter = firstLetter;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstLetter=" + firstLetter +
                '}';
    }
}
