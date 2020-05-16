package com.rxjava;

import com.rxjava.utility.datasets.GreekAlphabet;
import com.rxjava.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;

import java.util.concurrent.atomic.AtomicInteger;

public class TakeWhile {

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Observable<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishObservable()
                .repeat(3);

        // Create a counter so we can count the number of times we have
        // seen the "alpha" letter.
        AtomicInteger numberOfAlphas = new AtomicInteger();

        // We want to take letters until we have seen "alpha" 2 times.
        // On the second time, we stop caring about the stream.
        greekAlphabet.takeWhile( nextLetter -> !nextLetter.equals("alpha") || numberOfAlphas.incrementAndGet() != 2)
                .subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
