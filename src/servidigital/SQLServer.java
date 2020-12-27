package servidigital;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * @author Jose Tendero
 */

public class SQLServer {
    private ResultSet rs;	//Objeto de clase que funciona como contenedor de los registros que se consultan
    private Statement s;	//Objeto de clase que se conecta con el driver de conexi�n a la base de datos
    private Connection con;	//Objeto de clase que contiene los datos relativos a la conexi�n
    /****************************** CONSTRUCTOR *******************************/
    //La conexi�n se crea apenas se instancia un objeto de la clase BaseDeDatos
    public SQLServer() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                        "databaseName=AVALSYSTEMS;user=avalsys;password=2RALdzy1OCStnyS00;";
            con = DriverManager.getConnection(connectionUrl);
            s = con.createStatement();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()),
                    "SQLServer - BD.Constructor", JOptionPane.ERROR_MESSAGE);
        }
        catch(ClassNotFoundException E) {
            JOptionPane.showMessageDialog(null, E.getMessage());
        }
    } //CONSTRUCTOR

    /************************** INTERFAZ DE USUARIO
     * @return  ***************************/
    public ResultSet rslSet() {
        return rs; //Por convenci�n se establece el atributo de clase ResultSet como privado y se retorna con este metodo
    } 
    public Connection Con() { return con; } //Retorna la conexion utilizada al generar los reportes

    public boolean hayRegistros() {
        try {
            if(rs.next()) {
                rs.beforeFirst();
                return true;
            }
                else return false;
            }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLServer - hayRegistros()", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public boolean Siguiente() {
        try {
            return rs.next();
            }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLServer - Siguiente()", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public String Campo(String C) {
        try {
            return rs.getString(C);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, C + "\n" + e.getMessage(), "SQLServer - Campo(String)", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    //INTERFAZ
    /************************** METODOS DE LA CLASE ***************************/
    public ResultSet Consultar(String Sentencia) {
        //Se usa el ResultSet, hacer consultas SQL
	//El objeto s de la clase Statement es el acceso a los datos y estos pasan al objeto ResultSet para ser manipulados
        try {
            rs = null;
            rs = s.executeQuery(Sentencia);
            return rs;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),
                    "SQLServer - BD.Consultar()",
                    JOptionPane.ERROR_MESSAGE);
            return rs; //Debe retornarse un resultset, as� este vac�o
        }
    }
    public void Ejecutar(String Sentencia) {
        //No se usa ResultSet, esto es para Update y Delete
        try {
            s.executeUpdate(Sentencia);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),
                    "SQLServer - DB.Ejecutar()", JOptionPane.ERROR_MESSAGE);
        }
	//Es recomendado hacer esto para Updates y Deletes para optimizar el uso de memoria
    }
    public TableModel Modelo() throws SQLException {
	DefaultTableModel Mod = new DefaultTableModel(); //Es nuestro modelo de tabla a retornar
        
        ResultSetMetaData Meta = rs.getMetaData();
        int Cols = Meta.getColumnCount();	//La variable Cols representa la cantidad de campos por registro presente en el ResultSet
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
}