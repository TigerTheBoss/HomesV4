package org.slimecraft.homes.plugin.impl.model;

import org.slimecraft.bedrock.model.Identifiable;

import java.util.List;
import java.util.UUID;

public class User implements Identifiable<UUID> {
    private final UUID identifier;
    private List<Home> homes;
    private boolean creatingHome;

    public User(UUID identifier) {
        this.identifier = identifier;
    }

    public User(UUID identifier, List<Home> homes, boolean creatingHome) {
        this.identifier = identifier;
        this.homes = homes;
        this.creatingHome = creatingHome;
    }

    public boolean isCreatingHome() {
        return creatingHome;
    }

    public void setCreatingHome(boolean creatingHome) {
        this.creatingHome = creatingHome;
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
