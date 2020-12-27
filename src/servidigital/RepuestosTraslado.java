/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jose
 */
public class RepuestosTraslado extends javax.swing.JDialog {
    private int ID;
    private DefaultTableModel Tabla;
    private Deposito D_Origen;
    private Deposito D_Destino;
    /**
     * Creates new form RepuestosTraslado
     * @param parent
     * @param modal
     */
    public RepuestosTraslado(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        cargarDepositos();
        iniciarTabla();
        try {
            Main.BD.Consultar("select AUTO_INCREMENT from information_schema.TABLES where TABLE_SCHEMA='servidigital' and TABLE_NAME='trasrep'");
            String I = "";
            if(Main.BD.Siguiente()) {
                ID = Main.BD.rslSet().getInt("AUTO_INCREMENT");
                for(int i = 0; i<(8-Integer.toString(ID).length());i++)
                    I = I + "0";
                lblCargo.setText(I + ID);
            }
            else lblCargo.setText("**ERROR**");
        }
        catch(SQLException ee) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(ee.getMessage()), "ERROR AL CREAR CARGO", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarDepositos() {
        try {
            Main.BD.Consultar("select codigo from depositos de inner join depocat dc on de.id_deposito = dc.deposito inner join categusu ca on dc.categoria = ca.id_categoria where ca.id_categoria = "+Login.Actual.getCategoria());
            cmbOrigen.setModel(Main.BD.Combo("codigo"));
            D_Origen = new Deposito(cmbOrigen.getSelectedItem().toString());
            txtOrigen.setText(D_Origen.getDescripcion());
            
            Main.BD.Consultar("select codigo from depositos de inner join depocat dc on de.id_deposito = dc.deposito inner join categusu ca on dc.categoria = ca.id_categoria where ca.id_categoria = "+Login.Actual.getCategoria());
            cmbDestino.setModel(Main.BD.Combo("codigo"));
            D_Destino = new Deposito(cmbDestino.getSelectedItem().toString());
            txtDestino.setText(D_Destino.getDescripcion());
            
            txtCodigo.requestFocus();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "RepAsig - cgDep", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void iniciarTabla() {
        Tabla = new DefaultTableModel();
        Object Encabezados[] = new Object[4];
        Encabezados[0] = "Codigo";
        Encabezados[1] = "Descripción";
        Encabezados[2] = "Existencia";
        Encabezados[3] = "Trasladar";
        Tabla.setColumnIdentifiers(Encabezados);
        tblFilas.setModel(Tabla);
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
        tblFilas.setModel(Tabla);
        if (Tabla.getRowCount() > 0) {
            cmbOrigen.setEnabled(false);
            btnProcesar.setEnabled(true);
            mnuProcesar.setEnabled(true);
        }
        else{
            cmbOrigen.setEnabled(true);
            btnProcesar.setEnabled(false);
            mnuProcesar.setEnabled(false);
        }
    }
    private void agregarRepuesto(String C, String D, String E) {
        Boolean agregar = true;
        Object[] F = new Object[4];
        F[0] = C;
        F[1] = D;
        F[2] = E;
        F[3] = getCantidad(F[0].toString());
        for(int i = 0; i < tblFilas.getRowCount(); i++) {
            if(F[0].toString().equalsIgnoreCase(tblFilas.getValueAt(i, 0).toString())) agregar = false;
        }
        if(agregar)
            if(Integer.parseInt(E) - Integer.parseInt(F[3].toString()) >= 0) Tabla.addRow(F);
            else {
                JOptionPane.showMessageDialog(null, "La cantidad a descargar excede la existencia\nSe asignará la existencia como descargo","Codigo: " + C,JOptionPane.INFORMATION_MESSAGE);
                F[3] = E;
                Tabla.addRow(F);
            }
        else JOptionPane.showMessageDialog(null, "No puede ingresar un codigo repetido\nAplique la cantidad deseada al ya ingresado","Codigo: " + C,JOptionPane.ERROR_MESSAGE);
        actualizarCantidades();
    }
    private void agregarRepuesto() {
        Repuestos.aSel = true;
        Repuestos.depo = D_Origen.getCodigo();
        new Repuestos(null,true).setVisible(true);
        try {
            if(Repuestos.ID.length() > 0) {
                Main.BD.Consultar("select r.codigo, r.descrip, ifnull(e.cantidad,0) as cantidad from repuestos r join depositos d left join existencias e on r.id_repuesto = e.repuesto and d.id_deposito = e.deposito where id_deposito = "+ D_Origen.getId() +" and id_repuesto = " + Repuestos.ID);
                if(Main.BD.Siguiente()) {
                    if(Integer.parseInt(Main.BD.Campo("cantidad"))<=0)
                        JOptionPane.showMessageDialog(null, "El repuesto seleccionado no posee existencia en este deposito", "Atención", JOptionPane.ERROR_MESSAGE);
                    else agregarRepuesto(Main.BD.Campo("codigo"),Main.BD.Campo("descrip"),Main.BD.Campo("cantidad"));
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL SELECCIONAR PRODUCTO", JOptionPane.ERROR_MESSAGE);
        }
        Repuestos.depo = "";
    }
    private int getCantidad(String CODIGO) {
        int i = 0;
        try {
            i = Integer.parseInt(JOptionPane.showInputDialog(null, "Indique cantidad", CODIGO, JOptionPane.QUESTION_MESSAGE));
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
        cmbOrigen = new javax.swing.JComboBox();
        txtOrigen = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblFilas = new javax.swing.JLabel();
        lblUnidades = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        txtDestino = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFilas = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        btnProcesar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuAgregar = new javax.swing.JMenuItem();
        mnuProcesar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Traslado");
        setPreferredSize(new java.awt.Dimension(494, 460));

        jLabel1.setText("Origen:");

        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOrigenActionPerformed(evt);
            }
        });

        txtOrigen.setEditable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Traslado:");

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

        jLabel2.setText("Destino:");

        cmbDestino.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDestinoActionPerformed(evt);
            }
        });

        txtDestino.setEditable(false);

        jLabel3.setText("Código:");

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 255, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/lupa2020.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

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

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_add.png"))); // NOI18N
        btnAgregar.setText("Agregar (F1)");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnAtras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_cerrar.png"))); // NOI18N
        btnAtras.setText("Cancelar (ESC)");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        btnProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/procesar.png"))); // NOI18N
        btnProcesar.setText("Procesar (F3)");
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        jMenu1.setText("Opciones");

        mnuAgregar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mnuAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_add.png"))); // NOI18N
        mnuAgregar.setText("Agregar Repuesto");
        mnuAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAgregarActionPerformed(evt);
            }
        });
        jMenu1.add(mnuAgregar);

        mnuProcesar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mnuProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/procesar.png"))); // NOI18N
        mnuProcesar.setText("Procesar Traslado");
        mnuProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcesarActionPerformed(evt);
            }
        });
        jMenu1.add(mnuProcesar);
        jMenu1.add(jSeparator1);

        mnuSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        mnuSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_cerrar.png"))); // NOI18N
        mnuSalir.setText("Cancelar (Salir)");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        jMenu1.add(mnuSalir);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProcesar))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtOrigen)
                                    .addComponent(txtDestino)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBuscar)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuscar)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnAtras)
                    .addComponent(btnProcesar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAgregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_mnuAgregarActionPerformed

    private void mnuProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcesarActionPerformed
        procesarTraslado();
    }//GEN-LAST:event_mnuProcesarActionPerformed

    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        salir();
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void cmbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOrigenActionPerformed
        try {
            D_Origen = new Deposito(cmbOrigen.getSelectedItem().toString());
            txtOrigen.setText(D_Origen.getDescripcion());
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "RepAsig - cmbAction", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmbOrigenActionPerformed

    private void cmbDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDestinoActionPerformed
        try {
            D_Destino = new Deposito(cmbDestino.getSelectedItem().toString());
            txtDestino.setText(D_Destino.getDescripcion());
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "RepAsig - cmbAction", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmbDestinoActionPerformed

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void tblFilasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFilasMouseClicked
        int c;
        try {
            c = Integer.parseInt(JOptionPane.showInputDialog(null, "Indique nueva cantidad\nPara borrar fila indique 0", "Edición de Fila", JOptionPane.QUESTION_MESSAGE));
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ha ingresado un valor inválido, se asumirá 1\nIntente editar este valor nuevamente", "NUMERO INVÁLIDO", JOptionPane.WARNING_MESSAGE);
            c = 1;
        }
        if(c > 0)
        if(Integer.parseInt(tblFilas.getValueAt(tblFilas.getSelectedRow(), 2).toString()) - c >= 0) tblFilas.setValueAt(c, tblFilas.getSelectedRow(), 3);
        else {
            JOptionPane.showMessageDialog(null, "La cantidad a descargar excede la existencia\nSe asignará la existencia como descargo","Codigo: "+tblFilas.getValueAt(tblFilas.getSelectedRow(), 0),JOptionPane.INFORMATION_MESSAGE);
            tblFilas.setValueAt(tblFilas.getValueAt(tblFilas.getSelectedRow(), 2), tblFilas.getSelectedRow(), 3);
        }
        else Tabla.removeRow(tblFilas.getSelectedRow());
        actualizarCantidades();
    }//GEN-LAST:event_tblFilasMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        salir();
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        procesarTraslado();
    }//GEN-LAST:event_btnProcesarActionPerformed
    private void procesarTraslado() {
        String Descrip  = JOptionPane.showInputDialog(null, "Agregar Nota:", "DESCARGO", JOptionPane.QUESTION_MESSAGE);
        try {
            Main.BD.Con().setAutoCommit(false);
            Main.BD.Consultar("select AUTO_INCREMENT from information_schema.TABLES where TABLE_SCHEMA='servidigital' and TABLE_NAME='trasrep'");
            if(Main.BD.Siguiente()) {
                Main.BD.Ejecutar("insert into trasrep (usuario,descrip,fecha,deporigen,depdestin) values ("+Login.Actual.getCedula()+",'"+Descrip+"',curdate(),"+D_Origen.getId()+","+D_Destino.getId()+")");
                int C = Integer.parseInt(Main.BD.Campo("AUTO_INCREMENT"));
                for(int i=0; i<Tabla.getRowCount();i++) {
                    Repuesto R = new Repuesto(Tabla.getValueAt(i, 0).toString(), true);
                    Main.BD.Ejecutar("insert into itemtra (traslado,repuesto,cantidad) values ("+C+","+R.getId_repuesto()+","+Tabla.getValueAt(i, 3)+")");
                    
                    Main.BD.Ejecutar("update existencias set cantidad = cantidad - " + Tabla.getValueAt(i, 3) + " where  repuesto = " + R.getId_repuesto() + " and deposito = " + D_Origen.getId());
                   
                    Main.BD.Consultar("select * from existencias where repuesto = " + R.getId_repuesto() + " and deposito = " + D_Destino.getId());
                    if(Main.BD.Siguiente())
                        Main.BD.Ejecutar("update existencias set cantidad = cantidad + " + Tabla.getValueAt(i, 3) + " where repuesto = " + R.getId_repuesto() + " and deposito = " + D_Destino.getId());
                    else
                        Main.BD.Ejecutar("insert into existencias (deposito,repuesto,cantidad) values ("+D_Destino.getId()+","+R.getId_repuesto()+","+Tabla.getValueAt(i, 3)+")");
                }
                Main.BD.Con().commit();
                JOptionPane.showMessageDialog(null, "Descargo exitoso","Info",JOptionPane.INFORMATION_MESSAGE);
                Map P = new HashMap();
                P.put("traslado", C);
                new Reporte("Reportes/traslado.jasper",P).runReporte("Traslado Nº " + C, true);
                this.dispose();
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "SQLException", JOptionPane.ERROR_MESSAGE);
            try  {
                JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "SQLException e", JOptionPane.ERROR_MESSAGE);
                Main.BD.Con().rollback();
                this.dispose();
            }
            catch(SQLException ee) {
                JOptionPane.showMessageDialog(null, Main.BD.showException(ee.getMessage()), "SQLException ee", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        }
    }
    private void salir() {
        Main.BD.Ejecutar2("rollback");
        dispose();
    }
    
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
            java.util.logging.Logger.getLogger(RepuestosTraslado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RepuestosTraslado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RepuestosTraslado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RepuestosTraslado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RepuestosTraslado dialog = new RepuestosTraslado(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JComboBox cmbOrigen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lblCargo;
    private javax.swing.JLabel lblFilas;
    private javax.swing.JLabel lblUnidades;
    private javax.swing.JMenuItem mnuAgregar;
    private javax.swing.JMenuItem mnuProcesar;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JTable tblFilas;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtOrigen;
    // End of variables declaration//GEN-END:variables
}
