package servidigital;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * @author Jose Tendero
 */

public final class BaseDeDatos {
    private ResultSet rs;	//Objeto de clase que funciona como contenedor de los registros que se consultan
    private Statement s;	//Objeto de clase que se conecta con el driver de conexi�n a la base de datos
    private Connection con;	//Objeto de clase que contiene los datos relativos a la conexi�n
    private Statement s2;
    private ResultSet rs2;
    /****************************** CONSTRUCTOR
     * @param Exc
     * @return  *******************************/
    //La conexi�n se crea apenas se instancia un objeto de la clase BaseDeDatos
    public String showException(String Exc) {
        String Fin = "";
        int j = 0;
        for(int i = 0; i < Exc.length(); i++) {
            if(Exc.charAt(i) == "\n".charAt(0)) j = 0;
            if(j%80 == 0) Fin = Fin + "\n";
            Fin = Fin + Exc.charAt(i);
            j++;
        }
        return Fin;
    }
    public BaseDeDatos() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            
            con = DriverManager.getConnection("jdbc:mysql://SERVERMYSQL:3306/servidigital?useSSL=false",
                                                "root",
                                                "password");
            s = con.createStatement();
            s2 = con.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "No se puede acceder al servidor\n" + showException(e.getMessage()),
                    "MySQL - BD.Constructor", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    } //CONSTRUCTOR

    /************************** INTERFAZ DE USUARIO
     * @return  ***************************/
    public boolean hayRegistros() {
        try {
            if(rs.next()) {
                rs.beforeFirst();
                return true;
            }
                else return false;
            }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, showException(e.getMessage()), "MySQL - hayRegistros()", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean Siguientes()  throws SQLException {
        return rs.next();
    }
    public boolean Siguiente() throws SQLException {
        return rs.next();
    }
    public boolean esUltimo() {
        try {
            return rs.isLast();
            }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, showException(e.getMessage()), "MySQL - esUltimo()", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public String Campo(String C) {
        try {
            return rs.getString(C);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,showException(e.getMessage()),
                    "MySQL - BD.Campo(String)",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return null;
        }
    }
    public int Registros()      {
        int i = 0;
        try {
            if(hayRegistros()) {
                while(rs.next()) {
                    i++;
                }
                rs.beforeFirst();
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDeDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
    public ResultSet rslSet()   { return rs;  }
    public Connection Con()     { return con; } //Retorna la conexion utilizada al generar los reportes
    /************************** METODOS DE LA CLASE
     * @param Sentencia
     * @return 
     * @throws java.sql.SQLException ***************************/
    public ResultSet Consultar(String Sentencia) throws SQLException {
        rs = null;
        rs = s.executeQuery(Sentencia);
        return rs;
    }
    public void Ejecutar(String Sentencia) throws SQLException {
        s2.executeUpdate(Sentencia);
    }
    public void Ejecutar2(String Sentencia) {
        try {
            s2.executeUpdate(Sentencia);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, showException(e.getMessage()), "MySQL - Ejecutar2(String)", JOptionPane.ERROR_MESSAGE);
        }
    }
    public TableModel Modelo() {
	try {
            DefaultTableModel Mod = new DefaultTableModel(); //Es nuestro modelo de tabla a retornar
        
            ResultSetMetaData Meta = rs.getMetaData();
            int Cols = Meta.getColumnCount();	//Cantidad de campos por registro presente en el ResultSet
            Object Encabezados[] = new Object[Cols];//Se crea una matriz para guardar los nombres de los campos de los registros (no los registros)
            for(int Col = 0; Col<Cols; Col++)
                Encabezados[Col] = Meta.getColumnLabel(Col + 1);
	
            Mod.setColumnIdentifiers(Encabezados);

            while(rs.next()) { //Recorremos los registros en el resultset
                Object[] Fila = new Object[Cols];	//Matriz que contenera los datos de 1 registro
                for(int Col = 0; Col<Cols; Col++) 	//Bucle para ingresar los distintos campos de cada registro
                    Fila[Col] = rs.getObject(Col + 1);
                Mod.addRow(Fila);
            }
            return Mod; //Este valor de retorno se asigna a un JTable mediante el metodo JTable.setTableModel(TableModel);
        }
        catch(SQLException e) {
           JOptionPane.showMessageDialog(null,showException(e.getMessage()),
                    "MySQL - BD.Modelo()",
                    JOptionPane.ERROR_MESSAGE);
           return null;
        }
    }
    public DefaultListModel Lista(String Campo){
        try {
            DefaultListModel Mod = new DefaultListModel(); //Es nuestro modelo de tabla a retornar

            while(rs.next()) { //Recorremos los registros en el resultset
                Mod.addElement(rs.getString(Campo));
            }
            return Mod; //Este valor de retorno se asigna a un JTable mediante el metodo JTable.setTableModel(TableModel);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, showException(e.getMessage()), "MySQL - Lista()", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public ComboBoxModel Combo(String Campo) {
        try {
            DefaultComboBoxModel Mod = new DefaultComboBoxModel();
            while(rs.next()) {
                Mod.addElement(rs.getString(Campo));
            }
            return Mod;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, showException(e.getMessage()), "MySQL - Combo()", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}