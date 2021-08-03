package com.jimmydonlogan.model;

/**
 * The type Customer.
 */
public class Customer{


    /**
     * The Division id.
     */
    private int division_id;
    /**
     * The Cust id.
     */
    private  int cust_id;
    /**
     * The Name.
     */
    private  String name;
    /**
     * The Address.
     */
    private  String address;
    /**
     * The Postal code.
     */
    private  String postal_code;
    /**
     * The Phone.
     */
    private String phone;


    /**
     * Instantiates a new Customer.
     */
    public Customer()
    {


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
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public String getPostal_code() {
        return postal_code;
    }

    /**
     * Sets postal code.
     *
     * @param postal_code the postal code
     */
    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets division id.
     *
     * @return the division id
     */
    public int getDivision_id() {
        return division_id;
    }

    /**
     * Sets division id.
     *
     * @param division_id the division id
     */
    public void setDivision_id(int division_id) {
        this.division_id = division_id;
    }
}

