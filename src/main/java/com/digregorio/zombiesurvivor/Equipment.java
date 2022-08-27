package com.digregorio.zombiesurvivor;

import java.util.ArrayList;

import com.digregorio.zombiesurvivor.exceptions.FullEquipmentException;
import com.digregorio.zombiesurvivor.exceptions.NotFoundEquipmentException;

import lombok.Data;

@Data
public class Equipment {
    private ArrayList<Tool> inHand;
    private ArrayList<Tool> inReserve;
    public static final int MAX_IN_HAND = 2;
    public static final int MAX_IN_RESERVE = 3;

    public Equipment() {
        this.inHand = new ArrayList<>(MAX_IN_HAND);
        this.inReserve = new ArrayList<>(MAX_IN_RESERVE);
    }

    public void addToInHand(Tool toolToAdd) {
        inHand.add(toolToAdd);
    }

    public void addToInReserve(Tool toolToAdd) {
        inReserve.add(toolToAdd);
    }

    public void moveInHandToolToReserve(Tool tool) {
        if (inReserve.size() == MAX_IN_RESERVE)
            throw new FullEquipmentException(
                    "In reserve has reach the maximum capacity. Please move a tool to your hand or remove it.");
        if (!inHand.remove(tool)) {
            throw new NotFoundEquipmentException("Tool not found in your hand. You must have that tool to move it");
        }
        inReserve.add(tool);
    }

    public void moveInReserveToolToHand(Tool tool) {
        if (inHand.size() == MAX_IN_HAND)
            throw new FullEquipmentException(
                    "In hand has reach the maximum capacity. Please move a tool to your reserve.");
        if (!inReserve.remove(tool)) {
            throw new NotFoundEquipmentException("Tool not found in your reserve. You must have that tool to move it");
        }
        inHand.add(tool);
    }

    public Tool removeInHandTool(Tool toolToRemove) {
        boolean isRemoved = inHand.remove(toolToRemove);
        if (isRemoved)
            return toolToRemove;
        throw new NotFoundEquipmentException(
                "Tool not found in your selected inventory.");
    }

    public Tool removeInReserveTool(Tool toolToRemove) {
        boolean isRemoved = inReserve.remove(toolToRemove);
        if (isRemoved)
            return toolToRemove;
        throw new NotFoundEquipmentException(
                "Tool not found in your selected inventory.");
    }
}
