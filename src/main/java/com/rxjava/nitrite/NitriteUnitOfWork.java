package com.rxjava.nitrite;

import org.dizitart.no2.Nitrite;

public interface NitriteUnitOfWork {
    void apply(Nitrite database) throws Exception;
}
