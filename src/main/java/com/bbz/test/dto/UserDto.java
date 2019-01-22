package com.bbz.test.dto;

import com.bbz.test.validation.PasswordMatches;
import com.bbz.test.validation.PasswordStrong;
import com.bbz.test.validation.ValidEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@PasswordMatches(message = "{password.matching}")
public class UserDto {

    @NotNull(message = "{field.mustBeFilled}")
    @NotBlank(message = "{field.mustBeFilled}")
    private String name;

    @NotNull(message = "{field.mustBeFilled}")
    @NotBlank(message = "{field.mustBeFilled}")
    @ValidEmail(message = "{email.validLooking}")
    private String email;

    @NotNull(message = "{field.mustBeFilled}")
    @NotBlank(message = "{field.mustBeFilled}")
    @PasswordStrong(message = "{password.mustBeStrongTest}")
    private String password;

    private String matchingPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", matchingPassword='" + matchingPassword + '\'' +
                '}';
    }
}