package com.rxjava;

import com.rxjava.utility.ThreadHelper;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;

import java.util.concurrent.TimeUnit;

public class Scratchpad {

    public static void main(String[] args) {
        Observable<Observable<String>> timeIntervals =
                Observable.interval(1, TimeUnit.SECONDS)
                        .map(ticks -> Observable.interval(100, TimeUnit.MILLISECONDS)
                                .map(innerInterval -> "outer: " + ticks + " - inner: " + innerInterval));
        Disposable flatmap = timeIntervals.flatMap(Functions.identity()).subscribe(System.out::println);
        ThreadHelper.sleep(3, TimeUnit.SECONDS);
        flatmap.dispose();
        System.out.println("\n\n---------------------------------");
        Observable.switchOnNext(timeIntervals).subscribe(System.out::println);
        ThreadHelper.sleep(3, TimeUnit.SECONDS);
    }
}
