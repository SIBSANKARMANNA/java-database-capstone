package com.project.back_end.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Login {

    @NotNull(message = "Identifier is required")
    private String identifier; // email or username

    @NotNull(message = "Password is required")
    @Size(min = 6)
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
