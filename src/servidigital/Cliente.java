package servidigital;

import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tedndero
 */
public class Cliente {
    private String Nombre;
    private String Cedula;
    private String Email;
    private String Direccion;
    private boolean web;

    public Cliente() {
        Nombre = "";
        Cedula = "";
        Email = "";
        Direccion = "";
        web = false;
    }
    public Cliente(String Ced) {
        try {
            Main.BD.Consultar("select * from clientes where cedula = " + Ced);
            if(Main.BD.Siguiente()) {
                Cedula = Ced;
                Nombre = Main.BD.Campo("nombre");
                Email = Main.BD.Campo("email");
                Direccion = Main.BD.Campo("direccion");
                web = Boolean.parseBoolean(Main.BD.Campo("web"));
            }
            else {
                Nombre = "";
                Cedula = "";
                Email = "";
                Direccion = "";
                web = false;
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar: \n\n"+Main.BD.showException(e.getMessage()), "Cliente(String)", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getNombre()   { return Nombre;    }
    public String getCedula()   { return Cedula;    }
    public String getEmail()    { return Email;     }
    public String getDireccion(){ return Direccion; }
    public boolean getWeb()     { return web;       }

    public void setNombre(String N)     { Nombre = N;   }
    public void setCedula(String C)     { Cedula = C;   }
    public void setEmail(String E)      { Email = E;    }
    public void setDireccion(String D)  { Direccion = D;}
    public void setWeb(boolean W)       { web = W;      }
    
    public TableModel buscarClientes(String B) {
        try {
            Main.BD.Consultar("select cedula, nombre, email from clientes where instr(cedula,'"+ B +"') or instr(nombre,'"+ B +"') or instr(direccion,'"+ B +"') limit 1000");
            return Main.BD.Modelo();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            return new DefaultTableModel();
        }
    }
    public void registrar(String C, String N, String E, String D, String W) throws SQLException {
        Main.BD.Ejecutar("insert into clientes (cedula,nombre,email,direccion,web) values ('"+C+"','"+N+"','"+E+"','"+D+"',"+W+")");
    }
    public void actualizar(String C, String N, String E, String D, String W) throws SQLException {
        Main.BD.Ejecutar("update clientes set cedula = "+C+",nombre = '"+N+"',email = '"+E+"',direccion = '"+D+"',web=" +W+" where cedula=" + Cedula);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from clientes where cedula = "+Cedula);
    }
    public DefaultListModel buscarTelefonos() throws SQLException {
        Main.BD.Consultar("select * from telefonos where cedula = "+Cedula);
        return Main.BD.Lista("numero");
    }
    public void registrarTelefonos(ListModel Telefonos) throws SQLException {
        Main.BD.Ejecutar("delete from telefonos where cedula = "+ Cedula);
        for(int i = 0; i < Telefonos.getSize(); i++) {
            Main.BD.Ejecutar("insert into telefonos (cedula, numero) values ("+ Cedula +","+ Telefonos.getElementAt(i) +")");
        }
    }
}
