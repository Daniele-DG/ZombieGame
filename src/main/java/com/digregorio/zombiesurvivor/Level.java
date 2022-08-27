package com.digregorio.zombiesurvivor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Level {
    BLUE("Blue", 0),
    YELLOW("Yellow", 6),
    ORANGE("Orange", 18),
    RED("Red", 42);

    private String name;
    private int minExperience;
}
