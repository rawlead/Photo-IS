package com.rawlead.github.pojo;


import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserRegistrationForm {

    @NotNull
    @Size(min = 2)
    private String firstName;

    private String lastName;

    @NotNull
    @Email(message = ResponseMessage.INVALID_EMAIL)
    private String email;

    @NotNull
    @Size(min = 2)
    private String username;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Size(min = 6)
    private String passwordConfirm;

    public UserRegistrationForm() {
    }

    public UserRegistrationForm(String firstName, String lastName, String email, String username, String password, String passwordConfirm) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirmation) {
        this.passwordConfirm = passwordConfirmation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
