package org.slimecraft.homes.plugin.impl.service;

import org.slimecraft.homes.plugin.impl.model.Home;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    void createNewUser(UUID key);

    void addHome(UUID key, Home home);

    void removeHome(UUID key, UUID otherKey);

    CompletableFuture<List<Home>> getHomes(UUID key);

    CompletableFuture<Boolean> isCreatingHome(UUID key);

    void setCreatingHome(UUID key, boolean creatingHome);

    CompletableFuture<Boolean> exists(UUID key);
}
