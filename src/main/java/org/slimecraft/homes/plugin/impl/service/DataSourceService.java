package org.slimecraft.homes.plugin.impl.service;

import javax.sql.DataSource;

public interface DataSourceService {
    void initialize();

    DataSource getDataSource();
}
