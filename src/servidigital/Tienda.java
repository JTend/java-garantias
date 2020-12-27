package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Tienda {

    private String id_tienda;
    private String Nombre;
    private String Direccion;
    private String Telefono;
    private boolean web;

    public Tienda() {
        Nombre    = "";
        Direccion = "";
        Telefono  = "";
        web = false;
    }
    public Tienda(String ID) throws SQLException {
        Main.BD.Consultar("select * from tienda where id_tienda = " + ID);
        if(Main.BD.Siguiente()) {
            id_tienda = ID;
            Nombre = Main.BD.Campo("nombre");
            Direccion = Main.BD.Campo("direccion");
            Telefono = Main.BD.Campo("telefono");
            web = Boolean.parseBoolean(Main.BD.Campo("web"));
        }
        else JOptionPane.showMessageDialog(null, "No se puede cargar registro", "Tienda(String ID)", JOptionPane.ERROR_MESSAGE);
    }

    public String getID()       { return id_tienda;     }
    public String getNombre()   { return Nombre;        }
    public String getDireccion(){ return Direccion;     }
    public String getTelefono() { return Telefono;      }
    public boolean getWeb()     { return web;           }

    public void setID(String I)        {    id_tienda = I;  }
    public void setNombre(String N)    {    Nombre = N;     }
    public void setDireccion(String D) {    Direccion = D;  }
    public void setTelefono(String T)  {    Telefono = T;   }
    public void setWeb(boolean W)      {    web = W;        }

    public TableModel buscarTiendas(String B) throws SQLException {
        Main.BD.Consultar("select * from tiendas where instr(nombre,'"+ B +"')");
        return Main.BD.Modelo();
    }
    public void registrar(String N, String D, String T, String W) throws SQLException {
        Main.BD.Ejecutar("insert into tiendas (nombre, direccion, telefono, web) values ('"+ N +"','"+ D +"',"+ T +","+ W +")");
    }
    public void cargarTienda(String nmb) throws SQLException {
        Main.BD.Consultar("select * from tiendas where nombre = '"+ nmb +"'");
        if(Main.BD.Siguiente()) {
            id_tienda = Main.BD.Campo("id_tienda");
            Nombre = Main.BD.Campo("nombre");
            Direccion = Main.BD.Campo("direccion");
            Telefono = Main.BD.Campo("telefono");
            web = Boolean.parseBoolean(Main.BD.Campo("web"));
        }
        else JOptionPane.showMessageDialog(null, "No se puede cargar registro", "Tienda(String ID)", JOptionPane.ERROR_MESSAGE);
    }
    public void actualizar(String N, String D, String T, String W) throws SQLException {
        Main.BD.Ejecutar("update tiendas set nombre = '"+ N +"', direccion = '"+ D +"', telefono = "+ T +", web = "+ W +" where id_tienda = "+ id_tienda);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from tiendas where id_tienda = "+id_tienda);
    }
}
