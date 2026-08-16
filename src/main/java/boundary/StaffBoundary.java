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
public class StaffBoundary {

    public int getMenuInput() {
        return Input.getIntInput(
                """
                1. Manage Guests                
                2. Manage Rooms
                3. Manage Tiers
                4. Housekeeping
                5. Front Desk
                6. Log Out
                
                Input: \
                """, 1, 6);
    }
    
}
