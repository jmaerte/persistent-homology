package com.jmaerte.util.input;

import com.jmaerte.data_struc.point_set.Metadata;
import com.jmaerte.data_struc.point_set.Metric;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.calc.Function;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileIO {

    /**Parses a PointSet from a csv-file.
     * This is done by giving some characters that decide the csv-parsers behavior.
     * We need to give two functions that can convert one format to another. That is if we have real-valued tuples as
     * the point format T is Double and K is double[].
     * When parsing a double from the file we save a new T and if the file ends the input of the actual element(that is {@code nextElement} occurs)
     * we pack the T-Array to a vector.
     *
     * @param path File path
     * @param cast Cast function from String to T
     * @param packing Packs an Array of T elements to one Object of K
     * @param nextElement Determines when the next K element starts
     * @param delimiter Determines when the next T elements starts
     * @param textQualifier Determines when not to respect the delimiters.
     * @param <T> T
     * @param <K> T[]-like Object.
     * @return PointSet<K> consisting of the elements described in the file.
     */
    public static <T, K> PointSet<K> fromCSV(String path, Function<String, T> cast, Function<ArrayList<T>, K> packing, char nextElement, char delimiter, char textQualifier, Function<K, Metadata<K>> meta, Function<K, Writer<K>> writer) {
        File f = new File(path);
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            data = new byte[(int) f.length()];
            fis.read(data);
            fis.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        boolean isText = false;
        String text = "";
        String curr = "";
        char c;
        ArrayList<T> currEl = new ArrayList<>();
        ArrayList<K> elements = new ArrayList<>();
        for(int i = 0; i < data.length; i++) {
            c = (char) data[i];
            if(c == textQualifier) {
                if(isText) {
                    curr += text;
                }else {
                    text = "";
                }
                isText = !isText;
            }else if(c == delimiter && !isText) {
                try {
                    currEl.add(cast.eval(curr));
                } catch(Exception e) {}
                curr = "";
            }else if(c == nextElement && !isText) {
                if(!curr.equals("")) currEl.add(cast.eval(curr));
                elements.add(packing.eval(currEl));
                currEl = new ArrayList<>();
                curr = "";
            }else {
                if(isText) {
                    text += c;
                }else {
                    curr += c;
                }
            }
        }
        return new PointSet<K>(elements) {
            public Metadata<K> getMetadata() {
                return meta.eval(elements.get(0));
            }

            @Override
            public void write(BufferedWriter bw, K k) throws Exception {
                writer.eval(elements.get(0)).write(bw, k);
            }
        };
    }

    public static <T extends Writer & Function<T, Double>> void toCSV(String path, PointSet<T> S) {
        try {
            File f = new File(path);
            new File(f.getParent()).mkdirs();
            if(!f.exists()) f.createNewFile();
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            S.write(bw);
            bw.flush();
            bw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
