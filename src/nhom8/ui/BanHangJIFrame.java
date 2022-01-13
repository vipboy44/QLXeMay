package nhom8.ui;

import java.awt.Desktop;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDesktopPane;
import javax.swing.table.DefaultTableModel;
import nhom8.dao.HoaDonChiTietDao;
import nhom8.dao.HoaDonDao;
import nhom8.dao.KhachHangDao;
import nhom8.dao.XeDao;
import nhom8.helper.DateHelper;
import nhom8.helper.DialogHelper;
import nhom8.model.HoaDon;
import nhom8.model.HoaDonChiTiet;
import nhom8.model.KhachHang;
import nhom8.model.Xe;

public class BanHangJIFrame extends javax.swing.JInternalFrame {

    KhachHangDao khachHangDao = new KhachHangDao();
    HoaDonDao hoaDonDao = new HoaDonDao();
    HoaDonChiTietDao hoaDonChiTietDao = new HoaDonChiTietDao();
    XeDao xeDao = new XeDao();
    List<HoaDonChiTiet> gioHang = new ArrayList<>();//danh sách xe đã chọn.
    JDesktopPane Desktop;
    KhachHang kh;

    public BanHangJIFrame(JDesktopPane desktopPane, KhachHang khachHang) {
        initComponents();
        date_chooser2.setDateFormatString("MM/dd/yyyy");
        this.kh = khachHang;
        this.Desktop = desktopPane;
        fillTableHoaDon();
        if (kh != null) {
            setKhachHang(kh);
        }

    }

    /* 
     TAB DANH SÁCH HÓA ĐƠN.-------------------------------------------------------------
     */
    public void fillTableHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblTableHoaDon.getModel();
        model.setRowCount(0);
        try {
            String MaHD = txtMaHDTK.getText();
            List<HoaDon> list = hoaDonDao.selectByKeyword(MaHD);
            for (HoaDon hoaDon : list) {
                Object[] row = {
                    hoaDon.getMaHD(), hoaDon.getMaNV(), hoaDon.getMaKH(), DateHelper.toString(hoaDon.getNgayBan())
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy vấn danh sách hóa đơn thất bại.");
        }
    }

    public void viewHoaDon(String maHD){
        try {
            HoaDon hd = hoaDonDao.selectHoaDonByID(maHD);
            this.setHoaDon(hd);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu.");
        }
    }
    
    public void themHoaDon(){
          tabs.setSelectedIndex(0);
          this.moi();
    }
    
    public  void updateHoaDon(){
        try {
            hoaDonDao.update(getHoaDon());
            this.fillTableHoaDon();
            DialogHelper.alert(this, "Cập nhật thành công.");
        } catch (Exception e) {
            DialogHelper.alert(this, "Cập nhật cho hóa đơn thất bại.");
        }
    }
    
    public boolean checkHoaDon(){
        if (txtMaNV2.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập mã nhân viên.");
            txtMaNV2.requestFocus();
            return false;
        }
        if (txtMaKH2.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập mã khách hàng.");
            txtMaKH2.requestFocus();
            return false;
        }
        if (date_chooser2.getDate()==null) {
            DialogHelper.alert(this, "Xin vui lòng nhập ngày tháng"
                    + "\nĐịnh dạng MM/dd/yyyy");
            date_chooser2.requestFocus();
            return false;
        }
        return true;
    }
    
    
    public HoaDon getHoaDon(){
        HoaDon hd = new HoaDon();
        hd.setMaHD(txtMaHD.getText());
        hd.setMaKH(txtMaKH2.getText());
        hd.setMaNV(txtMaNV2.getText());
        hd.setNgayBan(date_chooser2.getDate());
        return hd;     
    }
    
    public void setHoaDon(HoaDon hd){
        txtMaHD.setText(hd.getMaHD());
        txtMaNV2.setText(hd.getMaNV());
        txtMaKH2.setText(hd.getMaKH());
        date_chooser2.setDate(hd.getNgayBan());
    }
    
    
    /*
     TAB BÁN HÀNG.----------------------------------------------------------------------
     */
    public void fillToTableHDCT(List<HoaDonChiTiet> list) {
        double tongtien = 0;
        DefaultTableModel model = (DefaultTableModel) tblHoaDonCT.getModel();
        model.setRowCount(0);
        if (list != null) {
            for (HoaDonChiTiet xemay : list) {
                Object[] row = {xemay.getSoKhung(), xemay.getGia()};
                model.addRow(row);
                tongtien = tongtien + xemay.getGia();
            }
        }
        txtTongTien.setText(tongtien + "");
    }

    private void BanHang() {
        HoaDon hd = null;

        //Thêm khách hàng.
        if (kh == null) {
            try {
                kh = getKhachHang();
                khachHangDao.insert(kh);
            } catch (Exception e) {
                DialogHelper.alert(this, "Thêm khách hàng không thành công.");
                return;
            }
        }

        if (gioHang.size() > 0) {///kiểm tra có xe trong giỏ hàng chưa .
            //Thêm Hóa đơn.
            String maDH;
            try {
                hd = new HoaDon();
                maDH = newMaHD();
                hd.setMaHD(maDH);
                hd.setMaNV("NV001");////////CHƯA SET USER LOGIN.
                hd.setMaKH(kh.getMaKH());
                hd.setNgayBan(DateHelper.now());
                hoaDonDao.insert(hd);
                lblMaDH.setToolTipText(maDH);
            } catch (Exception e) {
                e.printStackTrace();
                DialogHelper.alert(this, "Thêm hóa đơn không thành công.");
                return;
            }

            //Thêm Hóa đơn chi tiết và update trạng thái cho xe.
            for (int i = 0; i < gioHang.size(); i++) {
                try {
                    HoaDonChiTiet hdct = new HoaDonChiTiet(maDH, gioHang.get(i).getSoKhung(),
                            gioHang.get(i).getGia());
                    hoaDonChiTietDao.insert(hdct);
                    xeDao.updateTrangThai(0, gioHang.get(i).getSoKhung());
                    this.setStatus(false);
                } catch (Exception e) {
                    DialogHelper.alert(this, "Thêm xe " + gioHang.get(i).getSoKhung()
                            + " vào hóa đơn thất bại!");
                }
            }
            DialogHelper.alert(this, "Khách Hàng " + kh.getTenKH() + "\n- MaHD "
                    + hd.getMaHD());
            lblMaDH.setText("Đơn hàng " + hd.getMaHD());
        } else {
            DialogHelper.alert(this, "Không có xe nào được chọn mua.");
        }

    }

    public void xoaHoaDon(String maDH) {
        if (!maDH.equals("")) {
            List<HoaDonChiTiet> list = hoaDonChiTietDao.selectByMaHD(maDH);
            try {
                hoaDonDao.delete(maDH);//gọi thủ tục xóa cả hóa đơn chi tiết.
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        String sokhung = list.get(i).getSoKhung();
                        xeDao.updateTrangThai(1, sokhung);
                    }
                }
                this.fillTableHoaDon();
                this.moi();
                this.setHoaDon(new HoaDon()); 
                DialogHelper.alert(this, "Xóa hóa đơn " + maDH + " thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa hóa đơn thất bại.");
            }
        } else {
            DialogHelper.alert(this, "Không có hóa đơn");
        }
    }

    public void update() {
//        String maHD = lblMaDH.getToolTipText();
//        try {
//            List<HoaDonChiTiet> listCur = hoaDonChiTietDao.selectByMaHD(maHD);
//            for (int i = 0; i < listCur.size(); i++) {
//                for (int j = 0; j < gioHang.size(); j++) {
//                    if (listCur.get(i).getSoKhung().equals(gioHang.get(j).getSoKhung()) != true) {
//                        //Xoa xe
//                        hoaDonChiTietDao.deleteBySoKhung(listCur.get(i).getSoKhung());
//                        xeDao.updateTrangThai(1, listCur.get(i).getSoKhung());
////                       //Them xe
////                        HoaDonChiTiet hdct = new HoaDonChiTiet(maHD, gioHang.get(j).getSoKhung(),
////                                                                     gioHang.get(j).getGiaBan());
////                        hoaDonChiTietDao.insert(hdct);
////                        xeDao.updateTrangThai(0, gioHang.get(j).getSoKhung());
//                    }
//                }
//            }
//            fillToTable(gioHang);
//            DialogHelper.alert(this, "Update thành công.");
//        } catch (Exception e) {
//            DialogHelper.alert(this, "Update thất bại.");
//        }
//
    }

    public void addGioHang() {
        Xe xe = findXe();
        if (xe != null) {
            setXe(xe);//hiên thông tin xe
            for (int i = 0; i < tblHoaDonCT.getRowCount(); i++) {//kiểm tra xe đã dc chọn xe.
                if (xe.getSoKhung().equals(String.valueOf(tblHoaDonCT.getValueAt(i, 0)))) {
                    DialogHelper.alert(this, "Xe đã được chọn.");
                    return;
                }
            }
            HoaDonChiTiet hdct = new HoaDonChiTiet("", xe.getSoKhung(), xe.getGiaBan());
            gioHang.add(hdct);///thêm xe vào List chứa danh sách các xe đã chọn.               
            DialogHelper.alert(this, "Đã thêm xe vào giỏ hàng.");
        }
    }

    public void boChon() {
        if (gioHang != null) {
            for (int i = tblHoaDonCT.getRowCount() - 1; i >= 0; i--) {
                boolean xoa = Boolean.parseBoolean(String.valueOf(tblHoaDonCT.getValueAt(i, 2)));
                if (xoa) {
                    gioHang.remove(i);
                }
            }
            this.fillToTableHDCT(gioHang);
        }

    }

    public Xe findXe() {
        Xe xemay = null;
        String SoKhung = txtSoKhung.getText().trim();
        int trangthai = -1;
        try {
            xemay = xeDao.findXeBySoKhung_1(SoKhung);
            try {
                trangthai = xemay.getTrangThai();
            } catch (Exception e) {
                DialogHelper.alert(this, "Xe không tồn tại");
                this.setXe(new Xe());
                return xemay = null;
            }
            switch (trangthai) {
                case 0:
                    DialogHelper.alert(this, "Xe đã được bán.\nXin vui lòng chọn xe khác!");
                    this.setXe(new Xe());
                    return xemay = null;
                case 1:
                    return xemay;
                case 2:
                    DialogHelper.alert(this, "Xe chưa được phép bán.\nXin vui lòng chọn xe khác!");
                    this.setXe(new Xe());
                    return xemay = null;
                default:
                    break;
            }
        } catch (Exception e) {
            this.setXe(new Xe());
            DialogHelper.alert(this, "Tìm kiếm xe không thành công.");
        }
        return xemay;
    }

    public void view_KH_HDCT(String maHD) {
        try {
            HoaDon hoaDon = hoaDonDao.selectHoaDonByID(maHD);
            lblMaDH.setText("Đơn Hàng: " + maHD);
            lblMaDH.setToolTipText(maHD);
            ///Hiển thị thông tin khách hàng
            this.setKhachHang(khachHangDao.findById(hoaDon.getMaKH()));
            this.setStatus(false);
            ///Hiển thị danh sách xe lên bảng.
            gioHang.clear();//xóa trống để add mới.
            gioHang = hoaDonChiTietDao.selectByMaHD(maHD);
            this.fillToTableHDCT(gioHang);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu.");
        }
    }

    public void viewXe(String SoKhung) {
        try {
            Xe xemay = xeDao.findXeBySoKhung_1(SoKhung);
            this.setXe(xemay);
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu.");
        }
    }

    public void moi() {
        gioHang.clear();
        this.fillToTableHDCT(null);
        this.setXe(new Xe());
        this.setKhachHang(new KhachHang());
        this.setStatus(true);
        kh = null;
        txtTongTien.setText("");
        lblMaDH.setText("Đang tạo đơn hàng.....");
        lblMaDH.setToolTipText("");
        rdoNam.setSelected(true);
    }

    private KhachHang getKhachHang() {
        KhachHang kh = new KhachHang();
        kh.setMaKH(txtMaKH.getText().trim());
        kh.setTenKH(txtHoTen.getText().trim());
        kh.setGioiTinh(rdoNam.isSelected());
        kh.setDiaChi(txtDiaChi.getText().trim());
        kh.setSoDT(txtSoDT.getText().trim());
        return kh;
    }

    public void setKhachHang(KhachHang kh) {
        txtMaKH.setText(kh.getMaKH());
        txtHoTen.setText(kh.getTenKH());
        txtSoDT.setText(kh.getSoDT());
        txtDiaChi.setText(kh.getDiaChi());
        if (kh.isGioiTinh()) {
            rdoNam.setSelected(true);
        } else {
            rdoNu.setSelected(true);
        }
    }

    public void setXe(Xe x) {
        txtSoKhung.setText(x.getSoKhung());
        txtSoMay.setText(x.getSoKhung());
        txtPhanKhoi.setText(x.getPhanKhoi());
        txtMauXe.setText(x.getMauXe());
        txtGiaBan.setText(x.getGiaBan() + "");
    }

    public void setStatus(boolean insertHD) {
        txtMaKH.setEditable(insertHD);
        btnThemDonHang.setEnabled(insertHD);
        btnXoa.setEnabled(!insertHD);
        //btnUpdate.setEnabled(!insertHD);
        btnXuatHoaDon.setEnabled(!insertHD);
        btnOK.setEnabled(insertHD);
        btnBoChon.setEnabled(insertHD);
    }
    
    public String newMaHD() {
        String maHD = "";
        try {
            maHD = hoaDonDao.maxMaHD();
        } catch (Exception e) {
            DialogHelper.alert(this, "Truy vấn MaHD thất bại");
        }
        if (!maHD.equals("")) {
            int so = Integer.parseInt(maHD.substring(2, maHD.length()));//bắt đầu ở vị trí đầu tới vị trí cuối -1
            so = so + 1;
            maHD = "HD" + so;
        }
        return maHD;
    }
    
    public boolean checkKhachHang() {
        if (txtMaKH.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập mã khách hàng.");
            txtMaKH.requestFocus();
            return false;
        } else if (!txtMaKH.getText().matches("KH\\d{3,6}")) {
            DialogHelper.alert(this, "Mã khách hàng sai định dạng."
                    + "\n KHxxxxxx");
            txtMaKH.requestFocus();
            return false;
        } else if (txtHoTen.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập tên khách hàng.");
            txtHoTen.requestFocus();
            return false;
        } else if (txtSoDT.getText().equals("")) {
            DialogHelper.alert(this, "Xin vui lòng nhập số điện thoại.");
            txtSoDT.requestFocus();
            return false;
        } else if (!txtSoDT.getText().matches("0\\d{8,9}")) {
            DialogHelper.alert(this, "Số điện thoại không đúng đinh dạng.");
            txtSoDT.requestFocus();
            return false;
        }
        return true;
    }

//    public static void main(String[] args) {
//        new BanHangJIFrame().insertKhachHang();
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        tabs = new javax.swing.JTabbedPane();
        pnlBanHang = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtMaKH = new javax.swing.JTextField();
        lblHoTen = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        lblSoDT = new javax.swing.JLabel();
        lblDiaChi = new javax.swing.JLabel();
        txtSoDT = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        lblGioTinh = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        lblMaKH = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSoKhung = new javax.swing.JLabel();
        txtSoKhung = new javax.swing.JTextField();
        lblThongTinXe = new javax.swing.JLabel();
        lblSoMay = new javax.swing.JLabel();
        txtSoMay = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        lblMauXe = new javax.swing.JLabel();
        txtMauXe = new javax.swing.JTextField();
        lblPhanKhoi = new javax.swing.JLabel();
        lblGiaBan = new javax.swing.JLabel();
        txtPhanKhoi = new javax.swing.JTextField();
        txtGiaBan = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHoaDonCT = new javax.swing.JTable();
        btnXoa = new javax.swing.JButton();
        txtTongTien = new javax.swing.JTextField();
        btnBoChon = new javax.swing.JButton();
        btnXuatHoaDon = new javax.swing.JButton();
        btnThemDonHang = new javax.swing.JButton();
        lblTongTien = new javax.swing.JLabel();
        btnMoi = new javax.swing.JButton();
        lblMaDH = new javax.swing.JLabel();
        btnUpdateHDCT = new javax.swing.JButton();
        pnlDanhSach = new javax.swing.JPanel();
        lblMaHD = new javax.swing.JLabel();
        txtMaHDTK = new javax.swing.JTextField();
        btnTimKiem1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTableHoaDon = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lblHoaDon = new javax.swing.JLabel();
        lblMaHD2 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        lblMaHD3 = new javax.swing.JLabel();
        txtMaNV2 = new javax.swing.JTextField();
        lblMaKH2 = new javax.swing.JLabel();
        txtMaKH2 = new javax.swing.JTextField();
        lblNgayBan = new javax.swing.JLabel();
        date_chooser2 = new com.toedter.calendar.JDateChooser();
        btnMoi2 = new javax.swing.JButton();
        btnCapNhat2 = new javax.swing.JButton();
        btnXoa2 = new javax.swing.JButton();
        btnThemDH = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        lblHoTen.setText("Họ & tên:");

        lblSoDT.setText("Số điện thoại:");

        lblDiaChi.setText("Địa chỉ:");

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane1.setViewportView(txtDiaChi);

        lblGioTinh.setText("Giới tính:");

        buttonGroup1.add(rdoNam);
        rdoNam.setText("Nam");

        buttonGroup1.add(rdoNu);
        rdoNu.setText("Nữ");

        lblMaKH.setText("Mã khách hàng:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 184, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHoTen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGioTinh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(rdoNam)
                        .addGap(12, 12, 12)
                        .addComponent(rdoNu))
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(lblDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSoDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(rdoNam)
                                .addComponent(rdoNu))
                            .addComponent(lblGioTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Mặt hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        lblSoKhung.setText("Số khung xe:");

        lblThongTinXe.setText("Thông tin của xe:");

        lblSoMay.setText("Số máy:");

        lblMauXe.setText("Màu xe:");

        lblPhanKhoi.setText("Phân khối:");

        lblGiaBan.setText("Giá bán:");

        btnOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Add to basket_1.png"))); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblSoKhung, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnOK, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(lblThongTinXe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMauXe, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSoMay, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                            .addComponent(txtPhanKhoi)
                            .addComponent(txtMauXe)
                            .addComponent(txtGiaBan))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSoKhung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnOK))
                .addGap(27, 27, 27)
                .addComponent(lblThongTinXe, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPhanKhoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMauXe, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMauXe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGiaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        tblHoaDonCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Số Khung", "Giá", "Bỏ chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
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
        tblHoaDonCT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonCTMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHoaDonCT);

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTien.setText("0.0");

        btnBoChon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Remove from basket.png"))); // NOI18N
        btnBoChon.setText("Bỏ chọn");
        btnBoChon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBoChonActionPerformed(evt);
            }
        });

        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Print.png"))); // NOI18N
        btnXuatHoaDon.setText("Xuất Hóa Đơn");
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        btnThemDonHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Tick.png"))); // NOI18N
        btnThemDonHang.setText("Thêm ");
        btnThemDonHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDonHangActionPerformed(evt);
            }
        });

        lblTongTien.setText("Tổng tiền:");

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        lblMaDH.setText("Đang tạo đơn hàng.....");

        btnUpdateHDCT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Edit.png"))); // NOI18N
        btnUpdateHDCT.setText("Sửa");
        btnUpdateHDCT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateHDCTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBanHangLayout = new javax.swing.GroupLayout(pnlBanHang);
        pnlBanHang.setLayout(pnlBanHangLayout);
        pnlBanHangLayout.setHorizontalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlBanHangLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addGap(8, 8, 8))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanHangLayout.createSequentialGroup()
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addGap(0, 419, Short.MAX_VALUE)
                                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnBoChon)
                                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(22, 22, 22))
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXuatHoaDon)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdateHDCT, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnThemDonHang, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(lblMaDH, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlBanHangLayout.setVerticalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanHangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(lblMaDH, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBoChon)
                        .addGap(18, 18, 18)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXuatHoaDon)
                            .addComponent(btnThemDonHang)
                            .addComponent(btnMoi)
                            .addComponent(btnXoa)
                            .addComponent(btnUpdateHDCT))
                        .addGap(37, 37, 37))))
        );

        tabs.addTab("Bán Hàng", pnlBanHang);

        lblMaHD.setText("Mã hóa đơn:");

        btnTimKiem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/tim_kiem.png"))); // NOI18N
        btnTimKiem1.setText("Tìm kiếm");
        btnTimKiem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiem1ActionPerformed(evt);
            }
        });

        tblTableHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MaHD", "MaNV", "MaKH", "NgayBan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTableHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTableHoaDonMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblTableHoaDon);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblHoaDon.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        lblHoaDon.setForeground(new java.awt.Color(0, 0, 255));
        lblHoaDon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHoaDon.setText("HÓA ĐƠN");

        lblMaHD2.setText("Mã Hóa Đơn:");

        txtMaHD.setEditable(false);

        lblMaHD3.setText("Mã Nhân Viên:");

        lblMaKH2.setText("Mã Khách Hàng:");

        lblNgayBan.setText("Ngày Bán:");

        btnMoi2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Refresh.png"))); // NOI18N
        btnMoi2.setText("Mới");
        btnMoi2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoi2ActionPerformed(evt);
            }
        });

        btnCapNhat2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Documents.png"))); // NOI18N
        btnCapNhat2.setText("Cập Nhật");
        btnCapNhat2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhat2ActionPerformed(evt);
            }
        });

        btnXoa2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Delete.png"))); // NOI18N
        btnXoa2.setText("Xóa");
        btnXoa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoa2ActionPerformed(evt);
            }
        });

        btnThemDH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nhom8/icon/Add to basket.png"))); // NOI18N
        btnThemDH.setText("Thêm Đơn Hàng");
        btnThemDH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemDHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMaHD2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMaHD3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMaNV2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblMaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtMaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblNgayBan, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(date_chooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btnMoi2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnCapNhat2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addComponent(btnXoa2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnThemDH, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(98, 98, 98))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaHD2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaHD))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaHD3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNV2))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaKH2))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNgayBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(date_chooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMoi2)
                    .addComponent(btnXoa2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCapNhat2))
                .addGap(34, 34, 34)
                .addComponent(btnThemDH)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout pnlDanhSachLayout = new javax.swing.GroupLayout(pnlDanhSach);
        pnlDanhSach.setLayout(pnlDanhSachLayout);
        pnlDanhSachLayout.setHorizontalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtMaHDTK, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(btnTimKiem1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlDanhSachLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(438, Short.MAX_VALUE)))
        );
        pnlDanhSachLayout.setVerticalGroup(
            pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachLayout.createSequentialGroup()
                .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaHDTK, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTimKiem1)))
                    .addGroup(pnlDanhSachLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(pnlDanhSachLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlDanhSachLayout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(56, Short.MAX_VALUE)))
        );

        tabs.addTab("Danh Sách Hóa Đơn", pnlDanhSach);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 1111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBoChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBoChonActionPerformed
        boChon();

    }//GEN-LAST:event_btnBoChonActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        moi();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        String maDH = lblMaDH.getToolTipText();
        if (maDH != null) {
            InJIFrame print = new InJIFrame(maDH);
            Desktop.add(print);
            print.setLocation(Desktop.getWidth() / 2 - print.getWidth() / 2,
                    Desktop.getHeight() / 2 - print.getHeight() / 2);
            print.setVisible(true);
        } else {
            DialogHelper.alert(this, "Chưa có đơn hàng nào.");
        }


    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

    private void btnThemDonHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDonHangActionPerformed
        if (checkKhachHang()) {
            BanHang();
            fillTableHoaDon();
        }
    }//GEN-LAST:event_btnThemDonHangActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        addGioHang();
        fillToTableHDCT(gioHang);
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa?")) {
            String madh = lblMaDH.getToolTipText();
            this.xoaHoaDon(madh);
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnTimKiem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiem1ActionPerformed
        fillTableHoaDon();
    }//GEN-LAST:event_btnTimKiem1ActionPerformed

    private void tblTableHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTableHoaDonMouseClicked
       int index = tblTableHoaDon.getSelectedRow();
        if (evt.getClickCount() == 1) {
            String maDH = (String) tblTableHoaDon.getValueAt(index, 0);
            this.viewHoaDon(maDH);         
        }    
        if (evt.getClickCount() == 2) {
            String maDH = (String) tblTableHoaDon.getValueAt(index, 0);
            this.view_KH_HDCT(maDH);
            tabs.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tblTableHoaDonMouseClicked

    private void tblHoaDonCTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonCTMouseClicked
        if (evt.getClickCount() == 2) {
            int index = tblHoaDonCT.rowAtPoint(evt.getPoint());
            String sokhung = (String) tblHoaDonCT.getValueAt(index, 0);
            this.viewXe(sokhung);
            txtGiaBan.setText(String.valueOf(tblHoaDonCT.getValueAt(index, 1)));
        }
    }//GEN-LAST:event_tblHoaDonCTMouseClicked

    private void btnXoa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoa2ActionPerformed
       if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa?")) {
           String mahd = txtMaHD.getText();
            this.xoaHoaDon(mahd);      
        }
    }//GEN-LAST:event_btnXoa2ActionPerformed

    private void btnThemDHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemDHActionPerformed
        this.themHoaDon();
    }//GEN-LAST:event_btnThemDHActionPerformed

    private void btnCapNhat2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhat2ActionPerformed
        if (checkHoaDon()) {
          this.updateHoaDon();  
        }               
    }//GEN-LAST:event_btnCapNhat2ActionPerformed

    private void btnMoi2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoi2ActionPerformed
        this.setHoaDon(new HoaDon());
    }//GEN-LAST:event_btnMoi2ActionPerformed

    private void btnUpdateHDCTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateHDCTActionPerformed
       
    }//GEN-LAST:event_btnUpdateHDCTActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBoChon;
    private javax.swing.JButton btnCapNhat2;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnMoi2;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnThemDH;
    private javax.swing.JButton btnThemDonHang;
    private javax.swing.JButton btnTimKiem1;
    private javax.swing.JButton btnUpdateHDCT;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoa2;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser date_chooser2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblDiaChi;
    private javax.swing.JLabel lblGiaBan;
    private javax.swing.JLabel lblGioTinh;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblHoaDon;
    private javax.swing.JLabel lblMaDH;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaHD2;
    private javax.swing.JLabel lblMaHD3;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaKH2;
    private javax.swing.JLabel lblMauXe;
    private javax.swing.JLabel lblNgayBan;
    private javax.swing.JLabel lblPhanKhoi;
    private javax.swing.JLabel lblSoDT;
    private javax.swing.JLabel lblSoKhung;
    private javax.swing.JLabel lblSoMay;
    private javax.swing.JLabel lblThongTinXe;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlDanhSach;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblHoaDonCT;
    private javax.swing.JTable tblTableHoaDon;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtGiaBan;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaHDTK;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKH2;
    private javax.swing.JTextField txtMaNV2;
    private javax.swing.JTextField txtMauXe;
    private javax.swing.JTextField txtPhanKhoi;
    private javax.swing.JTextField txtSoDT;
    private javax.swing.JTextField txtSoKhung;
    private javax.swing.JTextField txtSoMay;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
