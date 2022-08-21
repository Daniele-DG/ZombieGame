package com.gitproject.zombiesurvivor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Turn {
    private Survivor survivor;
    private Action[] actions = new Action[3];
}
