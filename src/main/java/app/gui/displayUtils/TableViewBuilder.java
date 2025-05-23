package app.gui.displayUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import app.annotations.Column;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.sql.Timestamp;

/**
 * Utility class for dynamically building JavaFX {@link TableView} components based on annotated model classes.
 * <p>
 * It uses reflection to inspect fields annotated with {@link Column}, automatically creating table columns
 * for each and populating their values from the given data.
 */
public class TableViewBuilder {

    /**
     * Default constructor.
     */
    public TableViewBuilder() {}

    /**
     * Builds a {@link TableView} using the given list of items and their class type.
     * The fields annotated with {@link Column} will be displayed as columns in the table.
     *
     * @param items the list of items to populate the table with
     * @param type  the class type of the items
     * @param <T>   the type parameter of the model
     * @return a configured {@code TableView<T>} instance
     */
    public static <T> TableView<T> buildTableView(List<T> items, Class<T> type) {
        TableView<T> tableView = new TableView<>();

        if (items == null || items.isEmpty()) {
            return tableView;
        }

        List<TableColumn<T, Object>> columns = Arrays.stream(type.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .peek(field -> field.setAccessible(true))
                .map(TableViewBuilder::<T>getTableColumn)
                .toList();

        tableView.getColumns().addAll(columns);
        tableView.setItems(FXCollections.observableArrayList(items));
        return tableView;
    }

    /**
     * Creates a {@link TableColumn} for the given field.
     *
     * @param field the field to create the column for
     * @param <T>   the type of the objects in the table
     * @return a table column configured to show values from the specified field
     */
    private static <T> TableColumn<T, Object> getTableColumn(Field field) {
        Column columnAnnotation = field.getAnnotation(Column.class);

        String formattedColumnName = formatColumnName(columnAnnotation.name());
        TableColumn<T, Object> column = new TableColumn<>(formattedColumnName);

        column.setCellValueFactory(cellData -> {
            T instance = cellData.getValue();
            try {
                Object value = field.get(instance);
                if (value instanceof Timestamp timestamp) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    return new SimpleObjectProperty<>(timestamp.toLocalDateTime().format(formatter));
                }
                return new SimpleObjectProperty<>(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return new SimpleObjectProperty<>(null);
            }
        });
        return column;
    }

    /**
     * Formats a column name by converting snake_case to "Title Case".
     *
     * @param original the original column name
     * @return a human-readable version of the column name
     */
    private static String formatColumnName(String original) {
        return Arrays.stream(original.split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .reduce((w1, w2) -> w1 + " " + w2)
                .orElse(original);
    }

}
