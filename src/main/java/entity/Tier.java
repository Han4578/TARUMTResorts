/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author Liew Zheng Han
 */
public class Tier implements Serializable, Comparable<Tier> {
    private int tierId;
    private String name;
    private int priority;

    public Tier(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public int getTierId() {
        return tierId;
    }

    public void setTierId(int tierId) {
        this.tierId = tierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Tier o) {
        return this.priority == o.getPriority()? this.name.compareTo(o.getName()): Integer.compare(this.priority, o.getPriority());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tier && ((Tier) obj).getTierId() == this.tierId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.tierId;
        return hash;
    }
}
