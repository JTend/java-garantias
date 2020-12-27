/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

import java.sql.SQLException;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Solucion {
    private String id_solucion;
    private String Solucion;

    public String getID()           {   return id_solucion; }
    public String getSolucion()     {   return Solucion;    }

    public void setID(String I)         { id_solucion = I;  }
    public void setSolucion(String S)   { Solucion = S;     }

    public Solucion() {
        id_solucion = "";
        Solucion = "";
    }
    public Solucion(String ID) throws SQLException {
        Main.BD.Consultar("select * from soluciones where id_solucion = " + ID);
        if(Main.BD.Siguiente()) {
            id_solucion = Main.BD.Campo("id_solucion");
            Solucion = Main.BD.Campo("solucion");
        }
    }
    public TableModel buscarSoluciones(String Sol) throws SQLException {
        Main.BD.Consultar("select * from soluciones where instr(solucion,'"+Sol+"')");
        return Main.BD.Modelo();
    }
    public TableModel buscarPorDiagnostico(String Diag) throws SQLException {
        Main.BD.Consultar("select soluciones.* from diagnosticos,casos,soluciones "
                + "where casos.id_diagnostico = diagnosticos.id_diagnostico and casos.id_solucion = soluciones.id_solucion and instr(diagnostico,'"+Diag+"'");
        return Main.BD.Modelo();
    }
    public void registrar(String S) throws SQLException {
        Main.BD.Ejecutar("insert into soluciones (solucion) values ('"+S+"')");
    }
    public void actualizar(String S) throws SQLException {
        Main.BD.Ejecutar("update soluciones set solucion = '"+S+"' where id_solucion = "+id_solucion);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from soluciones where id_solucion = " + id_solucion);
    }
}
