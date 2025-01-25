package org.slimecraft.homes.plugin.impl.model;

import org.slimecraft.bedrock.model.Identifiable;

import java.util.List;
import java.util.UUID;

public class User implements Identifiable<UUID> {
    private final UUID identifier;
    private List<Home> homes;

    public User(UUID identifier) {
        this.identifier = identifier;
    }

    public User(UUID identifier, List<Home> homes) {
        this.identifier = identifier;
        this.homes = homes;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }
}
