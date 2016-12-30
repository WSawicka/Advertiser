package com.advertiser.service.util;

import java.io.*;
import java.util.Random;

public class Xorshift extends Random {
    private long seed;

    public Xorshift(long seed) {
        this.seed = seed;
    }

    @Override
    public int next(int nbits) {
        long x = seed;
        x ^= (x << 21);
        x ^= (x >>> 35);
        x ^= (x << 4);
        seed = x;
        x &= ((1L << nbits) - 1);
        return (int) x;
    }

        /*Random rand1 = new Xorshift((long) 100);
        Random rand2 = new Random();

        double deviation = 4;
        double mean = 11;

        int[] list1 = new int[24];
        int[] list2 = new int[24];
        for(int i=0;i<1000;i++){
            int value1 = (int) Math.round(rand1.nextGaussian() * deviation + mean);
            if(value1 >= 0 && value1 < 24)
                list1[value1]++;
            int value2 = (int) Math.round(rand2.nextGaussian() * deviation + mean);
            if(value2 >= 0 && value2 < 24)
                list2[value2]++;
        }*/
}
