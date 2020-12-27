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
public class Tecnico {
    private String Cedula;
    private String Empresa;
    private String Nombre;
    private String Telefono;

    public String getCedula()  {   return Cedula;  }
    public String getEmpresa() {   return Empresa; }
    public String getNombre()  {   return Nombre;  }
    public String getTelefono(){   return Telefono;}

    public void setCedula(String C)    {   Cedula = C;     }
    public void setEmpresa(String E)   {   Empresa = E;    }
    public void setNombre(String N)    {   Nombre = N;     }
    public void setTelefono(String T)  {   Telefono = T;   }

    public Tecnico() {
        Cedula = "";
        Empresa = "";
        Nombre = "";
        Telefono = "";
    }
    public Tecnico(String ID) throws SQLException {
        Main.BD.Consultar("select * from tecnicos where cedula = " + ID);
        if(Main.BD.Siguiente()) {
            Cedula = ID;
            Empresa = Main.BD.Campo("empresa");
            Nombre = Main.BD.Campo("nombre");
            Telefono = Main.BD.Campo("telefono");
        }
    }
    public boolean cedValida(String C) throws SQLException {
        Main.BD.Consultar("select * from tecnicos where cedula = " + C);
        if(Main.BD.hayRegistros())
            return false;
        else
            return true;
    }
    public TableModel buscarTecnicos(String B, String E) throws SQLException {
        Main.BD.Consultar("select * from tecnicos where (instr(cedula,'"+B+"') or instr(nombre,'"+B+"')) and empresa = '"+E+"'");
        return Main.BD.Modelo();
    }
    public void registrar(String C, String E, String N, String T) throws SQLException {
        Main.BD.Ejecutar("insert into tecnicos values ("+C+",'"+E+"','"+N+"','"+T+"')");
    }
    public void actualizar(String C, String E, String N, String T) throws SQLException {
        Main.BD.Ejecutar("update tecnicos set cedula = "+C+", empresa = '"+E+"', nombre = '"+N+"', telefono = '"+T+"' where cedula = "+ Cedula);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from tecnicos where cedula = " + Cedula);
    }
}
