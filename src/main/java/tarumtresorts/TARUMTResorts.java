/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tarumtresorts;

import control.CustomerControl;
import control.HousekeepingTaskControl;
import control.MainMenuControl;
import control.RoomAssignControl;
import control.StaffControl;
import control.TierControl;
import control.WalkInBookingControl;
import dao.RoomRepository;
import dao.TaskRepository;
import dao.TierRepository;
import dao.UserRepository;
import entity.Customer;
import entity.Reservation;
import entity.Room;
import entity.Tier;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

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
    private static HousekeepingTaskControl housekeepingTaskControl;
    private static WalkInBookingControl walkInBookingControl;
    
    public static void main(String[] args) {
        TARUMTResorts.load();
        
        walkInBookingControl = new WalkInBookingControl(userRepository, roomRepository, tierRepository);
        tierControl = new TierControl(tierRepository, roomRepository);
        roomAssignControl = new RoomAssignControl(roomRepository, tierRepository);
        housekeepingTaskControl = new HousekeepingTaskControl(roomRepository, taskRepository);
        
        customerControl = new CustomerControl(userRepository, tierRepository, walkInBookingControl);
        staffControl = new StaffControl(tierControl, roomAssignControl, housekeepingTaskControl, walkInBookingControl);
        
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
            userRepository = new UserRepository();
            tierRepository = new TierRepository(userRepository);
            roomRepository = new RoomRepository();
            taskRepository = new TaskRepository();
            
            loadDummyData();
        }
    }
    
    public static void save() {
        try(FileOutputStream stream = new FileOutputStream("data.ser")) {   
            ObjectOutputStream out = new ObjectOutputStream(stream);
            out.writeObject(userRepository);
            out.writeObject(tierRepository);
            out.writeObject(roomRepository);
            out.writeObject(taskRepository);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void loadDummyData() {
        tierRepository.addTier(new Tier("Diamond", 0), false);
        tierRepository.addTier(new Tier("Silver", 1), false);
        
        String[] emails = {"a@a.com", "b@b.com", "c@c.com"};
        String[] names = {"Alice", "Bob", "James"};
        LocalDate[] dates = {
            LocalDate.of(2027, 3, 21),
            LocalDate.of(2027, 3, 23),
            LocalDate.of(2027, 3, 28)
        };
        
        Customer bookedAll = new Customer("booked@booked.com", "123456", tierRepository.getDefaultTier());
        LocalDate date = LocalDate.of(2027, 3, 21);
        bookedAll.setName("Alex");
        
        for (Room room: roomRepository.getRooms()) {
            room.addReservation(new Reservation(bookedAll, date.minusDays(2), date));
        }
        
        for (int i = 0; i < emails.length; i++) {
            Customer c = new Customer(emails[i], "123456", tierRepository.getDefaultTier());
            c.setName(names[i]);
            userRepository.addUser(c);
            tierRepository.getQueue().insert(new Reservation(c, dates[i], dates[i].plusDays(2)), c.getTier().getPriority());
        }
        
        Customer c = new Customer("new@new.com", "123456", tierRepository.getTiers().get(0));
        userRepository.addUser(c);
        tierRepository.getQueue().insert(new Reservation(c, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 5)), c.getTier().getPriority());
    }
}
