package servidigital;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author José Tendero
 */
public class CasoManejar extends javax.swing.JDialog {
    private static String ID;
    private String Estado;

    private boolean hayTecnico = false;
    private boolean hayDiagnostico = false;
    private boolean haySolucion = false;

    private String Tec;
    private String Dia;
    private String Sol;

    /** Creates new form CasoEditar
     * @param parent
     * @param modal */
    public CasoManejar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        Tec = "";
        Dia = "";
        Sol = "";
        cargarCaso();
    }

    public static void setID(String id) {
        ID = id;
    }

    private void cargarCaso() {
        try {
            Main.BD.Consultar("select casos.*, productos.codigo, productos.descripcion, equipo.serial, clientes.nombre, tiendas.nombre as tienda "
                + "from casos, equipo, productos, clientes, tiendas "
                + "where equipo.id_producto = productos.id_producto and casos.id_equipo = equipo.id_equipo and tiendas.id_tienda = casos.id_tienda and clientes.cedula = casos.cedula and id_caso = " + ID);
        
            if(Main.BD.rslSet().next()) {
                this.setTitle("Caso Nº: " + ID);
                lblCedula.setText(Main.BD.rslSet().getString("cedula"));
                lblNombre.setText(Main.BD.rslSet().getString("nombre"));
                lblTienda.setText(Main.BD.rslSet().getString("tienda"));
                lblFactura.setText(Main.BD.rslSet().getString("factura"));
                lblCodigo.setText(Main.BD.rslSet().getString("codigo"));
                lblSerial.setText(Main.BD.rslSet().getString("serial"));
                lblEquipo.setText(Main.BD.rslSet().getString("descripcion"));
                lblFecha.setText(Main.BD.rslSet().getString("fecha_creacion"));
                txtDesc_caso.setText(Main.BD.rslSet().getString("desc_caso"));
                lblEstado.setText(Main.BD.rslSet().getString("estado"));
                Estado = lblEstado.getText();
                if(Estado.equals("Reparacion")) {
                    btnReparacion.setEnabled(false);
                    btnRepuestos.setEnabled(true);
                }
                if(Estado.equals("Listo")) {
                    btnReparacion.setEnabled(false);
                    btnListo.setEnabled(false);
                }
                if(Estado.equals("Entregado")) {
                    btnReparacion.setEnabled(false);
                    btnListo.setEnabled(false);
                    btnEntregado.setEnabled(false);
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(this, E.getMessage());
        }
    }

    private String asignarTecnico() {
        Tecnicos.aSel = true;
        new Tecnicos(null,true).setVisible(true);
        if(Tecnicos.aSel)
            return asignarTecnico();
        else
            return Tecnicos.ID;
    }

    private String asignarDiagnostico() {
        Diagnosticos.aSel = true;
        new Diagnosticos(null,true).setVisible(true);
        if(Diagnosticos.aSel)
            return asignarDiagnostico();
        else
            return Diagnosticos.ID;
    }

    private String asignarSolucion() {
        Soluciones.aSel = true;
        new Soluciones(null,true).setVisible(true);
        if(Soluciones.aSel)
            return asignarSolucion();
        else
            return Soluciones.ID;
    }

    private void procesar() {
        try {
            if(JOptionPane.showConfirmDialog(this, "Una vez procesado el cambio de estado este no puede regresar a un estado anterior\n¿Desea continuar?","Atencion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String SQL = "update casos set estado = '"+lblEstado.getText()+"'";
                if(hayTecnico)
                    SQL = SQL + ",fecha_reparacion = curdate(), id_tecnico = " + Tec;
                if(hayDiagnostico)
                    SQL = SQL + ",id_diagnostico = " + Dia;
                if(haySolucion)
                    SQL = SQL + ",id_solucion = " + Sol;
                if(lblEstado.getText().equals("Listo"))
                    SQL = SQL + ",fecha_solucion = curdate()";
                if(lblEstado.getText().equals("Entregado"))
                    SQL = SQL + ",fecha_entrega = curdate()";
                SQL = SQL + " where id_caso = " + ID;
                Main.BD.Ejecutar(SQL);
                JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente", "Manejo de casos", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblCedula = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblFactura = new javax.swing.JLabel();
        lblTienda = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        lblSerial = new javax.swing.JLabel();
        lblEquipo = new javax.swing.JLabel();
        lblAveria = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesc_caso = new javax.swing.JTextArea();
        btnReparacion = new javax.swing.JButton();
        btnListo = new javax.swing.JButton();
        btnEntregado = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        btnReimprimir = new javax.swing.JButton();
        btnRepuestos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manejo de Casos");

        jLabel1.setText("Cedula:");

        jLabel3.setText("Equipo:");

        jLabel2.setText("Codigo:");

        jLabel4.setText("Nombre:");

        jLabel5.setText("Serial:");

        jLabel6.setText("Tienda:");

        jLabel7.setText("Factura:");

        lblCedula.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblFactura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblTienda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblCodigo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblSerial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblEquipo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        lblAveria.setText("Avería:");

        txtDesc_caso.setColumns(20);
        txtDesc_caso.setEditable(false);
        txtDesc_caso.setLineWrap(true);
        txtDesc_caso.setRows(4);
        txtDesc_caso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDesc_casoKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(txtDesc_caso);

        btnReparacion.setText("2. Reparación");
        btnReparacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReparacionActionPerformed(evt);
            }
        });

        btnListo.setText("3. Listo");
        btnListo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListoActionPerformed(evt);
            }
        });

        btnEntregado.setText("4. Entregado");
        btnEntregado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntregadoActionPerformed(evt);
            }
        });

        jLabel9.setText("Estado:");

        lblEstado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(0, 0, 255));

        jLabel8.setText("Fecha:");

        lblFecha.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        btnSalir.setBackground(new java.awt.Color(255, 204, 204));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/btnCerrar.png"))); // NOI18N
        btnSalir.setText("Atras");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(204, 255, 204));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/Ok.png"))); // NOI18N
        btnGuardar.setText("Procesar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel10.setText("Cambiar:");

        btnReimprimir.setBackground(new java.awt.Color(204, 204, 255));
        btnReimprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/reports.png"))); // NOI18N
        btnReimprimir.setText("Reimprimir");
        btnReimprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReimprimirActionPerformed(evt);
            }
        });

        btnRepuestos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/tools.png"))); // NOI18N
        btnRepuestos.setText("Repuestos");
        btnRepuestos.setEnabled(false);
        btnRepuestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRepuestosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(lblFactura, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                            .addComponent(lblCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSerial, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(lblTienda, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                            .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAveria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRepuestos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReimprimir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(btnReparacion, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnListo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEntregado, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(lblCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(lblFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTienda, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5)
                    .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSerial, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(lblFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAveria)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEntregado, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(btnListo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReparacion, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSalir)
                    .addComponent(btnGuardar)
                    .addComponent(btnReimprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRepuestos))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDesc_casoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesc_casoKeyReleased
        if(Cadenas.MiTrim(100, txtDesc_caso.getText()).length() >= 100)
            txtDesc_caso.setText(Cadenas.MiTrim(100, txtDesc_caso.getText()));
}//GEN-LAST:event_txtDesc_casoKeyReleased

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
}//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        procesar();
}//GEN-LAST:event_btnGuardarActionPerformed

    private void btnReparacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReparacionActionPerformed
        try {
            if(lblEstado.getText().equals("Abierto")) {
                Tec = asignarTecnico();
                hayTecnico = true;
                Dia = asignarDiagnostico();
                hayDiagnostico = true;
                lblEstado.setText("Reparacion");
                procesar();
                dispose();
                new Caso(ID).imprimirOrden();
            }
            else
                JOptionPane.showMessageDialog(null, "No puede regresar a este estado", "Observación:", JOptionPane.WARNING_MESSAGE);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnReparacionActionPerformed

    private void btnListoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListoActionPerformed
        if(lblEstado.getText().equals("Reparacion")) {
            Sol = asignarSolucion();
            haySolucion = true;
            lblEstado.setText("Listo");
            procesar();
            
            try {
                Main.BD.Consultar("select * from reputi where caso = " + ID);
                if(Main.BD.Siguiente()) {
                    Map P = new HashMap();
                    P.put("caso", Integer.parseInt(ID));
                    new Reporte("asignacion.jasper",P).runReporte("Repuestos Casa Nº " + ID, true);
                }
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "Actualizar caso", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        }
        else
            JOptionPane.showMessageDialog(null, "Antes debe pasar a estado: \"Reparacion\"", "Observación:", JOptionPane.WARNING_MESSAGE);
    }//GEN-LAST:event_btnListoActionPerformed

    private void btnEntregadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregadoActionPerformed
        try {
            if(lblEstado.getText().equals("Listo")) {
                lblEstado.setText("Entregado");
                procesar();
                dispose();
                new Caso(ID).imprimirEntrega();
            }
            else
                JOptionPane.showMessageDialog(null, "Antes debe pasar a estado: \"Listo para entregar\"", "Observación:", JOptionPane.WARNING_MESSAGE);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "btnEntregado()", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEntregadoActionPerformed

    private void btnReimprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReimprimirActionPerformed
        try {
            if(lblEstado.getText().equals("Abierto"))
                new Caso(ID).imprimirApertura();
            else
                if(lblEstado.getText().equals("Reparacion") || lblEstado.getText().equals("Listo"))
                    new Caso(ID).imprimirOrden();
                else if(lblEstado.getText().equals("Entregado"))
                    new Caso(ID).imprimirEntrega();
                else JOptionPane.showMessageDialog(null, "Caso presenta error en estado, consulte al administrador");
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: \n\n"+Main.BD.showException(e.getMessage()), "btnReimprimir()", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnReimprimirActionPerformed

    private void btnRepuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRepuestosActionPerformed
        RepuestosAsignar.ID = Integer.parseInt(ID);
        new RepuestosAsignar(null,true).setVisible(true);
    }//GEN-LAST:event_btnRepuestosActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CasoManejar dialog = new CasoManejar(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntregado;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnListo;
    private javax.swing.JButton btnReimprimir;
    private javax.swing.JButton btnReparacion;
    private javax.swing.JButton btnRepuestos;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAveria;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblEquipo;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblFactura;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblSerial;
    private javax.swing.JLabel lblTienda;
    private javax.swing.JTextArea txtDesc_caso;
    // End of variables declaration//GEN-END:variables
}
