/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidigital;

import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Jose
 */
public class UsuarioCategorias extends java.awt.Dialog {
    private Categusu cat;
    /**
     * Creates new form UsuarioCategorias
     * @param parent
     * @param modal
     */
    public UsuarioCategorias(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        cargarCategorias();
    }
    private void cargarCategorias() {
        try {
            Main.BD.Consultar("select nombre from categusu");
            lstCategorias.setModel(Main.BD.Lista("nombre"));
            desactivarControles();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, Main.BD.showException(e.getMessage()), "UsuCat - cgCats()", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarCategoria() {
        if(lstCategorias.getSelectedIndex() >= 0) {
            try {
                cat = new Categusu(lstCategorias.getSelectedValue().toString());
                limpiarControles();
                txtNombre.setText(cat.getNombre());
                txtDescrip.setText(cat.getDescrip());
                cargarDepositos();
                cargarAsignados();
                Main.BD.Consultar("select opcion, valor from categacc where categusu = " + cat.getId_categoria());
                while(Main.BD.Siguiente()) {
                    int X = Integer.parseInt(Main.BD.Campo("opcion"));
                    int Y = Integer.parseInt(Main.BD.Campo("valor"));
                    boolean Val = (Y != 0);
                    switch(X) {
                        case 1: this.chkClientes.setSelected(Val); break;
                        case 2: this.chkTiendas.setSelected(Val); break;
                        case 3: this.chkSoportistas.setSelected(Val); break;
                        case 4: this.chkNuevoCaso.setSelected(Val); break;
                        case 5: this.chkAdmCasos.setSelected(Val); break;
                        case 6: this.chkCategRep.setSelected(Val); break;
                        case 7: this.chkEditCatRep.setSelected(Val); break;
                        case 8: this.chkRepuestos.setSelected(Val); break;
                        case 9: this.chkEditRep.setSelected(Val); break;
                        case 10: this.chkDepositos.setSelected(Val); break;
                        case 11: this.chkEditDepo.setSelected(Val); break;
                        case 12: this.chkReimprimir.setSelected(Val); break;
                        case 13: this.chkAjustes.setSelected(Val); break;
                        case 14: this.chkCargos.setSelected(Val); break;
                        case 15: this.chkDescargos.setSelected(Val); break;
                        case 16: this.chkTraslados.setSelected(Val); break;
                        case 17: this.chkRptClientes.setSelected(Val); break;
                        case 18: this.chkCasosMarcas.setSelected(Val); break;
                        case 19: this.chkCasosEquipos.setSelected(Val); break;
                        case 20: this.chkCasosClientes.setSelected(Val); break;
                        case 21: this.chkCasosDiagnosticos.setSelected(Val); break;
                        case 22: this.chkLimitarDepo.setSelected(Val); break;
                        case 23: this.chkInventario.setSelected(Val); break;
                        case 24: this.chkMovimientos.setSelected(Val); break;
                        case 25: this.chkCatUsu.setSelected(Val); break;
                        case 26: this.chkEditCatUsu.setSelected(Val); break;
                        case 27: this.chkUsuarios.setSelected(Val); break;
                        case 28: this.chkEditUsuario.setSelected(Val); break;
                        case 29: this.chkUsuarioPropio.setSelected(Val); break;
                    }
                }
            }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(this, Main.BD.showException(e.getMessage()), "UsuCat - cgCat()", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void cargarDepositos() {
        try {
            Main.BD.Consultar("select * from depositos");
            lstDepositos.setModel(Main.BD.Lista("codigo"));
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, Main.BD.showException(e.getMessage()), "CategUsu - cgDep()", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarAsignados() {
        try {
            Main.BD.Consultar("select depositos.codigo from depositos inner join depocat on depositos.id_deposito = depocat.deposito where categoria = "+cat.getId_categoria());
            lstAsignados.setModel(Main.BD.Lista("codigo"));
            descargarAsignados();
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, Main.BD.showException(e.getMessage()), "CategUsu - cgAsig()", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void descargarAsignados() {
        for(int i = 0; i < lstDepositos.getModel().getSize(); i++) {
            for(int j = 0; j < lstAsignados.getModel().getSize(); j++){
                if(lstDepositos.getModel().getElementAt(i).toString().equalsIgnoreCase(lstAsignados.getModel().getElementAt(j).toString())) {
                    DefaultListModel L = (DefaultListModel)lstDepositos.getModel();
                    L.remove(i);
                }
            }
        }
    }
    private void limpiarControles() {
        this.txtNombre.setText("");
        this.txtDescrip.setText("");
        
        this.chkClientes.setSelected(false);
        this.chkTiendas.setSelected(false);
        this.chkSoportistas.setSelected(false);
        this.chkNuevoCaso.setSelected(false);
        
        this.chkAdmCasos.setSelected(false);
        this.chkCategRep.setSelected(false);
        this.chkEditCatRep.setSelected(false);
        this.chkRepuestos.setSelected(false);
        this.chkEditRep.setSelected(false);
        this.chkDepositos.setSelected(false);
        this.chkEditDepo.setSelected(false);
        this.chkAjustes.setSelected(false);
        this.chkCargos.setSelected(false);
        this.chkDescargos.setSelected(false);
        this.chkTraslados.setSelected(false);
        this.chkReimprimir.setSelected(false);
        
        this.chkRptClientes.setSelected(false);
        this.chkCasosMarcas.setSelected(false);
        this.chkCasosEquipos.setSelected(false);
        this.chkCasosClientes.setSelected(false);
        this.chkCasosDiagnosticos.setSelected(false);
        this.chkLimitarDepo.setSelected(false);
        this.chkInventario.setSelected(false);
        this.chkMovimientos.setSelected(false);
        
        this.chkCatUsu.setSelected(false);
        this.chkEditCatUsu.setSelected(false);
        this.chkUsuarios.setSelected(false);
        this.chkEditUsuario.setSelected(false);
        this.chkUsuarioPropio.setSelected(false);
    }
    private void activarControles() {
        this.txtNombre.setEnabled(true);
        this.txtDescrip.setEnabled(true);
        
        this.chkClientes.setEnabled(true);
        this.chkTiendas.setEnabled(true);
        this.chkSoportistas.setEnabled(true);
        this.chkNuevoCaso.setEnabled(true);
        
        this.chkAdmCasos.setEnabled(true);
        this.chkCategRep.setEnabled(true);
        this.chkEditCatRep.setEnabled(true);
        this.chkRepuestos.setEnabled(true);
        this.chkEditRep.setEnabled(true);
        this.chkDepositos.setEnabled(true);
        this.chkEditDepo.setEnabled(true);
        this.chkAjustes.setEnabled(true);
        this.chkCargos.setEnabled(true);
        this.chkDescargos.setEnabled(true);
        this.chkTraslados.setEnabled(true);
        this.chkReimprimir.setEnabled(true);
        this.btnAdd.setEnabled(true);
        this.btnQuit.setEnabled(true);
        
        this.chkRptClientes.setEnabled(true);
        this.chkCasosMarcas.setEnabled(true);
        this.chkCasosEquipos.setEnabled(true);
        this.chkCasosClientes.setEnabled(true);
        this.chkCasosDiagnosticos.setEnabled(true);
        this.chkLimitarDepo.setEnabled(true);
        this.chkInventario.setEnabled(true);
        this.chkMovimientos.setEnabled(true);
        
        this.chkCatUsu.setEnabled(true);
        this.chkEditCatUsu.setEnabled(true);
        this.chkUsuarios.setEnabled(true);
        this.chkEditUsuario.setEnabled(true);
        this.chkUsuarioPropio.setEnabled(true);
        
        this.bthGuardar.setEnabled(true);
    }
    private void desactivarControles() {
        this.txtNombre.setEnabled(false);
        this.txtDescrip.setEnabled(false);
        
        this.chkClientes.setEnabled(false);
        this.chkTiendas.setEnabled(false);
        this.chkSoportistas.setEnabled(false);
        this.chkNuevoCaso.setEnabled(false);
        
        this.chkAdmCasos.setEnabled(false);
        this.chkCategRep.setEnabled(false);
        this.chkEditCatRep.setEnabled(false);
        this.chkRepuestos.setEnabled(false);
        this.chkEditRep.setEnabled(false);
        this.chkDepositos.setEnabled(false);
        this.chkEditDepo.setEnabled(false);
        this.chkAjustes.setEnabled(false);
        this.chkCargos.setEnabled(false);
        this.chkDescargos.setEnabled(false);
        this.chkTraslados.setEnabled(false);
        this.chkReimprimir.setEnabled(false);
        this.btnAdd.setEnabled(false);
        this.btnQuit.setEnabled(false);
        
        this.chkRptClientes.setEnabled(false);
        this.chkCasosMarcas.setEnabled(false);
        this.chkCasosEquipos.setEnabled(false);
        this.chkCasosClientes.setEnabled(false);
        this.chkCasosDiagnosticos.setEnabled(false);
        this.chkLimitarDepo.setEnabled(false);
        this.chkInventario.setEnabled(false);
        this.chkMovimientos.setEnabled(false);
        
        this.chkCatUsu.setEnabled(false);
        this.chkEditCatUsu.setEnabled(false);
        this.chkUsuarios.setEnabled(false);
        this.chkEditUsuario.setEnabled(false);
        this.chkUsuarioPropio.setEnabled(false);
        
        this.bthGuardar.setEnabled(false);
    }
    private int numero(boolean X) {
        if(X) return 1;
        return 0;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSpinner1 = new javax.swing.JSpinner();
        pnlCategorias = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstCategorias = new javax.swing.JList();
        pnlDetalle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtDescrip = new javax.swing.JTextField();
        pnlAccesos = new javax.swing.JPanel();
        panelAccesos = new javax.swing.JTabbedPane();
        pnlServidigital = new javax.swing.JPanel();
        chkClientes = new javax.swing.JCheckBox();
        chkTiendas = new javax.swing.JCheckBox();
        chkNuevoCaso = new javax.swing.JCheckBox();
        chkSoportistas = new javax.swing.JCheckBox();
        chkAdmCasos = new javax.swing.JCheckBox();
        pnlInventario = new javax.swing.JPanel();
        chkCategRep = new javax.swing.JCheckBox();
        chkRepuestos = new javax.swing.JCheckBox();
        chkEditRep = new javax.swing.JCheckBox();
        chkEditCatRep = new javax.swing.JCheckBox();
        chkAjustes = new javax.swing.JCheckBox();
        chkCargos = new javax.swing.JCheckBox();
        chkDescargos = new javax.swing.JCheckBox();
        chkTraslados = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDepositos = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstAsignados = new javax.swing.JList();
        btnAdd = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        chkReimprimir = new javax.swing.JCheckBox();
        chkEditDepo = new javax.swing.JCheckBox();
        chkDepositos = new javax.swing.JCheckBox();
        pnlReportes = new javax.swing.JPanel();
        chkRptClientes = new javax.swing.JCheckBox();
        chkCasosMarcas = new javax.swing.JCheckBox();
        chkCasosEquipos = new javax.swing.JCheckBox();
        chkCasosClientes = new javax.swing.JCheckBox();
        chkCasosDiagnosticos = new javax.swing.JCheckBox();
        chkLimitarDepo = new javax.swing.JCheckBox();
        chkInventario = new javax.swing.JCheckBox();
        chkMovimientos = new javax.swing.JCheckBox();
        pnlUsuarios = new javax.swing.JPanel();
        chkEditUsuario = new javax.swing.JCheckBox();
        chkCatUsu = new javax.swing.JCheckBox();
        chkUsuarios = new javax.swing.JCheckBox();
        chkUsuarioPropio = new javax.swing.JCheckBox();
        chkEditCatUsu = new javax.swing.JCheckBox();
        btnSalir = new javax.swing.JButton();
        bthGuardar = new javax.swing.JButton();
        btnCrear = new javax.swing.JButton();

        setBackground(java.awt.Color.white);
        setResizable(false);
        setTitle("Categorias o Grupos de Usuarios");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlCategorias.setBackground(new java.awt.Color(255, 255, 255));
        pnlCategorias.setBorder(javax.swing.BorderFactory.createTitledBorder("Categorias"));
        pnlCategorias.setToolTipText("");
        pnlCategorias.setEnabled(false);
        pnlCategorias.setName(""); // NOI18N

        lstCategorias.setToolTipText("Doble click para editar, Supr para borrar");
        lstCategorias.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstCategoriasMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lstCategoriasMousePressed(evt);
            }
        });
        lstCategorias.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstCategoriasValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(lstCategorias);

        javax.swing.GroupLayout pnlCategoriasLayout = new javax.swing.GroupLayout(pnlCategorias);
        pnlCategorias.setLayout(pnlCategoriasLayout);
        pnlCategoriasLayout.setHorizontalGroup(
            pnlCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
        );
        pnlCategoriasLayout.setVerticalGroup(
            pnlCategoriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pnlDetalle.setBackground(new java.awt.Color(255, 255, 255));
        pnlDetalle.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle"));
        pnlDetalle.setEnabled(false);
        pnlDetalle.setOpaque(false);

        jLabel1.setText("Nombre:");

        txtNombre.setToolTipText("");

        jLabel2.setText("Descripción:");

        javax.swing.GroupLayout pnlDetalleLayout = new javax.swing.GroupLayout(pnlDetalle);
        pnlDetalle.setLayout(pnlDetalleLayout);
        pnlDetalleLayout.setHorizontalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombre)
                    .addComponent(txtDescrip))
                .addContainerGap())
        );
        pnlDetalleLayout.setVerticalGroup(
            pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetalleLayout.createSequentialGroup()
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDescrip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAccesos.setBackground(new java.awt.Color(255, 255, 255));
        pnlAccesos.setBorder(javax.swing.BorderFactory.createTitledBorder("Accesos"));
        pnlAccesos.setEnabled(false);
        pnlAccesos.setOpaque(false);

        panelAccesos.setToolTipText("");

        pnlServidigital.setBackground(new java.awt.Color(255, 255, 255));
        pnlServidigital.setOpaque(false);

        chkClientes.setText("Registro de Clientes");
        chkClientes.setName("1"); // NOI18N
        chkClientes.setOpaque(false);

        chkTiendas.setText("Registro de Tiendas");
        chkTiendas.setName("2"); // NOI18N
        chkTiendas.setOpaque(false);

        chkNuevoCaso.setText("Generar Casos");
        chkNuevoCaso.setName("4"); // NOI18N
        chkNuevoCaso.setOpaque(false);

        chkSoportistas.setText("Registro de Soportistas");
        chkSoportistas.setName("3"); // NOI18N
        chkSoportistas.setOpaque(false);

        chkAdmCasos.setText("Administrar Casos");
        chkAdmCasos.setName("5"); // NOI18N
        chkAdmCasos.setOpaque(false);

        javax.swing.GroupLayout pnlServidigitalLayout = new javax.swing.GroupLayout(pnlServidigital);
        pnlServidigital.setLayout(pnlServidigitalLayout);
        pnlServidigitalLayout.setHorizontalGroup(
            pnlServidigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServidigitalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlServidigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkClientes)
                    .addComponent(chkTiendas)
                    .addComponent(chkNuevoCaso)
                    .addComponent(chkSoportistas)
                    .addComponent(chkAdmCasos))
                .addContainerGap(292, Short.MAX_VALUE))
        );
        pnlServidigitalLayout.setVerticalGroup(
            pnlServidigitalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlServidigitalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkClientes)
                .addGap(18, 18, 18)
                .addComponent(chkTiendas)
                .addGap(18, 18, 18)
                .addComponent(chkSoportistas)
                .addGap(18, 18, 18)
                .addComponent(chkNuevoCaso)
                .addGap(18, 18, 18)
                .addComponent(chkAdmCasos)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        panelAccesos.addTab("Servidigital", pnlServidigital);

        pnlInventario.setOpaque(false);

        chkCategRep.setText("Categorias");
        chkCategRep.setName("6"); // NOI18N
        chkCategRep.setOpaque(false);

        chkRepuestos.setText("Repuestos");
        chkRepuestos.setName("8"); // NOI18N
        chkRepuestos.setOpaque(false);

        chkEditRep.setText("Escribir Repuestos");
        chkEditRep.setName("9"); // NOI18N
        chkEditRep.setOpaque(false);

        chkEditCatRep.setText("Escribir Categorias");
        chkEditCatRep.setName("7"); // NOI18N
        chkEditCatRep.setOpaque(false);

        chkAjustes.setText("Efectuar Ajustes");
        chkAjustes.setName("13"); // NOI18N
        chkAjustes.setOpaque(false);

        chkCargos.setText("Efectuar Cargos");
        chkCargos.setName("14"); // NOI18N
        chkCargos.setOpaque(false);

        chkDescargos.setText("Efectuar Descargos");
        chkDescargos.setName("15"); // NOI18N
        chkDescargos.setOpaque(false);

        chkTraslados.setText("Efectuar Traslados");
        chkTraslados.setName("16"); // NOI18N
        chkTraslados.setOpaque(false);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Asigar Depósitos"));
        jPanel5.setEnabled(false);
        jPanel5.setOpaque(false);

        jScrollPane2.setViewportView(lstDepositos);

        jScrollPane3.setViewportView(lstAsignados);

        btnAdd.setBackground(new java.awt.Color(204, 255, 204));
        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setText(">");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnQuit.setBackground(new java.awt.Color(255, 204, 204));
        btnQuit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnQuit.setText("<");
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(btnQuit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnQuit)
                .addGap(26, 26, 26))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
            .addComponent(jScrollPane3)
        );

        chkReimprimir.setText("Reimprimir Operaciones");
        chkReimprimir.setName("12"); // NOI18N
        chkReimprimir.setOpaque(false);

        chkEditDepo.setText("Escribir Depositos");
        chkEditDepo.setName("11"); // NOI18N
        chkEditDepo.setOpaque(false);

        chkDepositos.setText("Depositos");
        chkDepositos.setName("10"); // NOI18N
        chkDepositos.setOpaque(false);

        javax.swing.GroupLayout pnlInventarioLayout = new javax.swing.GroupLayout(pnlInventario);
        pnlInventario.setLayout(pnlInventarioLayout);
        pnlInventarioLayout.setHorizontalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlInventarioLayout.createSequentialGroup()
                        .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkEditCatRep)
                            .addComponent(chkRepuestos)
                            .addGroup(pnlInventarioLayout.createSequentialGroup()
                                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkEditRep)
                                    .addComponent(chkCategRep))
                                .addGap(18, 18, 18)
                                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkEditDepo)
                                    .addComponent(chkReimprimir)
                                    .addComponent(chkDepositos))))
                        .addGap(2, 2, 2)
                        .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkTraslados)
                            .addComponent(chkCargos)
                            .addComponent(chkDescargos)
                            .addComponent(chkAjustes))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlInventarioLayout.setVerticalGroup(
            pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInventarioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCategRep)
                    .addComponent(chkAjustes)
                    .addComponent(chkDepositos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkEditCatRep)
                    .addComponent(chkCargos)
                    .addComponent(chkEditDepo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkRepuestos)
                    .addComponent(chkDescargos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkReimprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlInventarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkEditRep)
                        .addComponent(chkTraslados)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelAccesos.addTab("Inventario", pnlInventario);

        pnlReportes.setOpaque(false);

        chkRptClientes.setText("Clientes Registrados");
        chkRptClientes.setName("17"); // NOI18N
        chkRptClientes.setOpaque(false);

        chkCasosMarcas.setText("Casos/Marcas");
        chkCasosMarcas.setName("18"); // NOI18N
        chkCasosMarcas.setOpaque(false);

        chkCasosEquipos.setText("Casos/Equipos");
        chkCasosEquipos.setName("19"); // NOI18N
        chkCasosEquipos.setOpaque(false);

        chkCasosClientes.setText("Casos/Clientes");
        chkCasosClientes.setName("20"); // NOI18N
        chkCasosClientes.setOpaque(false);

        chkCasosDiagnosticos.setText("Casos/Diagnosticos");
        chkCasosDiagnosticos.setName("21"); // NOI18N
        chkCasosDiagnosticos.setOpaque(false);

        chkLimitarDepo.setText("Limitar a Depositos");
        chkLimitarDepo.setName("22"); // NOI18N
        chkLimitarDepo.setOpaque(false);

        chkInventario.setText("Inventario");
        chkInventario.setName("23"); // NOI18N
        chkInventario.setOpaque(false);

        chkMovimientos.setText("Movimientos");
        chkMovimientos.setName("24"); // NOI18N
        chkMovimientos.setOpaque(false);

        javax.swing.GroupLayout pnlReportesLayout = new javax.swing.GroupLayout(pnlReportes);
        pnlReportes.setLayout(pnlReportesLayout);
        pnlReportesLayout.setHorizontalGroup(
            pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkRptClientes)
                    .addComponent(chkCasosMarcas)
                    .addComponent(chkCasosEquipos)
                    .addComponent(chkCasosClientes)
                    .addComponent(chkCasosDiagnosticos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkLimitarDepo)
                    .addComponent(chkInventario)
                    .addComponent(chkMovimientos))
                .addGap(73, 73, 73))
        );
        pnlReportesLayout.setVerticalGroup(
            pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkRptClientes)
                    .addComponent(chkLimitarDepo))
                .addGap(18, 18, 18)
                .addComponent(chkCasosMarcas)
                .addGap(18, 18, 18)
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCasosEquipos)
                    .addComponent(chkInventario))
                .addGap(18, 18, 18)
                .addGroup(pnlReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCasosClientes)
                    .addComponent(chkMovimientos))
                .addGap(18, 18, 18)
                .addComponent(chkCasosDiagnosticos)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        panelAccesos.addTab("Reportes", pnlReportes);

        pnlUsuarios.setOpaque(false);

        chkEditUsuario.setText("Escribir Usuarios");
        chkEditUsuario.setToolTipText("");
        chkEditUsuario.setName("28"); // NOI18N
        chkEditUsuario.setOpaque(false);

        chkCatUsu.setText("Categorias de Usuarios");
        chkCatUsu.setToolTipText("");
        chkCatUsu.setActionCommand("");
        chkCatUsu.setName("25"); // NOI18N
        chkCatUsu.setOpaque(false);

        chkUsuarios.setText("Usuarios");
        chkUsuarios.setName("27"); // NOI18N
        chkUsuarios.setOpaque(false);

        chkUsuarioPropio.setText("Datos Propios");
        chkUsuarioPropio.setName("29"); // NOI18N
        chkUsuarioPropio.setOpaque(false);

        chkEditCatUsu.setText("Escribir Categorias");
        chkEditCatUsu.setActionCommand("");
        chkEditCatUsu.setName("26"); // NOI18N
        chkEditCatUsu.setOpaque(false);

        javax.swing.GroupLayout pnlUsuariosLayout = new javax.swing.GroupLayout(pnlUsuarios);
        pnlUsuarios.setLayout(pnlUsuariosLayout);
        pnlUsuariosLayout.setHorizontalGroup(
            pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUsuariosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkCatUsu)
                    .addComponent(chkEditCatUsu))
                .addGap(79, 79, 79)
                .addGroup(pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkUsuarios)
                    .addComponent(chkUsuarioPropio)
                    .addComponent(chkEditUsuario))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        pnlUsuariosLayout.setVerticalGroup(
            pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUsuariosLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkCatUsu)
                    .addComponent(chkUsuarios))
                .addGap(18, 18, 18)
                .addGroup(pnlUsuariosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkEditUsuario)
                    .addComponent(chkEditCatUsu))
                .addGap(18, 18, 18)
                .addComponent(chkUsuarioPropio)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        panelAccesos.addTab("Usuarios", pnlUsuarios);

        javax.swing.GroupLayout pnlAccesosLayout = new javax.swing.GroupLayout(pnlAccesos);
        pnlAccesos.setLayout(pnlAccesosLayout);
        pnlAccesosLayout.setHorizontalGroup(
            pnlAccesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAccesos, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pnlAccesosLayout.setVerticalGroup(
            pnlAccesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAccesos, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        btnSalir.setBackground(new java.awt.Color(255, 204, 204));
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/btnCerrar.png"))); // NOI18N
        btnSalir.setText("Atras");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        bthGuardar.setBackground(new java.awt.Color(204, 204, 255));
        bthGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/Ok.png"))); // NOI18N
        bthGuardar.setText("Guardar");
        bthGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bthGuardarActionPerformed(evt);
            }
        });

        btnCrear.setBackground(new java.awt.Color(204, 255, 204));
        btnCrear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/servidigital/imagenes/ico_add.png"))); // NOI18N
        btnCrear.setText("Nuevo");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bthGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCrear)
                        .addGap(6, 6, 6))
                    .addComponent(pnlAccesos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAccesos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bthGuardar)
                        .addComponent(btnCrear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSalir))
                .addContainerGap())
            .addComponent(pnlCategorias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
    private void bthGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bthGuardarActionPerformed
        try {
            Main.BD.Con().setAutoCommit(false);
            if(lstCategorias.isEnabled()) {
                String ID = new Categusu(txtNombre.getText(), txtDescrip.getText()).Registrar();
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkClientes.getName()+","+Boolean.compare(chkClientes.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkTiendas.getName()+","+numero(chkTiendas.isSelected())+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkSoportistas.getName()+","+Boolean.compare(chkSoportistas.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkNuevoCaso.getName()+","+Boolean.compare(chkNuevoCaso.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkAdmCasos.getName()+","+Boolean.compare(chkAdmCasos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCategRep.getName()+","+Boolean.compare(chkCategRep.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkEditCatRep.getName()+","+Boolean.compare(chkEditCatRep.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkRepuestos.getName()+","+Boolean.compare(chkRepuestos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkEditRep.getName()+","+Boolean.compare(chkEditRep.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkDepositos.getName()+","+Boolean.compare(chkDepositos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkEditDepo.getName()+","+Boolean.compare(chkEditDepo.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkAjustes.getName()+","+Boolean.compare(chkAjustes.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCargos.getName()+","+Boolean.compare(chkCargos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkDescargos.getName()+","+Boolean.compare(chkDescargos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkTraslados.getName()+","+Boolean.compare(chkTraslados.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkReimprimir.getName()+","+Boolean.compare(chkReimprimir.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkRptClientes.getName()+","+Boolean.compare(chkRptClientes.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCasosMarcas.getName()+","+Boolean.compare(chkCasosMarcas.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCasosEquipos.getName()+","+Boolean.compare(chkCasosEquipos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCasosClientes.getName()+","+Boolean.compare(chkCasosClientes.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCasosDiagnosticos.getName()+","+Boolean.compare(chkCasosDiagnosticos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkLimitarDepo.getName()+","+Boolean.compare(chkLimitarDepo.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkInventario.getName()+","+Boolean.compare(chkInventario.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkMovimientos.getName()+","+Boolean.compare(chkMovimientos.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkCatUsu.getName()+","+Boolean.compare(chkCatUsu.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkEditCatUsu.getName()+","+Boolean.compare(chkEditCatUsu.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkUsuarios.getName()+","+Boolean.compare(chkUsuarios.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkEditUsuario.getName()+","+Boolean.compare(chkEditUsuario.isSelected(), false)+")");
                Main.BD.Ejecutar("insert into categacc (categusu, opcion, valor) values ("+ID+","+chkUsuarioPropio.getName()+","+Boolean.compare(chkUsuarioPropio.isSelected(), false)+")");
                for(int i = 0; i < lstAsignados.getModel().getSize(); i++) {
                    Main.BD.Ejecutar("insert into categacc (categoria, deposito) values ("+ID+","+lstAsignados.getModel().getElementAt(i).toString()+")");
                }
                Main.BD.Con().commit();
                Main.BD.Con().setAutoCommit(true);
            }
            else {
                cat.setNombre(txtNombre.getText());
                cat.setDescrip(txtDescrip.getText());
                cat.Actualizar();
                Main.BD.Ejecutar("update categacc set valor = "+numero(chkClientes.isSelected())+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkClientes.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkTiendas.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkTiendas.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkSoportistas.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkSoportistas.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkNuevoCaso.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkNuevoCaso.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkAdmCasos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkAdmCasos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCategRep.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCategRep.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkEditCatRep.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkEditCatRep.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkRepuestos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkRepuestos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkEditRep.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkEditRep.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkDepositos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkDepositos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkEditDepo.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkEditDepo.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkAjustes.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkAjustes.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCargos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCargos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkDescargos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkDescargos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkTraslados.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkTraslados.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkReimprimir.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkReimprimir.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkRptClientes.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkRptClientes.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCasosMarcas.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCasosMarcas.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCasosEquipos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCasosEquipos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCasosClientes.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCasosClientes.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCasosDiagnosticos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCasosDiagnosticos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkLimitarDepo.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkLimitarDepo.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkInventario.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkInventario.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkMovimientos.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkMovimientos.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkCatUsu.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkCatUsu.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkEditCatUsu.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkEditCatUsu.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkUsuarios.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkUsuarios.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkEditUsuario.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkEditUsuario.getName());
                Main.BD.Ejecutar("update categacc set valor = "+Boolean.compare(chkUsuarioPropio.isSelected(), false)+" where categusu = "+cat.getId_categoria()+" and opcion = "+chkUsuarioPropio.getName());
                Main.BD.Consultar("select depositos.id_deposito, depositos.codigo from depositos inner join depocat on depositos.id_deposito = depocat.deposito where depocat.categoria = "+cat.getId_categoria());
                DefaultListModel L = (DefaultListModel)lstAsignados.getModel();
                while(Main.BD.Siguiente()) {
                    String C = Main.BD.Campo("codigo");
                    boolean borrar = true;
                    for(int i = 0; i < L.getSize(); i++) {
                        if(C.equalsIgnoreCase(L.getElementAt(i).toString())) {
                            L.removeElementAt(i);
                            borrar = false;
                        }
                    }
                    if(borrar) {
                        Main.BD.Ejecutar("delete from depocat where deposito = "+Main.BD.Campo("id_deposito")+" and categoria = "+cat.getId_categoria());
                    }
                }
                L = (DefaultListModel)lstAsignados.getModel();
                for(int i = 0; i < L.getSize(); i++) {
                    int depo = new Deposito(L.getElementAt(i).toString()).getId();
                    Main.BD.Ejecutar("insert into depocat (categoria, deposito) values ("+cat.getId_categoria()+","+depo+")");
                }
                Main.BD.Con().commit();
                Main.BD.Con().setAutoCommit(true);
            }
        }
        catch(SQLException e) {
            try {
                JOptionPane.showMessageDialog(this, Main.BD.showException(e.getMessage()), "UsuCat - Guardar()", JOptionPane.ERROR_MESSAGE);
                Main.BD.Con().rollback();
                Main.BD.Con().setAutoCommit(true);
            }
            catch(SQLException ex) {
                JOptionPane.showMessageDialog(this, Main.BD.showException(ex.getMessage()), "UsuCat - Guardar2()", JOptionPane.ERROR_MESSAGE);
            }
        }
        desactivarControles();
        cargarCategoria();
        lstCategorias.setEnabled(true);
    }//GEN-LAST:event_bthGuardarActionPerformed
    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed
        limpiarControles();
        activarControles();
    }//GEN-LAST:event_btnCrearActionPerformed
    private void lstCategoriasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstCategoriasValueChanged
        desactivarControles();
    }//GEN-LAST:event_lstCategoriasValueChanged
    private void lstCategoriasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCategoriasMouseClicked
        
    }//GEN-LAST:event_lstCategoriasMouseClicked
    private void lstCategoriasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstCategoriasMousePressed
        if(lstCategorias.getSelectedIndex() >= 0) {
            cargarCategoria();
        }
        if(evt.getClickCount() > 1) {
            lstCategorias.setEnabled(false);
            activarControles();
        }
    }//GEN-LAST:event_lstCategoriasMousePressed
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        int i = lstDepositos.getSelectedIndex();
        if(i >= 0) {
            DefaultListModel L = (DefaultListModel)lstAsignados.getModel();
            DefaultListModel D = (DefaultListModel)lstDepositos.getModel();
            L.addElement(D.remove(i));
        }
        else {
            JOptionPane.showMessageDialog(this, "Seleccione un deposito de la izquierda", "UsuCat - btnAdd()", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAddActionPerformed
    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        int i = lstAsignados.getSelectedIndex();
        if(i >= 0) {
            DefaultListModel L = (DefaultListModel)lstAsignados.getModel();
            DefaultListModel D = (DefaultListModel)lstDepositos.getModel();
            D.addElement(L.remove(i));
        }
        else {
            JOptionPane.showMessageDialog(this, "Seleccione un deposito de la derecha", "UsuCat - btnQuit()", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnQuitActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UsuarioCategorias dialog = new UsuarioCategorias(new java.awt.Frame(), true);
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
    private javax.swing.JButton bthGuardar;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnQuit;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox chkAdmCasos;
    private javax.swing.JCheckBox chkAjustes;
    private javax.swing.JCheckBox chkCargos;
    private javax.swing.JCheckBox chkCasosClientes;
    private javax.swing.JCheckBox chkCasosDiagnosticos;
    private javax.swing.JCheckBox chkCasosEquipos;
    private javax.swing.JCheckBox chkCasosMarcas;
    private javax.swing.JCheckBox chkCatUsu;
    private javax.swing.JCheckBox chkCategRep;
    private javax.swing.JCheckBox chkClientes;
    private javax.swing.JCheckBox chkDepositos;
    private javax.swing.JCheckBox chkDescargos;
    private javax.swing.JCheckBox chkEditCatRep;
    private javax.swing.JCheckBox chkEditCatUsu;
    private javax.swing.JCheckBox chkEditDepo;
    private javax.swing.JCheckBox chkEditRep;
    private javax.swing.JCheckBox chkEditUsuario;
    private javax.swing.JCheckBox chkInventario;
    private javax.swing.JCheckBox chkLimitarDepo;
    private javax.swing.JCheckBox chkMovimientos;
    private javax.swing.JCheckBox chkNuevoCaso;
    private javax.swing.JCheckBox chkReimprimir;
    private javax.swing.JCheckBox chkRepuestos;
    private javax.swing.JCheckBox chkRptClientes;
    private javax.swing.JCheckBox chkSoportistas;
    private javax.swing.JCheckBox chkTiendas;
    private javax.swing.JCheckBox chkTraslados;
    private javax.swing.JCheckBox chkUsuarioPropio;
    private javax.swing.JCheckBox chkUsuarios;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JList lstAsignados;
    private javax.swing.JList lstCategorias;
    private javax.swing.JList lstDepositos;
    private javax.swing.JTabbedPane panelAccesos;
    private javax.swing.JPanel pnlAccesos;
    private javax.swing.JPanel pnlCategorias;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlInventario;
    private javax.swing.JPanel pnlReportes;
    private javax.swing.JPanel pnlServidigital;
    private javax.swing.JPanel pnlUsuarios;
    private javax.swing.JTextField txtDescrip;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
