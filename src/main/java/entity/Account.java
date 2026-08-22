/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 *
 * @author Liew Zheng Han
 */
public class Account implements Comparable<Account>, Serializable {
    protected long accountId;
    protected String name = "";
    protected String email;
    protected String password;
    
    public Account(String email) {
        this.email = email.toLowerCase();
    }
    
    public Account(String email, String password) {
        this.email = email.toLowerCase();
        this.password = Account.hashPassword(password);
    }
    
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
    
    public long getAccountId() {
        return this.accountId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = Account.hashPassword(password);
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    @Override
    public int compareTo(Account o) {
        return this.getEmail().compareTo(o.getEmail());
    }

    @Override
    public boolean equals(Object account) {
        return account instanceof Account && this.email.equals(((Account)account).getEmail());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.email);
        return hash;
    }
}
