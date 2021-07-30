package com.jimmydonlogan.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginView {
    public final TextField txtUserName = new TextField();
    public final PasswordField txtPassword = new PasswordField();
    public final Button btnLogin = new Button("Login");
    public final Label lblMessage = new Label();
    public final Stage stgLoginForm = new Stage();

    public void buildView() {



        stgLoginForm.initModality(Modality.APPLICATION_MODAL);

        stgLoginForm.setTitle("Login");



        BorderPane bpLogin = new BorderPane();
        bpLogin.setPadding(new Insets(10, 50, 50, 50));

        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));

        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label("Username");

        Label lblPassword = new Label("Password");




        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtUserName, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtPassword, 1, 1);
        gridPane.add(btnLogin, 2, 1);
        gridPane.add(lblMessage, 1, 2);


        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        gridPane.setEffect(r);

        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);

        //Adding text and DropShadow effect to it
        Text txtLogin = new Text("Appointment Login");
        txtLogin.setFont(Font.font("Courier New", FontWeight.BOLD, 28));
        txtLogin.setEffect(dropShadow);

        //Adding text to HBox
        hb.getChildren().add(txtLogin);

        //Add ID's to Nodes
        bpLogin.setId("bp");
        btnLogin.setId("btnLogin");
        gridPane.setId("root");

        txtLogin.setId("textAppCss");



        //Add HBox and GridPane layout to BorderPane Layout
        bpLogin.setTop(hb);
        bpLogin.setCenter(gridPane);

        Scene scene = new Scene(bpLogin);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/app.css")).toExternalForm());
        stgLoginForm.setScene(scene);
        stgLoginForm.showAndWait();
    }

}
