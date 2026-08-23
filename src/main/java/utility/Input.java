/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Liew Zheng Han
 */
public class Input {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    
    public static int getIntInput() {        
        return getIntInput("");
    }
    
    public static int getIntInput(String message) {        
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            }            
            scanner.nextLine();
        }
    }
    
    public static int getIntInput(String message, int lowest, int highest) {        
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input < lowest || input > highest) continue;
                return input;
            }            
            scanner.nextLine();
        }
    }
    
    public static String getStringInput() {
        return getStringInput("");
    }
    
    public static String getStringInput(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
    
    public static boolean isValidEmail(String email) {
        return Input.emailPattern.matcher(email).matches();        
    }

    public static boolean getBooleanInput(String message) {
        while (true) {
            String reply = Input.getStringInput(message).toLowerCase();
            if (!("y".equals(reply) || "n".equals(reply))) continue;
            return reply.equals("y");
        }    
    }
    
    public static float getFloatInput(String message, float lowest, float highest) {        
        while (true) {
            System.out.print(message);
            if (scanner.hasNextInt()) {
                float input = scanner.nextFloat();
                scanner.nextLine();
                if (input < lowest || input > highest) {
                    System.out.println("Invalid value.");
                    continue;
                }
                return input;
            } else {
                System.out.println("Invalid input.");
            }            
            scanner.nextLine();
        }
    }
}
