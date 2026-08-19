/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

import boundary.MainMenuBoundary;
import entity.Account;
import entity.Customer;
import dao.TierRepository;
import dao.UserRepository;
import tarumtresorts.TARUMTResorts;
import utility.Input;

/**
 *
 * @author Liew Zheng Han
 */
public class MainMenuControl {
    private final MainMenuBoundary mainMenuUI = new MainMenuBoundary();
    private final UserRepository userRepository;
    private final TierRepository tierRepository;
    private final CustomerControl customerControl;
    private final StaffControl staffControl;

    
    public MainMenuControl(
            UserRepository userRepository, 
            TierRepository tierRepository, 
            CustomerControl customerControl,
            StaffControl staffControl
    ) {
        this.userRepository = userRepository;
        this.tierRepository = tierRepository;
        this.customerControl = customerControl;
        this.staffControl = staffControl;
    }
    
    public void start() {
        while (true) {
            switch (this.mainMenuUI.getMainMenuChoice()) {
                case 1 -> this.login();
                case 2 -> this.signUp();
                default -> {
                    return;
                }
            }
        }
    }
    
    private void login() {
        String email = this.mainMenuUI.getEmail();
        String password = this.mainMenuUI.getPassword();
        
        Account account = this.userRepository.getUser(email, password);
        
        if (account == null) {
            this.mainMenuUI.invalidCredentials();
            this.mainMenuUI.loginStatus(false);
        }
        else {
            this.mainMenuUI.loginStatus(true);
            if (account instanceof Customer customer) {
                this.customerControl.start(customer);
            } else this.staffControl.start();
        }
    }
    
    private void signUp() {
        String email = this.mainMenuUI.getEmail();
        if (!Input.isValidEmail(email)) {
            this.mainMenuUI.invalidEmail();
            this.mainMenuUI.signupStatus(false);
            return;
        }
        
        if (userRepository.userExists(email)) {
            this.mainMenuUI.emailTaken();
            this.mainMenuUI.signupStatus(false);
            return;
        }
        
        String password = this.mainMenuUI.getPassword();
        if (password.length() < 6) {
            this.mainMenuUI.passwordTooShort();
            this.mainMenuUI.signupStatus(false);
            return;
        }
        
        this.userRepository.addUser(new Customer(email, password, this.tierRepository.getDefaultTier()));
        TARUMTResorts.save();
        this.mainMenuUI.signupStatus(true);
    }
}
