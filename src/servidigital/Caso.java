package servidigital;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

/**
 *
 * @author José Tendero
 */
public class Caso {
    private String ID;
    private String Cedula;
    private String id_tienda;
    private String id_equipo;
    private String id_tecnico;
    private String id_diagnostico;
    private String id_solucion;
    private String Factura;
    private String Serial;
    private String descCaso;
    private String fechaCreacion;
    private String fechaSolucion;
    private String Estado;
    private boolean web;
    
    public Caso() {
        Cedula = "";        id_tienda = "";     id_equipo = "";     id_tecnico = "";
        id_diagnostico = "";id_solucion = "";   Factura = "";       Serial = "";
        descCaso = "";      fechaCreacion = ""; fechaSolucion = ""; Estado = "";        web = false;
    }
    public String getID()           { return ID;            }
    public String getCedula()       { return Cedula;        }
    public String getTienda()       { return id_tienda;     }
    public String getEquipo()       { return id_equipo;     }
    public String getTecnico()      { return id_tecnico;    }
    public String getDiagnostico()  { return id_diagnostico;}
    public String getSolucion()     { return id_solucion;   }
    public String getFactura()      { return Factura;       }
    public String getSerial()       { return Serial;        }
    public String getDescripcion()  { return descCaso;      }
    public String getFechaCreacion(){ return fechaCreacion; }
    public String getFechaSolucion(){ return fechaSolucion; }
    public String getEstado()       { return Estado;        }
    public boolean getWeb()         { return web;           }

    public void setID(String i)             { ID = i;            }
    public void setCedula(String C)         { Cedula = C;        }
    public void setTienda(String T)         { id_tienda = T;     }
    public void setEquipo(String E)         { id_equipo = E;     }
    public void setTecnico(String t)        { id_tecnico = t;    }
    public void setDiagnostico(String D)    { id_diagnostico = D;}
    public void setSolucion(String S)       { id_solucion = S;   }
    public void setFactura(String F)        { Factura = F;       }
    public void setSerial(String S)         { Serial = S;        }
    public void setDescripcion(String D)    { descCaso = D;      }
    public void setFechaCreacion(String F)  { fechaCreacion = F; }
    public void setFechaSolucion(String F)  { fechaSolucion = F; }
    public void setEstado(String E)         { Estado = E;        }
    public void setWeb(boolean W)           { web = W;           }

    public Caso(String id) throws SQLException {
        Main.BD.Consultar("select casos.*, serial from casos, equipo "
                + "where casos.id_equipo = equipo.id_equipo and id_caso = "+ id);
        if(Main.BD.Siguiente()) {
            ID =             Main.BD.Campo("id_caso");
            Cedula =         Main.BD.Campo("cedula");
            id_tienda =      Main.BD.Campo("id_tienda");
            id_equipo =      Main.BD.Campo("id_equipo");
            id_tecnico =     Main.BD.Campo("id_tecnico");
            id_diagnostico = Main.BD.Campo("id_diagnostico");
            id_solucion =    Main.BD.Campo("id_solucion");
            Factura =        Main.BD.Campo("factura");
            Serial =         Main.BD.Campo("serial");
            descCaso =       Main.BD.Campo("desc_caso");
            fechaCreacion =  Main.BD.Campo("fecha_creacion");
            fechaSolucion =  Main.BD.Campo("fecha_solucion");
            Estado =         Main.BD.Campo("estado");
            web = Boolean.parseBoolean(Main.BD.Campo("web"));
        }
        else {
            Cedula = "";        id_tienda = "";     id_equipo = "";     id_tecnico = "";
            id_diagnostico = "";id_solucion = "";   Factura = "";       Serial = "";
            descCaso = "";      fechaCreacion = ""; fechaSolucion = ""; Estado = "";        web = false;
        }
    }
    public TableModel buscarCasos(String B, String Estado) throws SQLException {
        Main.BD.Consultar("select id_caso, descripcion, nombre, date_format(fecha_creacion, '%d/%m/%Y') as fecha_creacion, estado "
                + "from casos, productos, equipo, clientes "
                + "where casos.cedula = clientes.cedula and casos.id_equipo = equipo.id_equipo and equipo.id_producto = productos.id_producto "
                + "and instr(estado,'"+Estado+"') and (instr(clientes.cedula, '"+ B +"') or instr(codigo,'"+ B +"') or instr(id_caso, '"+ B +"')) order by id_caso desc");
        return Main.BD.Modelo();
    }
    public void registrar(String C, String T, String E, String F, String D) throws SQLException {
        Main.BD.Ejecutar("insert into casos (cedula,id_tienda,id_equipo,factura,desc_caso, fecha_creacion) values ("+C+","+T+","+E+",'"+F+"','"+D+"', curdate())");
        Main.BD.Consultar("select * from casos where id_caso = last_insert_id()");
        if(Main.BD.Siguiente()) {
            ID =             Main.BD.Campo("id_caso");
            Cedula =         Main.BD.Campo("cedula");
            id_tienda =      Main.BD.Campo("id_tienda");
            id_equipo =      Main.BD.Campo("id_equipo");
            id_tecnico =     Main.BD.Campo("id_tecnico");
            id_diagnostico = Main.BD.Campo("id_diagnostico");
            id_solucion =    Main.BD.Campo("id_solucion");
            Factura =        Main.BD.Campo("factura");
            descCaso =       Main.BD.Campo("desc_caso");
            fechaCreacion =  Main.BD.Campo("fecha_creacion");
            fechaSolucion =  Main.BD.Campo("fecha_solucion");
            Estado =         Main.BD.Campo("estado");
            web = Boolean.parseBoolean(Main.BD.Campo("web"));
        }
        else JOptionPane.showMessageDialog(null, "Problemas con last_insert_id()", "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
    public void actualizar(String Campo, boolean esEntero, String Valor) throws SQLException {
        if(esEntero)
            Main.BD.Ejecutar("update casos set "+Campo+" = "+Valor+" where id_caso = "+ID);
        else
            Main.BD.Ejecutar("update casos set "+Campo+" = '"+Valor+"' where id_caso = "+ID);
    }
    public void suprimir() throws SQLException {
        Main.BD.Ejecutar("delete from casos where id_caso = "+ID);
    }




    public void imprimirApertura() throws SQLException {
        Main.BD.Consultar("select casos.id_caso, clientes.nombre as cliente, clientes.cedula, clientes.direccion, productos.descripcion, "
                + "ifnull(serial,'') as serial, marca, referencia, factura, tiendas.nombre as tienda, "
                + "date_format(fecha_creacion, '%d/%m/%Y') as fecha_creacion, desc_caso "

                + "from casos, clientes, tiendas, equipo, productos, marcas "

                + "where casos.cedula = clientes.cedula and casos.id_tienda = tiendas.id_tienda and casos.id_equipo = equipo.id_equipo "

                + "and equipo.id_producto = productos.id_producto and productos.id_marca = marcas.id_marca "

                + "and id_caso = " + ID);
        Map<Object, Object> Parametros = new HashMap<Object, Object>();
//
        if(Main.BD.Siguiente()) {
            try {
                Parametros.put("id_caso", Main.BD.Campo("id_caso"));
                Parametros.put("cliente", Main.BD.Campo("cliente"));
                Parametros.put("cedula", Main.BD.Campo("cedula"));
                Parametros.put("direccion", Main.BD.Campo("direccion"));
                Parametros.put("descripcion", Main.BD.Campo("descripcion"));
                Parametros.put("serial", Main.BD.Campo("serial"));
                Parametros.put("marca", Main.BD.Campo("marca"));
                Parametros.put("referencia", Main.BD.Campo("referencia"));
                Parametros.put("factura", Main.BD.Campo("factura"));
                Parametros.put("tienda", Main.BD.Campo("tienda"));
                Parametros.put("recepcion", Main.BD.Campo("fecha_creacion"));
                Parametros.put("desc_caso", Main.BD.Campo("desc_caso"));
                Main.BD.Consultar("select numero from telefonos where cedula = " + Main.BD.Campo("cedula"));
                String Tls = "";
                while(Main.BD.Siguiente()) {
                    Tls = Tls + Main.BD.Campo("numero");
                    if(Main.BD.esUltimo()) break;
                    Tls = Tls + "/";
                }
                Parametros.put("telefonos", Tls);
               
                Reporte Recepcion = new Reporte("/Reportes/Recepcion.jasper", Parametros);
                Recepcion.runReporte("Comprobante de Recepcion", true);
            }
            catch(UnsupportedOperationException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "Servidigital: UnsupportedOperationException", JOptionPane.ERROR_MESSAGE); }
            catch(ClassCastException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "Servidigital: ClassCastException", JOptionPane.ERROR_MESSAGE); }
            catch(NullPointerException e) {JOptionPane.showMessageDialog(null, e.getMessage(), "Servidigital: NullPointerException", JOptionPane.ERROR_MESSAGE);  }
            catch(IllegalArgumentException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "Servidigital: IllegalArgumentException", JOptionPane.ERROR_MESSAGE); }
        }
        else JOptionPane.showMessageDialog(null, "Reporte - Error al ingresar datos", "Error SQL", JOptionPane.ERROR_MESSAGE);
    }




    public void imprimirOrden() throws SQLException {
        Map<Object, Object> Parametros = new HashMap<Object, Object>();
        Main.BD.Consultar("select casos.id_caso, empresas_soporte.nombre as empresa, rif, empresas_soporte.telefono, tecnicos.nombre, productos.descripcion, "
                + "ifnull(serial,'') as serial, marca, referencia, factura, tiendas.nombre as tienda, "
                + "date_format(fecha_reparacion, '%d/%m/%Y') as fecha, desc_caso, diagnostico "

                + "from casos, empresas_soporte, tecnicos, tiendas, equipo, productos, marcas, diagnosticos "

                + "where casos.id_tecnico = tecnicos.cedula and empresas_soporte.rif = tecnicos.empresa "
                + "and casos.id_tienda = tiendas.id_tienda and casos.id_equipo = equipo.id_equipo "
                + "and equipo.id_producto = productos.id_producto and productos.id_marca = marcas.id_marca "
                + "and diagnosticos.id_diagnostico = casos.id_diagnostico and id_caso = " + ID);

        if(Main.BD.Siguiente()) {
            try {
                Parametros.put("id_caso", ID);
                Parametros.put("empresa", Main.BD.Campo("empresa"));
                Parametros.put("rif", Main.BD.Campo("rif"));
                Parametros.put("nombre", Main.BD.Campo("nombre"));
                Parametros.put("telefono", Main.BD.Campo("telefono"));
                Parametros.put("descripcion", Main.BD.Campo("descripcion"));
                Parametros.put("serial", Main.BD.Campo("serial"));
                Parametros.put("marca", Main.BD.Campo("marca"));
                Parametros.put("referencia", Main.BD.Campo("referencia"));
                Parametros.put("factura", Main.BD.Campo("factura"));
                Parametros.put("tienda", Main.BD.Campo("tienda"));
                Parametros.put("fecha", Main.BD.Campo("fecha"));
                Parametros.put("desc_caso", Main.BD.Campo("desc_caso"));
                Parametros.put("diagnostico", Main.BD.Campo("diagnostico"));

                
                Reporte Recepcion = new Reporte("/Reportes/Servicio.jasper", Parametros);
                Recepcion.runReporte("Orden de Servicio", true);
            }
            catch(UnsupportedOperationException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "UnsupportedOperationException", JOptionPane.ERROR_MESSAGE); }
            catch(ClassCastException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "ClassCastException", JOptionPane.ERROR_MESSAGE); }
            catch(NullPointerException e) {JOptionPane.showMessageDialog(null, e.getMessage(), "NullPointerException", JOptionPane.ERROR_MESSAGE);  }
            catch(IllegalArgumentException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "IllegalArgumentException", JOptionPane.ERROR_MESSAGE); }
        }
        else JOptionPane.showMessageDialog(null, "Reporte - Error al ingresar datos", "Error SQL", JOptionPane.ERROR_MESSAGE);
    }




    public void imprimirEntrega() throws SQLException {
        Map<Object, Object> Parametros = new HashMap<Object, Object>();
        Main.BD.Consultar("select casos.id_caso, clientes.nombre as cliente, clientes.cedula, clientes.direccion, productos.descripcion, "
                + "ifnull(serial,'') as serial, marca, referencia, factura, tiendas.nombre as tienda, "
                + "date_format(fecha_entrega, '%d/%m/%Y') as fecha, desc_caso, solucion "

                + "from casos, clientes, tiendas, equipo, productos, marcas, soluciones "

                + "where casos.id_solucion = soluciones.id_solucion and casos.cedula = clientes.cedula and casos.id_tienda = tiendas.id_tienda and casos.id_equipo = equipo.id_equipo "

                + "and equipo.id_producto = productos.id_producto and productos.id_marca = marcas.id_marca "

                + "and id_caso = " + ID);
//
        if(Main.BD.Siguiente()) {
            try {
                Parametros.put("id_caso", Main.BD.Campo("id_caso"));
                Parametros.put("cliente", Main.BD.Campo("cliente"));
                Parametros.put("cedula", Main.BD.Campo("cedula"));
                Parametros.put("direccion", Main.BD.Campo("direccion"));
                Parametros.put("descripcion", Main.BD.Campo("descripcion"));
                Parametros.put("serial", Main.BD.Campo("serial"));
                Parametros.put("marca", Main.BD.Campo("marca"));
                Parametros.put("referencia", Main.BD.Campo("referencia"));
                Parametros.put("factura", Main.BD.Campo("factura"));
                Parametros.put("tienda", Main.BD.Campo("tienda"));
                Parametros.put("fecha", Main.BD.Campo("fecha"));
                Parametros.put("desc_caso", Main.BD.Campo("desc_caso"));
                Parametros.put("solucion", Main.BD.Campo("solucion"));
                Main.BD.Consultar("select numero from telefonos where cedula = " + Main.BD.Campo("cedula"));
                String Tls = "";
                while(Main.BD.Siguiente()) {
                    Tls = Tls + Main.BD.Campo("numero");
                    if(Main.BD.esUltimo()) break;
                    Tls = Tls + "/";
                }
                Parametros.put("telefonos", Tls);

                Reporte Recepcion = new Reporte("/Reportes/Entrega.jasper", Parametros);
                Recepcion.runReporte("Comprobante de Entrega", true);
            }
            catch(UnsupportedOperationException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "UnsupportedOperationException", JOptionPane.ERROR_MESSAGE); }
            catch(ClassCastException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "ClassCastException", JOptionPane.ERROR_MESSAGE); }
            catch(NullPointerException e) {JOptionPane.showMessageDialog(null, e.getMessage(), "NullPointerException", JOptionPane.ERROR_MESSAGE);  }
            catch(IllegalArgumentException e) { JOptionPane.showMessageDialog(null, e.getMessage(), "IllegalArgumentException", JOptionPane.ERROR_MESSAGE); }
        }
        else JOptionPane.showMessageDialog(null, "Reporte - Error al ingresar datos", "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
}