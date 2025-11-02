package co.edu.unicauca.usuario_service.config;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("serial")
public class SchemaConnectionProvider implements MultiTenantConnectionProvider {

    private final DataSource dataSource;
    private final Map<String, Boolean> schemaCache = new ConcurrentHashMap<>();

    public SchemaConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        DataSourceUtils.releaseConnection(connection, dataSource);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = getAnyConnection();

        if (tenantIdentifier == null || tenantIdentifier.isBlank()) {
            tenantIdentifier = "public";
        }

        // Validar si el schema existe
        if (!schemaCache.containsKey(tenantIdentifier)) {
            var rs = connection.getMetaData().getSchemas();
            boolean exists = false;
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(tenantIdentifier)) {
                    exists = true;
                    break;
                }
            }
            rs.close();
            if (!exists) {
                connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS \"" + tenantIdentifier + "\"");
            }
            schemaCache.put(tenantIdentifier, true);
        }

        connection.setSchema(tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.setSchema("public"); // volver al esquema por defecto
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
