/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.function.BiPredicate;

/**
 *
 * @author Liew Zheng Han
 */
public class Util {
    public static <T> void bubbleSort(T[] array, BiPredicate<T, T> condition) {
        for (int i = 1; i < array.length; ++i) {
            for (int j = 0; j < array.length - i; ++j) {
                if (!condition.test(array[j], array[j + 1])) {
                    T temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
    
    public static String ellipsis(String s, int length) {
        return (s.length() <= length)? s: s.substring(0, length - 3) + "...";
    }
}
