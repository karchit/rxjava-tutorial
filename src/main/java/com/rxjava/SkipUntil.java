package com.rxjava;

import com.rxjava.utility.ThreadHelper;
import com.rxjava.utility.datasets.GreekAlphabet;
import com.rxjava.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SkipUntil {

    private final static Logger log = LoggerFactory.getLogger(SkipUntil.class);

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it FOREVER!
        Observable<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishObservable()
                .repeat();

        // We want to skip until the "interval" Observable emits in 2 seconds.
        greekAlphabet.skipUntil( Observable.interval(2, 10, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new DemoSubscriber<>());

        // Wait for 3 seconds before terminating the process.
        ThreadHelper.sleep(3, TimeUnit.SECONDS);

        System.exit(0);
    }
}
