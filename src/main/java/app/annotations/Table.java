package app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the annotated class is mapped to a database table.
 * <p>
 * This annotation is used to mark an entity class and associate it with a specific
 * table name in the database.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * The name of the table in the database that this class maps to.
     *
     * @return the table name
     */
    String name();
}

