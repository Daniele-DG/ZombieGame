package com.digregorio.zombiesurvivor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.digregorio.zombiesurvivor.exceptions.FullEquipmentException;
import com.digregorio.zombiesurvivor.exceptions.NotFoundEquipmentException;

class SurvivorTest {
    @Test
    void testAddInHand() {
        Survivor survivor = new Survivor("name");
        assertThat(survivor.getEquipment().getInHand()).isEmpty();
        survivor.addInHand(Tool.KATANA);
        assertThat(survivor.getEquipment().getInHand()).hasSize(1);
        survivor.addInHand(Tool.BOTTLED_WATER);
        assertThat(survivor.getEquipment().getInHand()).hasSize(2);
        survivor.addWound();
        assertThat(survivor.getEquipment().getInHand()).hasSize(1);
        assertThat(survivor.getEquipment().getInReserve()).hasSize(1);
        FullEquipmentException exception = assertThrows(FullEquipmentException.class,
                () -> survivor.addInHand(Tool.BASEBALL_BAT));
        assertThat(exception.getMessage())
                .contains("In hand equipment is full. Please move some item in your reserve.");
    }

    @Test
    void testAddInReserve() {
        Survivor survivor = new Survivor("name");
        assertThat(survivor.getEquipment().getInHand()).isEmpty();
        survivor.addInReserve(Tool.KATANA);
        survivor.addInReserve(Tool.KATANA);
        survivor.addInReserve(Tool.KATANA);
        assertThat(survivor.getEquipment().getInReserve()).hasSize(3);
        FullEquipmentException exception = assertThrows(FullEquipmentException.class,
                () -> survivor.addInReserve(Tool.BASEBALL_BAT));
        assertThat(exception.getMessage())
                .contains("In reserve equipment is full. Please remove some item to add a new one.");

    }

    @Test
    void testAddWound() {
        Survivor first = new Survivor("name");
        assertThat(first.getWound()).isZero();
        first.addWound();
        assertThat(first.getWound()).isEqualTo(1);
        first.addWound();
        assertThat(first.getWound()).isEqualTo(2);
        Survivor second = new Survivor("other name");
        second.addInHand(Tool.KATANA);
        second.addInHand(Tool.BOTTLED_WATER);
        assertThat(second.getEquipment().getInHand()).hasSize(2);
        assertThat(second.getEquipment().getInReserve()).isEmpty();
        second.addWound();
        assertThat(second.getEquipment().getInHand()).hasSize(1);
        assertThat(second.getEquipment().getInReserve()).hasSize(1);
        second.addWound();
        assertThat(second.getEquipment().getInHand()).hasSize(1);
        assertThat(second.getEquipment().getInReserve()).hasSize(1);
    }

    @Test
    void testIncrementExperienceAndLevel() {
        Survivor survivor = new Survivor("name");
        assertThat(survivor.getCurrentExperience()).isZero();
        assertThat(survivor.getCurrentLevel()).isEqualTo(Level.BLUE);
        survivor.incrementExperience(6);
        assertThat(survivor.getCurrentExperience()).isEqualTo(6);
        assertThat(survivor.getCurrentLevel()).isEqualTo(Level.YELLOW);
        survivor.incrementExperience(40);
        assertThat(survivor.getCurrentExperience()).isEqualTo(46);
        assertThat(survivor.getCurrentLevel()).isEqualTo(Level.RED);
    }

    @Test
    void testIsDead() {
        Survivor s = new Survivor("name");
        s.addWound();
        assertThat(s.isDead()).isFalse();
        s.addWound();
        assertThat(s.isDead()).isTrue();
    }

    @Test
    void testRemoveInHandTool() {
        Survivor s = new Survivor("name");
        NotFoundEquipmentException exception = assertThrows(NotFoundEquipmentException.class,
                () -> s.removeInHandTool(Tool.KATANA));
        assertThat(exception.getMessage()).contains("Tool not found in your selected inventory.");
        s.addInHand(Tool.KATANA);
        assertThat(s.getEquipment().getInHand()).contains(Tool.KATANA);
        s.removeInHandTool(Tool.KATANA);
        assertThat(s.getEquipment().getInHand()).doesNotContain(Tool.KATANA);
    }

    @Test
    void testRemoveInReserveTool() {
        Survivor s = new Survivor("name");
        NotFoundEquipmentException exception = assertThrows(NotFoundEquipmentException.class,
                () -> s.removeInReserveTool(Tool.KATANA));
        assertThat(exception.getMessage()).contains("Tool not found in your selected inventory.");
        s.addInReserve(Tool.KATANA);
        assertThat(s.getEquipment().getInReserve()).contains(Tool.KATANA);
        s.removeInReserveTool(Tool.KATANA);
        assertThat(s.getEquipment().getInReserve()).doesNotContain(Tool.KATANA);
    }
}
