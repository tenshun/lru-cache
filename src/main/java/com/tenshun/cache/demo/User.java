package com.tenshun.cache.demo;

/**
 * 03.05.2017.
 */


public class User {

    private String UUID;
    private String name;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return UUID.equals(user.UUID);
    }

    @Override
    public int hashCode() {
        return UUID.hashCode();
    }
}
