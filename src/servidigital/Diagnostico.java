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
public class Diagnostico {
    private String id_diagnostico;
    private String Diagnostico;

    public String getID()           {   return id_diagnostico;  }
    public String getDiagnostico()  {   return Diagnostico;     }

    public void setID(String I)         {   id_diagnostico = I; }
    public void setDiagnostico(String D){   Diagnostico = D;    }

    public Diagnostico() {
        id_diagnostico = "";
        Diagnostico = "";
    }
    public Diagnostico(String ID) throws SQLException {
        Main.BD.Consultar("select * from diagnosticos where id_diagnostico = "+ ID);
        if(Main.BD.Siguiente()) {
            id_diagnostico = ID;
            Diagnostico = Main.BD.Campo("diagnostico");
        }
    }
    public TableModel buscarDiagnosticos(String B) throws SQLException  {
        Main.BD.Consultar("select * from diagnosticos where instr(diagnostico,'"+B+"')");
        return Main.BD.Modelo();
    }
    public void registrar(String D) throws SQLException {
        Main.BD.Ejecutar("insert into diagnosticos (diagnostico) values ('"+D+"')");
    }
    public void actualizar(String D) throws SQLException {
        Main.BD.Ejecutar("update diagnosticos set diagnostico = '"+D+"' where id_diagnostico = " + id_diagnostico);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from diagnosticos where id_diagnostico = "+ id_diagnostico);
    }
}
