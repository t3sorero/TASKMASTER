import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DefinicionDTOTest {

    @Test
    public void testConstructorAndGetter() {
        DefinicionDTO definicion = new DefinicionDTO(1, "Definicion de ejemplo", 10);

        assertEquals(1, definicion.getId());
        assertEquals("Definicion de ejemplo", definicion.getDescripcion());
        assertEquals(10, definicion.getConceptoId());
    }

    @Test
    public void testSetters() {
        DefinicionDTO definicion = new DefinicionDTO(0, "", 0);

        definicion.setId(2);
        definicion.setDescripcion("Definicion de ejemplo 2");
        definicion.setConceptoId(20);

        assertEquals(2, definicion.getId());
        assertEquals("Definicion de ejemplo 2", definicion.getDescripcion());
        assertEquals(20, definicion.getConceptoId());
    }

    @Test
    public void testEqualsAndHashCode() {
        DefinicionDTO definicion1 = new DefinicionDTO(1, "Definicion de ejemplo", 10);
        DefinicionDTO definicion2 = new DefinicionDTO(1, "Definicion de ejemplo", 10);
        DefinicionDTO definicion3 = new DefinicionDTO(2, "Definicion de ejemplo 2", 20);

        assertEquals(definicion1, definicion2);
        assertNotEquals(definicion1, definicion3);

        assertEquals(definicion1.hashCode(), definicion2.hashCode());
        assertNotEquals(definicion1.hashCode(), definicion3.hashCode());
    }
}
