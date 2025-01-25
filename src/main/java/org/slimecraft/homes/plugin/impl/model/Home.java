package org.slimecraft.homes.plugin.impl.model;

import org.slimecraft.bedrock.model.Identifiable;
import org.slimecraft.bedrock.model.SafeLocation;

import java.util.UUID;

public class Home implements Identifiable<UUID> {
    private final UUID identifier;
    private String name;
    private SafeLocation safeLocation;

    public Home(UUID identifier) {
        this.identifier = identifier;
    }

    public Home(UUID identifier, String name, SafeLocation safeLocation) {
        this.identifier = identifier;
        this.name = name;
        this.safeLocation = safeLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SafeLocation getSafeLocation() {
        return safeLocation;
    }

    public void setSafeLocation(SafeLocation safeLocation) {
        this.safeLocation = safeLocation;
    }

    @Override
    public UUID getIdentifier() {
        return this.identifier;
    }
}
