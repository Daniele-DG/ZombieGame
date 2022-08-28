package com.digregorio.zombiesurvivor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import com.digregorio.zombiesurvivor.exceptions.EndGameException;
import com.digregorio.zombiesurvivor.exceptions.NameAlreadyExistException;
import com.digregorio.zombiesurvivor.exceptions.SurvivorNotFoundException;

class GameTest {

    @Test
    void testStart() {
        Game game = new Game();
        assertThat(game.getLevel()).isNotEqualTo(Level.BLUE);
        assertThat(game.getSurvivors()).isNull();
        game.start();
        assertThat(game.getLevel()).isEqualTo(Level.BLUE);
        assertThat(game.getSurvivors()).isEqualTo(new ArrayList<>());

        ArrayList<Survivor> survivors = new ArrayList<>();
        Survivor survivor = new Survivor("Pino");
        survivors.add(survivor);
        game.setSurvivors(survivors);
        assertThat(game.getSurvivors()).hasSize(1).contains(survivor);
        game.start();
        assertThat(game.getSurvivors()).doesNotContain(survivor).isEmpty();
    }

    @Test
    void testAddSurvivor() {
        Game game = new Game();
        game.start();
        Survivor survivorPino = new Survivor("Pino");
        game.addSurvivor(survivorPino);
        assertThat(game.getSurvivors()).hasSize(1).contains(survivorPino);

        Survivor survivorGino = new Survivor("Gino");
        survivorGino.incrementExperience(6);
        assertThat(survivorGino.getCurrentLevel()).isEqualTo(Level.YELLOW);
        NameAlreadyExistException exception = assertThrows(NameAlreadyExistException.class,
                () -> game.addSurvivor(survivorPino));
        assertThat(exception.getMessage()).contains("There is a survivor with the same name. Choose another name.");
        game.addSurvivor(survivorGino);
        assertThat(game.getSurvivors()).hasSize(2).contains(survivorPino, survivorGino);
        assertThat(game.getLevel()).isEqualTo(Level.YELLOW);
    }

    @Test
    void testAddExperienceToSurvivor() {
        Game game = new Game();
        game.start();
        Survivor survivorDead = new Survivor("Pino");
        survivorDead.addWound();
        survivorDead.addWound();
        assertThat(survivorDead.isDead()).isTrue();
        SurvivorNotFoundException exception = assertThrows(SurvivorNotFoundException.class,
                () -> game.addExperienceToSurvivor(survivorDead));
        assertThat(exception.getMessage()).contains("Survivor not found.");
        game.addSurvivor(survivorDead);
        SurvivorNotFoundException exception_dead = assertThrows(SurvivorNotFoundException.class,
                () -> game.addExperienceToSurvivor(survivorDead));
        assertThat(exception_dead.getMessage()).contains("Survivor not found.");

        Survivor survivorAlive = new Survivor("Gino");
        game.addSurvivor(survivorAlive);
        game.addExperienceToSurvivor(survivorAlive);
        assertThat(game.getSurvivors()).hasSize(2).contains(survivorAlive);
        assertThat(survivorAlive.getCurrentExperience()).isEqualTo(1);

        Survivor survivorToLevelUp = new Survivor("Alfredo");
        survivorToLevelUp.incrementExperience(5);
        game.addSurvivor(survivorToLevelUp);
        assertThat(game.getLevel()).isEqualTo(Level.BLUE);
        game.addExperienceToSurvivor(survivorToLevelUp);
        assertThat(game.getSurvivors()).hasSize(3).contains(survivorToLevelUp);
        assertThat(survivorToLevelUp.getCurrentExperience()).isEqualTo(6);
        assertThat(survivorToLevelUp.getCurrentLevel()).isEqualTo(Level.YELLOW);
        assertThat(game.getLevel()).isEqualTo(Level.YELLOW);
    }

    @Test
    void testAddWound() {
        Game game = new Game();
        game.start();
        Survivor survivor = new Survivor("Pino");

        SurvivorNotFoundException exception = assertThrows(SurvivorNotFoundException.class,
                () -> game.addWound(survivor));
        assertThat(exception.getMessage()).contains("No survivor has been found.");
        game.addSurvivor(survivor);
        game.addWound(survivor);
        EndGameException exceptionEndGame = assertThrows(EndGameException.class,
                () -> game.addWound(survivor));
        assertThat(exceptionEndGame.getMessage()).contains("The game is over. No survivor left.");
    }

}
