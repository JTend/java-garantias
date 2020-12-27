package servidigital;

import java.awt.Dialog.ModalExclusionType;
import java.io.File;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * @author José Tendero
 */
public class Reporte1 {
    /* Clase intermediaria entre el sistema y los reportes creados con el plugin de
     IReport para Netbeans para agilizar el llamado a los reportes */
    private final BaseDeDatos db;
    private JasperReport Master;
    private JasperPrint Impresion;
    private JasperViewer Mostrador;
    private final Map Param;
    private final String url;

    public Reporte1(String URL, Map Parametros) {
        //En el constructor se guardan los parametros en los atributos de la clase
        db = new BaseDeDatos();
        Param = Parametros;
        url = URL;
    }

    public void runReporte(String Titulo, Boolean Visualizar) {
        try {
            //Ubicacion del reporte en formato .Jasper
            String Archivo = System.getProperty("user.dir") + url;

            /* Objeto master que se encarga de leer la configuracion del archivo
            .jasper */
            //new File(Archivo);
            Master = (JasperReport)JRLoader.loadObject(new File(Archivo));

            /* Objeto que carga los datos requeridos por el reporte, la instancia
             de este objeto es todo lo que se necesita para imprimirse o mostrarse */
            Impresion = JasperFillManager.fillReport(Master, Param, db.Con());

            if(Visualizar) {
                /* El objeto mostrador es un objeto utilizado para mostrar en
                 pantalla el reporte con opciones generales como imprimir y exportar entre otras */
                Mostrador = new JasperViewer(Impresion, false);
                
                Mostrador.setAlwaysOnTop(true);
                Mostrador.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
                Mostrador.setTitle(Titulo);
                Mostrador.setVisible(true);
            }
            else {
                //Imprime el reporte sin mostrar opciones de impresion
                JasperPrintManager.printReport(Impresion, true);
            }
        }
        /*catch(NullPointerException ee) {
            JOptionPane.showMessageDialog(null,ee.getMessage(),"JRException",JOptionPane.ERROR_MESSAGE);
        }*/
        catch(JRException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),"JRException",JOptionPane.ERROR_MESSAGE);
        }
        catch(SecurityException E) {
            JOptionPane.showMessageDialog(null,E.getMessage(),"SecurityException",JOptionPane.ERROR_MESSAGE);
        }
    }
}
