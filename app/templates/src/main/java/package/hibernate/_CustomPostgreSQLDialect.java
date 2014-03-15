package <%=packageName%>.hibernate;

import org.hibernate.dialect.PostgresPlusDialect;

import java.sql.Types;

/**
 *
 */
public class CustomPostgreSQLDialect extends PostgresPlusDialect {
    public CustomPostgreSQLDialect() {
        super();
        registerColumnType(Types.OTHER, "pg-uuid");
        registerHibernateType(Types.OTHER, "pg-uuid");
    }
}
