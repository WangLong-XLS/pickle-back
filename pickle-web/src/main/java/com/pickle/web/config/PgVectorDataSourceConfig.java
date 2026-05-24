package com.pickle.web.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * PostgreSQL 数据源配置（用于向量存储）
 */
//@Configuration
public class PgVectorDataSourceConfig {

    /**
     * PostgreSQL 数据源
     */
    @Bean(name = "pgVectorDataSource")
    @ConfigurationProperties(prefix = "pg.datasource")
    public DataSource pgVectorDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /**
     * JdbcTemplate for PostgreSQL
     */
    @Bean(name = "pgVectorJdbcTemplate")
    public JdbcTemplate pgVectorJdbcTemplate(
            @Qualifier("pgVectorDataSource") DataSource pgVectorDataSource) {
        return new JdbcTemplate(pgVectorDataSource);
    }

    /**
     * PgVectorStore（手动配置，使用阿里云 DashScope EmbeddingModel）
     */
    @Bean
    public PgVectorStore pgVectorStore(
            @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate,
            EmbeddingModel embeddingModel) {

        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .initializeSchema(true)               // 自动创建表
                .dimensions(1536)                     // 向量维度（text-embedding-v1 是 1536）
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .build();
    }
}