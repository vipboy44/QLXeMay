/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nhom8.ui;

import java.awt.HeadlessException;
import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import nhom8.dao.NhanVienDao;
import nhom8.helper.DateHelper;
import nhom8.helper.DialogHelper;
import nhom8.helper.ShareHelper;
import nhom8.model.KhachHang;
import nhom8.model.NhanVien;

/**
 *
 * @author COMPUTER
 */
public class NhanVienJIFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form NhanVienJInternal
     */
    NhanVienDao dao = new NhanVienDao();

    public NhanVienJIFrame() {
        initComponents();
        load();
       
    }
     
    
    File file;
    
    void load() {
        DefaultTableModel model = (DefaultTableModel) tblView.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimkiem.getText();
            List<NhanVien> list = dao.selectByKeyword(keyword);

            for (NhanVien nv : list) {

                Object[] row = {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getMatKhau(),
                    nv.isGioiTinh() ? "Nam" : "Nữ",
                    nv.getsDT(),
                    DateHelper.toString(nv.getNgaySinh()),
                    nv.isChucVu() ? "Quản Lý" : "Nhân viên",
                    nv.getDiaChi(),
                    nv.getHinh(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
     void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            ShareHelper.setIcon(lblHinh, file);
        }
    }
     void insert() {
        NhanVien model = getModel();
        try {
            dao.insert(model);
            this.load();
            this.clear();
            DialogHelper.alert(this, "Thêm mới thành công!");
               if (file!=null) {
              ShareHelper.saveLogo(file);
            }
            
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm mới thất bại!");
        }
    }
      NhanVien getModel() {
        NhanVien model = new NhanVien();
        model.setMaNV(txtMaNV.getText());
        model.setTenNV(txtTenNV.getText());
        model.setMatKhau(txtPassword.getText());
        model.setGioiTinh(rdoNam.isSelected());
        model.setsDT(txtSoDT.getText());
        model.setNgaySinh((Date) DateHelper.toDate(txtNgaysinh.getText()));
        model.setChucVu(rdoTP.isSelected());
        model.setDiaChi(txtDiachi.getText());
        model.setHinh(lblHinh.getToolTipText());
      
        return model;
    }
      void clear() {
        NhanVien model = new NhanVien();
         
        this.setModel(model);
        file =null;
        lblHinh.setToolTipText("");
    }
      public void viewNV(String MaNV) {
        try {
            NhanVien nv = dao.findById(MaNV);
            
            this.setModel(nv);
            
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy vấn thất bại.");
        }

    }
        void setModel(NhanVien model) {
        txtMaNV.setText(model.getMaNV());
        txtTenNV.setText(model.getTenNV());
        txtPassword.setText(model.getMatKhau());
        rdoNam.setSelected(model.isGioiTinh());
        rdoNu.setSelected(!model.isGioiTinh()); 
        txtSoDT.setText(model.getsDT());
        txtNgaysinh.setText(DateHelper.toString(model.getNgaySinh()));
         rdoTP.setSelected(model.isChucVu());
        rdoNV.setSelected(!model.isChucVu()); 
        txtDiachi.setText(model.getDiaChi());
          if (model.getHinh() != null) {
            ShareHelper.setIcon(lblHinh, ShareHelper.readLogo(model.getHinh()));
        }
          else{
              lblHinh.setIcon(null);
              lblHinh.setToolTipText("");
              file= null;
          }
    }
        void update() {
        NhanVien model = getModel();
        try {
            dao.update(model);
            this.load();
            DialogHelper.alert(this, "Cập nhật thành công!");
            if (file!=null) {
              ShareHelper.saveLogo(file);
            }
            
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }
    }

    void delete() {
        if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa nhân viên này?")) {
            String manv = txtMaNV.getText();
            try {
                dao.delete(manv);
                this.load();
                this.clear();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa thất bại!");
            }
        }
    }
    NhanVien nhanvien = null;
          public void setStatus(boolean update) {
        txtMaNV.setEditable(update);
        btnThem.setEnabled(!update && nhanvien != null);
        btnSua.setEnabled(!update && nhanvien == null);
        btnXoa.setEnabled(!update && nhanvien == null);
        btnTaomoi.setEnabled(!update);
    }
      public void setStatus1(boolean Moi) {
        txtMaNV.setEditable(Moi);
        btnThem.setEnabled(!Moi && nhanvien == null);
        btnSua.setEnabled(!Moi && nhanvien != null);
        btnXoa.setEnabled(!Moi && nhanvien != null);
        btnTaomoi.setEnabled(!Moi);
    }
    int index =0;
     void edit() {
        try {
            String manv = (String) tblView.getValueAt(this.index, 0);
            NhanVien model = dao.findById(manv);
            if (model != null) {
                this.setModel(model);
                this.setStatus(false);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
     public boolean check() {
        String MaNv = txtMaNV.getText();
        String TenNV = txtTenNV.getText();
        String pass = txtPassword.getText();
        String sdt = txtSoDT.getText();
        String ngaysinh = txtNgaysinh.getText();
        String DiaChi = txtDiachi.getText();
        if (MaNv.equals("")) {
            DialogHelper.alert(this, "Bạn chưa nhập mã Nhân Viên.");
            txtMaNV.requestFocus();
            return false;
        } else if (!MaNv.matches("NV\\d{3,5}")) {
            DialogHelper.alert(this, "Mã Nhân Viên sai định định dạng."
                    + "\n NVxxxxx");
            txtMaNV.requestFocus();
            return false;
        } else if (txtTenNV.getText().trim().length() == 0) {
            DialogHelper.alert(this, "Bạn chưa nhập tên");
            txtTenNV.requestFocus();
            return false;
        } else if(pass.equals(""))
        {
            DialogHelper.alert(this, "Bạn chưa nhập Password");
            txtPassword.requestFocus();
            return false;
        }


        if (sdt.equals("")) {
            DialogHelper.alert(this, "Vui lòng nhập số điện thoại.");
            txtSoDT.requestFocus();
            return false;
        } else if (sdt.matches("0\\d{8,9}") == false) {
            DialogHelper.alert(this, "Sai định dạng số điện thoại. ");
            txtSoDT.requestFocus();
            return false;
        }else if(ngaysinh.equals(""))
        {
            DialogHelper.alert(this, "Vui lòng nhập Ngày Sinh.");
            txtNgaysinh.requestFocus();
            return false;
        }
        else if (DiaChi.equals("")) {
            DialogHelper.alert(this, "Vui lòng nhập Địa Chỉ.");
            txtDiachi.requestFocus();
            return false;
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
        lblTitle = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        pnlThonTinNhanVien = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        lblTenNV = new javax.swing.JLabel();
        lblMatKhau = new javax.swing.JLabel();
        lblGioiTinh = new javax.swing.JLabel();
        lblSoDT = new javax.swing.JLabel();
        lblNgaySinh = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtTenNV = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        txtSoDT = new javax.swing.JTextField();
        txtNgaysinh = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiachi = new javax.swing.JTextArea();
        lblChucVu = new javax.swing.JLabel();
        rdoTP = new javax.swing.JRadioButton();
        rdoNV = new javax.swing.JRadioButton();
        pnlDanhMuc = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblView = new javax.swing.JTable();
        pnlTimKiem = new javax.swing.JPanel();
        txtTimkiem = new javax.swing.JTextField();
        btnTimkiem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnTaomoi = new javax.swing.JButton();
        btnVeDau = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnVeCuoi = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        lblTitle.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        lblTitle.setText("Quản Lý Nhân Viên");

        jTextField1.setBackground(new java.awt.Color(0, 0, 0));

        jTextField2.setBackground(new java.awt.Color(0, 0, 0));

        pnlThonTinNhanVien.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)), "Thông Tin Nhân Viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51, 255))); // NOI18N

        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));
        lblHinh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHinhMouseClicked(evt);
            }
        });

        lblMaNV.setText("Mã NV:");

        lblTenNV.setText("Tên NV:");

        lblMatKhau.setText("Mật Khẩu:");

        lblGioiTinh.setText("Giới Tính:");

        lblSoDT.setText("Điện Thoại:");

        lblNgaySinh.setText("Ngày Sinh:");

        lblDiaChi.setText("Địa Chỉ:");

        rdoNam.setText("Nam");

        rdoNu.setText("Nữ");

        txtDiachi.setColumns(20);
        txtDiachi.setRows(5);
        jScrollPane1.setViewportView(txtDiachi);

        lblChucVu.setText("Chức Vụ:");

        buttonGroup1.add(rdoTP);
        rdoTP.setText("Quản Lý");

        buttonGroup1.add(rdoNV);
        rdoNV.setText("Nhân Viên");

        javax.swing.GroupLayout pnlThonTinNhanVienLayout = new javax.swing.GroupLayout(pnlThonTinNhanVien);
        pnlThonTinNhanVien.setLayout(pnlThonTinNhanVienLayout);
        pnlThonTinNhanVienLayout.setHorizontalGroup(
            pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
                    .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                        .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblTenNV)
                                .addComponent(lblMaNV))
                            .addComponent(lblMatKhau))
                        .addGap(17, 17, 17)
                        .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPassword)
                            .addComponent(txtTenNV)
                            .addComponent(txtMaNV)))
                    .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                                .addComponent(lblGioiTinh)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNam)
                                .addGap(18, 18, 18)
                                .addComponent(rdoNu)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSoDT)
                                    .addComponent(lblNgaySinh)
                                    .addComponent(lblChucVu)
                                    .addComponent(lblDiaChi))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNgaysinh)
                                    .addComponent(txtSoDT)
                                    .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                                        .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rdoNV)
                                            .addComponent(rdoTP))
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlThonTinNhanVienLayout.setVerticalGroup(
            pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThonTinNhanVienLayout.createSequentialGroup()
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenNV)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMatKhau)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoNu)
                    .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblGioiTinh)
                        .addComponent(rdoNam)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoDT)
                    .addComponent(txtSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNgaySinh)
                    .addComponent(txtNgaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlThonTinNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChucVu)
                    .addComponent(rdoTP))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoNV)
                .addGap(3, 3, 3)
                .addComponent(lblDiaChi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        pnlDanhMuc.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)), "Danh Mục", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        tblView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NV", "Tên NV", "Mật Khẩu", "Giới Tính", "Điện Thoại", "Ngày Sinh", "Chức Vụ", "Địa Chỉ", "Hình"
            }
        ));
        tblView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblViewMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblView);

        pnlTimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)), "Tìm Kiếm", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(0, 0, 255))); // NOI18N

        btnTimkiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/tim_kiem.png"))); // NOI18N
        btnTimkiem.setText("Tìm");
        btnTimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimkiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTimKiemLayout = new javax.swing.GroupLayout(pnlTimKiem);
        pnlTimKiem.setLayout(pnlTimKiemLayout);
        pnlTimKiemLayout.setHorizontalGroup(
            pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimkiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTimKiemLayout.setVerticalGroup(
            pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimkiem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDanhMucLayout = new javax.swing.GroupLayout(pnlDanhMuc);
        pnlDanhMuc.setLayout(pnlDanhMucLayout);
        pnlDanhMucLayout.setHorizontalGroup(
            pnlDanhMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhMucLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDanhMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(pnlTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDanhMucLayout.setVerticalGroup(
            pnlDanhMucLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDanhMucLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Edit.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Tick.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnTaomoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnTaomoi.setText("Tạo Mới");
        btnTaomoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaomoiActionPerformed(evt);
            }
        });

        btnVeDau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/First record.png"))); // NOI18N
        btnVeDau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVeDauActionPerformed(evt);
            }
        });

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Rewind.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Fast-forward.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnVeCuoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Last recor.png"))); // NOI18N
        btnVeCuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVeCuoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlThonTinNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlDanhMuc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(btnVeDau)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBack)
                        .addGap(18, 18, 18)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnVeCuoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTaomoi)
                        .addGap(29, 29, 29)
                        .addComponent(btnThem)
                        .addGap(21, 21, 21)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnXoa)
                        .addGap(25, 25, 25))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTitle)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlDanhMuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoa)
                            .addComponent(btnSua)
                            .addComponent(btnThem)
                            .addComponent(btnTaomoi)
                            .addComponent(btnVeDau)
                            .addComponent(btnBack)
                            .addComponent(btnNext)
                            .addComponent(btnVeCuoi)))
                    .addComponent(pnlThonTinNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if(check())
        {
        update(); }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void lblHinhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHinhMouseClicked
        // TODO add your handling code here:
        selectImage();
    }//GEN-LAST:event_lblHinhMouseClicked

    private void btnTaomoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaomoiActionPerformed
        // TODO add your handling code here:
        clear();
        setStatus1(false);
    }//GEN-LAST:event_btnTaomoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if(check())
        {
        insert(); }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        if(check()){
        delete();}
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnTimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiemActionPerformed
        // TODO add your handling code here:
        load();
        clear();
    }//GEN-LAST:event_btnTimkiemActionPerformed

    private void tblViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblViewMouseClicked
        // TODO add your handling code here:
//        index = tblView.getSelectedRow();
//       
//        TableModel model = tblView.getModel();
//        
//         NhanVien nv = new NhanVien();
//         
//         
//        txtMaNV.setText((model.getValueAt(index,0).toString()));
//        txtTenNV.setText((model.getValueAt(index,1).toString()));
//        txtPassword.setText((model.getValueAt(index,2).toString()));
//        String GioiTinh = model.getValueAt(index, 3).toString();
//        if(GioiTinh.equals("Nam"))
//        {
//            rdoNam.setSelected(true);
//        } else
//        {
//            rdoNu.setSelected(true);
//        }
//         txtSoDT.setText(model.getValueAt(index,4).toString());
//        txtNgaysinh.setText(model.getValueAt(index,5).toString());
//         String ChucVu = model.getValueAt(index, 6).toString();
//         if(ChucVu.equals("Quản Lý"))
//        {
//            rdoTP.setSelected(true);
//        } else
//        {
//            rdoNV.setSelected(true);
//        }
//                
//        txtDiachi.setText(model.getValueAt(index,7).toString());
//        lblHinh.setToolTipText(model.getValueAt(index,8).toString());
            int index = tblView.getSelectedRow();
        String MaNV = (String) tblView.getValueAt(index, 0);
        this.viewNV(MaNV);
        this.setStatus(false);
    }//GEN-LAST:event_tblViewMouseClicked

    private void btnVeDauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVeDauActionPerformed
        // TODO add your handling code here:
             this.index = 0;
        this.edit();
    }//GEN-LAST:event_btnVeDauActionPerformed

    private void btnVeCuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVeCuoiActionPerformed
        // TODO add your handling code here:
        this.index = tblView.getRowCount() - 1;
        this.edit();
    }//GEN-LAST:event_btnVeCuoiActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        this.index--;
        this.edit();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        this.index++;
        this.edit();
    }//GEN-LAST:event_btnNextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaomoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimkiem;
    private javax.swing.JButton btnVeCuoi;
    private javax.swing.JButton btnVeDau;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblNgaySinh;
    private javax.swing.JLabel lblSoDT;
    private javax.swing.JLabel lblTenNV;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlDanhMuc;
    private javax.swing.JPanel pnlThonTinNhanVien;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JRadioButton rdoNV;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JRadioButton rdoTP;
    private javax.swing.JTable tblView;
    private javax.swing.JTextArea txtDiachi;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgaysinh;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtSoDT;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTimkiem;
    // End of variables declaration//GEN-END:variables
}
