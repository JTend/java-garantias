package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author José Tendero
 */
public class Equipo {
    private String id_equipo;
    private String id_producto;
    private String Serial;

    public String getID() { return id_equipo; }
    public String getProd() { return id_producto; }
    public String getSerial() { return Serial; }

    public void setID(String I) { id_equipo = I; }
    public void setProd(String P) { id_producto = P; }
    public void setSerial(String S) { Serial = S; }

    public Equipo() {
        id_equipo = "";
        id_producto = "";
        Serial = "";
    }
    public Equipo(String ID) throws SQLException {
        Main.BD.Consultar("select * from equipo where id_equipo = " + ID);
        if(Main.BD.Siguiente()) {
            id_equipo = ID;
            id_producto = Main.BD.Campo("id_producto");
            Serial = Main.BD.Campo("serial");
        }
    }
    public void registrar(String Prod, String Serial) throws SQLException {
        Producto P = new Producto(Prod);
        if(Serial.equals("")) {
            Main.BD.Ejecutar("insert into equipo (id_producto, serial) values ("+ P.getID() +",null)");
            Main.BD.Consultar("select * from equipo where id_equipo = last_insert_id()");
            if(Main.BD.Siguiente()) {
                id_equipo = Main.BD.Campo("id_equipo");
                id_producto = Main.BD.Campo("id_producto");
                Serial = Main.BD.Campo("serial");
            }
        }
        else {
            Main.BD.Consultar("select equipo.* from equipo, casos where equipo.id_equipo = casos.id_equipo and serial = '"+ Serial +"'");
            if(Main.BD.hayRegistros()) {
                JOptionPane.showMessageDialog(null, "Advertencia, este equipo ya presenta\ncasos registrados anteriormente!", "Equipo registrado", JOptionPane.WARNING_MESSAGE);
                if(Main.BD.Siguiente()) {
                    id_equipo = Main.BD.Campo("id_equipo");
                    id_producto = Main.BD.Campo("id_producto");
                    Serial = Main.BD.Campo("serial");
                }
            }
            else {
                Main.BD.Ejecutar("insert into equipo (id_producto, serial) values (" + P.getID() + ",'" + Serial + "')");
                Main.BD.Consultar("select * from equipo where id_equipo = last_insert_id()");
                if(Main.BD.Siguiente()) {
                    id_equipo = Main.BD.Campo("id_equipo");
                    id_producto = Main.BD.Campo("id_producto");
                    Serial = Main.BD.Campo("serial");
                }
            }
        }
    }
        
    public void actualizar(String Prod, String Serial) throws SQLException {
        Producto P = new Producto(Prod);
        Main.BD.Ejecutar("update equipo set id_producto = " + P.getID() + ", serial = '" + Serial + "' where id_equipo = " + id_equipo);
    }
    public void suprimir(String ID) throws SQLException {
        Main.BD.Ejecutar("delete from equipo where id_equipo = " + ID);
    }
}
