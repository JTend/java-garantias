package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Usuario {
    private String Cedula;
    private String Nombre;
    private String Cargo;
    private String Clave;
    private String Categoria;
    private static String Reg = "";
    public static Boolean[] Permitido;

    public Usuario() {
        Cedula = "";
        Nombre = "";
        Cargo = "";
        Clave = "";
        Categoria = "";
        Permitido = new Boolean[30];
    }

    public Usuario(String Ced) throws SQLException {
        Main.BD.Consultar("select * from usuarios where cedula =" + Ced);
        if(Main.BD.Siguiente()) {
            Cedula = Ced;
            Nombre = Main.BD.Campo("nombre");
            Cargo = Main.BD.Campo("cargo");
            Clave = Main.BD.Campo("clave");
            Categoria = Main.BD.Campo("categoria");
            Permitido = new Boolean[30];
        }
        else {
            JOptionPane.showMessageDialog(null, "No se puede cargar registro", "Usuario(String)", JOptionPane.ERROR_MESSAGE);
            Cedula = "";
            Nombre = "";
            Cargo = "";
            Clave = "";
            Categoria = "";
            Permitido = new Boolean[30];
        }
    }
    public void Insertar() throws SQLException {
        Main.BD.Ejecutar("insert into usuarios (cedula, nombre, cargo, clave, categoria) values ("+Cedula+",'"+Nombre+"','"+Cargo+"','"+Clave+"',"+Categoria+")");
    }
    public void Actualizar(String C) throws SQLException {
        Main.BD.Ejecutar("update usuarios set cedula = "+C+", nombre = '"+Nombre+"', cargo = '"+Cargo+"', clave = '"+Clave+"', categoria = "+Categoria+" where cedula = "+Cedula);
    }
    public TableModel buscarUsuarios(String B) throws SQLException {
        Main.BD.Consultar("select cedula, nombre, cargo from usuarios where instr(cedula,'"+ B +"') or instr(nombre,'"+ B +"') or instr(cargo,'"+ B +"')");
        return Main.BD.Modelo();
    }
    public boolean Exitoso(String U, String K) throws SQLException {
        Main.BD.Consultar("select * from usuarios where cedula = "+ U);
        if(Main.BD.Siguiente()) {
            Main.BD.Consultar("select * from usuarios where cedula = " + U + " and clave = '"+ K +"'");
            if(Main.BD.Siguiente()) {
                Cedula = U;
                Nombre = Main.BD.Campo("nombre");
                Cargo = Main.BD.Campo("cargo");
                Clave = Main.BD.Campo("clave");
                Categoria = Main.BD.Campo("categoria");
                Main.BD.Consultar("select opcion, valor from categacc where categusu = "+Categoria);
                while(Main.BD.Siguiente()) {
                    int X = Integer.parseInt(Main.BD.Campo("opcion"));
                    Permitido[X] = Main.BD.Campo("valor").equalsIgnoreCase("1");
                }
                return true;
            }
            else {
                Reg = Reg  + "Clave invalida!" + "\n";
                return false;
            }
        }
        else {
            Reg = Reg + "Usuario inexistente!" + "\n";
            return false;
        }
    }

    public void setCedula(String C) { Cedula = C;   }
    public void setNombre(String N) { Nombre = N;   }
    public void setCargo(String C)  { Cargo = C;    }
    public void setCategoria(String cat) { Categoria = cat;   }

    public void setClave(String Clave) { this.Clave = Clave; }
    public String getClave() { return Clave;  }

    public String getNombre()   { return Nombre;}
    public String getCedula()   { return Cedula;}
    public String getCargo()    { return Cargo; }
    public String getCategoria(){ return Categoria;}
    public String getReg()      { return Reg;   }
}
