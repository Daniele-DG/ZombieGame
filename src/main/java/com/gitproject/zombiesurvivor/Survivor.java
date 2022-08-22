package com.gitproject.zombiesurvivor;

import java.util.List;

import exceptions.FullEquipmentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@ToString
@AllArgsConstructor
@Slf4j
public class Survivor {

    private final String name;
    private int wound;
    @Singular
    private List<Turn> turns;
    private Equipment equipment;

    public void addWound() {
        this.wound++;
        log.info("Added wound to survivor: " + this);
        Tool[] inHand = this.equipment.getInHand();
        Tool toolRemoved = this.removeInHandTool(getLastElement(inHand));
        try {
            addInReserve(toolRemoved);
        } catch (FullEquipmentException exception) {
            Tool[] inReserve = this.equipment.getInReserve();
            Tool removeInReserveTool = this.removeInReserveTool(getLastElement(inReserve));
            log.info("Removed tool " + removeInReserveTool);
            addInReserve(toolRemoved);
        }
    }

    public boolean isDead() {
        return this.wound > 1;
    }

    public void addInHand(Tool toolToAdd) {
        Tool[] inHand = this.equipment.getInHand();
        if (getNumberOfTools(inHand) > 1) {
            throw new FullEquipmentException("In hand equipment is full. Please move some item in your reserve.");
        }
        this.equipment.addToEmptyPosition(toolToAdd, inHand, inHand.length, wound);
    }

    public void addInReserve(Tool toolToAdd) {
        Tool[] inReserve = this.equipment.getInReserve();
        if (getNumberOfTools(inReserve) > 3) {
            throw new FullEquipmentException("In reserve equipment is full. Please move some item in your reserve.");
        }
        this.equipment.addToEmptyPosition(toolToAdd, inReserve, inReserve.length, wound);
    }

    public Tool removeInHandTool(Tool toolToRemove) {
        Tool[] inHand = this.equipment.getInHand();
        return this.equipment.remove(toolToRemove, inHand, inHand.length);
    }

    public Tool removeInReserveTool(Tool toolToRemove) {
        Tool[] inReserve = this.equipment.getInReserve();
        return this.equipment.remove(toolToRemove, inReserve, inReserve.length);
    }

    private Tool getLastElement(Tool[] tools) {
        return tools[tools.length - 1];
    }

    private int getNumberOfTools(Tool[] tools) {
        int size = 0;
        for (int i = 0; i < tools.length; i++) {
            if (tools[i] != null)
                size++;
        }
        return size;
    }
}
