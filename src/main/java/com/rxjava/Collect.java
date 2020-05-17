package com.rxjava;

import com.rxjava.utility.datasets.GreekAlphabet;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

public class Collect {

    private final static Logger log = LoggerFactory.getLogger(Collect.class);

    public static void main(String[] args) {

        // collect is useful to combine a stream of events into a single event
        // or object.  In this case, we will make an ArrayList<String> that contains
        // all of the greek letters.
        ArrayList<String> greekLetterArray = GreekAlphabet.greekAlphabetInGreekObservable()
                .collect(
                        // What is the initial state?  In this case a blank ArrayList
                        (Callable<ArrayList<String>>) ArrayList::new,

                        // The collection function.  Put the greekLetter into the arraylist.
                        ArrayList::add)

                // We block and get the value out of the Single that was returned
                // by the collect operation.
                .blockingGet();

        // Emit each letter
        greekLetterArray.forEach(
                log::info
        );

        Observable.just("Kirk", "Spock", "Chekov", "Sulu")
                .collect(() -> new StringJoiner(" \uD83D\uDD96 "), StringJoiner::add)
                .subscribe(System.out::println);


        System.exit(0);
    }
}
