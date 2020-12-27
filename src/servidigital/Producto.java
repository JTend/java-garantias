package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Producto {
    private String id_producto;
    private String id_marca;
    private String Codigo;
    private String Descripcion;
    private String Referencia;

    public String getID()           {   return id_producto; }
    public String getMarca()        {   return id_marca;    }
    public String getCodigo()       {   return Codigo;      }
    public String getDescripcion()  {   return Descripcion; }
    public String getReferencia()   {   return Referencia;  }

    public void setID(String I)         {   id_producto = I;}
    public void setMarca(String M)      {   id_marca = M;   }
    public void setCodigo(String C)     {   Codigo = C;     }
    public void setDescripcion(String D){   Descripcion = D;}
    public void setReferencia(String R) {   Referencia = R; }

    public Producto() {
        id_producto = "";
        id_marca = "";
        Codigo = "";
        Descripcion = "";
        Referencia = "";
    }
    public Producto(String ID) throws SQLException {
        Main.BD.Consultar("select * from productos where codigo = '" + ID + "'");
        if(Main.BD.Siguiente()) {
            id_producto = Main.BD.Campo("id_producto");
            id_marca = Main.BD.Campo("id_marca");
            Codigo = Main.BD.Campo("codigo");
            Descripcion = Main.BD.Campo("descripcion");
            Referencia = Main.BD.Campo("referencia");
        }
        else JOptionPane.showMessageDialog(null, ID + "\nNo se pudo cargar registro", "Producto(String)", JOptionPane.ERROR_MESSAGE);
    }
    public TableModel buscarProductos(String B) throws SQLException {
        Main.BD.Consultar("select * from productos where instr(codigo,'"+B+"') or instr(descripcion,'"+B+"') or instr(referencia,'"+B+"')");
        return Main.BD.Modelo();
    }
    public void registrar(String Marca, String Cod, String Desc, String Ref) throws SQLException {
        Main.BD.Ejecutar("insert into productos (id_marca, codigo, descripcion, referencia) values ("+ Marca +",'"+ Cod +"','"+ Desc +"','"+ Ref +"')");
    }
    public void actualizar(String Marca, String Cod, String Desc, String Ref) throws SQLException {
        Main.BD.Ejecutar("update productos set id_marca = "+ Marca +", codigo = '"+ Cod +"', descripcion = '"+ Desc +"', referencia = '"+ Ref +"')");
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from productos where id_producto = "+ id_producto);
    }
}
