/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import utility.Input;

/**
 *
 * @author Liew Zheng Han
 */
public class MainMenuBoundary {
    
    public int getMainMenuChoice() {
        return Input.getIntInput(
                """
                Welcome!
                1. Log In
                2. Sign Up as Customer
                3. Exit
                
                Input: \
                """, 1, 3);
    }
    
    public String getEmail() {
        return Input.getStringInput("Enter email: ");
    }
    
    public String getPassword() {
        return Input.getStringInput("Enter password: ");
    }
    
    public void loginStatus(boolean success) {
        System.out.println("Login " + (success? "Success": "Failed"));
    }
    
    public void signupStatus(boolean success) {
        System.out.println("Signup " + (success? "Success": "Failed"));
    }
    
    public void invalidEmail() {
        System.out.print("Invalid email. ");
    }
    
    public void emailTaken() {
        System.out.print("Email is already taken. ");
    }

    public void passwordTooShort() {
        System.out.print("Password must be at least 6 characters. ");
    }
    
    public void invalidCredentials() {
        System.out.print("Incorrect email or password. ");
    }
}
