package ex.utils;

import java.math.BigDecimal;

public class SimpleUtils 
{
    public static float round(float d, int decimalPlace) 
    {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
