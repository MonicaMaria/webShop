package main.java.model;

import java.util.Arrays;

public enum Category {
    ELECTRONICS("Electronics"),
    COMPUTERS("Computers"),
    SMART_HOME("Smart Home"),
    FASHION("Fashion"),
    HEALTH("Health"),
    SPORTS("Sports"),
    GAMES("Games");

    private String name;

    Category(String name) {
        this.name = name;
    }

    public static Category getCategoryForName(String name) {
        return Arrays.stream(values())
                .filter(value -> value.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }
}
