package com.jimmydonlogan.model;

import java.sql.Timestamp;


/**
 *  created by James Logan class to create for holding appointment record
 */
public class Appointment {

    private int appointment_ID;
    private String name;
    private String title;
    private String description;
    private String location;
    private String typeOfAppointment;
    private Timestamp start;
    private Timestamp end;
    private java.util.Date dateStart;
    private java.util.Date dateEnd;
    private int cust_id;
    private int user_ID;
    private int contactId;
    private String contactName;



     public Appointment()
     {

     }
    public int getAppointment_ID() {
        return appointment_ID;
    }

    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTypeOfAppointment() {
        return typeOfAppointment;
    }

    public void setTypeOfAppointment(String apmttype) {
        this.typeOfAppointment = apmttype;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }









    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }



    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCust_id() {
        return cust_id;
    }


    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }






    public java.util.Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(java.util.Date dateStart) {
        this.dateStart = dateStart;
    }

    public java.util.Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(java.util.Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
