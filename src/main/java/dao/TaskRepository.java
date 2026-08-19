/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.ArrayStack;
import adt.StackInterface;
import entity.TaskLog;
import java.io.Serializable;

/**
 *
 * @author Lye Wei Keong
 */
public class TaskRepository implements Serializable{

    private final StackInterface<TaskLog> tasks;
    
    public TaskRepository() {
        this.tasks = new ArrayStack<>();
    }

    public StackInterface<TaskLog> getTasks() {
        return this.tasks;
    }
    
}
