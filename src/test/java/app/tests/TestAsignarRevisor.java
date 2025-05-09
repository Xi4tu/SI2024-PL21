package app.tests;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import app.model.AsignarRevisoresModel;
import giis.demo.util.Database;
import junitparams.JUnitParamsRunner;

/**
 * Pruebas JUnit del proceso de negocio: Asignación de revisores a artículos.
 * Condiciones:
 * - El revisor no puede ser autor.
 * - El revisor no puede compartir grupo de investigación con los autores.
 * - Máximo 3 revisores por artículo.
 */
@RunWith(JUnitParamsRunner.class)
public class TestAsignarRevisor {

    private static Database db = new Database();
    private AsignarRevisoresModel model;

    @Before
    public void setUp() {
        db.createDatabase(true);
        model = new AsignarRevisoresModel();

        db.executeBatch(new String[]{
            "DELETE FROM Revision",
            "DELETE FROM Articulo_Usuario",
            "DELETE FROM Articulo",
            "DELETE FROM Usuario",
            "DELETE FROM Track",
            "DELETE FROM Conferencia",

            // Insertar conferencia, track y artículo
            "INSERT INTO Conferencia(idConferencia, nombre, deadlineEnvio, deadlineDiscusion, deadlineRevision) " +
            "VALUES (1, 'ConfTest', '2025-06-01', '2025-06-15', '2025-06-30')",

            "INSERT INTO Track(idTrack, idConferencia, nombre, palabrasClave) " +
            "VALUES (1, 1, 'TrackTest', 'IA')",

            // Insertar usuarios: autor y revisores
            "INSERT INTO Usuario(email, nombre, organizacion, grupoInvestigacion, palabrasClave) VALUES " +
            "('autor@uniovi.es', 'Autor Uno', 'Uni', 'GI', 'IA')," +
            "('revisor1@uniovi.es', 'Revisor Uno', 'Uni', 'GI', 'IA')," +
            "('revisor2@uniovi.es', 'Revisor Dos', 'Uni', 'G2', 'IA')," +
            "('revisor3@uniovi.es', 'Revisor Tres', 'Uni', 'G2', 'IA')," +
            "('revisor4@uniovi.es', 'Revisor Cuatro', 'Uni', 'G2', 'IA')",

            // Insertar artículo
            "INSERT INTO Articulo(idArticulo, idTrack, titulo, palabrasClave, palabrasClaveTrack, resumen, nombreFichero, fechaEnvio) " +
            "VALUES (1, 1, 'Articulo Test', 'IA', 'IA', 'Resumen', 'fichero.pdf', '2025-05-01')",

            // Autor del artículo
            "INSERT INTO Articulo_Usuario(idArticulo, emailUsuario, esEnviador) VALUES " +
            "(1, 'autor@uniovi.es', 1)"
        });
    }

    @Test
    public void testAsignacionValida() {
        try {
            model.asignarRevisor(1, "revisor4@uniovi.es");  // diferente GI y no autor
        } catch (Exception e) {
            fail("No debería fallar la asignación válida.");
        }
    }

    @Test
    public void testAsignacionAutorComoRevisor() {
        assertThrows(Exception.class, () -> {
            model.asignarRevisor(1, "autor@uniovi.es");
        });
    }

    @Test
    public void testAsignacionConGrupoInvestigacionIgual() {
        // Mismo grupo de investigación que el autor (GI)
        assertThrows(Exception.class, () -> {
            model.asignarRevisor(1, "revisor1@uniovi.es");
        });
    }

    @Test
    public void testAsignacionSuperaLimiteDeRevisores() {
        // Los tres revisores permitidos
        model.asignarRevisor(1, "revisor2@uniovi.es");
        model.asignarRevisor(1, "revisor3@uniovi.es");
        model.asignarRevisor(1, "revisor4@uniovi.es");

        // Cuarta asignación no permitida
        assertThrows(Exception.class, () -> {
            model.asignarRevisor(1, "revisor1@uniovi.es");
        });
    }
}
