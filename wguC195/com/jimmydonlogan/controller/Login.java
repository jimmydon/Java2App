package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.User;
import com.jimmydonlogan.util.CustomersAppointsUtils;
import com.jimmydonlogan.view.LoginView;

import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Login {


    private final LoginView loginView= new LoginView();
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    static private FileHandler fileHandler;
    private final User user;
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    public Boolean validLogin=false;

    public Login(User user)
    {
        this.user=user;
        setUpLogger();
        AddEvents();
        loginView.buildView();
    }
    private void setUpLogger()
    {
        SimpleFormatter formatter = new SimpleFormatter();
        try {
            fileHandler = new FileHandler("login_attempts.txt");
            LOGGER.setLevel(Level.ALL);

            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }



        LOGGER.addHandler(fileHandler);

    }
    public void AddEvents()
    {

        loginView.btnLogin.setOnAction(event -> {
            user.setName(loginView.txtUserName.getText());
            util.getUserFromUserName(user);
            LOGGER.info("user is:"+ user.getName());
            LOGGER.info("password is:"+ user.getPassword());
            final String strcheckUser = loginView.txtUserName.getText();
            final String strcheckPw = loginView.txtPassword.getText();
            if (strcheckUser.equals(user.getName()) && strcheckPw.equals(user.getPassword())) {
                loginView.lblMessage.setText("Congratulations!");
                loginView.lblMessage.setTextFill(Color.GREEN);
                loginView.stgLoginForm.close();
                validLogin=true;
                LOGGER.info(strcheckUser + " has logged in.");
                loginView.stgLoginForm.close();

            } else {
                loginView.lblMessage.setText("Incorrect user or password.");
                loginView.lblMessage.setTextFill(Color.RED);
                LOGGER.info(strcheckUser + " has failed log in.");
            }
            loginView.txtUserName.setText("");
            loginView.txtPassword.setText("");
        });
    }









}
