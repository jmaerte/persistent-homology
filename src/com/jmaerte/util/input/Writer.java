package com.jmaerte.util.input;

import java.io.BufferedWriter;

public interface Writer<T> {

    void write(BufferedWriter bw, T t) throws Exception;


    static Writer<double[]> DoubleArray(String delimiter, String next) {
        return ((bw, doubles) -> {
            for(int i = 0; i < doubles.length; i++) {
                bw.write(doubles[i] + (i + 1 == doubles.length ? "" : delimiter));
            }
            bw.write(next);
        });
    }

    static Writer<String> String() {
        return ((bw, string) -> {
            bw.write(string);
        });
    }
}
