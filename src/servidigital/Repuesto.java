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
public class Repuesto {
    private String id_repuesto;
    private String codigo;
    private Categrep categoria;
    private String nombre;
    private String descrip;
    private String cantidad;

    public String getId_repuesto() { return id_repuesto; }
    public String getCodigo() { return codigo; }
    public Categrep getCategoria() { return categoria; }
    public String getNombre() { return nombre; }
    public String getDescrip() { return descrip; }
    public String getCantidad() { return cantidad; }

    public void setId_repuesto(String id_repuesto) { this.id_repuesto = id_repuesto; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setCategoria(Categrep categoria) { this.categoria = categoria; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescrip(String descrip) { this.descrip = descrip; }
    public void setCantidad(String cantidad) { this.cantidad = cantidad; }
    
    public Repuesto() {
        id_repuesto = "";
        codigo = "";
        categoria = new Categrep();
        nombre = "";
        descrip = "";
        cantidad = "";
    }
    public Repuesto(String id_repuesto) throws SQLException {
        Main.BD.Consultar("select * from repuestos where id_repuesto = " + id_repuesto);
        if(Main.BD.Siguiente()) {
            this.id_repuesto = id_repuesto;
            this.codigo = Main.BD.Campo("codigo");
            this.categoria = new Categrep(Main.BD.Campo("categoria"));
            this.nombre = Main.BD.Campo("nombre");
            this.descrip = Main.BD.Campo("descrip");
            this.cantidad = Main.BD.rslSet().getString(5);
        }
        else {
            JOptionPane.showMessageDialog(null, "No se ha podido cargar el repuesto", "Constructor Repuesto("+id_repuesto+")", JOptionPane.ERROR_MESSAGE);
        }
    }
    public Repuesto (String Cod, boolean Codigooo) throws SQLException {
        Main.BD.Consultar("select * from repuestos where codigo = '"+Cod+"'");
        if(Main.BD.Siguiente()) {
            this.id_repuesto = Main.BD.Campo("id_repuesto");
            this.codigo = Main.BD.Campo("codigo");
            this.categoria = new Categrep(Main.BD.Campo("categoria"));
            this.nombre = Main.BD.Campo("nombre");
            this.descrip = Main.BD.Campo("descrip");
            this.cantidad = Main.BD.rslSet().getString(5);
        }
    }
    public Repuesto(String id_repuesto, String codigo, Categrep categoria, String nombre, String descrip, String cantidad) {
        this.id_repuesto = id_repuesto;
        this.codigo = codigo;
        this.categoria = categoria;
        this.nombre = nombre;
        this.descrip = descrip;
        this.cantidad = cantidad;
    }
    public int Existencia(String Deposit) {
        try {
            Main.BD.Consultar("select e.cantidad as Exis from existencias e join depositos d on e.deposito = d.id_deposito where d.codigo = '"+ Deposit +"' and repuesto = " + this.getId_repuesto());
            if(Main.BD.Siguiente())
                return Integer.parseInt(Main.BD.Campo("Exis"));
            else
                return 0;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            return 0;
        }
    }
    public TableModel Tabla(String B, String D) {
        try {
            Main.BD.Consultar("select r.id_repuesto, r.nombre, d.codigo, ifnull(e.cantidad,0) as cantidad from repuestos r join depositos d left join existencias e on r.id_repuesto = e.repuesto and d.id_deposito = e.deposito where instr(d.codigo, '"+D+"') and (instr(r.codigo,'"+ B +"') or instr(r.nombre,'"+ B +"') or instr(r.descrip,'"+ B +"'))");
            return Main.BD.Modelo();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            return new DefaultTableModel();
        }
    }
    public TableModel Tabla(String B) {
        try {
            Main.BD.Consultar("select id_repuesto as ID, nombre, cantidad from repuestos where instr(codigo,'"+ B +"') or instr(nombre,'"+ B +"') or instr(descrip,'"+ B +"') limit 1000");
            return Main.BD.Modelo();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            return new DefaultTableModel();
        }
    }
    
    public String Registrar(String codigo, String categoria, String nombre, String descrip) throws SQLException {
        Main.BD.Ejecutar("insert into repuestos (codigo,categoria,nombre,descrip) values ('"+codigo+"',"+categoria+",'"+nombre+"','"+descrip+"')");
        Main.BD.Consultar("select last_insert_id() as ID from repuestos");
        if (Main.BD.Siguiente()) return Main.BD.Campo("ID");
        else return "";
    }
    
    public void Actualizar(String ID, String codigo, String categoria, String nombre, String descrip) throws SQLException {
        Main.BD.Ejecutar("update repuestos set codigo = '"+codigo+"', categoria = "+categoria+", nombre = '"+nombre+"', descrip = '"+descrip+"' where id_repuesto = "+ID+"");
    }
    
    public void setExistencia(String ID, String Depo, int Cantidad) throws SQLException {
        Main.BD.Ejecutar("update repuestos set cantidad = cantidad + "+Cantidad+" where id_repuesto = "+ID+"");
        Main.BD.Ejecutar("update depositos set cantidad = cantidad + "+Cantidad+" where repuesto = "+ID+" and deposito = "+Depo);
    }
    
    public void Suprimir(String ID) throws SQLException {
        Main.BD.Ejecutar("delete from repuestos where id_repuesto = "+ID);
    }
}
