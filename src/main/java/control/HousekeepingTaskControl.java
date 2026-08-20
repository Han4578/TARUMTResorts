package control;

import adt.ArrayStack;
import adt.SortedListInterface;
import adt.StackInterface;
import boundary.HousekeepingTaskBoundary;
import dao.RoomRepository;
import dao.TaskRepository;
import entity.Room;
import entity.TaskLog;
import tarumtresorts.TARUMTResorts;

/**
 *
 * @author Lye Wei Keong
 */

public class HousekeepingTaskControl {
    private StackInterface<TaskLog> taskStack;
    private SortedListInterface<Room> roomList;
    private int taskCounter;
    private final RoomRepository roomRepository;
    private HousekeepingTaskBoundary housekeepingTaskBoundary = new HousekeepingTaskBoundary();

    public HousekeepingTaskControl(RoomRepository roomRepository, TaskRepository taskRepository) {
        taskStack = taskRepository.getTasks();
        taskCounter = 1;
        this.roomRepository = roomRepository;
        this.roomList = roomRepository.getRooms();
    }
    
    public void start() {
        while (true) {
            switch (this.housekeepingTaskBoundary.displayMenu()) {
                case 1 -> this.housekeepingTaskBoundary.viewRoomStatus(this.roomStatus());
                case 2 -> this.housekeepingTaskBoundary.viewTasks(this.displayTaskHistory());
                case 3 -> addTask();
                case 4 -> this.housekeepingTaskBoundary.rollbackStatus(this.rollbackStatus());
                case 5 -> generateReport();
                case 6 -> {
                    return;
                }
                default -> System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }
    
    private void addTask() {
        while (true) {
            String roomNumberStr = this.housekeepingTaskBoundary.getRoomNumber();
            int roomNumber;

            if (roomNumberStr.equalsIgnoreCase("X")) {
                return;
            }
            
            try {
                roomNumber = Integer.parseInt(roomNumberStr);
            } catch (NumberFormatException e) {
                continue;
            }

            Room room = this.searchRoom(roomNumber);

            if (room == null) {
                this.housekeepingTaskBoundary.roomNotFound();
                continue;
            }

            String staffName = this.housekeepingTaskBoundary.getStaffName();

            if (this.updateRoomStatus(roomNumber, staffName)) {
                this.housekeepingTaskBoundary.taskAddedSuccessfully(this.searchRoom(roomNumber));
            }
        }
    }

    // Update room cleaning status
    public boolean updateRoomStatus(int roomNumber, String staffName) {
        Room room = roomRepository.getRoom(roomNumber);

        if (room == null) {
            return false;
        }

        String previousStatus = room.getStatus();

        String newStatus = switch(previousStatus.toLowerCase()) {
            case "ready for check-in" -> "Dirty";
            case "dirty" -> "Cleaning";
            case "cleaning" -> "Inspected";
            case "inspected" -> "Ready for Check-In";
            default -> "ready for check-in";
        };

        TaskLog task = new TaskLog(
            "",
            room,
            previousStatus,
            newStatus,
            staffName
        );
        
        if (!this.housekeepingTaskBoundary.confirmAddTask(task)) return false;
        
        task.setTaskID(this.generateTaskID());

        taskStack.push(task);
        room.setStatus(newStatus);
        TARUMTResorts.save();

        return true;
    }

    // Rollback the latest status change
    public boolean rollbackStatus() {
        if (!taskStack.isEmpty()) {
            TaskLog lastTask = taskStack.pop();
            Room room = lastTask.getRoom();
            if (room != null) {
                room.setStatus(lastTask.getPreviousStatus());
            }
            TARUMTResorts.save();
            return true;
        }
        return false;
    }
    
    private void generateReport() {
        while (true) {
            switch (this.housekeepingTaskBoundary.getReportChoice()) {
                case "1" -> {
                    this.housekeepingTaskBoundary.showReport(this.generateTaskLogReport());
                }

                case "2" -> {
                    this.housekeepingTaskBoundary.showReport(this.generateWorkloadReport());
                }

                case "3" -> {
                    this.housekeepingTaskBoundary.showReport(this.generateStaffTaskReport(this.housekeepingTaskBoundary.getStaffName()));
                }

                case "X", "x" -> {
                    return;
                }
                
                default -> this.housekeepingTaskBoundary.invalidChoice();
            }
        }
    }

    // Generate task ID automatically
    private String generateTaskID() {
        String id = "T00" + taskCounter;
        taskCounter++;
        return id;
    }

    // Return task history
    public String displayTaskHistory() {
        String output = "";
        StackInterface<TaskLog> tempStack = new ArrayStack<>();
        while (!taskStack.isEmpty()) {
            TaskLog task = taskStack.pop();
            output += task.toString() + "\n";
            tempStack.push(task);
        }

        // Restore original stack
        while (!tempStack.isEmpty()) {
            taskStack.push(tempStack.pop());
        }
        return output;
    }

    public Room searchRoom(int roomNumber) {
        return this.roomRepository.getRoom(roomNumber);
    }

    public String searchTaskLogs(int roomNumber) {
        String output = "";
        StackInterface<TaskLog> tempStack = new ArrayStack<>();

        while (!taskStack.isEmpty()) {
            TaskLog task = taskStack.pop();

            if (task.getRoom().getRoomNumber() == roomNumber) {
                output += task.toString() + "\n";
            }

            tempStack.push(task);
        }

        while (!tempStack.isEmpty()) {
            taskStack.push(tempStack.pop());
        }

        return output;
    }

    public String generateTaskLogReport() {
        String output = "========== TASK LOG REPORT ==========\n";
        output += String.format("%-8s %-8s %-20s %-20s %-20s %-16s%n",
                "Task ID", "Room", "Previous Status", "Current Status", "Staff", "Date & Time");
        output += "-------------------------------------------------------------------------------------------------\n";

        StackInterface<TaskLog> tempStack = new ArrayStack<>();

        while (!taskStack.isEmpty()) {
            TaskLog task = taskStack.pop();

            output += String.format("%-8s %-8d %-20s %-20s %-20s %-16s%n",
                    task.getTaskID(),
                    task.getRoom().getRoomNumber(),
                    task.getPreviousStatus(),
                    task.getCurrentStatus(),
                    task.getStaffName(),
                    task.getFormattedTimestamp());

            tempStack.push(task);
        }

        while (!tempStack.isEmpty()) {
            taskStack.push(tempStack.pop());
        }

        return output;
    }

    public String roomStatus() {
        String output = "======== ROOM STATUS ========\n";
        output += String.format("%-8s %-18s%n", "Room No", "Status");
        output += "------------------------------------\n";

        for (Room room: roomList) {
            output += String.format("%-8d %-18s%n",
                    room.getRoomNumber(),
                    room.getStatus());
        }

        return output;
    }

    public String generateWorkloadReport() {
        int dirty = 0;
        int cleaning = 0;
        int inspected = 0;
        int ready = 0;

        String output = "===== HOUSEKEEPING WORKLOAD REPORT =====\n\n";
        output += String.format("%-8s %-18s%n", "Room No", "Current Status");
        output += "------------------------------------\n";

        for (Room room: roomList) {
            String status = room.getStatus();

            if (status.equals("Dirty")) {
                dirty++;
            } else if (status.equals("Cleaning")) {
                cleaning++;
            } else if (status.equals("Inspected")) {
                inspected++;
            } else if (status.equals("Ready for Check-In")) {
                ready++;
            }

            if (!status.equals("Ready for Check-In")) {
                output += String.format("%-8d %-18s%n",
                        room.getRoomNumber(),
                        status);
            }
        }

        output += "\n===== WORKLOAD SUMMARY =====\n";
        output += "Dirty Rooms              : " + dirty + "\n";
        output += "Cleaning                 : " + cleaning + "\n";
        output += "Awaiting Inspection      : " + inspected + "\n";
        output += "Ready for Check-In       : " + ready + "\n";
        output += "Rooms Requiring Action   : " + (dirty + cleaning + inspected) + "\n";

        return output;
    }

    public String generateStaffTaskReport(String staffName) {
        String output = "===== STAFF TASK REPORT =====\n";
        output += "Staff Name: " + staffName + "\n\n";
        output += String.format("%-8s %-8s %-20s %-20s %-16s%n",
                "Task ID", "Room", "Previous Status", "Current Status", "Date & Time");
        output += "----------------------------------------------------------------------------\n";

        int taskCount = 0;
        StackInterface<TaskLog> tempStack = new ArrayStack<>();

        while (!taskStack.isEmpty()) {
            TaskLog task = taskStack.pop();

            if (task.getStaffName().equalsIgnoreCase(staffName)) {
                output += String.format("%-8s %-8d %-20s %-20s %-16s%n",
                        task.getTaskID(),
                        task.getRoom().getRoomNumber(),
                        task.getPreviousStatus(),
                        task.getCurrentStatus(),
                        task.getFormattedTimestamp());

                taskCount++;
            }

            tempStack.push(task);
        }

        while (!tempStack.isEmpty()) {
            taskStack.push(tempStack.pop());
        }

        if (taskCount == 0) {
            output += "No tasks found for this staff member.\n";
        }

        output += "\nTotal Tasks Performed: " + taskCount + "\n";

        return output;
    }
}