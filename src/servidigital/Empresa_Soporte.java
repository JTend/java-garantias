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
public class Empresa_Soporte {
    private String RIF;
    private String Nombre;
    private String Telefono;
    private String Direccion;
    private String Email;
    private String Website;

    public String getRIF()      {   return RIF;         }
    public String getNombre()   {   return Nombre;      }
    public String getTelefono() {   return Telefono;    }
    public String getDireccion(){   return Direccion;   }
    public String getEmail()    {   return Email;       }
    public String getWebsite()  {   return Website;     }

    public void setRIF(String R)        {   RIF = R;        }
    public void setNombre(String N)     {   Nombre = N;     }
    public void setTelefono(String T)   {   Telefono = T;   }
    public void setDireccion(String D)  {   Direccion = D;  }
    public void setEmail(String E)      {   Email = E;      }
    public void setWebsite(String W)    {   Website = W;    }

    public Empresa_Soporte() {
        RIF = "";
        Nombre = "";
        Telefono = "";
        Direccion = "";
        Email = "";
        Website = "";
    }
    public Empresa_Soporte(String rif) throws SQLException {
        Main.BD.Consultar("select * from empresas_soporte where rif = '" + rif + "'");
        if(Main.BD.Siguiente()) {
            RIF = rif;
            Nombre = Main.BD.Campo("nombre");
            Telefono = Main.BD.Campo("telefono");
            Direccion = Main.BD.Campo("direccion");
            Email = Main.BD.Campo("email");
            Website = Main.BD.Campo("website");
        }
        else {
            RIF = "";
            Nombre = "";
            Telefono = "";
            Direccion = "";
            Email = "";
            Website = "";
        }
    }
    public boolean empresaValida() throws SQLException {
        Main.BD.Consultar("select * from empresas_soporte where rif = '"+ RIF +"' or nombre = '"+ Nombre +"'");
        return !Main.BD.hayRegistros();
    }
    public TableModel buscarSoportistas(String B) throws SQLException {
        Main.BD.Consultar("select * from empresas_soporte where instr(rif,'"+B+"') or instr(nombre,'"+B+"')");
        return Main.BD.Modelo();
    }
    public void registrar() throws SQLException {
        Main.BD.Ejecutar("insert into empresas_soporte values ('"+RIF+"','"+Nombre+"','"+Telefono+"','"+Direccion+"','"+Email+"','"+Website+"')");
    }
    public void registrar(String R, String N, String T, String D, String E, String W) throws SQLException {
        Main.BD.Ejecutar("insert into empresas_soporte values ('"+R+"','"+N+"','"+T+"','"+D+"','"+E+"','"+W+"')");
    }
    public void actualizar(String R, String N, String T, String D, String E, String W) throws SQLException {
        Main.BD.Ejecutar("update empresas_soporte set rif = '"+R+"', nombre = '"+N+"', telefono = '"+T+"',"
                + " direccion = '"+D+"', email = '"+E+"', website = '"+W+"'");
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from empresas_soporte where rif = '"+RIF+"'");
    }
}
