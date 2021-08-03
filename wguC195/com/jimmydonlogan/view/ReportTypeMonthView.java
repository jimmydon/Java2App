

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
 * The type Report type month view.
 */
public class ReportTypeMonthView {

    /**
     * The Tbl type month.
     */
    public final TableView<ReportTypeMonth> tblTypeMonth = buildTableOfTypeMonth();


    /**
     * The Stg reports.
     */
    public final Stage stgReports;
    /**
     * The Title.
     */
    private String title;


    /**
     * Instantiates a new Report type month view.
     */
    public ReportTypeMonthView() {


        try {
            title = "Customer Appointments by Type and Month";

            stgReports = new Stage();
            stgReports.initModality(Modality.APPLICATION_MODAL);
        } finally {

        }


    }

    /**
     * Build report type month form.
     */
    public void buildReportTypeMonthForm() {


        try {
            stgReports.setTitle(title);

            VBox vboxlayout = new VBox(tblTypeMonth);
            Scene sceneAddReport = new Scene(vboxlayout, 500, 450);
            sceneAddReport.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/app.css")).toExternalForm());
            stgReports.setScene(sceneAddReport);

            stgReports.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Build table of type month table view.
     *
     * @return the table view
     */
    TableView<ReportTypeMonth> buildTableOfTypeMonth() {


        try {
            TableColumn<ReportTypeMonth, String> colType = new TableColumn<>("Type");

            TableColumn<ReportTypeMonth, String> colMonth = new TableColumn<>("Month");
            TableColumn<ReportTypeMonth, Integer> colTotal = new TableColumn<>("Total");

            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
            colTotal.setCellValueFactory(new PropertyValueFactory<>("totalMonth"));


            final var tbl = new TableView();
            tbl.getColumns().addAll(colType, colMonth, colTotal);

            return tbl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
