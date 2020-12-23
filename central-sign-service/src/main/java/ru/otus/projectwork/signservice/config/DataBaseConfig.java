package ru.otus.projectwork.signservice.config;

import org.flywaydb.core.Flyway;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DataBaseConfig {

    @Value("${db.ds.hk.maximumPoolSize:5}")
    private int maximumPoolSize;

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("/db/migration")
                .load();
    }

    @Bean(name = "hikariDataSource")
    protected HikariDataSource hds(DataSource ds) {
        HikariDataSource hds = new HikariDataSource();
        hds.setMaximumPoolSize(maximumPoolSize);
        hds.setDataSource(ds);
        return hds;
    }


    @Bean(name = "jdbcTemplate")
    protected JdbcTemplate jdbc(@Qualifier("hikariDataSource") HikariDataSource ds) {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(ds);
        return template;
    }
}
