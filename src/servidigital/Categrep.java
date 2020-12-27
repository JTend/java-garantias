/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author wiZa
 */
public class Categrep {
    private String ID;
    private String Padre;
    private String Nombre;
    private String Descrip;
    private String Nivel;

    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public String getPadre() { return Padre; }
    public void setPadre(String Padre) { this.Padre = Padre; }
    public String getNombre() { return Nombre; }
    public void setNombre(String Nombre) { this.Nombre = Nombre; }
    public String getDescrip() { return Descrip; }
    public void setDescrip(String Descrip) { this.Descrip = Descrip; }
    public String getNivel() { return Nivel; }
    public void setNivel(String Nivel) { this.Nivel = Nivel; }

    public Categrep() {
    }

    public Categrep(String ID) throws SQLException {
            Main.BD.Consultar("select * from categrep where id_categrep = " + ID);
            if(Main.BD.Siguiente()) {
                this.ID = ID;
                this.Nombre = Main.BD.Campo("nombre");
                this.Padre = Main.BD.Campo("padre");
                this.Descrip = Main.BD.Campo("descrip");
                this.Nivel = Main.BD.Campo("nivel");
            }
            else {
                this.ID = "";
                this.Nombre = "";
                this.Padre = "";
                this.Descrip = "";
                this.Nivel = "";
            }
    }
    
    public TableModel buscarCats(String B) {
        try {
            Main.BD.Consultar("select id_categrep as ID, nombre, descrip as Descripción from categrep where instr(id_categrep,'"+ B +"') or instr(nombre,'"+ B +"') or instr(descrip,'"+ B +"') limit 1000");
            return Main.BD.Modelo();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            return new DefaultTableModel();
        }
    }
    public void registrar(String P, String N, String D, String V) throws SQLException {
        Main.BD.Ejecutar("insert into categrep (padre,nombre,descrip,nivel) values ("+P+",'"+N+"','"+D+"',"+V+")");
    }
    public void actualizar(String ID, String P, String N, String D, String V) throws SQLException {
        Main.BD.Ejecutar("update categrep set padre = "+P+",nombre = '"+N+"',descrip = '"+D+"',nivel=" +V+" where id_categrep=" + ID);
    }
        public void suprimir(String ID) throws SQLException {
        Main.BD.Ejecutar("delete from categrep where id_categrep = "+ID);
    }
}
