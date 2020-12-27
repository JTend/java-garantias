/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

/**
 *
 * @author José Tendero
 */
public class Main {

    public static BaseDeDatos BD;
    public static SQLServer Saint;

    public static void main(String[] args) {
        BD = new BaseDeDatos();
        Saint = new SQLServer();
        new Login(null,true).setVisible(true);
    }
}
