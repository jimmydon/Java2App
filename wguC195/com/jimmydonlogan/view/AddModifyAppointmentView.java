package com.jimmydonlogan.view;

import com.jimmydonlogan.model.*;

import java.time.format.DateTimeFormatter;
import java.util.Objects;



import com.jimmydonlogan.util.ContactConverter;
import com.jimmydonlogan.util.AppointmentTimeSpinner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The type Add modify appointment view.
 */
public class AddModifyAppointmentView {

    /**
     * The constant txtApmntTitle.
     */
    public static final TextField txtApmntTitle = new TextField();
    /**
     * The constant txtDescription.
     */
    public static final TextField txtDescription = new TextField();
    /**
     * The constant txtLocation.
     */
    public static final TextField txtLocation = new TextField();
    /**
     * The constant txtType.
     */
    public static final TextField txtType = new TextField();

    /**
     * The constant apmntID.
     */
    public static int apmntID;
    /**
     * The Cmbx contacts.
     */
    public final ComboBox<Contact> cmbxContacts = new ComboBox<>();

    /**
     * The Cmbx customers.
     */
    public static ComboBox<Customer> cmbxCustomers;

    /**
     * The Dtp start.
     */
    public final DatePicker dtpStart= new DatePicker();
    /**
     * The Dtp end.
     */
    public final DatePicker dtpEnd= new DatePicker();
    /**
     * The Spn start.
     */
    public final AppointmentTimeSpinner spnStart= new AppointmentTimeSpinner();
    /**
     * The Spn end.
     */
    public final AppointmentTimeSpinner spnEnd= new AppointmentTimeSpinner();

    /**
     * The Stg add appointment form.
     */
    public final Stage stgAddAppointmentForm;
    /**
     * The Title.
     */
    private String title;
    /**
     * The Btn cancel.
     */
    private final Button btnCancel;
    /**
     * The Btn save.
     */
    private final Button btnSave;
    /**
     * The Add or modify.
     */
    public AddModify addOrModify=AddModify.MODIFY;


    /**
     * Instantiates a new Add modify appointment view.
     *
     * @param btnSave       the btn save
     * @param btnCancel     the btn cancel
     * @param cmbxCustomers the cmbx customers
     */
    public AddModifyAppointmentView(Button btnSave, Button btnCancel,ComboBox<Customer> cmbxCustomers) {
        try {

            AddModifyAppointmentView.cmbxCustomers =cmbxCustomers;


            stgAddAppointmentForm = new Stage();
            stgAddAppointmentForm.initModality(Modality.APPLICATION_MODAL);
            cmbxContacts.setConverter(new ContactConverter());
            initSpinners();
        } finally {
            this.btnCancel = btnCancel;
            this.btnSave = btnSave;

        }


    }

    /**
     * Build add appointment form.
     */
    public void buildAddAppointmentForm() {

        try {
            title = "Modify Appointment";
            if (addOrModify == AddModify.ADD)
                title = "Add Appointment";
            stgAddAppointmentForm.setTitle(title);


            GridPane grid = new GridPane();
            //grid.setId("root");
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10, 10, 10, 10));


            Label lblID = new Label("Appointment ID");
            grid.add(lblID, 0, 0);
            TextField txtID = new TextField();
            txtID.setText("Auto Gen - Disabled");
            txtID.setDisable(true);
            grid.add(txtID, 1, 0);

            Label lblCustomer = new Label("Customer");
            grid.add(lblCustomer,0,1);
            grid.add(cmbxCustomers,1,1);


            Label lblTitle = new Label("Title:");
            grid.add(lblTitle, 0, 2);

            txtApmntTitle.setPromptText("Please enter a Title");
            grid.add(txtApmntTitle, 1, 2);

            Label lblCountry = new Label("Contact");
            grid.add(lblCountry, 0, 3);
            grid.add(cmbxContacts, 1, 3);


            Label lblDescription = new Label("Description");
            grid.add(lblDescription, 0, 4);

            txtDescription.setPromptText("Please enter a Description");
            grid.add(txtDescription, 1, 4);


            Label lblLocation = new Label("Location");
            grid.add(lblLocation, 0, 5);

            txtLocation.setPromptText("Please enter a Location");
            grid.add(txtLocation, 1, 5);


            Label lblType = new Label("Type");
            grid.add(lblType, 0, 6);

            txtType.setPromptText("Please enter a Type");
            grid.add(txtType, 1, 6);

            Label lblStart = new Label("Start(Date HR MIN)");
            grid.add(lblStart,0,7);
            grid.add(dtpStart,1,7);
            grid.add(spnStart,2,7);


            Label lblEnd = new Label("End(HR MIN)");
            grid.add(lblEnd,0,8);
            grid.add(dtpEnd,1,8);
            grid.add(spnEnd,2,8);

            //dtpEnd.setDisable(true);

            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.CENTER);
            hbBtn.getChildren().add(btnSave);
            hbBtn.getChildren().add(btnCancel);


            grid.add(hbBtn, 1, 9);


            VBox vboxlayout = new VBox(40);

            vboxlayout.getChildren().addAll(grid);

            vboxlayout.setAlignment(Pos.TOP_LEFT);
            //Add ID's to Nodes
            btnSave.setId("btnAppCss");
            //vboxlayout.setId("root");
            btnCancel.setId("btnAppCss");
            //txtTitle.setId("text");
            Scene sceneAddAppointment = new Scene(vboxlayout, 500, 450);
            sceneAddAppointment.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/app.css")).toExternalForm());
            stgAddAppointmentForm.setScene(sceneAddAppointment);

            stgAddAppointmentForm.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Init spinners. using lambda for spinners;concise code,avoid using bulky anonymous class implementation
     */
    public void initSpinners()
    {


        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            spnStart.valueProperty().addListener((obs, oldTime, newTime) ->
                    System.out.println(formatter.format(newTime)));
            spnEnd.valueProperty().addListener((obs, oldTime, newTime) ->
                    System.out.println(formatter.format(newTime)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
