package com.rxjava;

import com.rxjava.utility.GateBasedSynchronization;
import com.rxjava.utility.datasets.FibonacciSequence;
import com.rxjava.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulersExample {

    private final static Logger log = LoggerFactory.getLogger(SchedulersExample.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Our base observable for this example will be a FibonacciSequence with 10 numbers.
        Observable<Long> fibonacciObservable = FibonacciSequence.create(10)
                .doOnSubscribe( disposable -> {
                    log.info("fibonacciObservable::onSubscribe");
                });

        // -----------------------------------------------------------------------------------------

        // First, let's look at subscription with no threading modification.
        fibonacciObservable.subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Scan the numbers on the computation thread pool
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // SubscribeOn example illustrating how first SubscribeOn wins.
        fibonacciObservable
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io()) // This will be ignored.  subscribeOn is always first come, first served.
                .subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Illustrate how observeOn's position effects which scheduler is used.
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // Illustrate how observeOn's position alters the scheduler that is
        // used for the observation portion of the code.
        fibonacciObservable
                .doOnSubscribe(d -> log.info("Subscribed!")) //Logged on NewThread Pool
                .doOnDispose(() -> log.info("Disposed"))
                .doOnNext(n -> log.info("Next is {}", n)) //Logged on NewThread Pool
                .doOnComplete(() -> log.info("Completed!"))
                .observeOn(Schedulers.computation())
                .map(x -> {
                    log.info("Number is {}", x); //This will run on on ComputationThread
                    return x * 2;
                })
                // The location of subscribeOn doesn't matter.
                // First subscribeOn always wins.
                .subscribeOn(Schedulers.newThread())

                // the last observeOn takes precedence.
                .observeOn(Schedulers.io())

                .subscribe(new DemoSubscriber<>(gate)); //Any logging by observer is on io thread

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        System.exit(0);
    }
}
