package com.jmaerte.util.input;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.complex.Tree;
import com.jmaerte.data_struc.point_set.Metadata;
import com.jmaerte.data_struc.point_set.Metric;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.calc.Function;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.*;

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

    public static Filtration fromCSV(String path) {
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
        int n = getMaximumVertex(data);
        HashMap<Double, LinkedList<Tree>> table = new HashMap<>();
        char c;
        Filtration filtration = new Filtration(n);
        table.computeIfAbsent(0d, m -> new LinkedList<>());
        table.get(0d).addLast(filtration.simplices);
        ArrayList<Integer> currPath = new ArrayList<>();
        String currIndex = "";
        String valuation = "";
        boolean readPath = false;
        for(int i = 0; i < data.length; i++) {
            c = (char) data[i];
            if(readPath) {
                if(c == '}') {
                    currPath.add(Integer.valueOf(currIndex));
                    readPath = false;
                    currIndex = "";
                    continue;
                }
                if(c == ' ') {
                    currPath.add(Integer.valueOf(currIndex));
                    currIndex = "";
                }else {
                    currIndex += c;
                }
            } else if(c != ' ' && c != '{') {
                valuation += c;
            }
            if(c == '{') readPath = true;
            if(c == '\n') {
                int[] patharr = new int[currPath.size()];
                for(int j = 0; j < patharr.length; j++) patharr[j] = currPath.get(j);
                currPath = new ArrayList<>();
                filtration.insert(patharr, Double.valueOf(valuation), table);
                valuation = "";

            }
        }
        filtration.pack(table);
        return filtration;
    }

    private static int getMaximumVertex(byte[] data) {
        char c;
        PriorityQueue<Integer> pq = new PriorityQueue<>((a,b) -> b - a);
        String currIndex = "";
        boolean readPath = false;
        for(int i = 0; i < data.length; i++) {
            c = (char) data[i];
            if(readPath) {
                if(c == '}') {
                    pq.add(Integer.valueOf(currIndex));
                    readPath = false;
                    currIndex = "";
                    continue;
                }
                if(c == ' ') {
                    pq.add(Integer.valueOf(currIndex));
                    currIndex = "";
                }else {
                    currIndex += c;
                }
            }
            if(c == '{') readPath = true;
        }
        return pq.peek() + 1;
    }

    public static <T> void toCSV(String path, PointSet<T> S) {
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
