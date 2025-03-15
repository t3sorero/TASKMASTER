import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ConceptoDTOTest {


    @Test
    public void testConstructorAndGetters(){
        ConceptoDTO concepto = new ConceptoDTO(1, "Concepto 1");

        assertEquals(1, concepto.getId());
        assertEquals("Concepto 1", concepto.getNombre());
    }

    @Test
    public void testSetters(){
        ConceptoDTO concepto = new ConceptoDTO(0, "");

        concepto.setId(2);
        concepto.setNombre("Concepto 2");

        assertEquals(2, concepto.getId());
        assertEquals("Concepto 2", concepto.getNombre());
    }

    @Test
    public void testEqualsAndHashCode(){
        ConceptoDTO concepto1 = new ConceptoDTO(1, "Concepto 1");
        ConceptoDTO concepto2 = new ConceptoDTO(1, "Concepto 1");
        ConceptoDTO concepto3 = new ConceptoDTO(2, "Concepto 2");

        assertEquals(concepto1, concepto2);
        assertNotEquals(concepto1, concepto3);

        assertEquals(concepto1.hashCode(), concepto2.hashCode());
        assertNotEquals(concepto1.hashCode(), concepto3.hashCode());
    }

}
