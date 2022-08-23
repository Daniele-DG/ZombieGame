package com.gitproject.zombiesurvivor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum Tool {
    BASEBALL_BAT("Baseball bat"), FRYING_PAN("Frying pan"), KATANA("Katana"), PISTOL("Pistol"),
    MOLOTOV("Molotov"), BOTTLED_WATER("Bottled Water");

    private String name;
}
