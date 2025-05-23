package app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated field corresponds to a column in a database table.
 * <p>
 * This annotation is intended for use with custom ORM utilities. It contains metadata
 * such as the column name, type, and constraints like primary key or nullability.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    /**
     * The name of the column in the database.
     *
     * @return the column name
     */
    String name();

    /**
     * The SQL data type of the column.
     *
     * @return the SQL type
     */
    String type();

    /**
     * Whether this column is a primary key.
     * Defaults to {@code false}.
     *
     * @return true if the column is a primary key, false otherwise
     */
    boolean primaryKey() default false;

    /**
     * Indicates whether the column can accept {@code null} values.
     * Defaults to {@code true}.
     *
     * @return true if the column is nullable, false otherwise
     */
    boolean nullable() default true;

    /**
     * Indicates whether the column value can be updated.
     * Defaults to {@code true}.
     *
     * @return true if the column is updatable, false otherwise
     */
    boolean updatable() default true;

    /**
     * If this column is a foreign key, specifies the name of the referenced table.
     * Defaults to an empty string if not a foreign key.
     *
     * @return the referenced foreign key table name
     */
    String foreignKeyTable() default "";

    /**
     * If this column is a foreign key, specifies the name of the referenced column.
     * Defaults to an empty string if not a foreign key.
     *
     * @return the referenced foreign key column name
     */
    String foreignKeyColumn() default "";
}