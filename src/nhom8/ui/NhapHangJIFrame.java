package nhom8.ui;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;
import nhom8.dao.LoaiXeDao;
import nhom8.dao.PhieuNhapHangDao;
import nhom8.dao.XeDao;
import nhom8.helper.DateHelper;
import nhom8.helper.DialogHelper;
import nhom8.helper.ShareHelper;
import nhom8.model.LoaiXe;
import nhom8.model.PhieuNhapHang;
import nhom8.model.Xe;

public class NhapHangJIFrame extends javax.swing.JInternalFrame {

    PhieuNhapHangDao phieuNHD = new PhieuNhapHangDao();
    XeDao xeDao = new XeDao();
    LoaiXeDao loaiXeDao = new LoaiXeDao();

    public NhapHangJIFrame() {
        initComponents();
        date_chooser.setDateFormatString("MM/dd/yyyy");
        fillBangPhieuNhap();
        fillCboLoaiXe();

    }
    Xe xemay = null;
    File file = null;

    public void fillBangPhieuNhap() {
        DefaultTableModel model = (DefaultTableModel) tblBangPhieuNhap.getModel();
        model.setRowCount(0);
        try {
            String mapn = txtMaPNTK.getText();
            List<PhieuNhapHang> list = phieuNHD.selectLikeMaPN(mapn);
            for (PhieuNhapHang pNH : list) {
                Object[] row = {
                    pNH.getMaPN(), pNH.getMaNV(), pNH.getNhaCungCap(), DateHelper.toString(pNH.getNgayNhap())
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy xuất danh sách phiếu nhập hàng thất bại.");
        }
    }

    public void fillBangXe() {
        double tongtien = 0;
        DefaultTableModel model = (DefaultTableModel) tblBangXe.getModel();
        model.setRowCount(0);
        try {
            String maPN = txtMaPN.getText();
            List<Xe> list = xeDao.selectByMaPN(maPN);
            for (Xe xe : list) {
                Object[] row = {
                    xe.getMaPN(), xe.getMaLX(), xe.getSoKhung(), xe.getSoMay(),
                    xe.getMauXe(), xe.getPhanKhoi(), xe.getGiaNhap()
                };
                model.addRow(row);
                tongtien = tongtien + xe.getGiaNhap();
                txtTongTien.setText(tongtien + "");
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy xuất bảng xe thất bại.");
        }
    }

    public void fillCboLoaiXe() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLoaiXe.getModel();
        model.removeAllElements();
        try {
            List<LoaiXe> list = loaiXeDao.select();
            for (LoaiXe loaiXe : list) {
                model.addElement(loaiXe);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy vấn loại xe thất bại.");
        }
    }

    public void fillPhieuNhap() {
        try {
            String maPN = txtMaPN.getText();
            PhieuNhapHang pNH = phieuNHD.selectByMaPN(maPN);
            setPhieuNhapHang(pNH);
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy xuất dữ liệu thất bại.");
        }
    }

    public void viewXe(String soKhung) {
        try {
            xemay = xeDao.findXeBySoKhung_1(soKhung);
            this.setXe(xemay);
            this.setStatus(false);
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy vấn thất bại.");
        }

    }

    public void insertXe() {
        try {
            Xe xe = getXe();
            xeDao.nhapXe(xe);
            System.out.println(xe.getSoKhung());
            fillBangXe();
            xemay = xe;
            setStatus(false);
            if (file != null) {
                ShareHelper.saveLogo(file);///lưu vào thư mục logos
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm xe không thành công.");
        }
    }

    public void updateXe() {
        try {
            Xe xe = getXe();
            xeDao.updateXeNhap(xe);
            xemay = xe;
            fillBangXe();
            if (file != null) {
                ShareHelper.saveLogo(file);///lưu vào thư mục logos
            }
            DialogHelper.alert(this, "Cập nhật thành công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật xe thất bại.");
        }
    }

    public void deleteXe() {
        try {
            String soKhung = txtSoKhung.getText();
            xeDao.delete(soKhung);
            fillBangXe();
            this.xeMoi();
        } catch (Exception e) {
            DialogHelper.alert(this, "Xóa thất bại.");
        }
    }

    public void insertPhieuNhap() {
        try {
            PhieuNhapHang pnh = getPhieuNhapHang();
            phieuNHD.insert(pnh);
            this.fillBangPhieuNhap();
            DialogHelper.alert(this, "Thêm phiếu nhập thành công.");
            this.setStatus(false);
        } catch (Exception e) {
            DialogHelper.alert(this, "Thêm phiếu nhâp thất bại.");
        }
    }

    public void updatePhieuNhap() {
        try {
            PhieuNhapHang pnh = getPhieuNhapHang();
            phieuNHD.update(pnh);
            this.fillBangPhieuNhap();
            DialogHelper.alert(this, "Cập nhật thành công.");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật thất bại.");
        }
    }

    public void deletePhieuNhap() {
        try {
            String maPN = txtMaPN.getText();
            phieuNHD.delete(maPN);
            this.fillBangPhieuNhap();
            this.moi();
            DialogHelper.alert(this, "Xóa thành công.");
        } catch (Exception e) {
            DialogHelper.alert(this, "Xóa thất bại.");
        }
    }

    private void selectImage() {///Chọn hình
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();///lấy đường dẫn file hình.
            ShareHelper.setIcon(lblAnh, file);   //hàm có label.setToolTipText.   
        }
    }

    public void setXe(Xe xe) {
        txtSoKhung.setText(xe.getSoKhung());
        txtSoMay.setText(xe.getSoMay());
        cboLoaiXe.getModel().setSelectedItem(loaiXeDao.selectByID(xe.getMaLX()));
        txtMau.setText(xe.getMauXe());
        txtPhanKhoi.setText(xe.getPhanKhoi());
        txtGiaNhap.setText(xe.getGiaNhap() + "");
        if (xe.getAnh() != null) {   //kiểm tra nếu ảnh (if ảnh ko tồn tại lỗi hàm setimage)
            ShareHelper.setIcon(lblAnh, ShareHelper.readLogo(xe.getAnh()));
        } else {
            lblAnh.setIcon(null);
            lblAnh.setToolTipText("");
            file = null;
        }
    }

    public Xe getXe() {
        Xe xe = new Xe();
        xe.setSoKhung(txtSoKhung.getText().trim());
        xe.setSoMay(txtSoMay.getText().trim());
        xe.setMaPN(txtMaPN.getText());
        LoaiXe loaiXe = (LoaiXe) cboLoaiXe.getSelectedItem();
        xe.setMaLX(loaiXe.getMaLX());
        xe.setMauXe(txtMau.getText().trim());
        xe.setPhanKhoi(txtPhanKhoi.getText().trim());
        xe.setGiaNhap(Double.parseDouble(txtGiaNhap.getText().trim()));
        xe.setGiaBan(0);
        xe.setAnh(lblAnh.getToolTipText());
        xe.setTrangThai(0);
        return xe;
    }

    public PhieuNhapHang getPhieuNhapHang() {
        PhieuNhapHang pNH = new PhieuNhapHang(
                txtMaPN.getText(), txtMaNV.getText(),
                txtNhaCC.getText(), date_chooser.getDate());
        return pNH;
    }

    public void setPhieuNhapHang(PhieuNhapHang pnh) {
        txtMaPN.setText(pnh.getMaPN());
        txtMaNV.setText(pnh.getMaNV());
        txtNhaCC.setText(pnh.getNhaCungCap());
        date_chooser.setDate(pnh.getNgayNhap());
    }

    public void moi() {
        this.setPhieuNhapHang(new PhieuNhapHang());
        txtMaNV.setText("NV001");///////////////////////////////////////////
        this.setStatus(true);
        this.fillBangXe();
        this.xeMoi();
    }

    public void xeMoi() {
        xemay = null;
        file = null;
        lblAnh.setToolTipText("");
        setXe(new Xe());
        cboLoaiXe.setSelectedIndex(0);
    }

    public void setStatus(boolean insert) {
        txtMaPN.setEditable(insert);
        btnLuu.setEnabled(insert);
        btnCapNhat.setEnabled(!insert);
        btnHuyDonHang.setEnabled(!insert);
        txtSoKhung.setEnabled(!insert && xemay == null);
        btnThem.setEnabled(!insert && xemay == null);
        btnSua.setEnabled(!insert && xemay != null);
        btnXoa.setEnabled(!insert && xemay != null);
        btnXeMoi.setEnabled(!insert);
    }

    public boolean checkPhieuNhap() {
        String mapn = txtMaPN.getText();
        if (mapn.length() <= 0) {
            DialogHelper.alert(this, "Xin vui lòng nhập mã phiếu.");
            txtMaPN.requestFocus();
            return false;
        } 
        if (!mapn.matches("PN\\d{3,6}")) {
            DialogHelper.alert(this, "Mã Phiếu sai định dạng."
                    + "\n PNxxxxxx");
            txtMaPN.requestFocus();
            return false;
        } 
        if (txtMaNV.getText().equals("")) {
            DialogHelper.alert(this, "Mã nhân viên không được để trống.");
            txtMaNV.requestFocus();
            return false;
        } 
        if (txtNhaCC.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập nhà cung cấp.");
            txtNhaCC.requestFocus();
           return false;
        } 
        
        if (date_chooser.getDate()==null) {
            DialogHelper.alert(this, "Xin vui lòng nhập ngày tháng"
                    + "\nĐịnh dạng MM/dd/yyyy");
            date_chooser.requestFocus();
            return false;
        }
        return true;
    }

    public boolean checkXe() {
        if (txtSoKhung.getText().equals("")) {
            DialogHelper.alert(this, "Số khung không được để trống.");
            txtSoKhung.requestFocus();
            return false;
        } 
         if (txtSoMay.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập số máy.");
            txtSoMay.requestFocus();
            return false;
        } 
         if (txtMau.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập màu xe.");
            txtMau.requestFocus();
            return false;
        } 
         if (txtPhanKhoi.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập phân khối xe.");
            txtPhanKhoi.requestFocus();
            return false;
        } 
         if (txtGiaNhap.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập giá.");
            txtGiaNhap.requestFocus();
            return false;
        }
        try {
            Double gia = Double.parseDouble(txtGiaNhap.getText());
        } catch (Exception e) {
            DialogHelper.alert(this, "Sai định dạng số.");
            txtGiaNhap.requestFocus();
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

        tabs = new javax.swing.JTabbedPane();
        pnlPhieuNhapHang = new javax.swing.JPanel();
        pnlDonHang = new javax.swing.JPanel();
        lblMaPN = new javax.swing.JLabel();
        txtMaPN = new javax.swing.JTextField();
        lblNgayNhap = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtNhaCC = new javax.swing.JTextField();
        lblNhaCC = new javax.swing.JLabel();
        btnHuyDonHang = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        date_chooser = new com.toedter.calendar.JDateChooser();
        pnlSanPham = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        lblSoKhung = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtSoKhung = new javax.swing.JTextField();
        txtSoMay = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        lblSoMay = new javax.swing.JLabel();
        lblMauXe = new javax.swing.JLabel();
        txtMau = new javax.swing.JTextField();
        txtPhanKhoi = new javax.swing.JTextField();
        lblPhanKhoi = new javax.swing.JLabel();
        lblGiaNhap = new javax.swing.JLabel();
        txtGiaNhap = new javax.swing.JTextField();
        lblDonVi = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        lblLoaiXe = new javax.swing.JLabel();
        cboLoaiXe = new javax.swing.JComboBox<>();
        btnXeMoi = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblBangXe = new javax.swing.JTable();
        lblTongTien = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        pnlDanhSach = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBangPhieuNhap = new javax.swing.JTable();
        txtMaPNTK = new javax.swing.JTextField();
        lblMaPN_TK = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(1200, 650));
        setMinimumSize(new java.awt.Dimension(1200, 650));
        setPreferredSize(new java.awt.Dimension(1200, 650));

        tabs.setMaximumSize(new java.awt.Dimension(1142, 650));

        pnlPhieuNhapHang.setMaximumSize(null);
        pnlPhieuNhapHang.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlPhieuNhapHang.setPreferredSize(new java.awt.Dimension(0, 0));
        pnlPhieuNhapHang.setRequestFocusEnabled(false);

        pnlDonHang.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Đơn hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        lblMaPN.setText("Mã phiếu nhập:");

        txtMaPN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPNActionPerformed(evt);
            }
        });

        lblNgayNhap.setText("Ngày nhập:");

        lblNhanVien.setText("Nhân viên:");

        txtMaNV.setText("NV001");

        txtNhaCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNhaCCActionPerformed(evt);
            }
        });

        lblNhaCC.setText("Nhà cung cấp:");

        btnHuyDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Stop.png"))); // NOI18N
        btnHuyDonHang.setText("Hủy");
        btnHuyDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDonHangActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Save.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Documents.png"))); // NOI18N
        btnCapNhat.setText("Cập Nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDonHangLayout = new javax.swing.GroupLayout(pnlDonHang);
        pnlDonHang.setLayout(pnlDonHangLayout);
        pnlDonHangLayout.setHorizontalGroup(
            pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDonHangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDonHangLayout.createSequentialGroup()
                        .addComponent(lblMaPN, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaPN, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78)
                        .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDonHangLayout.createSequentialGroup()
                        .addComponent(lblNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDonHangLayout.createSequentialGroup()
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(lblNgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(date_chooser, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 284, Short.MAX_VALUE))
                    .addGroup(pnlDonHangLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCapNhat)
                        .addGap(18, 18, 18)
                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnHuyDonHang)))
                .addContainerGap())
        );
        pnlDonHangLayout.setVerticalGroup(
            pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDonHangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_chooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMaPN, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMaPN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNgayNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLuu)
                        .addComponent(btnHuyDonHang)
                        .addComponent(btnCapNhat)
                        .addComponent(btnMoi))
                    .addGroup(pnlDonHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pnlSanPham.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N

        lblAnh.setBackground(new java.awt.Color(255, 255, 255));
        lblAnh.setForeground(new java.awt.Color(51, 51, 51));
        lblAnh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 51)));
        lblAnh.setMaximumSize(new java.awt.Dimension(137, 162));
        lblAnh.setMinimumSize(new java.awt.Dimension(137, 162));
        lblAnh.setPreferredSize(new java.awt.Dimension(137, 162));
        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        lblSoKhung.setText("Số khung");

        lblSoMay.setText("Số máy");

        lblMauXe.setText("Màu xe:");

        lblPhanKhoi.setText("Phân khối:");

        lblGiaNhap.setText("Giá nhập:");

        lblDonVi.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        lblDonVi.setText("(vnđ)");

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Create.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

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

        lblLoaiXe.setText("Loại Xe:");

        btnXeMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnXeMoi.setText("Mới");
        btnXeMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXeMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSanPhamLayout = new javax.swing.GroupLayout(pnlSanPham);
        pnlSanPham.setLayout(pnlSanPhamLayout);
        pnlSanPhamLayout.setHorizontalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addGroup(pnlSanPhamLayout.createSequentialGroup()
                        .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSanPhamLayout.createSequentialGroup()
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXeMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThem)
                        .addGap(18, 18, 18))
                    .addGroup(pnlSanPhamLayout.createSequentialGroup()
                        .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblLoaiXe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblGiaNhap, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMauXe, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPhanKhoi, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                        .addGap(31, 31, 31)
                        .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMau, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        pnlSanPhamLayout.setVerticalGroup(
            pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSanPhamLayout.createSequentialGroup()
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSanPhamLayout.createSequentialGroup()
                        .addComponent(lblSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(txtSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLoaiXe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMauXe, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDonVi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pnlSanPhamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnXeMoi))
                .addContainerGap())
        );

        tblBangXe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Phiếu Nhập", "Mã Loại Xe", "Số Khung", "Số Máy", "Màu", "Phân Khối", "Giá Nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangXe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangXeMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblBangXe);

        lblTongTien.setText("Tổng tiền:");

        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTien.setText("0.0");

        javax.swing.GroupLayout pnlPhieuNhapHangLayout = new javax.swing.GroupLayout(pnlPhieuNhapHang);
        pnlPhieuNhapHang.setLayout(pnlPhieuNhapHangLayout);
        pnlPhieuNhapHangLayout.setHorizontalGroup(
            pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuNhapHangLayout.createSequentialGroup()
                .addGroup(pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlDonHang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlPhieuNhapHangLayout.createSequentialGroup()
                        .addComponent(pnlSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlPhieuNhapHangLayout.createSequentialGroup()
                                .addGap(512, 512, 512)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlPhieuNhapHangLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3)))))
                .addContainerGap())
        );
        pnlPhieuNhapHangLayout.setVerticalGroup(
            pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPhieuNhapHangLayout.createSequentialGroup()
                .addComponent(pnlDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPhieuNhapHangLayout.createSequentialGroup()
                        .addComponent(pnlSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnlPhieuNhapHangLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(pnlPhieuNhapHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))))
        );

        tabs.addTab("Phiếu nhập hàng", pnlPhieuNhapHang);

        pnlDanhSach.setMaximumSize(new java.awt.Dimension(1142, 650));
        pnlDanhSach.setPreferredSize(new java.awt.Dimension(0, 0));

        tblBangPhieuNhap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Phiếu Nhập", "Nhân Viên", "Nhà Cung Câp", "Ngày Nhập"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBangPhieuNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangPhieuNhapMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBangPhieuNhap);

        lblMaPN_TK.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        lblMaPN_TK.setText("Mã phiếu nhập:");

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/tim_kiem.png"))); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addComponent(lblMaPN_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaPNTK, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(btnTimKiem)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaPNTK, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaPN_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Danh Sách", pnlDanhSach);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 1191, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 547, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        fillBangPhieuNhap();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtMaPNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaPNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaPNActionPerformed

    private void txtNhaCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNhaCCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNhaCCActionPerformed

    private void tblBangPhieuNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangPhieuNhapMouseClicked
        if (evt.getClickCount() == 2) {
            int index = tblBangPhieuNhap.rowAtPoint(evt.getPoint());
            txtMaPN.setText((String) tblBangPhieuNhap.getValueAt(index, 0));
            this.fillBangXe();
            this.fillPhieuNhap();
            this.setStatus(false);
            setXe(new Xe());
            tabs.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblBangPhieuNhapMouseClicked

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        if (checkPhieuNhap()) {
            this.insertPhieuNhap();
        }
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        this.moi();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        if (checkPhieuNhap()) {
            this.updatePhieuNhap();
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnHuyDonHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyDonHangActionPerformed
        if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa phiếu nhập?")) {
            this.deletePhieuNhap();
        }
    }//GEN-LAST:event_btnHuyDonHangActionPerformed

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhMouseClicked
        selectImage();
    }//GEN-LAST:event_lblAnhMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        if (checkXe()) {
            this.insertXe();
        }

    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (checkXe()) {
            this.updateXe();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
          if (DialogHelper.confirm(this, "Xóa xe khỏi phiếu nhập này?")) {
              this.deleteXe();
        }   
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnXeMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXeMoiActionPerformed
        this.xeMoi();
        this.setStatus(false);
    }//GEN-LAST:event_btnXeMoiActionPerformed

    private void tblBangXeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangXeMouseClicked

        int index = tblBangXe.getSelectedRow();
        String sokhung = (String) tblBangXe.getValueAt(index, 2);
        this.viewXe(sokhung);

    }//GEN-LAST:event_tblBangXeMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnHuyDonHang;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXeMoi;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiXe;
    private com.toedter.calendar.JDateChooser date_chooser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblDonVi;
    private javax.swing.JLabel lblGiaNhap;
    private javax.swing.JLabel lblLoaiXe;
    private javax.swing.JLabel lblMaPN;
    private javax.swing.JLabel lblMaPN_TK;
    private javax.swing.JLabel lblMauXe;
    private javax.swing.JLabel lblNgayNhap;
    private javax.swing.JLabel lblNhaCC;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblPhanKhoi;
    private javax.swing.JLabel lblSoKhung;
    private javax.swing.JLabel lblSoMay;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JPanel pnlDonHang;
    private javax.swing.JPanel pnlPhieuNhapHang;
    private javax.swing.JPanel pnlSanPham;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblBangPhieuNhap;
    private javax.swing.JTable tblBangXe;
    private javax.swing.JTextField txtGiaNhap;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMaPN;
    private javax.swing.JTextField txtMaPNTK;
    private javax.swing.JTextField txtMau;
    private javax.swing.JTextField txtNhaCC;
    private javax.swing.JTextField txtPhanKhoi;
    private javax.swing.JTextField txtSoKhung;
    private javax.swing.JTextField txtSoMay;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
