import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

public class ConceptosDefinicionesTOATest {

    @Test
    public void testConstructorAndGetters(){
        ConceptoDTO concepto = new ConceptoDTO(1, "Concepto 1");
        DefinicionDTO definicion = new DefinicionDTO(1, "Definicion 1", 10);

        List<ConceptoDTO> conceptos = new ArrayList<>();
        conceptos.add(concepto);
        List<DefinicionDTO> definiciones = new ArrayList<>();
        definiciones.add(definicion);

        ConceptosDefinicionesTOA toa = new ConceptosDefinicionesTOA(conceptos, definiciones);

        assertEquals(conceptos, toa.getConceptos());
        assertEquals(definiciones, toa.getDefiniciones());
    }

    @Test
    public void testSetters(){
        ConceptoDTO concepto1 = new ConceptoDTO(1, "Concepto 1");
        DefinicionDTO definicion1 = new DefinicionDTO(1, "Definicion 1", 10);

        ConceptoDTO concepto2 = new ConceptoDTO(2, "Concepto 2");
        DefinicionDTO definicion2 = new DefinicionDTO(2, "Definicion 2", 20);

        List<ConceptoDTO> conceptosIniciales = new ArrayList<>();
        conceptosIniciales.add(concepto1);
        List<DefinicionDTO> definicionesIniciales = new ArrayList<>();
        definicionesIniciales.add(definicion1);

        ConceptosDefinicionesTOA toa = new ConceptosDefinicionesTOA(conceptosIniciales, definicionesIniciales);

        List<ConceptoDTO> conceptosNuevos = new ArrayList<>();
        conceptosNuevos.add(concepto2);
        List<DefinicionDTO> definicionesNuevas = new ArrayList<>();
        definicionesNuevas.add(definicion2);

        toa.setConceptos(conceptosNuevos);
        toa.setDefiniciones(definicionesNuevas);

        assertEquals(conceptosNuevos, toa.getConceptos());
        assertEquals(definicionesNuevas, toa.getDefiniciones());
    }

    @Test
    public void testEqualsAndHashCode(){
        ConceptoDTO concepto1 = new ConceptoDTO(1, "Concepto 1");
        DefinicionDTO definicion1 = new DefinicionDTO(1, "Definicion 1", 10);

        ConceptoDTO concepto2 = new ConceptoDTO(2, "Concepto 2");
        DefinicionDTO definicion2 = new DefinicionDTO(2, "Definicion 2", 20);

        List<ConceptoDTO> conceptos1 = new ArrayList<>();
        conceptos1.add(concepto1);
        List<DefinicionDTO> definiciones1 = new ArrayList<>();
        definiciones1.add(definicion1);

        ConceptosDefinicionesTOA toa1 = new ConceptosDefinicionesTOA(conceptos1, definiciones1);
        ConceptosDefinicionesTOA toa2 = new ConceptosDefinicionesTOA(conceptos1, definiciones1);

        List<ConceptoDTO> conceptos2 = new ArrayList<>();
        conceptos2.add(concepto2);
        List<DefinicionDTO> definiciones2 = new ArrayList<>();
        definiciones2.add(definicion2);

        ConceptosDefinicionesTOA toa3 = new ConceptosDefinicionesTOA(conceptos2, definiciones2);

        assertEquals(toa1, toa2);
        assertNotEquals(toa1, toa3);

        assertEquals(toa1.hashCode(), toa2.hashCode());
        assertNotEquals(toa1.hashCode(), toa3.hashCode());
    }
}
