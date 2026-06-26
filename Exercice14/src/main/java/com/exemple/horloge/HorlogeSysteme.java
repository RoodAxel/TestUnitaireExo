package com.exemple.horloge;

import java.time.LocalDate;

public class HorlogeSysteme implements Horloge {

    @Override
    public LocalDate aujourdhui() {
        return LocalDate.now();
    }
}
