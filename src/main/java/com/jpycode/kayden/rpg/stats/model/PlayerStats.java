package com.jpycode.kayden.rpg.stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PlayerStats {
    private UUID playerUUID;
    private int strength;
    private int health;
    private int defense;
    private int agility;
    private int intelligence;
}
