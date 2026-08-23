/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import entity.Reservation;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

/**
 *
 * @author Tan Kah Chao
 */
public class SaveFile {
    public static void saveConfirmNoToFile(Reservation reservation) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("confirmNo.txt", true))) {
            String confirmNo = reservation.getConfirmNo();
            String customerName = reservation.getCustomer().getName();
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate = reservation.getEndDate();
            boolean isCheckIn = reservation.isCheckIn();
            bw.write(confirmNo + "," + customerName + "," + startDate + "," + endDate + "," + isCheckIn);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
    
    public static void saveConfirmNoToDummyFile(Reservation reservation) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("DummyConfirmNo.txt", true))) {
            String confirmNo = reservation.getConfirmNo();
            String customerName = reservation.getCustomer().getName();
            LocalDate startDate = reservation.getStartDate();
            LocalDate endDate = reservation.getEndDate();
            boolean isCheckIn = reservation.isCheckIn();
            bw.write(confirmNo + "," + customerName + "," + startDate + "," + endDate + "," + isCheckIn);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
        
    public static void clearDummyConfirmNoFile() {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter("DummyConfirmNo.txt", false))) {
        } catch (IOException e) {
            System.out.println("Error clearing file: " + e.getMessage());
        }
    }
    
}
