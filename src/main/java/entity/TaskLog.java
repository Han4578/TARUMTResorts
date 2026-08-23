package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Lye Wei Keong
 */

public class TaskLog implements Serializable {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String taskID;
    private Room room;
    private String previousStatus;
    private String currentStatus;
    private String staffName;
    private final LocalDateTime timeStamp;

    public TaskLog(String taskID, Room room, String previousStatus,
                   String currentStatus, String staffName) {

        this.taskID = taskID;
        this.room = room;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
        this.staffName = staffName;
        this.timeStamp = LocalDateTime.now();
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoomID(Room room) {
        this.room = room;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public String getFormattedTimestamp() {
        return timeStamp.format(FORMATTER);
    }

    @Override
    public String toString() {
        return taskID + " | " + room.getRoomNumber() + " | "
                + previousStatus + " -> "
                + currentStatus + " | "
                + staffName + " | "
                + getFormattedTimestamp();
    }
}       