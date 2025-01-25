package org.slimecraft.homes.plugin.impl.service;

import org.slimecraft.bedrock.model.Dao;
import org.slimecraft.bedrock.model.MappedDao;
import org.slimecraft.homes.plugin.impl.model.Home;
import org.slimecraft.homes.plugin.impl.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserServiceImpl implements UserService {
    private final Dao<UUID, User> userDao;
    private final Dao<UUID, Home> homeDao;
    private final MappedDao<UUID, Home> userHomeMappedDao;

    public UserServiceImpl(Dao<UUID, User> userDao, Dao<UUID, Home> homeDao, MappedDao<UUID, Home> userHomeMappedDao) {
        this.userDao = userDao;
        this.homeDao = homeDao;
        this.userHomeMappedDao = userHomeMappedDao;
    }

    @Override
    public void createNewUser(UUID key) {
        CompletableFuture.runAsync(() -> {
            this.userDao.create(new User(key));
        });
    }

    @Override
    public void addHome(UUID key, Home home) {
        CompletableFuture.runAsync(() -> {
            this.homeDao.create(home);
            this.userHomeMappedDao.create(key, home);
        });
    }

    @Override
    public void removeHome(UUID key, UUID otherKey) {
        CompletableFuture.runAsync(() -> {
            this.userHomeMappedDao.delete(key, otherKey);
            this.homeDao.delete(otherKey);
        });
    }

    @Override
    public CompletableFuture<List<Home>> getHomes(UUID key) {
        return CompletableFuture.supplyAsync(() -> {
            List<Home> mappedHomes = this.userHomeMappedDao.get(key);

            for (Home mappedHome : mappedHomes) {
                final Optional<Home> daoHomeOptional = this.homeDao.get(mappedHome.getIdentifier());
                if (daoHomeOptional.isEmpty()) continue;
                final Home daoHome = daoHomeOptional.get();

                mappedHome.setName(daoHome.getName());
                mappedHome.setSafeLocation(daoHome.getSafeLocation());
            }
            return mappedHomes;
        });
    }

    @Override
    public CompletableFuture<Boolean> userExists(UUID key) {
        return CompletableFuture.supplyAsync(() -> this.userDao.get(key).isPresent());
    }
}
