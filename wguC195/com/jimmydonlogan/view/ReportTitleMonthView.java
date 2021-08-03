package com.jimmydonlogan.view;

import com.jimmydonlogan.model.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * The type Report title month view.
 */
public class ReportTitleMonthView {

    /**
     * The Tbl title month.
     */
    public final TableView<ReportTitleMonth> tblTitleMonth = buildTableOfTitleMonth();


    /**
     * The Stg reports.
     */
    public final Stage stgReports;
    /**
     * The Title.
     */
    private String title;


    /**
     * Instantiates a new Report title month view.
     */
    public ReportTitleMonthView() {


        try {
            title = "Customer Appointments by Title and Month";

            stgReports = new Stage();
            stgReports.initModality(Modality.APPLICATION_MODAL);
        } finally {

        }


    }

    /**
     * Build report title month form.
     */
    public void buildReportTitleMonthForm() {


        try {
            stgReports.setTitle(title);

            VBox vboxlayout = new VBox(tblTitleMonth);
            Scene sceneAddReport = new Scene(vboxlayout, 500, 450);
            sceneAddReport.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/app.css")).toExternalForm());
            stgReports.setScene(sceneAddReport);

            stgReports.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Build table of title month table view.
     *
     * @return the table view
     */
    TableView<ReportTitleMonth> buildTableOfTitleMonth() {


        try {
            TableColumn<ReportTitleMonth, String> colTitle = new TableColumn<>("Title");

            TableColumn<ReportTitleMonth, String> colMonth= new TableColumn<>("Month");
            TableColumn<ReportTitleMonth, Integer> colTotal = new TableColumn<>("Total");

            colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("totalMonth"));


            final var tbl = new TableView();
            tbl.getColumns().addAll(colTitle, colMonth, colTotal);

            return tbl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
