package com.rxjava.nitrite.aggregate;

import com.rxjava.nitrite.entity.Customer;
import com.rxjava.nitrite.entity.CustomerAddress;
import com.rxjava.nitrite.entity.Product;
import io.reactivex.Observable;
import io.reactivex.Single;

public class CustomerAggregateOperations {

    public static Single<CustomerAggregate> aggregate(Observable<Object> customerPartObservable) {

        return customerPartObservable.collect(
                CustomerAggregate::new,
                (customerAggregate, nextObject) -> {
                    if( nextObject instanceof Customer) {
                        customerAggregate.setCustomer((Customer)nextObject);
                    }
                    else if( nextObject instanceof CustomerAddress) {
                        customerAggregate.addCustomerAddress((CustomerAddress)nextObject);
                    }
                    else if( nextObject instanceof Product) {
                        customerAggregate.addOwnedProduct((Product)nextObject);
                    }
                }
        );
    }
}
