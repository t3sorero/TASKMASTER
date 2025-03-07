import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearDB {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:conceptos.db";

        String crearTablaConceptos = "CREATE TABLE IF NOT EXISTS conceptos ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "concepto TEXT NOT NULL, "
                + "definicion TEXT NOT NULL"
                + ");";

        String[] inserts = {
            "INSERT INTO conceptos (concepto, definicion) VALUES ('Product Owner', 'Persona responsable de maximizar el valor del producto.')",
            "INSERT INTO conceptos (concepto, definicion) VALUES ('Sprint', 'Iteración corta donde se desarrolla un incremento del producto.')",
            "INSERT INTO conceptos (concepto, definicion) VALUES ('Backlog', 'Lista ordenada de funcionalidades del producto.')",
            "INSERT INTO conceptos (concepto, definicion) VALUES ('Scrum Master', 'Encargado de asegurar que el equipo sigue las prácticas de Scrum.')"
        };

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Creación de la tabla conceptos
            stmt.execute("CREATE TABLE IF NOT EXISTS conceptos (\n"
                    + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + " concepto TEXT NOT NULL,\n"
                    + " definicion TEXT NOT NULL\n"
                    + ");");

            // Insertar conceptos y definiciones
            for (String insert : inserts) {
                stmt.execute(insert);
            }

            System.out.println("Base de datos y tabla creadas correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos: " + e.getMessage());
        }
    }
}
