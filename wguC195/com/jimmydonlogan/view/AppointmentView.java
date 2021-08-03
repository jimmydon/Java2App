
package com.jimmydonlogan.view;

import com.jimmydonlogan.model.BuildView;
import com.jimmydonlogan.model.Appointment;
import com.jimmydonlogan.model.Contact;
import com.jimmydonlogan.model.MonthYear;
import com.jimmydonlogan.util.ContactConverter;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Date;

/**
 * The type Appointment view.
 */
public class AppointmentView implements BuildView {

    /**
     * The Tbl appointment.
     */
    public final TableView<Appointment> tblAppointment = new TableView<>();

    /**
     * The Btn add appointment.
     */
    public final Button btnAddAppointment = new Button("Add Appointment");
    /**
     * The Btn modify appointment.
     */
    public final Button btnModifyAppointment = new Button("Modify Appointment");
    /**
     * The Btn delete appointment.
     */
    public final Button btnDeleteAppointment = new Button("Delete Appointment");
    /**
     * The Rdb week.
     */
    public final RadioButton rdbWeek = new RadioButton("Appointments by week");
    /**
     * The Rdb month.
     */
    public final RadioButton rdbMonth = new RadioButton("Appointments by month.");
    /**
     * The Rdb all.
     */
    public final RadioButton rdbAll = new RadioButton("All Appointments.");

    /**
     * The Rd grp select table view.
     */
    public final ToggleGroup rdGrpSelectTableView = new ToggleGroup();
    /**
     * The Cmbx months.
     */
    public final ComboBox<MonthYear> cmbxMonths = new ComboBox<>();
    /**
     * The Dtp weeks.
     */
    public final DatePicker dtpWeeks = new DatePicker();
    /**
     * The Btn report cust apmnts.
     */
    public final Button btnReportCustApmnts = new Button("Customer Appointments Report");
    /**
     * The Btn report contact.
     */
    public final Button btnReportContact = new Button("Click for Schedule for Contact");
    /**
     * The Btn report title month.
     */
    public final Button btnReportTitleMonth = new Button("Title Appointments Report");
    /**
     * The Cmbx rpt contacts.
     */
    public final ComboBox<Contact> cmbxRptContacts = new ComboBox<>();


    /**
     * Build table v box.
     *
     * @return the v box
     */
    public VBox buildTable() {


        try {
            HBox hbTop = new HBox();
            hbTop.setSpacing(20);


            Text txtRadioSelectTitle = new Text("Select Appointment view.");
            txtRadioSelectTitle.setId("textAppCss");

            Tooltip tooltipMonth = new Tooltip("Appointments by month");
            rdbMonth.setTooltip(tooltipMonth);


            Tooltip tooltipWeek = new Tooltip("Appointments by week");
            rdbWeek.setTooltip(tooltipWeek);
            Tooltip tooltipAll = new Tooltip("All Appointments");
            rdbAll.setTooltip(tooltipAll);
            rdbAll.setSelected(true);

            rdbMonth.setToggleGroup(rdGrpSelectTableView);
            rdbWeek.setToggleGroup(rdGrpSelectTableView);
            rdbAll.setToggleGroup(rdGrpSelectTableView);

            hbTop.setSpacing(10);
            hbTop.getChildren().addAll(txtRadioSelectTitle, rdbWeek, rdbMonth, rdbAll);

            HBox hbMonthWeek = new HBox();
            hbMonthWeek.setSpacing(20);
            hbMonthWeek.getChildren().addAll(dtpWeeks, cmbxMonths);


            VBox vCustomerBox = new VBox();


            buildTableOfAppointments();


            btnAddAppointment.setId("btnAppCss");
            btnModifyAppointment.setId("btnAppCss");
            btnDeleteAppointment.setId("btnAppCss");
            btnReportCustApmnts.setId("btnReportsCss");
            btnReportContact.setId("btnReportsCss");
            btnReportTitleMonth.setId("btnReportsCss");

            Tooltip toolReportTitleMonth= new Tooltip("Click for a Report of Appointments Titles per month");
            btnReportTitleMonth.setTooltip(toolReportTitleMonth);

            Tooltip tooltipReportCustApmnts = new Tooltip("Click for a Report of Customer Appointments");
            btnReportCustApmnts.setTooltip(tooltipReportCustApmnts);

            Tooltip tooltipReportContact = new Tooltip("Select Contact to get schedule.");
            cmbxRptContacts.setTooltip(tooltipReportContact);

            Tooltip tooltipBtnReportContact = new Tooltip("Click to get Schedule for Contact");
            btnReportContact.setTooltip(tooltipBtnReportContact );

            vCustomerBox.setSpacing(5);
            vCustomerBox.setPadding(new Insets(10, 0, 0, 10));

            HBox hbAddModDel = new HBox();
            hbAddModDel.setSpacing(20);
            hbAddModDel.getChildren().addAll(btnAddAppointment, btnModifyAppointment, btnDeleteAppointment);

            HBox hbContactReport = new HBox();


            cmbxRptContacts.setConverter(new ContactConverter());
            hbContactReport.getChildren().addAll( btnReportContact, cmbxRptContacts);

            VBox vReports = new VBox();

            vReports.setPadding(new Insets(10, 0, 0, 10));
            vReports.setSpacing(40);
            vReports.getChildren().addAll(btnReportCustApmnts, hbContactReport,btnReportTitleMonth);


            vCustomerBox.getChildren().addAll(hbTop, hbMonthWeek, tblAppointment, hbAddModDel, vReports);


            return vCustomerBox;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * Build table of appointments.
     */
    private void buildTableOfAppointments() {

        TableColumn<Appointment, Integer> colApmntID = new TableColumn<>("Appointment_ID");   // new TableColumn("Appointment_ID");
        TableColumn<Appointment, Integer> colCustID = new TableColumn<>("Cust ID");

        TableColumn<Appointment, String> colName = new TableColumn<>("Name");
        TableColumn<Appointment, String> colTitle = new TableColumn<>("Title");
        TableColumn<Appointment, String> colType = new TableColumn<>("Type");
        TableColumn<Appointment, String> colDesc = new TableColumn<>("Description");
        TableColumn<Appointment, Date> colStart = new TableColumn<>("Start");
        TableColumn<Appointment, Date> colEnd = new TableColumn<>("End");
        TableColumn<Appointment, String> colLocation = new TableColumn<>("Location");
        TableColumn<Appointment, String> colContact = new TableColumn<>("Contact");


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

