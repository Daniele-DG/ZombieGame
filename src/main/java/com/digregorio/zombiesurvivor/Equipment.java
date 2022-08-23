package com.digregorio.zombiesurvivor;

import com.digregorio.zombiesurvivor.exceptions.NotFoundEquipmentException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Equipment {
    private Tool[] inHand = new Tool[2];
    private Tool[] inReserve = new Tool[3];

    public void addToEmptyPosition(Tool toolToAdd, Tool[] tools, int equipSize, int woundsReceived) {
        for (int i = 0; i < equipSize - woundsReceived; i++) {
            Tool actualTool = tools[i];
            if (actualTool == null)
                tools[i] = toolToAdd;
        }
    }

    public Tool remove(Tool toolToRemove, Tool[] tools, int equipSize) {
        for (int i = 0; i < equipSize; i++) {
            Tool actualTool = tools[i];
            if (actualTool == toolToRemove) {
                tools[i] = null;
                return toolToRemove;
            }
        }
        throw new NotFoundEquipmentException(
                "Tool not found in your selected inventory.");
    }
}
