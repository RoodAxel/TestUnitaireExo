package com.exemple;

import java.util.ArrayList;
import java.util.List;

public class Frame {

    private static final int MAX_QUILLES = 10;

    private int score;
    private boolean lastFrame;
    private IGenerateur generateur;
    private List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public boolean makeRoll() {
        if (!peutLancer()) {
            return false;
        }

        int quillesTombees = generateur.randomPin(quillesDisponibles());
        rolls.add(new Roll(quillesTombees));
        score += quillesTombees;
        return true;
    }

    public int getScore() {
        return score;
    }

    private boolean peutLancer() {
        if (lastFrame) {
            return switch (rolls.size()) {
                case 0, 1 -> true;
                case 2 -> strikeOuSpareSurLesDeuxPremiers();
                default -> false;
            };
        }

        return switch (rolls.size()) {
            case 0 -> true;
            case 1 -> !estUnStrike(rolls.get(0));
            default -> false;
        };
    }

    private boolean strikeOuSpareSurLesDeuxPremiers() {
        return rolls.get(0).getPins() + rolls.get(1).getPins() >= MAX_QUILLES;
    }

    private boolean estUnStrike(Roll roll) {
        return roll.getPins() == MAX_QUILLES;
    }

    private int quillesDisponibles() {
        if (rolls.isEmpty()) {
            return MAX_QUILLES;
        }

        Roll dernier = rolls.get(rolls.size() - 1);
        if (estUnStrike(dernier)) {
            return MAX_QUILLES;
        }

        if (lastFrame && rolls.size() >= 2) {
            Roll avantDernier = rolls.get(rolls.size() - 2);
            if (avantDernier.getPins() + dernier.getPins() == MAX_QUILLES) {
                return MAX_QUILLES;
            }
        }

        return MAX_QUILLES - dernier.getPins();
    }
}
