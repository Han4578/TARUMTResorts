/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.Random;

/**
 *
 * @author Tan Kah Chao
 */
public class Generate {
    
    public static String generateConfirmationNumber() {
        Random random = new Random();

        String confirmationNumber;
        
            int number = 10000000 + random.nextInt(90000000);
            
            confirmationNumber = String.valueOf(number);
            
        return confirmationNumber;
    }
}
