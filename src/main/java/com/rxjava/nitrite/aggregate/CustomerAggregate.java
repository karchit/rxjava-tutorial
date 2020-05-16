package com.rxjava.nitrite.aggregate;

import com.rxjava.nitrite.entity.Customer;
import com.rxjava.nitrite.entity.CustomerAddress;
import com.rxjava.nitrite.entity.Product;

import java.util.ArrayList;

public class CustomerAggregate {

    private Customer customer;
    private ArrayList<CustomerAddress> addresses;
    private ArrayList<Product> ownedProducts;

    public CustomerAggregate() {
        this.addresses = new ArrayList<>();
        this.ownedProducts = new ArrayList<>();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer( Customer c ) {
        customer = c;
    }

    public ArrayList<CustomerAddress> getAddresses() {
        return addresses;
    }

    public void addCustomerAddress( CustomerAddress a ) {
        this.addresses.add(a);
    }

    public ArrayList<Product> getOwnedProducts() {
        return ownedProducts;
    }

    public void addOwnedProduct( Product product ) {
        this.ownedProducts.add(product);
    }

    @Override
    public String toString() {
        return "CustomerAggregate{" +
                "customer=" + customer +
                ", addresses=" + addresses +
                ", ownedProducts=" + ownedProducts +
                '}';
    }
}
