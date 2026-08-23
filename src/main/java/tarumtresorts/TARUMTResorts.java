/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package tarumtresorts;

import adt.DictionaryInterface;
import control.CustomerControl;
import control.FrontDeskControl;
import control.HousekeepingTaskControl;
import control.MainMenuControl;
import control.RoomAssignControl;
import control.StaffControl;
import control.TierControl;
import control.WalkInBookingControl;
import dao.CheckInRepository;
import dao.ReservationRepository;
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
import utility.Generate;
import utility.SaveFile;

/**
 *
 * @author Liew Zheng Han
 */
public class TARUMTResorts {
    private static UserRepository userRepository;
    private static TierRepository tierRepository;
    private static RoomRepository roomRepository;
    private static TaskRepository taskRepository;
    private static ReservationRepository reservationRepository;
    private static CheckInRepository checkInRepository;
    
    private static CustomerControl customerControl;
    private static StaffControl staffControl;
    private static TierControl tierControl;
    private static RoomAssignControl roomAssignControl;
    private static HousekeepingTaskControl housekeepingTaskControl;
    private static WalkInBookingControl walkInBookingControl;
    private static FrontDeskControl frontDeskControl;
    
    public static void main(String[] args) {
        TARUMTResorts.load();
        walkInBookingControl = new WalkInBookingControl(userRepository, roomRepository, tierRepository, reservationRepository);
        tierControl = new TierControl(tierRepository, roomRepository);
        roomAssignControl = new RoomAssignControl(roomRepository, tierRepository, reservationRepository);
        housekeepingTaskControl = new HousekeepingTaskControl(roomRepository, taskRepository);
        frontDeskControl = new FrontDeskControl(checkInRepository, reservationRepository, roomRepository);
        customerControl = new CustomerControl(userRepository, tierRepository, walkInBookingControl);
        staffControl = new StaffControl(tierControl, roomAssignControl, housekeepingTaskControl, walkInBookingControl, frontDeskControl);
        
        new MainMenuControl(userRepository, tierRepository, customerControl, staffControl).start();
    }
    
    private static void load() {
        try(FileInputStream stream = new FileInputStream("data.ser")) {   
            ObjectInputStream in = new ObjectInputStream(stream);
            userRepository = (UserRepository) in.readObject();
            tierRepository = (TierRepository) in.readObject();
            roomRepository = (RoomRepository) in.readObject();
            taskRepository = (TaskRepository) in.readObject();
            reservationRepository = (ReservationRepository) in.readObject();
            checkInRepository = (CheckInRepository) in.readObject(); 
            
        } catch (Exception e) {
            userRepository = new UserRepository();
            tierRepository = new TierRepository(userRepository);
            roomRepository = new RoomRepository();
            taskRepository = new TaskRepository();
            reservationRepository = new ReservationRepository();
            checkInRepository = new CheckInRepository();
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
            out.writeObject(reservationRepository);
            out.writeObject(checkInRepository);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private static void loadDummyData() {
        SaveFile.clearDummyConfirmNoFile();
        
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
            Reservation reservation = createReservation(bookedAll, date.minusDays(2), date);
            room.addReservation(reservation);
        }
        
        for (int i = 0; i < emails.length; i++) {
            Customer c = new Customer(emails[i], "123456", tierRepository.getDefaultTier());
            c.setName(names[i]);
            userRepository.addUser(c);
            Reservation reservation = createReservation(c, dates[i], dates[i].plusDays(2));
            tierRepository.getQueue().insert(reservation, c.getTier().getPriority());
        }
        
        Customer c = new Customer("new@new.com", "123456", tierRepository.getTiers().get(0));
        userRepository.addUser(c);
        Reservation reservation = createReservation(c, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 5));
        tierRepository.getQueue().insert(reservation, c.getTier().getPriority());
    }
    
    private static Reservation createReservation(Customer customer, LocalDate startDate, LocalDate endDate) {
        DictionaryInterface<String, Reservation>reservationTable = reservationRepository.getReservationTable();

        String confirmNo;

        do {
            confirmNo = Generate.generateConfirmationNumber();
        } while (reservationTable.containsKey(confirmNo));

        Reservation reservation = new Reservation(confirmNo, customer, startDate, endDate);

        reservationRepository.addToResersevationTable(reservation.getConfirmNo(), reservation);

        SaveFile.saveConfirmNoToDummyFile(reservation);

        return reservation;
    }
}
