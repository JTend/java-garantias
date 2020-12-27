/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidigital;

import java.sql.SQLException;

/**
 *
 * @author Jose
 */
public class Deposito {
    private int id_deposito;
    private String codigo;
    private String descripcion;

    public int getId() {        return id_deposito;    }
    public void setId(int id_deposito) {        this.id_deposito = id_deposito;    }
    public String getCodigo() {        return codigo;    }
    public void setCodigo(String codigo) {        this.codigo = codigo;    }
    public String getDescripcion() {        return descripcion;    }
    public void setDescripcion(String descripcion) {        this.descripcion = descripcion;    }
    
    public Deposito() {
        id_deposito = 0;
        codigo = "";
        descripcion = "";
    }
    public Deposito(int id) throws SQLException {
        Main.BD.Consultar("select * from depositos where id_deposito = " + id);
        if(Main.BD.Siguiente()) {
            id_deposito = id;
            codigo = Main.BD.Campo("codigo");
            descripcion = Main.BD.Campo("descripcion");
        }
        else {
            id_deposito = 0;
            codigo = "";
            descripcion = "";
        }
    }
    public Deposito(String CODIGO) throws SQLException {
        Main.BD.Consultar("select * from depositos where codigo = '"+CODIGO+"'");
        if(Main.BD.Siguiente()) {
            id_deposito = Integer.parseInt(Main.BD.Campo("id_deposito"));
            codigo = CODIGO;
            descripcion = Main.BD.Campo("descripcion");
        }
        else {
            id_deposito = 0;
            codigo = "";
            descripcion = "";
        }
    }
    public Deposito(String codigo, String descripcion) {
        this.id_deposito = 0;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    public String Registrar() throws SQLException {
        Main.BD.Ejecutar("insert into depositos (codigo,descripcion) values ('"+codigo+"','"+descripcion+"')");
        Main.BD.Consultar("select last_insert_id() as ID from depositos");
        if (Main.BD.Siguiente()) return Main.BD.Campo("ID");
        else return "0";
    }
    public void Actualizar() throws SQLException {
        Main.BD.Ejecutar("update depositos set codigo = '"+codigo+"', descripcion = '"+descripcion+"' where id_deposito = '"+id_deposito+"'");
    }
    public void setExistencia(String ID, String Depo, int Cantidad) throws SQLException {
        Main.BD.Ejecutar("update repuestos set cantidad = cantidad + "+Cantidad+" where id_repuesto = "+ID+"");
        Main.BD.Ejecutar("update depositos set cantidad = cantidad + "+Cantidad+" where repuesto = "+ID+" and deposito = "+Depo);
    }
    public void Suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from depositos where id_deposito = "+id_deposito);
    }
}
