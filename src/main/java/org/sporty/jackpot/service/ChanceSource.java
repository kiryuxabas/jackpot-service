package org.sporty.jackpot.service;

import java.math.BigDecimal;

@FunctionalInterface
public interface ChanceSource {

    BigDecimal next();
}
