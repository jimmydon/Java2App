package com.jimmydonlogan.model;

/**
 * The type User.
 */
public class User {

    /**
     * The User id.
     */
    private  int userId;
    /**
     * The Name.
     */
    private  String name;
    /**
     * The Password.
     */
    private  String password;


    /**
     * Instantiates a new User.
     *
     * @param userId   the user id
     * @param name     the name
     * @param password the password
     */
    public User(int userId,String name,String password)
    {

        this.userId=userId;
        this.name=name;
        this.password=password;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
