package com.example.chartcolumn.util;

import java.util.List;

public class Utiles {

    /**
     * 求得集合中最大值
     * @param args
     * @return
     */
    public static int main(List<Object> args)
    {

        int max = 0;
        int maxIndex = 0;

        for (int i = 0; i < args.size(); i++) {

            if (Integer.parseInt(args.get(i).toString()) > max) {
                max = Integer.parseInt(args.get(i).toString());
                maxIndex = i;
            }
        }
        System.out.println(max + "----" + maxIndex);
        return max;
    }


}
