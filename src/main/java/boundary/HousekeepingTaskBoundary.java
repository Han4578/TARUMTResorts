package boundary;

import entity.Room;
import entity.TaskLog;
import utility.Input;

/**
 *
 * @author Lye Wei Keong
 */

public class HousekeepingTaskBoundary {
    public int displayMenu() {
        return Input.getIntInput(
                """
            =======================================
                  HOUSEKEEPING TASK MANAGEMENT
            =======================================
            1 View Room Status
            2 View Tasks
            3 Add Task
            4 Rollback Task
            5 Generate Report
            6 Back
            =======================================
            Enter your choice: \
            """, 1, 6);
    }

    public void viewRoomStatus(String roomStatus) {
        System.out.println("\n" + roomStatus);
        returnWhenX();
    }

    public void viewTasks(String tasks) {
        System.out.println("\n=============== TASK LIST ===============");

        if (tasks == null || tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println(tasks);
        }

        returnWhenX();
    }

    public String getRoomNumber() {
        return Input.getStringInput("\nEnter Room Number (X to exit): ");
    }
    
    public String getAddTaskChoice(Room room) {
        System.out.println("\n======== ADD TASK ========");
        System.out.println("Room: " + room);
        System.out.println("\n1. Cleaning");
        System.out.println("2. Inspected");
        System.out.println("3. Ready for Check-In");
        System.out.println("X. Exit");

        return Input.getStringInput("Enter new status: ");
    }

    public String getReportChoice() {
        System.out.println("\n===== GENERATE REPORT =====");
        System.out.println("1. Task Log Report");
        System.out.println("2. Housekeeping Workload Report");
        System.out.println("3. Staff Task Report");
        System.out.println("X. Back");

        return Input.getStringInput("Enter your choice: ");
    }

    public String getStatus(String choice) {
        switch (choice) {
            case "1" -> { return "Cleaning"; }
            case "2" -> { return "Inspected"; }
            case "3" -> { return "Ready for Check-In"; }
            default -> {
                System.out.println("Invalid status choice.");
                return null;
            }
        }
    }
    
    public String getStaffName() {
        return Input.getStringInput("\nAssigned Staff: ");
    }

    private void returnWhenX() {
        while (!Input.getStringInput("\nType X to return: ").trim().equalsIgnoreCase("X")) {
            System.out.print("Please type X to return: ");
        }
    }
    
    public void taskAddedSuccessfully(Room searchRoom) {
        System.out.println("\nTask added successfully.");
        System.out.println(searchRoom);    
    }

    public void invalidStatusChange() {
        System.out.println("\nInvalid status change.");
        System.out.println("Please follow the status sequence.");
    }

    public void roomNotFound() {
        System.out.println("Room not found. Please try again.");    
    }

    public void rollbackStatus(boolean rollbackStatus) {
        System.out.println("\n===== ROLLBACK TASK =====");

        if (rollbackStatus) {
            System.out.println("Latest task has been rolled back successfully.");
        } else {
            System.out.println("No task is available to roll back.");
        }
        
        returnWhenX();
    }

    public void showReport(String report) {
        System.out.println("\n" + report);
        returnWhenX();
    }

    public void invalidChoice() {
        System.out.println("Invalid choice.");    
    }

    public boolean confirmAddTask(TaskLog task) {
        System.out.println(
            """
            ==========================
            Room Number     : %d
            Original Status : %s
            New Status      : %s
            Staff Name      : %s
            ==========================
            """.formatted(task.getRoom().getRoomNumber(), task.getPreviousStatus(), task.getCurrentStatus(), task.getStaffName()));
        
        return Input.getBooleanInput("Confirm Add Task? [y/n]: ");
    }
}