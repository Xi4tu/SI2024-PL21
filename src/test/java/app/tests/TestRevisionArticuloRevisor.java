package app.tests;

import static org.junit.Assert.*;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import app.dto.RevisionArticuloRevisionDTO;
import app.dto.RevisionArticuloRevisorDTO;
import app.model.RevisionArticuloRevisorModel;
import giis.demo.util.Database;

public class TestRevisionArticuloRevisor {

    private static Database db = new Database();
    private RevisionArticuloRevisorModel model;

    @Before
    public void setUp() {
        db.createDatabase(true);
        model = new RevisionArticuloRevisorModel();

        db.executeBatch(new String[]{
            "DELETE FROM Revision",
            "DELETE FROM Articulo",
            "DELETE FROM Usuario",
            "DELETE FROM Track",
            "DELETE FROM Conferencia",

            "INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) " +
            "VALUES (1, 'ConfTest', '2025-06-01', '2025-06-15', '2025-06-30')",

            "INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) VALUES (1, 1, 'TrackTest', 'IA')",

            "INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion, palabrasClave) VALUES " +
            "('revisor1@uniovi.es', 'Revisor Uno', 'Uni', 'GI1', 'IA')," +
            "('revisor2@uniovi.es', 'Revisor Dos', 'Uni', 'GI2', 'IA')," +
            "('revisor3@uniovi.es', 'Revisor Tres', 'Uni', 'GI3', 'IA')," +
            "('revisor4@uniovi.es', 'Revisor Cuatro', 'Uni', 'GI4', 'IA')",

            "INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) " +
            "VALUES (1, 1, 'Articulo A', 'IA', 'IA', 'Resumen', 'a.pdf', '2025-05-01')," +
            "       (2, 1, 'Articulo B', 'IA', 'IA', 'Resumen', 'b.pdf', '2025-05-01')",

            // Pendiente (para CP1)
            "INSERT INTO Revision(idArticulo, emailUsuario) VALUES (1, 'revisor1@uniovi.es')",

            // Revisado (para CP2 y CP5)
            "INSERT INTO Revision(idArticulo, emailUsuario, decisionRevisor, fechaRevision) " +
            "VALUES (2, 'revisor3@uniovi.es', 2, '2025-05-10')",

            // Otra revisión del mismo artículo (CP5/CP6)
            "INSERT INTO Revision(idArticulo, emailUsuario, decisionRevisor, fechaRevision) " +
            "VALUES (2, 'revisor2@uniovi.es', 1, '2025-05-10')"
        });
    }

    @Test
    public void testCP1_obtenerPendientes() {
        List<RevisionArticuloRevisorDTO> pendientes = model.obtenerArticulosAsignados("revisor1@uniovi.es");
        assertEquals(1, pendientes.size());
        assertEquals("Articulo A", pendientes.get(0).getTitulo());
    }

    @Test
    public void testCP2_obtenerRevisados() {
        List<RevisionArticuloRevisorDTO> revisados = model.obtenerArticulosRevisados("revisor3@uniovi.es");
        assertEquals(1, revisados.size());
        assertEquals("Articulo B", revisados.get(0).getTitulo());
    }

    @Test
    public void testCP3_guardarRevisionEnPlazo() {
        String fechaHoy = "2025-05-20";
      
            model.guardarOActualizarRevision(1, "revisor1@uniovi.es", "Comentario", "Interno", "Alto", 2, fechaHoy);
        
    }

    @Test
    public void testCP4_guardarRevisionFueraDePlazo() {
        String fechaFuera = "2025-07-01"; // después del deadline 2025-06-30
        boolean fueraDePlazo = !model.periodoRevisionActivoPorConferencia(1);
        if (fueraDePlazo) {
            assertThrows(Exception.class, () -> {
                model.guardarOActualizarRevision(1, "revisor1@uniovi.es", "Comentario", "Coord", "Medio", 1, fechaFuera);
            });
        }
    }

    @Test
    public void testCP5_verRevisionesOtrosTrasRevisar() {
        List<RevisionArticuloRevisionDTO> revisionesOtros = model.obtenerRevisionesDeOtrosRevisores(2, "revisor3@uniovi.es");
        assertEquals(1, revisionesOtros.size()); // debe ver revisión de revisor2
        assertEquals("revisor2@uniovi.es", revisionesOtros.get(0).getEmail());
    }

    @Test
    public void testCP6_verRevisionesOtrosSinHaberRevisado() {
        List<RevisionArticuloRevisionDTO> revisionesOtros = model.obtenerRevisionesDeOtrosRevisores(2, "revisor4@uniovi.es");
        assertEquals(1, revisionesOtros.size()); 
    }
}
