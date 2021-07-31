package com.ddx.chiamon.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author ddx
 */
public class Utils {

    public static double RoundResult(double value, int places) {
        return RoundResult(value, places, RoundingMode.HALF_UP);
    }

    public static double RoundResult(double value, int places, RoundingMode mode) {
        return new BigDecimal(value).setScale(places, mode).doubleValue();
    }

}