package com.rxjava;

import com.rxjava.utility.GateBasedSynchronization;
import com.rxjava.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BackPressure {

    private static Logger log = LoggerFactory.getLogger(BackPressure.class);

    public static void main(String[] args) {

        // Synchronization helper
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an ever-repeating number counter that counts from 1 to 1 billion.
        Observable<Integer> rangeOfNumbers = Observable.range(1 , 1_000_000_000)
                .repeat()
                .doOnNext( nextInt -> log.info("emitting int {}", nextInt))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread());

        // Create a DemoSubscriber with a slight delay of 10ms.
        // This should make the rangeOfNumber's emission far outpace
        // the subscriber.
        DemoSubscriber<Integer> demoSubscriber = new DemoSubscriber<>(
                10L, TimeUnit.MILLISECONDS,
                gate, "onError", "onComplete"
        );

        // Subscribe to start the numbers flowing.
        rangeOfNumbers.subscribe(demoSubscriber);

        // Wait for things to finish
        gate.waitForAny("onError", "onComplete");

        System.exit(0);
    }
}
