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
public class Categusu {
    private int id_categoria;
    private String nombre;
    private String descrip;

    public int getId_categoria() {        return id_categoria;    }
    public void setId_categoria(int id_categoria) {        this.id_categoria = id_categoria;    }

    public String getNombre() {        return nombre;    }
    public void setNombre(String nombre) {        this.nombre = nombre;    }

    public String getDescrip() {        return descrip;    }
    public void setDescrip(String descrip) {        this.descrip = descrip;    }

    public Categusu() {
        this.id_categoria = 0;
        this.nombre = "";
        this.descrip = "";
    }
    public Categusu(String nombre) throws SQLException {
        Main.BD.Consultar("select * from categusu where nombre = '" + nombre + "'");
            if(Main.BD.Siguiente()) {
                this.id_categoria = Integer.parseInt(Main.BD.Campo("id_categoria"));
                this.nombre = Main.BD.Campo("nombre");
                this.descrip = Main.BD.Campo("descrip");
            }
            else {
                this.id_categoria = 0;
                this.nombre = "";
                this.descrip = "";
            } 
    }
    public Categusu(int id_categoria) throws SQLException {
            Main.BD.Consultar("select * from categusu where id_categoria = " + id_categoria);
            if(Main.BD.Siguiente()) {
                this.id_categoria = id_categoria;
                this.nombre = Main.BD.Campo("nombre");
                this.descrip = Main.BD.Campo("descrip");
            }
            else {
                this.id_categoria = 0;
                this.nombre = "";
                this.descrip = "";
            }                
    }
    public Categusu(String nombre, String descrip) {
        this.nombre = nombre;
        this.descrip = descrip;
    }
    
    public String Registrar() throws SQLException {
        
        Main.BD.Ejecutar("insert into categusu (nombre,descrip) values ('"+nombre+"','"+descrip+"')");
        Main.BD.Consultar("select last_insert_id() as ID from categusu");
        if (Main.BD.Siguiente()) return Main.BD.Campo("ID");
        else return "0";
    }
    public void Actualizar() throws SQLException {
        Main.BD.Ejecutar("update categusu set nombre = '"+nombre+"', descrip = '"+descrip+"' where id_categoria = '"+id_categoria+"'");
    }
    public void Suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from categusu where id_categoria = "+id_categoria);
    }
}
