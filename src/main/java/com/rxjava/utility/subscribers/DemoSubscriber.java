package com.rxjava.utility.subscribers;

import com.rxjava.utility.GateBasedSynchronization;
import com.rxjava.utility.ThreadHelper;
import io.reactivex.observers.ResourceObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoSubscriber<TEvent> extends ResourceObserver<TEvent> {

    private static final Logger log = LoggerFactory.getLogger(DemoSubscriber.class);

    private final GateBasedSynchronization gate;
    private final String errorGateName;
    private final String completeGateName;

    private final long onNextDelayDuration;
    private final TimeUnit onNextDelayTimeUnit;
    private AtomicInteger atomicInteger;

    public DemoSubscriber() {
        this.gate = new GateBasedSynchronization();
        this.errorGateName = "onError";
        this.completeGateName = "onComplete";
        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
        this.atomicInteger = new AtomicInteger();
    }

    public DemoSubscriber(GateBasedSynchronization gate) {
        this.gate = gate;
        this.errorGateName = "onError";
        this.completeGateName = "onComplete";

        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
        this.atomicInteger = new AtomicInteger();
    }

    public DemoSubscriber(GateBasedSynchronization gate, String errorGateName, String completeGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.completeGateName = completeGateName;

        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
        this.atomicInteger = new AtomicInteger();
    }

    public DemoSubscriber(long onNextDelayDuration , TimeUnit onNextDelayTimeUnit, GateBasedSynchronization gate,
                          String errorGateName, String completeGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.completeGateName = completeGateName;

        this.onNextDelayDuration = onNextDelayDuration;
        this.onNextDelayTimeUnit = onNextDelayTimeUnit;
        this.atomicInteger = new AtomicInteger();
    }

    public GateBasedSynchronization getGates() {
        return this.gate;
    }

    @Override
    public void onNext(TEvent event) {
        log.info( "onNext - {}" , event == null ? "<NULL>" : event.toString());
        atomicInteger.incrementAndGet();
        // Drag our feet if requested to do so...
        if( onNextDelayDuration > 0 ) {
            ThreadHelper.sleep(onNextDelayDuration, onNextDelayTimeUnit);
        }
    }

    @Override
    public void onError(Throwable e) {
        log.error( "onError - {}" , e.getMessage());
        gate.openGate(errorGateName);
    }

    @Override
    public void onComplete() {
        log.info( "onComplete - {}", atomicInteger.get());
        gate.openGate(completeGateName);
    }
}
