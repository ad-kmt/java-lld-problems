package org.kmt.lld.meetingscheduler.models;

import java.util.Objects;

/**
 * Represents a user with a name and email.
 */
public class User {
    private String name;
    private String email;

    /**
     * Constructs a User with the specified name and email.
     *
     * @param name  the name of the user
     * @param email the email of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

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

    @Override
    public String toString() {
        return "User { " +
                "name: '" + name + '\'' +
                ", email: '" + email + '\'' +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
