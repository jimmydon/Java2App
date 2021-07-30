
package com.jimmydonlogan.view;

import com.jimmydonlogan.model.BuildView;
import com.jimmydonlogan.model.Appointment;
import com.jimmydonlogan.model.MonthYear;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Date;

/**
 * created by James Logan creates the GUI for viewing Appointment data
 */
public class AppointmentView implements BuildView {

    public final TableView<Appointment> tblAppointment = new TableView<>();

    public final Button btnAddAppointment = new Button("Add Appointment");
    public final Button btnModifyAppointment = new Button("Modify Appointment");
    public final Button btnDeleteAppointment = new Button("Delete Appointment");
    public final RadioButton rdbWeek= new RadioButton("Appointments by week");
    public final RadioButton rdbMonth= new RadioButton("Appointments by month.");
    public final RadioButton rdbAll= new RadioButton("All Appointments.");

    public final ToggleGroup rdGrpSelectTableView = new ToggleGroup();
    public final ComboBox<MonthYear> cmbxMonths= new ComboBox<>();
    public final DatePicker dtpWeeks= new DatePicker();

    public VBox buildTable() {
        //Scene scene = new Scene(new Group(),200,100, Color.GREY);


        HBox hbTop = new HBox();
        hbTop.setSpacing(20);



        Text txtRadioSelectTitle = new Text("Select Appointment view.");
        txtRadioSelectTitle.setId("textAppCss");

        Tooltip tooltipMonth= new Tooltip("Appointments by month");
        rdbMonth.setTooltip(tooltipMonth);


        Tooltip tooltipWeek= new Tooltip("Appointments by week");
        rdbWeek.setTooltip(tooltipWeek);
        Tooltip tooltipAll= new Tooltip("All Appointments");
        rdbAll.setTooltip(tooltipAll);
        rdbAll.setSelected(true);

        rdbMonth.setToggleGroup(rdGrpSelectTableView);
        rdbWeek.setToggleGroup(rdGrpSelectTableView);
        rdbAll.setToggleGroup(rdGrpSelectTableView);

        hbTop.setSpacing(10);
        hbTop.getChildren().addAll(txtRadioSelectTitle,rdbWeek,rdbMonth,rdbAll);

        HBox hbMonthWeek = new HBox();
        hbMonthWeek.setSpacing(20);
        hbMonthWeek.getChildren().addAll(dtpWeeks,cmbxMonths);






        VBox vCustomerBox = new VBox();


        buildTableOfAppointments();


        btnAddAppointment.setId("btnAppCss");
        btnModifyAppointment.setId("btnAppCss");
        btnDeleteAppointment.setId("btnAppCss");


        vCustomerBox.setSpacing(5);
        vCustomerBox.setPadding(new Insets(10, 0, 0, 10));
        HBox hbAddModDel = new HBox();
        hbAddModDel.setSpacing(20);
        hbAddModDel.getChildren().addAll(btnAddAppointment, btnModifyAppointment, btnDeleteAppointment);
        vCustomerBox.getChildren().addAll(hbTop,hbMonthWeek, tblAppointment, hbAddModDel);


        return vCustomerBox;

    }



    private void buildTableOfAppointments() {

        TableColumn<Appointment,Integer>  colApmntID =new TableColumn<>("Appointment_ID");   // new TableColumn("Appointment_ID");
        TableColumn<Appointment,Integer>  colCustID = new TableColumn<>("Cust ID");

        TableColumn<Appointment,String> colName = new TableColumn<>("Name");
        TableColumn<Appointment,String> colTitle = new TableColumn<>("Title");
        TableColumn<Appointment,String>  colType = new TableColumn<>("Type");
        TableColumn<Appointment,String>  colDesc = new TableColumn<>("Description");
        TableColumn<Appointment, Date> colStart = new TableColumn<>("Start");
        TableColumn<Appointment, Date>  colEnd = new TableColumn<>("End");
        TableColumn<Appointment,String> colLocation= new TableColumn<>("Location");
        TableColumn<Appointment,String> colContact = new TableColumn<>("Contact");


        colApmntID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
        colCustID.setCellValueFactory(new PropertyValueFactory<>("cust_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeOfAppointment"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));


        tblAppointment.getColumns().addAll(colApmntID, colTitle, colDesc, colLocation, colContact, colType, colStart, colEnd, colCustID, colName);


    }

}

