/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tarumtresorts;

import boundary.HousekeepingTaskUI;
import control.CustomerControl;
import control.MainMenuControl;
import control.RoomAssignControl;
import control.StaffControl;
import control.TierControl;
import control.WalkInBookingControl;
import entity.RoomRepository;
import entity.TaskRepository;
import entity.TierRepository;
import entity.UserRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Liew Zheng Han
 */
public class TARUMTResorts {
    private static UserRepository userRepository;
    private static TierRepository tierRepository;
    private static RoomRepository roomRepository;
    private static TaskRepository taskRepository;
    
    private static CustomerControl customerControl;
    private static StaffControl staffControl;
    private static TierControl tierControl;
    private static RoomAssignControl roomAssignControl;
    
    private static HousekeepingTaskUI housekeepingTaskUI;
    private static WalkInBookingControl walkInBookingControl;
    
    public static void main(String[] args) {
        TARUMTResorts.load();
        walkInBookingControl = new WalkInBookingControl(userRepository, roomRepository, tierRepository);
        tierControl = new TierControl(tierRepository, roomRepository);
        roomAssignControl = new RoomAssignControl(roomRepository, tierRepository);
        housekeepingTaskUI = new HousekeepingTaskUI(roomRepository, taskRepository);
        
        customerControl = new CustomerControl(userRepository, tierRepository, walkInBookingControl);
        staffControl = new StaffControl(tierControl, roomAssignControl, housekeepingTaskUI, walkInBookingControl);
        
        new MainMenuControl(userRepository, tierRepository, customerControl, staffControl).start();
    }
    
    private static void load() {
        try(FileInputStream stream = new FileInputStream("data.ser")) {   
            ObjectInputStream in = new ObjectInputStream(stream);
            userRepository = (UserRepository) in.readObject();
            tierRepository = (TierRepository) in.readObject();
            roomRepository = (RoomRepository) in.readObject();
            taskRepository = (TaskRepository) in.readObject();
            
        } catch (Exception e) {
            System.out.println("Save not found");
            userRepository = new UserRepository();
            tierRepository = new TierRepository(userRepository);
            roomRepository = new RoomRepository();
            taskRepository = new TaskRepository();
        }
    }
    
    public static void save() {
        try(FileOutputStream stream = new FileOutputStream("data.ser")) {   
            ObjectOutputStream in = new ObjectOutputStream(stream);
            in.writeObject(userRepository);
            in.writeObject(tierRepository);
            in.writeObject(roomRepository);
            in.writeObject(taskRepository);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
