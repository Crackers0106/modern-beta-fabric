package com.bespectacled.modernbeta.compat;

public class CompatColormatic {
    private static boolean isLoaded = false;
    
    public static void addCompat() {
        isLoaded = true;
    }
    
    public static boolean isLoaded() {
        return isLoaded;
    }
}
