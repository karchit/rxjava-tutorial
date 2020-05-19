package com.rxjava;

import com.rxjava.nitrite.NitriteTestDatabase;
import com.rxjava.nitrite.aggregate.CustomerAggregate;
import com.rxjava.nitrite.aggregate.CustomerAggregateOperations;
import com.rxjava.nitrite.dataaccess.CustomerAddressDataAccess;
import com.rxjava.nitrite.dataaccess.CustomerDataAccess;
import com.rxjava.nitrite.dataaccess.CustomerProductPurchaseHistoryDataAccess;
import com.rxjava.nitrite.datasets.NitriteCustomerDatabaseSchema;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceAggregation {

    private final static Logger log = LoggerFactory.getLogger(ServiceAggregation.class);

    public static void main(String[] args) {

        try {
            NitriteCustomerDatabaseSchema schema = new NitriteCustomerDatabaseSchema();
            try(NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

                // Create an Observable of Observable<Object>.  Each entry will be one of the
                // Observables for our database that we want to run concurrently.
                Observable<Observable<Object>> ioFetchStreams = Observable.fromArray(

                        // Fetch the customer information
                        CustomerDataAccess.select(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                                .subscribeOn(Schedulers.io()).cast(Object.class),

                        // Fetch any address information associated with the customer.
                        CustomerAddressDataAccess.select(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                                .subscribeOn(Schedulers.io()).cast(Object.class),

                        // Obtain the customer's product history.
                        CustomerProductPurchaseHistoryDataAccess.selectOwnedProducts(testDatabase.getNitriteDatabase(),
                                schema.Customer1UUID)
                                .subscribeOn(Schedulers.io()).cast(Object.class)
                );

                // This time we are going to use flatMap to perform the concurrent processing.
                // We restrict the amount of concurrency to 2 simultaneous
                // subscriptions.  We just want to pass through the Observable<Object>, so
                // we use the RxJava 2 built-in identity function.
                Observable<Object> customerAggregateStream = ioFetchStreams.flatMap((Function) Functions.identity(),
                        2);

                // Let's assemble a CustomerAggregate.
                Single<CustomerAggregate> customerAggregate = CustomerAggregateOperations.aggregate(customerAggregateStream);

                // Get the aggregated customer data!
                CustomerAggregate finalCustomer = customerAggregate.blockingGet();

                log.info(finalCustomer.toString());
            }
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }

        System.exit(0);
    }
}
