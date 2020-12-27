package servidigital;

import java.sql.SQLException;
import javax.swing.ComboBoxModel;
import javax.swing.ListModel;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Marca {
    private String id_marca;
    private String marca;

    public String getID()   {   return id_marca;}
    public String getMarca(){   return marca;   }

    public void setID(String I)     {   id_marca = I;   }
    public void setMarca(String M)  {   marca = M;      }

    public Marca() {
        id_marca = "";
        marca = "";
    }
    public Marca(String ID) throws SQLException {
        Main.BD.Consultar("select * from marcas where marca = '" + ID +"'");
        if(Main.BD.Siguiente()) {
            id_marca = Main.BD.Campo("id_marca");
            marca = Main.BD.Campo("marca");
        }
    }
    public TableModel tblMarcas(String B) throws SQLException {
        Main.BD.Consultar("select * from marcas where instr(marca,'"+B+"')");
        return Main.BD.Modelo();
    }
    public ListModel lstMarcas() throws SQLException {
        Main.BD.Consultar("select marca from marcas");
        return Main.BD.Lista("marca");
    }
    public ComboBoxModel cmbMarcas() throws SQLException {
        Main.BD.Consultar("select marca from marcas");
        return Main.BD.Combo("marca");
    }
    public String registrar(String M) throws SQLException {
        Main.BD.Ejecutar("insert into marcas (marca) values('"+M+"')");
        Main.BD.Consultar("select last_insert_id()");
        if(Main.BD.Siguiente()) return Main.BD.Campo("last_insert_id()");
        else return "";
    }
    public void actualizar(String M) throws SQLException {
        Main.BD.Ejecutar("update marcas set marca = '"+M+"' where id_marca = " + id_marca);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from marcas where id_marca = " + id_marca);
    }
}
