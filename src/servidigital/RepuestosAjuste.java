/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidigital;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wiZa
 */
public class RepuestosAjuste extends javax.swing.JDialog {
    private int ID;
    private DefaultTableModel Tabla;
    /**
     * Creates new form RepuestosCargo
     * @param parent
     * @param modal
     */
    public RepuestosAjuste(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        iniciarTabla();
        try {
            Main.BD.Ejecutar("start transaction");
            Main.BD.Consultar("select ifnull(max(id_ajuste), 0) as maximo from ajustrep");
            String I = "";
            if(Main.BD.Siguiente()) {
                ID = Main.BD.rslSet().getInt("maximo") + 1;
                for(int i = 0; i<(8-Integer.toString(ID).length());i++)
                    I = I + "0";
                lblCargo.setText(I + ID);
            }
            else lblCargo.setText("**ERROR**");
        }
        catch(SQLException ee) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(ee.getMessage()), "ERROR AL INICIAR DESCARGO", JOptionPane.ERROR_MESSAGE);
            Main.BD.Ejecutar2("rollback");
        }
    }
    private void iniciarTabla() {
        Tabla = new DefaultTableModel();
        Object Encabezados[] = new Object[5];
        Encabezados[0] = "Codigo";
        Encabezados[1] = "Nombre";
        Encabezados[2] = "Anterior";
        Encabezados[3] = "Actual";
        Encabezados[4] = "Ajuste";
        Tabla.setColumnIdentifiers(Encabezados);
    }
    private void actualizarCantidades() {
        int acum = 0;
        String J = "";
        String K = "";
        for(int i=0; i<Tabla.getRowCount();i++)
            acum += Integer.parseInt(Tabla.getValueAt(i, 4).toString());
        for(int j=0; j<(4-Integer.toString(Tabla.getRowCount()).length()); j++)
            J = J + "0";
        for(int k=0; k<(4-Integer.toString(acum).length()); k++)
            K = K + "0";
        lblFilas.setText(J + Tabla.getRowCount());
        lblUnidades.setText(K + acum);
        tblFilas.setModel(Tabla);
    }
    private void agregarRepuesto(String C, String D, String E) {
        Boolean agregar = true;
        Object[] F = new Object[5];
        F[0] = C;
        F[1] = D;
        F[2] = E;
        F[3] = getCantidad(F[0].toString(), F[2].toString());
        F[4] = Integer.parseInt(F[3].toString()) - Integer.parseInt(F[2].toString());
        for(int i = 0; i < tblFilas.getRowCount(); i++) {
            if(F[0].toString().equalsIgnoreCase(tblFilas.getValueAt(i, 0).toString())) agregar = false;
        }
        if(agregar)
            if(Integer.parseInt(F[3].toString()) >= 0 && Integer.parseInt(F[4].toString()) != 0 ) Tabla.addRow(F);
            else {
                JOptionPane.showMessageDialog(null, "La nueva existencia no es válida\nSe asumirá un ajuste a cero (0)","Codigo: " + C,JOptionPane.INFORMATION_MESSAGE);
                F[3] = 0;
                F[4] = Integer.parseInt(F[3].toString()) - Integer.parseInt(F[2].toString());
                Tabla.addRow(F);
            }
        else JOptionPane.showMessageDialog(null, "No puede ingresar un codigo repetido\nAgregue la cantidad deseada a la fila previa","Codigo: " + C,JOptionPane.ERROR_MESSAGE);
        //JOptionPane.showMessageDialog(null, "*"+F[4].toString().replaceFirst("-", "")+"*");
        actualizarCantidades();
    }
    private void agregarRepuesto() {
        Repuestos.aSel = true;
        new Repuestos(null,true).setVisible(true);
        try {
            if(Repuestos.ID.length() > 0) {
                Main.BD.Consultar("select codigo,descrip,cantidad from repuestos where id_repuesto = " + Repuestos.ID);
                if(Main.BD.Siguiente()) {
                    agregarRepuesto(Main.BD.Campo("codigo"),Main.BD.Campo("descrip"),Main.BD.Campo("cantidad"));
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL SELECCIONAR PRODUCTO", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void agregarDesde() {
        Repuestos.aSel = true;
        new Repuestos(null,true).setVisible(true);
        try {
            if(Repuestos.ID.length() > 0) {
                Main.BD.Consultar("select codigo,nombre from repuestos where id_repuesto = " + Repuestos.ID);
                if(Main.BD.Siguiente()) {
                    txtDesde.setText(Main.BD.Campo("codigo"));
                    txtDescripDesde.setText(Main.BD.Campo("nombre"));
                }
                else JOptionPane.showMessageDialog(null, "Error al seleccionar repuesto");
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL SELECCIONAR PRODUCTO", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void agregarHasta() {
        Repuestos.aSel = true;
        new Repuestos(null,true).setVisible(true);
        try {
            if(Repuestos.ID.length() > 0) {
                Main.BD.Consultar("select codigo,nombre from repuestos where id_repuesto = " + Repuestos.ID);
                if(Main.BD.Siguiente()) {
                    txtHasta.setText(Main.BD.Campo("codigo"));
                    txtDescripHasta.setText(Main.BD.Campo("nombre"));
                }
                else JOptionPane.showMessageDialog(null, "Error al seleccionar repuesto");
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL SELECCIONAR REPUESTO", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void generarFilas() {
        if(txtDescripDesde.getText().length() > 0 && txtDescripHasta.getText().length() > 0) {
            try {
                Main.BD.Consultar("select id_repuesto from repuestos where codigo = '"+txtDesde.getText()+"'");
                Main.BD.Siguiente();
                String i = Main.BD.Campo("id_repuesto");
                Main.BD.Consultar("select id_repuesto from repuestos where codigo = '"+txtHasta.getText()+"'");
                Main.BD.Siguiente();
                String j = Main.BD.Campo("id_repuesto");
                Main.BD.Consultar("select * from repuestos where id_repuesto >= " + i + " and id_repuesto <= " + j);
                while(Main.BD.Siguiente()) {
                    agregarRepuesto(Main.BD.Campo("codigo"),Main.BD.Campo("nombre"),Main.BD.Campo("cantidad"));
                }
            } catch (SQLException ex) {
                Logger.getLogger(RepuestosAjuste.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else JOptionPane.showMessageDialog(null, "Debe ingresar codigos de inicio y fin para generar automáticamente", "Atención", JOptionPane.WARNING_MESSAGE);
    }
    private void procesarAjuste() {
        String D  = JOptionPane.showInputDialog(null, "Descripción de transacción:", "AJUSTE", JOptionPane.QUESTION_MESSAGE);
        try {
            Main.BD.Ejecutar("insert into ajustrep (usuario,descrip,fecha) values ("+Login.Actual.getCedula()+",'"+D+"',curdate())");
            Main.BD.Consultar("select last_insert_id() as nuevo from ajustrep");
            if(Main.BD.Siguiente()) {
                int C = Integer.parseInt(Main.BD.Campo("nuevo"));
                int R = 0;
                for(int i=0; i<Tabla.getRowCount();i++) {
                    Main.BD.Consultar("select id_repuesto from repuestos where codigo = '"+Tabla.getValueAt(i, 0)+"'");
                    if(Main.BD.Siguiente()) R = Integer.parseInt(Main.BD.Campo("id_repuesto"));
                    int Signo = 1;
                    if(Integer.parseInt(Tabla.getValueAt(i, 2).toString()) > Integer.parseInt(Tabla.getValueAt(i, 3).toString())) Signo = -1;
                    Main.BD.Ejecutar("insert into operep (tipo,id_tipo,repuesto,signo,cantidad,fecha,cantidadAnt,cantidadAct) values (3,"+C+",(select id_repuesto from repuestos where codigo = '"+Tabla.getValueAt(i, 0)+"'),"+Signo+","+Tabla.getValueAt(i, 4).toString().replaceFirst("-", "")+",curdate(),"+Tabla.getValueAt(i, 2)+","+Tabla.getValueAt(i, 3)+")");
                    Main.BD.Ejecutar("update repuestos set cantidad = cantidad + " + Tabla.getValueAt(i, 4) + " where id_repuesto = " + R);
                }
                JOptionPane.showMessageDialog(null, "Ajuste exitoso","Info",JOptionPane.INFORMATION_MESSAGE);
                Main.BD.Ejecutar("commit");
                Map P = new HashMap();
                P.put("ajuste", C);
                new Reporte("/Reportes/ajuste.jasper",P).runReporte("Ajuste Nº " + C, true);
                this.dispose();
            }
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "SQLException", JOptionPane.ERROR_MESSAGE);
            Main.BD.Ejecutar2("rollback");
            dispose();
        }
    }
    private int getCantidad(String CODIGO, String Exis) {
        int i = 0;
        try {
            i = Integer.parseInt(JOptionPane.showInputDialog(null, "Indique nueva existencia\nPara borrar indique  la misma", "Reuesto: "+ CODIGO +" ("+Exis+")", JOptionPane.QUESTION_MESSAGE));
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(e.getMessage()), "NUMERO ENTERO INVALIDO", JOptionPane.ERROR_MESSAGE);
        }
        return i;
    }
    private void salir() {
        Main.BD.Ejecutar2("rollback");
        dispose();
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDesde = new javax.swing.JTextField();
        txtHasta = new javax.swing.JTextField();
        txtDeposito = new javax.swing.JTextField();
        btnDeposito = new javax.swing.JButton();
        btnCAT1 = new javax.swing.JButton();
        btnCAT2 = new javax.swing.JButton();
        txtDescripDesde = new javax.swing.JTextField();
        txtDescripHasta = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblCargo = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblFilas = new javax.swing.JLabel();
        lblUnidades = new javax.swing.JLabel();
        btnGenerar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFilas = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        mnuAgregar = new javax.swing.JMenuItem();
        mnuGenerar = new javax.swing.JMenuItem();
        mnuProcesar = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AJUSTE DE INVENTARIO DE REPUESTOS");

        jLabel1.setText("Depósito:");

        jLabel2.setText("Desde:");

        jLabel3.setText("Hasta:");

        txtDesde.setToolTipText("Ingrese codigo y presione ENTER");
        txtDesde.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDesdeFocusLost(evt);
            }
        });
        txtDesde.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDesdeKeyPressed(evt);
            }
        });

        txtHasta.setToolTipText("Ingrese codigo y presione ENTER");
        txtHasta.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHastaFocusLost(evt);
            }
        });
        txtHasta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHastaKeyPressed(evt);
            }
        });

        txtDeposito.setEditable(false);
        txtDeposito.setText("Servidigital");
        txtDeposito.setFocusable(false);
        txtDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepositoActionPerformed(evt);
            }
        });

        btnDeposito.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/lupa.png"))); // NOI18N
        btnDeposito.setEnabled(false);
        btnDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositoActionPerformed(evt);
            }
        });

        btnCAT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/lupa.png"))); // NOI18N
        btnCAT1.setFocusable(false);
        btnCAT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCAT1ActionPerformed(evt);
            }
        });

        btnCAT2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/lupa.png"))); // NOI18N
        btnCAT2.setFocusable(false);
        btnCAT2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCAT2ActionPerformed(evt);
            }
        });

        txtDescripDesde.setEditable(false);
        txtDescripDesde.setFocusable(false);

        txtDescripHasta.setEditable(false);
        txtDescripHasta.setFocusable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Ajuste");

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

        btnGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/generar.png"))); // NOI18N
        btnGenerar.setText("Generar filas (F2)");
        btnGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarActionPerformed(evt);
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
        jButton2.setText("Procesar (F3)");
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

        mnuGenerar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        mnuGenerar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/generar.png"))); // NOI18N
        mnuGenerar.setText("Generar Filas");
        mnuGenerar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGenerarActionPerformed(evt);
            }
        });
        jMenu1.add(mnuGenerar);

        mnuProcesar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        mnuProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/procesar.png"))); // NOI18N
        mnuProcesar.setText("Procesar Ajuste");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtDeposito, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(txtDesde)
                            .addComponent(txtHasta))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGenerar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCAT1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDescripDesde))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCAT2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDescripHasta)))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnCAT1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addComponent(btnCAT2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1))
                                    .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGenerar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
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

    private void txtDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepositoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepositoActionPerformed

    private void btnDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositoActionPerformed
        
    }//GEN-LAST:event_btnDepositoActionPerformed

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        salir();
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void mnuAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAgregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_mnuAgregarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        agregarRepuesto();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarActionPerformed
        generarFilas();
    }//GEN-LAST:event_btnGenerarActionPerformed

    private void mnuGenerarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGenerarActionPerformed
        generarFilas();
    }//GEN-LAST:event_mnuGenerarActionPerformed

    private void mnuProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcesarActionPerformed
        procesarAjuste();
    }//GEN-LAST:event_mnuProcesarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        procesarAjuste();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        salir();
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void tblFilasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFilasMouseClicked
        int c = -1;
        try {
            c = getCantidad(tblFilas.getValueAt(tblFilas.getSelectedRow(), 0).toString(), tblFilas.getValueAt(tblFilas.getSelectedRow(), 2).toString());
        }
        catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Número INVÁLIDO\nSe descartará esta fila","Codigo: "+tblFilas.getValueAt(tblFilas.getSelectedRow(), 0),JOptionPane.INFORMATION_MESSAGE);
        }
        if(c >= 0)
            if(Integer.parseInt(tblFilas.getValueAt(tblFilas.getSelectedRow(), 2).toString()) != c) {
                tblFilas.setValueAt(c, tblFilas.getSelectedRow(), 3);
                tblFilas.setValueAt(c - Integer.parseInt(tblFilas.getValueAt(tblFilas.getSelectedRow(), 2).toString()), tblFilas.getSelectedRow(), 4);
            }
            else {
                JOptionPane.showMessageDialog(null, "La nueva existencia es igual a la anterior\nSe descartará esta fila","Codigo: "+tblFilas.getValueAt(tblFilas.getSelectedRow(), 0),JOptionPane.INFORMATION_MESSAGE);
                Tabla.removeRow(tblFilas.getSelectedRow());
            }
        else Tabla.removeRow(tblFilas.getSelectedRow());
        actualizarCantidades();
    }//GEN-LAST:event_tblFilasMouseClicked

    private void txtDesdeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDesdeKeyPressed
        if(evt.getKeyCode() == 10) {
            try { //Desde
                Main.BD.Consultar("select codigo,nombre from repuestos where codigo = " + txtDesde.getText());
                if(Main.BD.Siguiente()) {
                    txtDesde.setText(Main.BD.Campo("codigo"));
                    txtDescripDesde.setText(Main.BD.Campo("nombre"));
                }
                else {
                    txtDesde.setText("");
                    txtDescripDesde.setText("");
                    agregarDesde();
                }
            } catch (SQLException ex) {
                txtDesde.setText("");
                txtDescripDesde.setText("");
                agregarDesde();
            }
        }
    }//GEN-LAST:event_txtDesdeKeyPressed

    private void txtHastaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHastaKeyPressed
        if(evt.getKeyCode() == 10) {
            try { //Hasta
                Main.BD.Consultar("select codigo,nombre from repuestos where codigo = " + txtHasta.getText());
                if(Main.BD.Siguiente()) {
                    txtHasta.setText(Main.BD.Campo("codigo"));
                    txtDescripHasta.setText(Main.BD.Campo("nombre"));
                }
                else {
                    txtHasta.setText("");
                    txtDescripHasta.setText("");
                    agregarHasta();
                }
            } catch (SQLException ex) {
                txtHasta.setText("");
                txtDescripHasta.setText("");
                agregarHasta();
            }
        }
    }//GEN-LAST:event_txtHastaKeyPressed

    private void btnCAT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCAT1ActionPerformed
        agregarDesde();
    }//GEN-LAST:event_btnCAT1ActionPerformed

    private void btnCAT2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCAT2ActionPerformed
        agregarHasta();
    }//GEN-LAST:event_btnCAT2ActionPerformed

    private void txtDesdeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDesdeFocusLost
        try {
            if(txtDesde.getText().length() > 0) {
                Main.BD.Consultar("select codigo,nombre from repuestos where codigo = " + txtDesde.getText());
                if(Main.BD.Siguiente()) {
                    txtDesde.setText(Main.BD.Campo("codigo"));
                    txtDescripDesde.setText(Main.BD.Campo("nombre"));
                }
                else {
                    txtDesde.setText("");
                    txtDescripDesde.setText("");
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL VALIDAR REPUESTO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtDesdeFocusLost

    private void txtHastaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHastaFocusLost
        try {
            if(txtHasta.getText().length() > 0) {
                Main.BD.Consultar("select codigo,nombre from repuestos where codigo = " + txtHasta.getText());
                if(Main.BD.Siguiente()) {
                    txtHasta.setText(Main.BD.Campo("codigo"));
                    txtDescripHasta.setText(Main.BD.Campo("nombre"));
                }
                else {
                    txtHasta.setText("");
                    txtDescripHasta.setText("");
                }
            }
        }
        catch(SQLException E) {
            JOptionPane.showMessageDialog(null, Main.BD.showException(E.getMessage()), "ERROR AL VALIDAR REPUESTO", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtHastaFocusLost

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
            java.util.logging.Logger.getLogger(RepuestosAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RepuestosAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RepuestosAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RepuestosAjuste.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RepuestosAjuste dialog = new RepuestosAjuste(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCAT1;
    private javax.swing.JButton btnCAT2;
    private javax.swing.JButton btnDeposito;
    private javax.swing.JButton btnGenerar;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JMenuItem mnuGenerar;
    private javax.swing.JMenuItem mnuProcesar;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JTable tblFilas;
    private javax.swing.JTextField txtDeposito;
    private javax.swing.JTextField txtDescripDesde;
    private javax.swing.JTextField txtDescripHasta;
    private javax.swing.JTextField txtDesde;
    private javax.swing.JTextField txtHasta;
    // End of variables declaration//GEN-END:variables
}
