/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wiZa
 */
public class RepuestosAsignar extends javax.swing.JDialog {
    public static int ID;
    private DefaultTableModel Tabla;
    private Object[] Codigos;
    private Object[] Depositos;
    private int[] Cantidades;
    private Deposito D;
    /**
     * Creates new form RepuestosCargo
     * @param parent
     * @param modal
     */
    public RepuestosAsignar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        cargarDepositos();
        iniciarTabla();
        String I = "";
        if(ID > 0) {
            for(int i = 0; i<(8-Integer.toString(ID).length());i++)
                I = I + "0";
            lblCargo.setText(I + ID);
        }
        else lblCargo.setText("**ERROR**");
    }
    private void cargarDepositos() {
        try {
            Main.BD.Consultar("select codigo from depositos de inner join depocat dc on de.id_deposito = dc.deposito inner join categusu ca on dc.categoria = ca.id_categoria where ca.id_categoria = "+Login.Actual.getCategoria());
            cmbDepositos.setModel(Main.BD.Combo("codigo"));
            D = new Deposito(cmbDepositos.getSelectedItem().toString());
            txtDescripcion.setText(D.getDescripcion());
            txtCodigo.requestFocus();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "RepAsig - cgDep", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    private void iniciarTabla() {
        Tabla = new DefaultTableModel();
        Object Encabezados[] = new Object[4];
        Encabezados[0] = "Repuesto";
        Encabezados[1] = "Deposito";
        Encabezados[2] = "Fecha";
        Encabezados[3] = "Cantidad";
        Tabla.setColumnIdentifiers(Encabezados);
        try {
            Main.BD.Consultar("select r.codigo as repuesto, d.codigo as deposito, i.fecha, i.cantidad from reputi i join repuestos r on r.id_repuesto = i.repuesto join depositos d on d.id_deposito = i.deposito WHERE i.caso = " + ID);
            int r = Main.BD.Registros();
            Codigos = new Object[r];
            Depositos = new Object[r];
            Cantidades = new int[r];
            r = 0;
            while(Main.BD.Siguiente()) {
                Object[] Fila = new Object[4];
                Fila[0] = Main.BD.Campo("repuesto");
                Fila[1] = Main.BD.Campo("deposito");
                Fila[2] = Main.BD.Campo("fecha");
                Fila[3] = Main.BD.Campo("cantidad");
                Codigos[r] = Fila[0];
                Depositos[r] = Fila[1];
                Cantidades[r] = Integer.parseInt(Fila[3].toString());
                Tabla.addRow(Fila);
                r++;
            }
            tblFilas.setModel(Tabla);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),"iniciarTabla",JOptionPane.ERROR_MESSAGE);
        }
        actualizarCantidades();
    }
    private void actualizarCantidades() {
        int acum = 0;
        String J = "";
        String K = "";
        for(int i=0; i<Tabla.getRowCount();i++)
            acum += Integer.parseInt(Tabla.getValueAt(i, 3).toString());
        for(int j=0; j<(4-Integer.toString(Tabla.getRowCount()).length()); j++)
            J = J + "0";
        for(int k=0; k<(4-Integer.toString(acum).length()); k++)
            K = K + "0";
        lblFilas.setText(J + Tabla.getRowCount());
        lblUnidades.setText(K + acum);
    }
    private void agregarRepuesto(String R, String D, String E) {
        Boolean agregar = true;
        Object[] F = new Object[4];
        F[0] = R;
        F[1] = D;
        F[2] = new java.util.Date().toString();
        F[3] = getCantidad(F[0].toString());
        for(int i = 0; i < tblFilas.getRowCount(); i++) {
            if(F[0].toString().equalsIgnoreCase(tblFilas.getValueAt(i, 0).toString()) && F[1].toString().equalsIgnoreCase(tblFilas.getValueAt(i, 1).toString())) agregar = false;
        }
        if(agregar)
            if(Integer.parseInt(E) - Integer.parseInt(F[3].toString()) >= 0) Tabla.addRow(F);
            else {
                JOptionPane.showMessageDialog(null, "La cantidad a descargar excede la existencia\nSe asignará una unidad como descargo","Codigo: " + R,JOptionPane.INFORMATION_MESSAGE);
                F[3] = "1";
                Tabla.addRow(F);
            }
        else JOptionPane.showMessageDialog(null, "No puede ingresar un codigo repetido\nAgregue la cantidad deseada a la fila anterior","Codigo: " + R,JOptionPane.ERROR_MESSAGE);
        actualizarCantidades();
    }
    private void agregarRepuesto() {
        Repuestos.aSel = true;
        Repuestos.depo = D.getCodigo();
        new Repuestos(null,true).setVisible(true);
        try {
            if(Repuestos.ID.length() > 0) {
                Main.BD.Consultar("select r.codigo as repuesto, d.codigo as deposito, i.cantidad from existencias i join repuestos r on r.id_repuesto = i.repuesto join depositos d on d.id_deposito = i.deposito where id_deposito = "+ D.getId() +" and id_repuesto = " + Repuestos.ID);
                if(Main.BD.Siguiente()) {
                    if(Integer.parseInt(Main.BD.Campo("cantidad"))<=0)
                        JOptionPane.showMessageDialog(null, "El repuesto seleccionado ("+Main.BD.Campo("codigo")+") no posee existencia", "Atención", JOptionPane.ERROR_MESSAGE);
                    else agregarRepuesto(Main.BD.Campo("repuesto"),Main.BD.Campo("deposito"),Main.BD.Campo("cantidad"));
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL SELECCIONAR PRODUCTO", JOptionPane.ERROR_MESSAGE);
        }
        Repuestos.depo = "";
    }
        
    private void procesarDescargo() {
        try {
            Main.BD.Con().setAutoCommit(false);
            for(int i = 0; i<Codigos.length; i++) {
                boolean Borrar = true;
                for(int j = 0; j<Tabla.getRowCount(); j++) {
                    if(Codigos[i].toString().equals(Tabla.getValueAt(j, 0).toString()) && Depositos[i].toString().equals(Tabla.getValueAt(j, 1).toString()))
                        Borrar = false;
                }
                if(Borrar) {
                    String R = new Repuesto(Codigos[i].toString(),true).getId_repuesto();
                    int U = new Deposito(Depositos[i].toString()).getId();
                    Main.BD.Ejecutar("update repuestos set cantidad = cantidad + " + Cantidades[i] + " where id_repuesto = " + R);
                    Main.BD.Ejecutar("update existencias set cantidad = cantidad + " + Cantidades[i] + " where repuesto = " + R + " and deposito = " + U);
                    Main.BD.Ejecutar("delete from reputi where repuesto = " + R + " and deposito = " + U + " and caso = " + ID);
                }
            }
            for(int j = 0; j<Tabla.getRowCount(); j++) {
                boolean repetido = false;
                String R = new Repuesto(Tabla.getValueAt(j, 0).toString(),true).getId_repuesto();
                int U = new Deposito(Tabla.getValueAt(j, 1).toString()).getId();
                for(int i = 0; i<Codigos.length; i++) {
                    if(Codigos[i].toString().equals(Tabla.getValueAt(j, 0).toString()) && Depositos[i].toString().equals(Tabla.getValueAt(j, 1).toString())) {
                        repetido = true;
                        Main.BD.Ejecutar("update repuestos set cantidad = cantidad + ("+Cantidades[i]+"-"+Tabla.getValueAt(j, 3).toString()+") where id_repuesto = " + R);
                        Main.BD.Ejecutar("update existencias set cantidad = cantidad + ("+Cantidades[i]+"-"+Tabla.getValueAt(j, 3).toString()+") where repuesto = " + R + " and deposito = " + U);
                        Main.BD.Ejecutar("update reputi set cantidad = " + Tabla.getValueAt(j, 3).toString() + " where repuesto = " + R + " and deposito = " + U + " and caso = " + ID);
                    }
                }
                if(!repetido) {
                    Main.BD.Ejecutar("update repuestos set cantidad = cantidad - " + Tabla.getValueAt(j, 3).toString() + " where id_repuesto = " + R);
                    Main.BD.Ejecutar("update existencias set cantidad = cantidad - " + Tabla.getValueAt(j, 3).toString() + " where repuesto = " + R + " and deposito = " + U);
                    Main.BD.Ejecutar("insert into reputi (caso, repuesto, cantidad, usuario, fecha, deposito) values ("+ID+","+R+","+Tabla.getValueAt(j, 3).toString()+","+Login.Actual.getCedula()+",curdate(),"+U+")");
                }
            }
            Main.BD.Con().commit();
            JOptionPane.showMessageDialog(null, "Asignación exitosa!","Info",JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        catch(SQLException e) {
            String txt = "";
            try {
                txt = e.getMessage();
                Main.BD.Con().rollback();
            }
            catch(SQLException ee) {
                txt = txt + "\n" + ee.getMessage();
            }
            JOptionPane.showMessageDialog(null, txt, "Asignacion", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
    private int getCantidad(String CODIGO) {
        int i = 0;
        try {
            i = Integer.parseInt(JOptionPane.showInputDialog(null, "Indique cantidad", "Codigo: " + CODIGO, JOptionPane.QUESTION_MESSAGE));
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "NUMERO ENTERO INVALIDO", JOptionPane.ERROR_MESSAGE);
        }
        return i;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblFilas = new javax.swing.JLabel();
        lblUnidades = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFilas = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        cmbDepositos = new javax.swing.JComboBox();
        txtDescripcion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ASIGNACIÓN DE REPUESTOS");

        jLabel1.setText("Depósito:");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Caso Nº");

        lblCargo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblCargo.setForeground(new java.awt.Color(255, 0, 0));
        lblCargo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCargo.setText("000001");

        jLabel6.setText("Repuestos:");

        jLabel7.setText("Unidades:");

        lblFilas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFilas.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFilas.setText("0000");

        lblUnidades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUnidades.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnidades.setText("0000");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(4, 4, 4)
                        .addComponent(lblCargo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblFilas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(13, 13, 13)
                        .addComponent(lblUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblCargo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblFilas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblUnidades))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblFilas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Descripción", "Cantidad"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFilas.setColumnSelectionAllowed(true);
        tblFilas.setFocusable(false);
        tblFilas.getTableHeader().setReorderingAllowed(false);
        tblFilas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFilasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFilas);
        tblFilas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFilas.getColumnModel().getColumnCount() > 0) {
            tblFilas.getColumnModel().getColumn(0).setResizable(false);
            tblFilas.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFilas.getColumnModel().getColumn(1).setResizable(false);
            tblFilas.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblFilas.getColumnModel().getColumn(2).setResizable(false);
            tblFilas.getColumnModel().getColumn(2).setPreferredWidth(25);
        }

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_add.png"))); // NOI18N
        btnAgregar.setText("Agregar (F1)");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/procesar.png"))); // NOI18N
        jButton2.setText("Guardar (F3)");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnAtras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_cerrar.png"))); // NOI18N
        btnAtras.setText("Cancelar (ESC)");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        cmbDepositos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDepositos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDepositosActionPerformed(evt);
            }
        });

        txtDescripcion.setEditable(false);

        jLabel2.setText("Código:");

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/lupa2020.png"))); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtCodigo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addComponent(txtDescripcion)
                            .addComponent(cmbDepositos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbDepositos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(jButton2)
                    .addComponent(btnAtras))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        procesarDescargo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblFilasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFilasMouseClicked
        int c;
        try {
            int E = new Repuesto(tblFilas.getValueAt(tblFilas.getSelectedRow(), 0).toString(), true).Existencia(tblFilas.getValueAt(tblFilas.getSelectedRow(), 1).toString());
            int ante = Integer.parseInt(tblFilas.getValueAt(tblFilas.getSelectedRow(), 3).toString());
            c = Integer.parseInt(JOptionPane.showInputDialog(null, "Indique nueva cantidad\nPara borrar fila indique 0", "Edición de Fila", JOptionPane.QUESTION_MESSAGE));
            if(c > 0)
                if(c > ante && (c - ante) > E)
                    JOptionPane.showMessageDialog(null, "La diferencia a descargar excede la existencia\nintente de nuevo","Codigo: "+tblFilas.getValueAt(tblFilas.getSelectedRow(), 0),JOptionPane.INFORMATION_MESSAGE);
             else {
                    tblFilas.setValueAt(c, tblFilas.getSelectedRow(), 3);
                }
            else Tabla.removeRow(tblFilas.getSelectedRow());
            actualizarCantidades();
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ha ingresado un valor inválido!\n\n"+e.getMessage(), "NUMERO INVÁLIDO", JOptionPane.WARNING_MESSAGE);
            evt.consume();
        }
        catch(SQLException ee) {
            JOptionPane.showMessageDialog(null, "Error SQL\n\n"+ee.getMessage(), "NUMERO INVÁLIDO", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_tblFilasMouseClicked

    private void cmbDepositosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDepositosActionPerformed
        try {
            D = new Deposito(cmbDepositos.getSelectedItem().toString());
            txtDescripcion.setText(D.getDescripcion());
            txtCodigo.requestFocus();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "RepAsig - cmbAction", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmbDepositosActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RepuestosAsignar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RepuestosAsignar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RepuestosAsignar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RepuestosAsignar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RepuestosAsignar dialog = new RepuestosAsignar(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAtras;
    private javax.swing.JComboBox cmbDepositos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblFilas;
    private javax.swing.JLabel lblUnidades;
    private javax.swing.JTable tblFilas;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
