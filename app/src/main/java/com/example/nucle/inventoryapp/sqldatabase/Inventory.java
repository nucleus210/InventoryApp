package com.example.nucle.inventoryapp.sqldatabase;

public class Inventory {

    //private variables
    private long   _id;
    private String mImage;
    private String mProductName;
    private float  mProductPrice;
    private int    mQuantity;
    private String mSupplierName;
    private String mSupplierContact;
    private float  mDiscount;
    private String mProductDescription;

    // Empty constructor
    public Inventory() { }

    /**
     * Add constructor for this class and give arguments for constructor:
     */
    public Inventory(long   id,
                     String image,
                     String productName,
                     float  productPrice,
                     int    quantity,
                     String supplierName,
                     String supplierContact,
                     float  discount,
                     String notes) {
        this.setID(id);
        this.setImage(image);
        this.setProductName(productName);
        this.setProductPrice(productPrice);
        this.setQuantity(quantity);
        this.setSupplierName(supplierName);
        this.setSupplierContact(supplierContact);
        this.setDiscount(discount);
        this.setProductDescription(notes);
    }

    // getters and setters methods

    public long getID() { return _id; }

    public void setID(long _id) { this._id = _id; }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String mProductName) {
        this.mProductName = mProductName;
    }

    public float getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(float mProductPrice) {
        this.mProductPrice = mProductPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String b) {
        this.mImage = b;
    }

    public String getSupplierName() {
        return mSupplierName;
    }

    public void setSupplierName(String mSupplierName) {
        this.mSupplierName = mSupplierName;
    }

    public String getSupplierContact() {
        return mSupplierContact;
    }

    public void setSupplierContact(String mSupplierContact) { this.mSupplierContact = mSupplierContact; }

    public float getDiscount() {
        return mDiscount;
    }

    public void setDiscount(float mDiscount) {
        this.mDiscount = mDiscount;
    }

    public String getProductDescription() { return mProductDescription;}

    public void setProductDescription(String mProductDescription) {
        this.mProductDescription = mProductDescription;
    }
}