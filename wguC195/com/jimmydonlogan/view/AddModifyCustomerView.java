package com.jimmydonlogan.view;

import com.jimmydonlogan.model.*;
import com.jimmydonlogan.util.AddCountryDivContactsToObsLst;
import com.jimmydonlogan.util.CountryConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The type Add modify customer view.
 */
public class AddModifyCustomerView {

    /**
     * The constant txtName.
     */
    public static final TextField txtName = new TextField();
    /**
     * The constant txtAddress.
     */
    public static final TextField txtAddress = new TextField();
    /**
     * The constant txtPostalCode.
     */
    public static final TextField txtPostalCode = new TextField();
    /**
     * The constant txtPhone.
     */
    public static final TextField txtPhone = new TextField();
    /**
     * The constant cmbxCountries.
     */
    public static final ComboBox<Country> cmbxCountries = new ComboBox<>();
    /**
     * The constant cmbxFirstLvlDiv.
     */
    public static final ComboBox<FirstLevelDivision> cmbxFirstLvlDiv = new ComboBox<>();
    /**
     * The constant custid.
     */
    public static int custid = 0;
    /**
     * The Stg add customer form.
     */
    public final Stage stgAddCustomerForm;
    /**
     * The Btn cancel.
     */
    private final Button btnCancel;
    /**
     * The Btn save.
     */
    private final Button  btnSave;
    /**
     * The Title.
     */
    private String title;

    /**
     * Instantiates a new Add modify customer view.
     *
     * @param addOrModify the add or modify
     * @param btnSave     the btn save
     * @param btnCancel   the btn cancel
     */
    public AddModifyCustomerView(AddModify addOrModify,Button  btnSave,Button  btnCancel) {
        try {

            title = "Modify Customer";
            if (addOrModify == AddModify.ADD)
                title = "Add Customer";

            stgAddCustomerForm = new Stage();
            stgAddCustomerForm.initModality(Modality.APPLICATION_MODAL);
            cmbxCountries.setConverter(new CountryConverter());
            cmbxFirstLvlDiv.setConverter(new AddCountryDivContactsToObsLst.FirstLevelDivisionConverter());
        } finally {
            this.btnCancel=btnCancel;
            this.btnSave=btnSave;

        }


    }

    /**
     * Build add customer form.
     */
    public void buildAddCustomerForm() {


        try {
            stgAddCustomerForm.setTitle(title);


            Text txtTitle = new Text(title);
            txtTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


            GridPane grid = new GridPane();
            //grid.setId("root");
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10, 10, 10, 10));

            Label lblID = new Label("Cust ID:");
            grid.add(lblID, 0, 0);
            TextField txtID = new TextField();
            txtID.setText("Auto Gen - Disabled");
            txtID.setDisable(true);
            grid.add(txtID, 1, 0);

            Label lblName = new Label("Name:");
            grid.add(lblName, 0, 1);

            txtName.setPromptText("Please enter Customer Name");
            grid.add(txtName, 1, 1);

            Label lblCountry = new Label("Country");
            grid.add(lblCountry,0,2);
            grid.add(cmbxCountries,1,2);
            Label lblDivision = new Label("Division");
            grid.add(lblDivision,2,2);
            grid.add(cmbxFirstLvlDiv,3,2);

            Label lblAddress = new Label("Address");
            grid.add(lblAddress, 0, 3);

            txtAddress.setPromptText("Please enter Customer Address");
            grid.add(txtAddress, 1, 3);


            Label lblPostalCode = new Label("Postal Code");
            grid.add(lblPostalCode, 0, 4);

            txtPostalCode.setPromptText("Please enter Postal Code");
            grid.add(txtPostalCode, 1, 4);


            Label lblPhone = new Label("Phone");
            grid.add(lblPhone, 0, 5);

            txtPhone.setPromptText("xxx-xxx-xxxx");
            grid.add(txtPhone, 1, 5);


            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.CENTER);
            hbBtn.getChildren().add(btnSave);
            hbBtn.getChildren().add(btnCancel);

            grid.add(hbBtn, 1, 7);


            VBox vboxlayout = new VBox(40);


            vboxlayout.getChildren().addAll(grid);

            vboxlayout.setAlignment(Pos.TOP_LEFT);
            //Add ID's to Nodes
            btnSave.setId("btnAppCss");
            //vboxlayout.setId("root");
            btnCancel.setId("btnAppCss");
            txtTitle.setId("text");
            Scene sceneAddCustomer = new Scene(vboxlayout, 500, 450);
            sceneAddCustomer.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/app.css")).toExternalForm());
            stgAddCustomerForm.setScene(sceneAddCustomer);

            stgAddCustomerForm.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
