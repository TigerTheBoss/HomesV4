package org.slimecraft.homes.plugin.impl.service;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ConfigurationServiceImpl implements ConfigurationService {
    private final YamlConfigurationLoader configurationLoader;
    private CommentedConfigurationNode root;
    private final CommentedConfigurationNode teleportWaitTimeNode;

    public ConfigurationServiceImpl(YamlConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
        try {
            this.root = this.configurationLoader.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        teleportWaitTimeNode = this.root.node("teleport-wait-time");
    }

    @Override
    public int getTeleportWaitTime() {
        return this.teleportWaitTimeNode.getInt();
    }

    @Override
    public void setTeleportWaitTime(int teleportWaitTime) {
        try {
            this.teleportWaitTimeNode.set(teleportWaitTime);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
    }
}
