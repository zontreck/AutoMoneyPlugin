package dev.zontreck.amp;

public class Configuration {
    public static double g_dPaymentAmount = 0.0;
    public static int g_iPaymentInterval = 0;
    public static String g_sBankAccount = "";
    

    public static boolean PaymentEnabled() {
        return g_iPaymentInterval > 0;
    }
}
