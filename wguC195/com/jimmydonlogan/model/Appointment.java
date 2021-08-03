package com.jimmydonlogan.model;

import java.sql.Timestamp;


/**
 * The type Appointment.
 */
public class Appointment {

    /**
     * The Appointment id.
     */
    private int appointment_ID;
    /**
     * The Name.
     */
    private String name;
    /**
     * The Title.
     */
    private String title;
    /**
     * The Description.
     */
    private String description;
    /**
     * The Location.
     */
    private String location;
    /**
     * The Type of appointment.
     */
    private String typeOfAppointment;
    /**
     * The Start.
     */
    private Timestamp start;
    /**
     * The End.
     */
    private Timestamp end;
    /**
     * The Date start.
     */
    private java.util.Date dateStart;
    /**
     * The Date end.
     */
    private java.util.Date dateEnd;
    /**
     * The Cust id.
     */
    private int cust_id;
    /**
     * The User id.
     */
    private int user_ID;
    /**
     * The Contact id.
     */
    private int contactId;
    /**
     * The Contact name.
     */
    private String contactName;


    /**
     * Instantiates a new Appointment.
     */
    public Appointment()
     {

     }

    /**
     * Gets appointment id.
     *
     * @return the appointment id
     */
    public int getAppointment_ID() {
        return appointment_ID;
    }

    /**
     * Sets appointment id.
     *
     * @param appointment_ID the appointment id
     */
    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location.
     *
     * @param location the location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets type of appointment.
     *
     * @return the type of appointment
     */
    public String getTypeOfAppointment() {
        return typeOfAppointment;
    }

    /**
     * Sets type of appointment.
     *
     * @param apmttype the apmttype
     */
    public void setTypeOfAppointment(String apmttype) {
        this.typeOfAppointment = apmttype;
    }

    /**
     * Gets start.
     *
     * @return the start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * Sets start.
     *
     * @param start the start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * Gets end.
     *
     * @return the end
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * Sets end.
     *
     * @param end the end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }


    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUser_ID() {
        return user_ID;
    }

    /**
     * Sets user id.
     *
     * @param user_ID the user id
     */
    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }


    /**
     * Gets contact id.
     *
     * @return the contact id
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets contact id.
     *
     * @param contactId the contact id
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets cust id.
     *
     * @return the cust id
     */
    public int getCust_id() {
        return cust_id;
    }


    /**
     * Sets cust id.
     *
     * @param cust_id the cust id
     */
    public void setCust_id(int cust_id) {
        this.cust_id = cust_id;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    /**
     * Gets date start.
     *
     * @return the date start
     */
    public java.util.Date getDateStart() {
        return dateStart;
    }

    /**
     * Sets date start.
     *
     * @param dateStart the date start
     */
    public void setDateStart(java.util.Date dateStart) {
        this.dateStart = dateStart;
    }

    /**
     * Gets date end.
     *
     * @return the date end
     */
    public java.util.Date getDateEnd() {
        return dateEnd;
    }

    /**
     * Sets date end.
     *
     * @param dateEnd the date end
     */
    public void setDateEnd(java.util.Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
