package com.reu_24.wardmagic.util;

public final class Functions {
    private Functions() {}

    public static float exponentialMore(int value, float e, float morePerLevel) {
        float result = morePerLevel;
        for (int i = 1; i < value; i++) {
            result += morePerLevel + result * e;
        }
        return result;
    }
}
