package com.rxjava;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubjectExample {
    private final static Logger log = LoggerFactory.getLogger(SubjectExample.class);

    public static void main(String[] args){
        PublishSubject<Long> publishSubject = PublishSubject.create();

        TestObserver<Long> testObserver1 = publishSubject.observeOn(Schedulers.newThread())
                .doOnNext(e -> log.info("{} on new thread", e))
                .test();

        TestObserver<Long> testObserver2 = publishSubject.observeOn(Schedulers.io())
                .doOnNext(e -> log.info("{} on io thread", e))
                .test();

        publishSubject.onNext(1L);
        publishSubject.onNext(2L);
        publishSubject.onNext(3L);
        publishSubject.onNext(4L);
        publishSubject.onNext(5L);
        publishSubject.onNext(6L);
        publishSubject.onComplete();

        testObserver1.awaitTerminalEvent();
        testObserver2.awaitTerminalEvent();

    }
}
