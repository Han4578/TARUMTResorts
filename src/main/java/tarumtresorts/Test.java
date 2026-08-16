/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarumtresorts;

import adt.BucketPriorityQueue;
import adt.PriorityQueueInterface;

/**
 *
 * @author Liew Zheng Han
 */
public class Test {
    public static void main(String[] args) {
        PriorityQueueInterface<Integer> reservations = new BucketPriorityQueue<>();
        
        reservations.insert(1, 2);
        reservations.insert(2, 1);
        reservations.insert(3, 3);
        
//        System.out.println(reservations.peek());
        
//        for (int n: reservations) {
//            System.out.println(n);
//        }
//        
        System.out.println(reservations.isEmpty(0));
    }
}
