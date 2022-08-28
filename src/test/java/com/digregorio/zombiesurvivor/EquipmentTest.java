package com.digregorio.zombiesurvivor;

import static com.digregorio.zombiesurvivor.Tool.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.digregorio.zombiesurvivor.exceptions.FullEquipmentException;
import com.digregorio.zombiesurvivor.exceptions.NotFoundEquipmentException;

class EquipmentTest {

    @Test
    void testAddToInHandOrInReserve() {
        Survivor survivor = new Survivor("name");
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInHandTools = new ArrayList<Tool>();
        listOfInHandTools.add(BASEBALL_BAT);
        equipment.setInHand(listOfInHandTools);

        survivor.setEquipment(equipment);

        assertThat(survivor.getEquipment().getInHand()).hasSize(1).contains(BASEBALL_BAT);
        survivor.addInHand(KATANA);
        assertThat(survivor.getEquipment().getInHand()).hasSize(2).contains(BASEBALL_BAT,
                KATANA);
        FullEquipmentException exceptionThrowed = assertThrows(FullEquipmentException.class,
                () -> survivor.addInHand(PISTOL));
        assertThat(exceptionThrowed.getMessage())
                .contains("In hand equipment is full. Please move some item in your reserve.");
        assertThat(survivor.getEquipment().getInHand()).doesNotContain(PISTOL);
        assertThat(survivor.getEquipment().getInReserve()).doesNotContain(PISTOL);
        survivor.addInReserve(PISTOL);
        assertThat(survivor.getEquipment().getInReserve()).contains(PISTOL);
        survivor.addInReserve(MOLOTOV);
        survivor.addInReserve(KATANA);
        exceptionThrowed = assertThrows(FullEquipmentException.class,
                () -> survivor.addInReserve(FRYING_PAN));
        assertThat(exceptionThrowed.getMessage())
                .contains("In reserve equipment is full. Please remove some item to add a new one.");
    }

    @Test
    void testGetInHand() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInHandTools = new ArrayList<Tool>();
        listOfInHandTools.add(BASEBALL_BAT);
        equipment.setInHand(listOfInHandTools);
        assertThat(equipment.getInHand()).hasSize(1).contains(BASEBALL_BAT);
    }

    @Test
    void testGetInReserve() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInReserveTools = new ArrayList<Tool>();
        listOfInReserveTools.add(BASEBALL_BAT);
        equipment.setInReserve(listOfInReserveTools);
        assertThat(equipment.getInReserve()).hasSize(1).contains(BASEBALL_BAT);
    }

    @Test
    void testRemoveInHand() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInHandTools = new ArrayList<Tool>();
        listOfInHandTools.add(BASEBALL_BAT);
        listOfInHandTools.add(KATANA);
        equipment.setInHand(listOfInHandTools);
        assertThat(equipment.getInHand()).hasSize(2).contains(BASEBALL_BAT, KATANA);
        equipment.removeInHandTool(BASEBALL_BAT);
        assertThat(equipment.getInHand()).hasSize(1).contains(KATANA);
    }

    @Test
    void testRemoveInReserve() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInReserveTools = new ArrayList<Tool>();
        listOfInReserveTools.add(BASEBALL_BAT);
        listOfInReserveTools.add(KATANA);
        equipment.setInReserve(listOfInReserveTools);
        assertThat(equipment.getInReserve()).hasSize(2).contains(BASEBALL_BAT, KATANA);
        equipment.removeInReserveTool(BASEBALL_BAT);
        assertThat(equipment.getInReserve()).hasSize(1).contains(KATANA);
    }

    @Test
    void testMoveInHandToolToReserve() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInHandTools = new ArrayList<Tool>();
        listOfInHandTools.add(BASEBALL_BAT);
        listOfInHandTools.add(KATANA);
        equipment.setInHand(listOfInHandTools);
        equipment.moveInHandToolToReserve(KATANA);
        assertThat(equipment.getInHand()).hasSize(1).contains(BASEBALL_BAT);
        assertThat(equipment.getInReserve()).hasSize(1).contains(KATANA);

        assertThrows(NotFoundEquipmentException.class, () -> equipment.moveInHandToolToReserve(BOTTLED_WATER));
        equipment.addToInReserve(MOLOTOV);
        equipment.addToInReserve(KATANA);
        assertThat(equipment.getInReserve()).hasSize(3).contains(KATANA, MOLOTOV, KATANA);
        assertThrows(FullEquipmentException.class, () -> equipment.moveInHandToolToReserve(BASEBALL_BAT));
    }

    @Test
    void testMoveInReserveToolToHand() {
        Equipment equipment = new Equipment();
        ArrayList<Tool> listOfInReserveTools = new ArrayList<Tool>();
        listOfInReserveTools.add(BASEBALL_BAT);
        listOfInReserveTools.add(KATANA);
        equipment.setInReserve(listOfInReserveTools);
        equipment.moveInReserveToolToHand(KATANA);
        assertThat(equipment.getInReserve()).hasSize(1).contains(BASEBALL_BAT);
        assertThat(equipment.getInHand()).hasSize(1).contains(KATANA);

        assertThrows(NotFoundEquipmentException.class, () -> equipment.moveInReserveToolToHand(BOTTLED_WATER));

        equipment.addToInHand(MOLOTOV);
        assertThat(equipment.getInHand()).hasSize(2).contains(KATANA, MOLOTOV);
        assertThrows(FullEquipmentException.class, () -> equipment.moveInReserveToolToHand(KATANA));
    }
}
