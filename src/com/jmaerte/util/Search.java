package com.jmaerte.util;

public class Search {

    public static int binarySearch(int[] arr, int entry, int begin, int end) {
        if(begin == end || arr[end - 1] < entry) return end;

        int min = begin;
        int max = end;
        while(min < max) {
            int mid = (min+max)/2;
            if(arr[mid] < entry) min = mid + 1;
            else if(arr[mid] > entry) max = mid;
            else return mid;
        }
        return min;
    }

}
