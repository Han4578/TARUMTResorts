package boundary;

import control.HousekeepingTaskControl;
import entity.Room;
import entity.RoomRepository;
import entity.TaskRepository;
import java.util.Scanner;
import utility.Input;

/**
 *
 * @author Lye Wei Keong
 */

public class HousekeepingTaskUI {

    private final HousekeepingTaskControl control;
    private final Scanner scanner;

    public HousekeepingTaskUI() {
        this(new RoomRepository(), new TaskRepository());
    }

    public HousekeepingTaskUI(RoomRepository roomRepository, TaskRepository taskRepository) {

        this.control = new HousekeepingTaskControl(roomRepository, taskRepository);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewRoomStatus();
                case "2" -> viewTasks();
                case "3" -> addTask();
                case "4" -> rollbackTask();
                case "5" -> generateReport();
                case "6" -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("       HOUSEKEEPING TASK MANAGEMENT");
        System.out.println("========================================");
        System.out.println("1. View Room Status");
        System.out.println("2. View Tasks");
        System.out.println("3. Add Task");
        System.out.println("4. Rollback Task");
        System.out.println("5. Generate Report");
        System.out.println("6. Back");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
    }

    private void viewRoomStatus() {
        System.out.println("\n" + control.roomStatus());
        returnWhenX();
    }

    private void viewTasks() {
        System.out.println("\n=============== TASK LIST ===============");

        String tasks = control.displayTaskHistory();

        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println(tasks);
        }

        returnWhenX();
    }

    private void addTask() {
        while (true) {
            String roomNumberStr = Input.getStringInput("\nEnter Room Number (X to exit): ");
            int roomNumber;

            if (roomNumberStr.equalsIgnoreCase("X")) {
                return;
            }
            
            try {
                roomNumber = Integer.parseInt(roomNumberStr);
            } catch (NumberFormatException e) {
                System.out.println("Not a number");
                continue;
            }

            Room room = control.searchRoom(roomNumber);

            if (room == null) {
                System.out.println("Room not found. Please try again.");
                continue;
            }

            while (true) {
                System.out.println("\n======== ADD TASK ========");
                System.out.println("Room: " + room);
                System.out.println("\n1. Cleaning");
                System.out.println("2. Inspected");
                System.out.println("3. Ready for Check-In");
                System.out.println("X. Exit");
                System.out.print("Enter new status: ");

                String choice = scanner.nextLine().trim();

                if (choice.equalsIgnoreCase("X")) {
                    return;
                }

                String newStatus = getStatus(choice);

                if (newStatus == null) {
                    System.out.println("Invalid status choice.");
                    continue;
                }

                String staffName = Input.getStringInput("\nAssigned Staff: ");

                if (control.updateRoomStatus(roomNumber, newStatus, staffName)) {
                    System.out.println("\nTask added successfully.");
                    System.out.println(control.searchRoom(roomNumber));
                    break;
                }

                System.out.println("\nInvalid status change.");
                System.out.println("Please follow the status sequence.");
            }
        }
    }

    private void rollbackTask() {
        System.out.println("\n===== ROLLBACK TASK =====");

        if (control.rollbackStatus()) {
            System.out.println("Latest task has been rolled back successfully.");
        } else {
            System.out.println("No task is available to roll back.");
        }

        returnWhenX();
    }

    private void generateReport() {
        while (true) {
            System.out.println("\n===== GENERATE REPORT =====");
            System.out.println("1. Task Log Report");
            System.out.println("2. Housekeeping Workload Report");
            System.out.println("3. Staff Task Report");
            System.out.println("X. Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n" + control.generateTaskLogReport());
                    returnWhenX();
                }

                case "2" -> {
                    System.out.println("\n" + control.generateWorkloadReport());
                    returnWhenX();
                }

                case "3" -> {
                    System.out.println("\n"
                            + control.generateStaffTaskReport(Input.getStringInput("Enter staff name: ")));
                    returnWhenX();
                }

                case "X", "x" -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private String getStatus(String choice) {
        return switch (choice) {
            case "1" -> "Cleaning";
            case "2" -> "Inspected";
            case "3" -> "Ready for Check-In";
            default -> null;
        };
    }

    private void returnWhenX() {
        System.out.print("\nType X to return: ");

        while (!scanner.nextLine().trim().equalsIgnoreCase("X")) {
            System.out.print("Please type X to return: ");
        }
    }

    public static void main(String[] args) {
        HousekeepingTaskUI ui = new HousekeepingTaskUI();
        ui.start();
    }
}