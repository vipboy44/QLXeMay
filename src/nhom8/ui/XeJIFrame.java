/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.ui;

import java.awt.HeadlessException;
import java.io.File;
import java.sql.ResultSet;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import nhom8.dao.XeDao;
import nhom8.helper.DateHelper;
import nhom8.helper.DialogHelper;
import nhom8.helper.JdbcHelper;
import nhom8.helper.ShareHelper;
import nhom8.model.KhachHang;
import nhom8.model.NhanVien;
import nhom8.model.Xe;

/**
 *
 * @author Asus
 */
public class XeJIFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form Xe
     */
    File file;
    XeDao dao = new XeDao();

    public XeJIFrame() {
        initComponents();
        rdoTatCa.setSelected(true);
        load();

    }

    void load() {
        DefaultTableModel model = (DefaultTableModel) tblView.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtSoKhung_TK.getText();
            List<Xe> list = dao.selectByKeyword(keyword);
            for (Xe x : list) {
                Object[] row = {
                    x.getSoKhung(),
                    x.getMaPN(),
                    x.getMaLX(),
                    x.getSoMay(),
                    x.getMauXe(),
                    x.getPhanKhoi(),
                    x.getGiaNhap(),
                    x.getGiaBan(),
                    x.getAnh(),
                    x.getTrangThai()

                };

                if (rdoTatCa.isSelected()) {
                    model.addRow(row);
                } else if (rdoDaBan.isSelected() && x.getTrangThai() <= 0) {
                    model.addRow(row);
                } else if (rdoDangBan.isSelected() && x.getTrangThai() > 0 && x.getTrangThai() < 2) {
                    model.addRow(row);
                } else if (rdoChuaBan.isSelected() && x.getTrangThai() > 1) {
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "L???i truy v???n d??? li???u!");
            e.printStackTrace();

        }
    }

    public void viewXe(String SK) {
        try {
            Xe x = dao.findXeBySoKhung_1(SK);

            this.setModel(x);

        } catch (Exception e) {
            DialogHelper.alert(this, "Truy v???n th???t b???i.");
        }

    }

    void setModel(Xe model) {
        txtSoKhung.setText(model.getSoKhung());
        txtMaPN.setText(model.getMaPN());
        txtMaLX.setText(model.getMaLX());
        txtSoMay.setText(model.getSoMay());
        txtMauXe.setText(model.getMauXe());
        txtPhanKhoi.setText(model.getPhanKhoi());
        txtGiaNhap.setText(String.valueOf(model.getGiaNhap()));
        txtGiaBan.setText(String.valueOf(model.getGiaBan()));
        if (model.getAnh() != null) {
            ShareHelper.setIcon(lblAnh, ShareHelper.readLogo(model.getAnh()));
        } else {
            lblAnh.setIcon(null);
            lblAnh.setToolTipText("");
            file = null;
        }
        int trangthai = model.getTrangThai();
        if (trangthai == 0) {
            rdoDaBanX.setSelected(true);
        } else if (trangthai == 1) {
            rdoDangBanX.setSelected(true);
        } else {
            rdoChuaBanX.setSelected(true);
        }

    }

    Xe getModel() {
        Xe model = new Xe();
        model.setSoKhung(txtSoKhung.getText());
        model.setMaPN(txtMaPN.getText());
        model.setMaLX(txtMaLX.getText());
        model.setSoMay(txtSoMay.getText());
        model.setMauXe(txtMauXe.getText());
        model.setPhanKhoi(txtPhanKhoi.getText());
        model.setGiaNhap(Double.valueOf(txtGiaNhap.getText()));
        model.setGiaBan(Double.valueOf(txtGiaBan.getText()));
        model.setAnh(lblAnh.getToolTipText());
        if (rdoDaBanX.isSelected()) {
            int trangthai = 0;
            model.setTrangThai(trangthai);
        } else if (rdoDangBanX.isSelected()) {
            int trangthai = 1;
            model.setTrangThai(trangthai);
        } else {
            int trangthai = 2;
            model.setTrangThai(trangthai);
        }

        return model;
    }

    void insert() {
        Xe model = getModel();
        try {
            dao.nhapXe(model);
            this.load();
            this.clear();
            DialogHelper.alert(this, "Th??m m???i th??nh c??ng!");
            if (file != null) {
                ShareHelper.saveLogo(file);
            }

        } catch (Exception e) {
            DialogHelper.alert(this, "Th??m m???i th???t b???i!");
            e.printStackTrace();
        }
    }

    void clear() {
        Xe model = new Xe();

        this.setModel(model);
        file = null;
        lblAnh.setToolTipText("");
    }

    void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            ShareHelper.setIcon(lblAnh, file);
        }
    }

    void delete() {
        if (DialogHelper.confirm(this, "B???n th???c s??? mu???n x??a ng?????i h???c n??y?")) {
            String sk = txtSoKhung.getText();
            try {
                dao.delete(sk);
                this.load();
                this.clear();
                DialogHelper.alert(this, "X??a th??nh c??ng!");
            } catch (Exception e) {
                DialogHelper.alert(this, "X??a th???t b???i!");
            }
        }
    }

    void update() {
        Xe model = getModel();
        try {
            dao.update(model);
            this.load();
            DialogHelper.alert(this, "C???p nh???t th??nh c??ng!");
            if (file != null) {
                ShareHelper.saveLogo(file);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "C???p nh???t th???t b???i!");
        }
    }
    Xe xemay = null;

    public void setStatus(boolean update) {
        txtSoKhung.setEditable(update);
        btnThem.setEnabled(!update && xemay != null);
        btnSua.setEnabled(!update && xemay == null);
        btnXoa.setEnabled(!update && xemay == null);
        btnTaoMoi.setEnabled(!update);
    }

    public void setStatus1(boolean Moi) {
        txtSoKhung.setEditable(Moi);
        btnThem.setEnabled(!Moi && xemay == null);
        btnSua.setEnabled(!Moi && xemay != null);
        btnXoa.setEnabled(!Moi && xemay != null);
        btnTaoMoi.setEnabled(!Moi);
    }

    public boolean check() {
        String SK = txtSoKhung.getText();
        String MaPN = txtMaPN.getText();
        String MaLX = txtMaLX.getText();
        String SoMay = txtSoMay.getText();
        String MauXe = txtMauXe.getText();
        String PhanKhoi = txtPhanKhoi.getText();
        String GiaNhap = txtGiaNhap.getText();
        String GiaBan = txtGiaBan.getText();
        if (SK.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p S??? Khung.");
            txtSoKhung.requestFocus();
            return false;
        } else if (MaPN.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p Phi???u Nh???p.");
            txtMaPN.requestFocus();
            return false;
        } else if (!MaPN.matches("PN\\d{3,5}")) {
            DialogHelper.alert(this, "M?? Phi???u Nh???p sai ?????nh d???ng."
                    + "\n PNxxxxx");
            txtMaPN.requestFocus();
            return false;
        } else if (MaLX.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p M?? Lo???i Xe");
            txtMaLX.requestFocus();
            return false;
        } else if (SoMay.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p S??? M??y");
            txtSoMay.requestFocus();
            return false;
        } else if (MauXe.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p M??u Xe");
            txtMauXe.requestFocus();
            return false;
        } else if (PhanKhoi.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p Ph??n Kh???i");
            txtPhanKhoi.requestFocus();
            return false;
        } else if (GiaNhap.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p Gi?? Nh???p");
            txtGiaNhap.requestFocus();
            return false;
        }

        try {
            double gianhap = Double.parseDouble(txtGiaNhap.getText());
            if (gianhap < 1) {
                DialogHelper.alert(this, "Gi?? ph???i l???n h??n 0.");
                txtGiaNhap.requestFocus();
                return false;
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Sai ?????nh d???ng s???.");
            txtGiaNhap.requestFocus();
            return false;
        }
        if (GiaBan.equals("")) {
            DialogHelper.alert(this, "B???n ch??a nh???p gi?? B??n.");
            txtGiaBan.requestFocus();
            return false;
        } else {
            try {
                double giaban = Double.parseDouble(txtGiaBan.getText());
                if (giaban < 1) {
                    DialogHelper.alert(this, "Gi?? ph???i l???n h??n 0.");
                    txtGiaBan.requestFocus();
                    return false;
                }
            } catch (Exception e) {
                DialogHelper.alert(this, "Sai ?????nh d???ng s???.");
                txtGiaBan.requestFocus();
                return false;
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblView = new javax.swing.JTable();
        rdoDaBan = new javax.swing.JRadioButton();
        rdoDangBan = new javax.swing.JRadioButton();
        rdoChuaBan = new javax.swing.JRadioButton();
        rdoTatCa = new javax.swing.JRadioButton();
        btnTaoMoi = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        lblQuanLX = new javax.swing.JLabel();
        pnl = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        lblSoKhung = new javax.swing.JLabel();
        txtSoKhung = new javax.swing.JTextField();
        lblSoMay = new javax.swing.JLabel();
        txtSoMay = new javax.swing.JTextField();
        lblMaLX = new javax.swing.JLabel();
        lblMaPhieuNhap = new javax.swing.JLabel();
        txtMaPN = new javax.swing.JTextField();
        lblMauXe = new javax.swing.JLabel();
        txtMauXe = new javax.swing.JTextField();
        lblPhanKhoi = new javax.swing.JLabel();
        txtPhanKhoi = new javax.swing.JTextField();
        lblGiaBan = new javax.swing.JLabel();
        txtGiaBan = new javax.swing.JTextField();
        lblGiaNhap = new javax.swing.JLabel();
        txtGiaNhap = new javax.swing.JTextField();
        lblTrangThai = new javax.swing.JLabel();
        rdoDaBanX = new javax.swing.JRadioButton();
        rdoDangBanX = new javax.swing.JRadioButton();
        rdoChuaBanX = new javax.swing.JRadioButton();
        txtMaLX = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtSoKhung_TK = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        tblView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "S??? Khung", "M?? Phi???u Nh???p", "M?? Lo???i Xe", "S??? M??y", "M??u Xe", "Ph??n Kh???i", "Gi?? Nh???p", "Gi?? B??n", "Anh", "Tr???ng Th??i"
            }
        ));
        tblView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblView);

        buttonGroup1.add(rdoDaBan);
        rdoDaBan.setText("???? B??n");
        rdoDaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDaBanActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoDangBan);
        rdoDangBan.setText("??ang B??n");
        rdoDangBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDangBanActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoChuaBan);
        rdoChuaBan.setText("Ch??a B??n");
        rdoChuaBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChuaBanActionPerformed(evt);
            }
        });

        buttonGroup1.add(rdoTatCa);
        rdoTatCa.setText("T???t C???");
        rdoTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTatCaActionPerformed(evt);
            }
        });

        btnTaoMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnTaoMoi.setText("T???o M???i");
        btnTaoMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMoiActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Edit.png"))); // NOI18N
        btnSua.setText("C???p Nh???t");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Tick.png"))); // NOI18N
        btnThem.setText("Th??m");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Delete.png"))); // NOI18N
        btnXoa.setText("X??a");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        lblQuanLX.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lblQuanLX.setForeground(new java.awt.Color(0, 0, 255));
        lblQuanLX.setText("QU???N L?? XE");

        pnl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblAnh.setBackground(new java.awt.Color(255, 255, 255));
        lblAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        lblSoKhung.setText("S??? Khung:");

        lblSoMay.setText("S??? M??y:");

        lblMaLX.setText("M?? Lo???i Xe");

        lblMaPhieuNhap.setText("M?? Phi???u Nh???p");

        lblMauXe.setText("M??u Xe:");

        lblPhanKhoi.setText("Ph??n Kh???i:");

        lblGiaBan.setText("Gi?? B??n:");

        lblGiaNhap.setText("Gi?? Nh???p:");

        lblTrangThai.setText("Tr???ng Th??i:");

        buttonGroup2.add(rdoDaBanX);
        rdoDaBanX.setText("???? B??n");
        rdoDaBanX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDaBanXActionPerformed(evt);
            }
        });

        buttonGroup2.add(rdoDangBanX);
        rdoDangBanX.setText("??ang B??n");

        buttonGroup2.add(rdoChuaBanX);
        rdoChuaBanX.setText("Ch??a B??n");

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSoKhung)
                            .addComponent(txtSoMay)
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSoKhung)
                                    .addComponent(lblSoMay)
                                    .addComponent(lblMaPhieuNhap))
                                .addGap(0, 136, Short.MAX_VALUE))
                            .addComponent(txtMaPN)))
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addComponent(lblPhanKhoi)
                                .addGap(18, 18, 18)
                                .addComponent(txtPhanKhoi))
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addComponent(lblMauXe)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addComponent(lblMaLX, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaLX))))
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addComponent(lblTrangThai)
                                .addGap(18, 18, 18)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(rdoDangBanX)
                                    .addComponent(rdoDaBanX)
                                    .addComponent(rdoChuaBanX))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlLayout.createSequentialGroup()
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtGiaNhap)
                                    .addComponent(txtGiaBan))))))
                .addContainerGap())
            .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLayout.createSequentialGroup()
                    .addGap(114, 114, 114)
                    .addComponent(txtMauXe, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlLayout.setVerticalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLayout.createSequentialGroup()
                        .addComponent(lblSoKhung)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSoMay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblMaPhieuNhap)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaLX)
                    .addComponent(txtMaLX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(lblMauXe)
                .addGap(18, 18, 18)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhanKhoi))
                .addGap(18, 18, 18)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGiaNhap))
                .addGap(18, 18, 18)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiaBan)
                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTrangThai)
                    .addComponent(rdoDaBanX))
                .addGap(18, 18, 18)
                .addComponent(rdoDangBanX)
                .addGap(18, 18, 18)
                .addComponent(rdoChuaBanX)
                .addContainerGap(11, Short.MAX_VALUE))
            .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlLayout.createSequentialGroup()
                    .addGap(264, 264, 264)
                    .addComponent(txtMauXe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(264, Short.MAX_VALUE)))
        );

        jLabel1.setText("T??m ki???m theo s??? khung  ");

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/tim_kiem.png"))); // NOI18N
        btnTimKiem.setText("T??m Ki???m");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(rdoTatCa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoDaBan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoDangBan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rdoChuaBan)
                                .addGap(63, 63, 63)
                                .addComponent(btnTaoMoi)
                                .addGap(28, 28, 28)
                                .addComponent(btnSua)
                                .addGap(32, 32, 32)
                                .addComponent(btnThem)
                                .addGap(32, 32, 32)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtSoKhung_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnTimKiem))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(489, 489, 489)
                .addComponent(lblQuanLX, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(lblQuanLX, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSoKhung_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTimKiem))
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoTatCa)
                            .addComponent(rdoDaBan)
                            .addComponent(rdoDangBan)
                            .addComponent(rdoChuaBan)
                            .addComponent(btnTaoMoi)
                            .addComponent(btnSua)
                            .addComponent(btnThem)
                            .addComponent(btnXoa)))
                    .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTatCaActionPerformed
        // TODO add your handling code here:
        load();
    }//GEN-LAST:event_rdoTatCaActionPerformed

    private void rdoDaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDaBanActionPerformed
        // TODO add your handling code here:
        load();
    }//GEN-LAST:event_rdoDaBanActionPerformed

    private void rdoDangBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDangBanActionPerformed
        // TODO add your handling code here:
        load();
    }//GEN-LAST:event_rdoDangBanActionPerformed

    private void rdoChuaBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChuaBanActionPerformed
        // TODO add your handling code here:
        load();
    }//GEN-LAST:event_rdoChuaBanActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        load();
        clear();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void rdoDaBanXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDaBanXActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDaBanXActionPerformed

    private void tblViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblViewMouseClicked
        // TODO add your handling code here:
        int index = tblView.getSelectedRow();
        String SK = (String) tblView.getValueAt(index, 0);
        this.viewXe(SK);
        this.setStatus(false);
    }//GEN-LAST:event_tblViewMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (check()) {
            insert();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        // TODO add your handling code here:
        selectImage();
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (check()) {
            update();
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnTaoMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMoiActionPerformed
        // TODO add your handling code here:
        clear();
        setStatus1(false);
    }//GEN-LAST:event_btnTaoMoiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaoMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblGiaBan;
    private javax.swing.JLabel lblGiaNhap;
    private javax.swing.JLabel lblMaLX;
    private javax.swing.JLabel lblMaPhieuNhap;
    private javax.swing.JLabel lblMauXe;
    private javax.swing.JLabel lblPhanKhoi;
    private javax.swing.JLabel lblQuanLX;
    private javax.swing.JLabel lblSoKhung;
    private javax.swing.JLabel lblSoMay;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnl;
    private javax.swing.JRadioButton rdoChuaBan;
    private javax.swing.JRadioButton rdoChuaBanX;
    private javax.swing.JRadioButton rdoDaBan;
    private javax.swing.JRadioButton rdoDaBanX;
    private javax.swing.JRadioButton rdoDangBan;
    private javax.swing.JRadioButton rdoDangBanX;
    private javax.swing.JRadioButton rdoTatCa;
    private javax.swing.JTable tblView;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMaLX;
    private javax.swing.JTextField txtMaPN;
    private javax.swing.JTextField txtMauXe;
    private javax.swing.JTextField txtPhanKhoi;
    private javax.swing.JTextField txtSoKhung;
    private javax.swing.JTextField txtSoKhung_TK;
    private javax.swing.JTextField txtSoMay;
    // End of variables declaration//GEN-END:variables
}
