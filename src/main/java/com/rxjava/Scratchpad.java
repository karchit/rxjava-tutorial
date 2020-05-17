package com.rxjava;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

public class Scratchpad {

    public static void main(String[] args){
        Observable<Long> newsRefreshes = Observable.interval(100, TimeUnit.SECONDS);
        Observable<Long> weatherRefreshes = Observable.interval(50, TimeUnit.SECONDS);
        Observable.combineLatest(newsRefreshes, weatherRefreshes,
                (newsRefreshTimes, weatherRefreshTimes) ->
                        "Refreshed news " + newsRefreshTimes + " times and weather " + weatherRefreshTimes)
                .subscribe(x -> {
                    System.out.println(x);
                });
    }
}
