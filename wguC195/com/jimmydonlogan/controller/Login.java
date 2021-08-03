package com.jimmydonlogan.controller;

import com.jimmydonlogan.model.User;
import com.jimmydonlogan.util.CustomersAppointsUtils;
import com.jimmydonlogan.util.Print;
import com.jimmydonlogan.util.TextFieldIsEmpty;
import com.jimmydonlogan.view.LoginView;


import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.ResourceBundle;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * The type Login.
 */
public class Login {


    /**
     * The Login view.
     */
    private final LoginView loginView = new LoginView();
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    /**
     * The File handler.
     */
    static private FileHandler fileHandler;
    /**
     * The User.
     */
    private final User user;
    /**
     * The Util.
     */
    private final CustomersAppointsUtils util = new CustomersAppointsUtils();
    /**
     * The Valid login.
     */
    public Boolean validLogin = false;
    /**
     * The Prt.
     */
    private final Print prt = new Print();
    /**
     * The Login messages.
     */
    ResourceBundle loginMessages;

    /**
     * Instantiates a new Login.
     *
     * @param user          the user
     * @param loginMessages the login messages
     */
    public Login(User user, ResourceBundle loginMessages) {
        this.loginMessages = loginMessages;
        this.user = user;
        setUpLogger();
        AddEvents();
        loginView.buildView(loginMessages);


    }

    /**
     * Sets up logger.
     */
    private void setUpLogger() {
        SimpleFormatter formatter = new SimpleFormatter();
        try {
            fileHandler = new FileHandler("login_activity.txt", true);
            LOGGER.setLevel(Level.ALL);

            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LOGGER.addHandler(fileHandler);

    }

    /**
     * Add events. using lambda concise code,avoid using bulky anonymous class implementation
     */
    public void AddEvents() {

        try {
            loginView.btnLogin.setOnAction(event -> {


                user.setName(loginView.txtUserName.getText());
                util.getUserFromUserName(user);

                final String strCheckUser = loginView.txtUserName.getText();
                final String strCheckPw = loginView.txtPassword.getText();
                if (emptyInput())
                    return;
                if (strCheckUser.equals(user.getName()) && strCheckPw.equals(user.getPassword())) {
                    loginView.lblMessage.setText(loginMessages.getString("Congratulations"));
                    loginView.lblMessage.setTextFill(Color.GREEN);
                    loginView.stgLoginForm.close();
                    validLogin = true;
                    LOGGER.info(strCheckUser + " has logged in.");
                    loginView.stgLoginForm.close();

                } else {
                    loginView.lblMessage.setText(loginMessages.getString("Incorrect_user_password"));
                    loginView.lblMessage.setTextFill(Color.RED);
                    LOGGER.info(strCheckUser + " has failed log in.");
                }
                loginView.txtUserName.setText("");
                loginView.txtPassword.setText("");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Empty input boolean. using lambda functional interface for power ease of use and concise code
     *
     * @return the boolean
     */
    private boolean emptyInput() {

        try {
            TextFieldIsEmpty tstText = new TextFieldIsEmpty();
            if (tstText.emptyValue.test(loginView.txtUserName)) {
                util.showAlert(loginMessages.getString("Enter_User_Name"));
                loginView.txtUserName.requestFocus();
                return true;

            }
            if (tstText.emptyValue.test(loginView.txtPassword)) {
                util.showAlert(loginMessages.getString("Enter_Password"));
                loginView.txtPassword.requestFocus();
                return true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return false;
    }


}
