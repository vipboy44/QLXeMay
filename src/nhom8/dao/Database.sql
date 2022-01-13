use master
go

if exists(select * from sys.databases where name = 'DuAn1_Nhom8')
	drop database DuAn1_Nhom8
go

Create database DuAn1_Nhom8
go

use DuAn1_Nhom8
go

create table NhanVien(
MaNV varchar(5) primary key,
TenNV nvarchar(50) not null,
MatKhau varchar(10) not null,
ChucVu bit not null,
GioiTinh bit default 1,
DiaChi nvarchar(100) not null,
DienThoai varchar(10) not null,
NgaySinh date not null,
Hinh varchar(20)
)
go
create table HangXe(
MaHX varchar(5) primary key,
TenHX nvarchar(20) not null
)
go

create table LoaiXe(
MaLX varchar(10) primary key,
MaHX varchar(5) references HangXe(MaHX) not null,
Tenloai nvarchar(50) not null
)
go

create table PhieuNhapHang(
MaPN varchar(8) primary key,
MaNV varchar(5) references NhanVien(MaNV) not null,
NhaCungCap nvarchar(50),
NgayNhap date not null
)
go
create table Xe(
SoKhung varchar(50) primary key,
MaPN varchar(8) references PhieuNhapHang(MaPN) not null,
MaLX varchar(10) references LoaiXe(MaLX) not null,
SoMay varchar(50) not null,
MauXe nvarchar(20) not null,
PhanKhoi varchar(10) not null,
GiaNhap float not null,
GiaBan float not null,
Anh varchar(20),
TrangThai int not null  ----0 đã bán, 1 đang bán,2 chưa bán.
)
go


create table KhachHang(
MaKH varchar(8) primary key,
TenKH nvarchar(50) not null,
GioiTinh bit default 1,
DiaChi nvarchar(100),
DienThoai varchar(10) not null
)
go

create table HoaDon(
MaHD varchar(8) primary key,
MaNV varchar(5) references NhanVien(MaNV) not null,
MaKH varchar(8) references KhachHang(MaKH) not null,
NgayBan date not null
)
go

create table HoaDonChiTiet(
MaHD varchar(8) references HoaDon(MaHD) not null,
SoKhung varchar(50) references Xe(SoKhung) unique not null,
Gia float not null,
Constraint pk_HDCT primary key(MaHD,SoKhung)
)
go

--insert Nhan Vien
insert into NhanVien values('NV001',N'Huỳnh Long Minh Trí',123,1,1,N'Đồng Nai','098976589','1996/08/19','Tri.jpg')
insert into NhanVien values('NV002',N'Trịnh Trọng Thái',456,0,1,N'Gò Vấp','0358897888','1995/07/01','Thai.jpg')
insert into NhanVien values('NV003',N'Trần Quốc Vương',789,0,1,N'Tân Phú','0353060088','1996/12/07','Vuong.jpg')

--insert bang HangXe
insert into HangXe values('HD','Honda')
insert into HangXe values('YMH','Yamaha')
insert into HangXe values('SZK','Suzuki')
insert into HangXe values('PGO','Piaggio')
insert into HangXe values('SYM','SYM')
--insert LoaiXe
insert into LoaiXe values('HD001','HD','WAVE S')
insert into LoaiXe values('HD002','HD','VARIO')
insert into LoaiXe values('HD003','HD','AIR BLADE')
insert into LoaiXe values('HD004','HD','WASE RSX')
insert into LoaiXe values('SYM001','SYM','GALAXY')
insert into LoaiXe values('SZK001','SZK','AXELO')
insert into LoaiXe values('YMH001','YMH','EXICTER')
insert into LoaiXe values('YMH002','YMH','JUPITER')
insert into LoaiXe values('YMH003','YMH','SIRIUS FI')
insert into LoaiXe values('YMH004','YMH','NVX')
insert into LoaiXe values('PGO001','PGO','VESPA')




--insert Phiếu nhập hàng
insert into PhieuNhapHang values('PN001','NV002',N'Công ty Tiến Dũng','2018/03/18')
insert into PhieuNhapHang values('PN002','NV001',N'Công ty Tiến Dũng','2018/05/28')
insert into PhieuNhapHang values('PN003','NV002',N'Công ty Thành Công','2018/06/08')
insert into PhieuNhapHang values('PN004','NV002',N'Công ty Thành Công','2018/07/09')

--insert Xe
insert into Xe values('WS346785','PN001','HD001','WS153455',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',0)--0 đã bán, 1 đang bán, 2 chưa bán.
insert into Xe values('WS346786','PN001','HD001','WS153456',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',1)
insert into Xe values('WS346787','PN002','HD001','WS153457',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',1) 
insert into Xe values('WS346788','PN002','HD001','WS143458',N'Đỏ đen','110Cc',16000000,21000000,'Anh2.jpg',0)
insert into Xe values('WS346789','PN003','HD001','WS123459',N'Đỏ đen','110Cc',16000000,21000000,'Anh2.jpg',1)
insert into Xe values('VRO878122','PN003','HD002','VRO056482',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',0)
insert into Xe values('VRO878123','PN004','HD002','VRO056483',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',1)
insert into Xe values('VRO878124','PN004','HD002','VRO056484',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',1)
insert into Xe values('EX456231','PN001','YMH001','EX256541',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',0)
insert into Xe values('EX456232','PN001','YMH001','EX256542',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',0)
insert into Xe values('EX456233','PN001','YMH001','EX256543',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('EX456234','PN002','YMH001','EX256544',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('EX456235','PN002','YMH001','EX256545',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('JPT189564','PN002','YMH002','JPT789854',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',0)
insert into Xe values('JPT189565','PN003','YMH002','JPT789856',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',1)
insert into Xe values('JPT189566','PN003','YMH002','JPT789857',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',1)
insert into Xe values('JPT189567','PN003','YMH002','JPT789858',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)
insert into Xe values('JPT189568','PN004','YMH002','JPT789853',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)
insert into Xe values('JPT189569','PN004','YMH002','JPT789851',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)



--insert Khachhang
insert into KhachHang values('KH001',N'Phạm Duy Biên',1,N'Gò Vấp','077789899')
insert into KhachHang values('KH002',N'Nguyễn Đức Tùng',1,N'Gò Vấp','0348597856')
insert into KhachHang values('KH003',N'Thạch Thị Thảo',0,N'Quận 3','0357894562')
insert into KhachHang values('KH004',N'Hoằng Tế Nhị',1,N'Quận 10','0789856213')
insert into KhachHang values('KH005',N'Đỗ Thị Quỳnh Anh',0,N'Quận Phú Nhuận','0365897852')
insert into KhachHang values('KH006',N'Lê Mạnh',1,N'Quận 1','0378988981')
insert into KhachHang values('KH007',N'Lê Hùng',1,N'Quận 3','0789563254')
insert into KhachHang values('KH008',N'Nguyễn Thị Thảo',0,N'Quận Phú Nhuận','0798563248')
insert into KhachHang values('KH009',N'Đoàn Văn Công',1,N'Quận 10','0372585855')
insert into KhachHang values('KH010',N'Lê Hữ Nghĩa',1,N'Quận Tân Bình','0384568993')

--insert HoaDon 
insert into HoaDon values('HD101','NV002','KH001','2019/02/22')
insert into HoaDon values('HD102','NV002','KH002','2019/04/23')
insert into HoaDon values('HD103','NV002','KH003','2019/05/28')
insert into HoaDon values('HD104','NV002','KH004','2019/01/15')
insert into HoaDon values('HD105','NV002','KH005','2019/02/19')

--insert into HoaDonCT
insert into HoaDonChiTiet values('HD101','WS346785',20000000)
insert into HoaDonChiTiet values('HD101','WS346788',21000000)
insert into HoaDonChiTiet values('HD102','EX456231',51000000)
insert into HoaDonChiTiet values('HD103','EX456232',51000000)
insert into HoaDonChiTiet values('HD104','JPT189564',24000000)
insert into HoaDonChiTiet values('HD104','JPT189569',23000000)
insert into HoaDonChiTiet values('HD105','VRO878122',59000000)

go







-------------------------------------------CÁC THỦ TỤC---------------------------------------------------
-----------Thống kê doanh thu theo năm.
IF OBJECT_ID ('sp_doanhthunam') is not null
DROP PROC sp_doanhthunam
GO
CREATE PROC sp_doanhthunam
@year varchar(4)
AS
BEGIN
	SELECT 
	    LX.TenLoai,
		LX.MaLX,
		COUNT(HDCT.SoKhung) AS SoLuong,
		Sum(HDCT.Gia) AS DoanhThu,
		SUM(HDCT.Gia - X.GiaNhap) AS LoiNhuan
		
	FROM HoaDonChiTiet HDCT join Xe X ON HDCT.SoKhung = X.SoKhung
							join LoaiXe LX ON X.MaLX = LX.MaLX
							join HoaDon HD ON HDCT.MaHD = HD.MaHD

	WHERE YEAR(HD.NgayBan) = @year
	GROUP BY LX.MaLX, LX.TenLoai			
END
GO
---Exec sp_doanhthunam @year=2019

---------------Thống kê doanh thu theo tháng.
IF OBJECT_ID ('sp_doanhthuthang') is not null
DROP PROC sp_doanhthuthang
GO
CREATE PROC sp_doanhthuthang
@year varchar(4),
@month varchar(2)
AS
BEGIN
    SELECT
	    LX.TenLoai,
		LX.MaLX,
		COUNT(HDCT.SoKhung) AS SoLuong,
		Sum(HDCT.Gia) AS DoanhThu,
		SUM(HDCT.Gia - X.GiaNhap) AS LoiNhuan
		
	FROM HoaDonChiTiet HDCT join Xe X ON HDCT.SoKhung = X.SoKhung
							join LoaiXe LX ON X.MaLX = LX.MaLX
							join HoaDon HD ON HDCT.MaHD = HD.MaHD
	WHERE YEAR(HD.NgayBan) = @year and MONTH(HD.NgayBan) = @month
	GROUP BY LX.MaLX, LX.TenLoai			
END
GO
---Exec sp_doanhthuthang @year=2019, @month =3

------------------Thống kê xe theo trạng thái.------
IF OBJECT_ID ('sp_xe') is not null
DROP PROC sp_xe
GO
CREATE PROC sp_xe
@TrangThai varchar(10)
AS
IF @TrangThai is null
	BEGIN
		SELECT *FROM Xe	
	END
ELSE
	BEGIN
		SELECT * FROM Xe WHERE TrangThai =@TrangThai
	END
GO

---Exec sp_xe @TrangThai=2


-----------------Xóa Hóa đơn(HoaDon, HoaDonChiTiet, Cập nhật trạng thái Xe)
IF OBJECT_ID ('sp_xoadonhang') is not null
DROP PROC sp_xoadonhang
GO
CREATE PROC sp_xoadonhang
@MaHD varchar(8)
AS
BEGIN
    DELETE FROM HoaDonChiTiet WHERE MaHD = @MaHD
	DELETE FROM HoaDon WHERE MaHD = @MaHD
END
GO

---EXEC sp_xoadonhang @MaHD='HD108' 

--------------------Thủ tục update Xe---------------
IF OBJECT_ID ('sp_updateXe') is not null
DROP PROC sp_updateXe
GO
CREATE PROC  sp_updateXe
@SoKhung varchar(50),
@MaPN varchar(8),
@MaLX varchar(10),
@SoMay varchar(50),
@MauXe nvarchar(20),
@PhanKhoi varchar(10),
@GiaNhap float,
@GiaBan float,
@Anh varchar(20),
@TrangThai int
AS
IF EXISTS (Select * From HoaDonChiTiet Where SoKhung = @SoKhung)
BEGIN
    UPDATE XE SET MaPN = @MaPN, MaLX = @MaLX, SoMay =@SoMay, MauXe = @MauXe
					,PhanKhoi = @PhanKhoi, GiaNhap = @GiaNhap, GiaBan = @GiaBan, Anh = @Anh
			WHERE SoKhung = @SoKhung
END
ELSE
BEGIN
   UPDATE XE SET MaPN = @MaPN, MaLX = @MaLX, SoMay =@SoMay, MauXe = @MauXe
					,PhanKhoi = @PhanKhoi, GiaNhap = @GiaNhap, GiaBan = @GiaBan, Anh = @Anh,
					TrangThai = @TrangThai
			WHERE SoKhung = @SoKhung
END
GO

----exec sp_updateXe 'WS346785','PN002','HD001','WS1',N'Đỏ','Cc',15000000,20000000,'.jpg',1

-----exec sp_updateXe 'WS346786','PN001','HD001','WS153456',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',0


--select * from HoaDonChiTiet
--select * from XE WHERE SOKHUNG = 'WS346786'
--select * from KhachHang
--select * from HoaDon
---select * from nhanvien

--Insert into Xe(SoKhung, MaPN , MaLX, SoMay, MauXe, PhanKhoi, GiaNhap,GiaBan, Anh, TrangThai)
                 --values('123454','PN001','HD001','321',N'Đỏ','120CC',10000,0,'Anh1.jpg',0)

use master
go

if exists(select * from sys.databases where name = 'DuAn1_Nhom8')
	drop database DuAn1_Nhom8
go

Create database DuAn1_Nhom8
go

use DuAn1_Nhom8
go

create table NhanVien(
MaNV varchar(5) primary key,
TenNV nvarchar(50) not null,
MatKhau varchar(10) not null,
ChucVu bit not null,
GioiTinh bit default 1,
DiaChi nvarchar(100) not null,
DienThoai varchar(10) not null,
NgaySinh date not null,
Hinh varchar(20)
)
go
create table HangXe(
MaHX varchar(5) primary key,
TenHX nvarchar(20) not null
)
go

create table LoaiXe(
MaLX varchar(10) primary key,
MaHX varchar(5) references HangXe(MaHX) not null,
Tenloai nvarchar(50) not null
)
go

create table PhieuNhapHang(
MaPN varchar(8) primary key,
MaNV varchar(5) references NhanVien(MaNV) not null,
NhaCungCap nvarchar(50),
NgayNhap date not null
)
go
create table Xe(
SoKhung varchar(50) primary key,
MaPN varchar(8) references PhieuNhapHang(MaPN) not null,
MaLX varchar(10) references LoaiXe(MaLX) not null,
SoMay varchar(50) not null,
MauXe nvarchar(20) not null,
PhanKhoi varchar(10) not null,
GiaNhap float not null,
GiaBan float not null,
Anh varchar(20),
TrangThai int not null  ----0 đã bán, 1 đang bán,2 chưa bán.
)
go


create table KhachHang(
MaKH varchar(8) primary key,
TenKH nvarchar(50) not null,
GioiTinh bit default 1,
DiaChi nvarchar(100),
DienThoai varchar(10) not null
)
go

create table HoaDon(
MaHD varchar(8) primary key,
MaNV varchar(5) references NhanVien(MaNV) not null,
MaKH varchar(8) references KhachHang(MaKH) not null,
NgayBan date not null
)
go

create table HoaDonChiTiet(
MaHD varchar(8) references HoaDon(MaHD) not null,
SoKhung varchar(50) references Xe(SoKhung) unique not null,
Gia float not null,
Constraint pk_HDCT primary key(MaHD,SoKhung)
)
go

--insert Nhan Vien
insert into NhanVien values('NV001',N'Huỳnh Long Minh Trí',123,1,1,N'Đồng Nai','098976589','1996/08/19','Tri.jpg')
insert into NhanVien values('NV002',N'Trịnh Trọng Thái',456,0,1,N'Gò Vấp','0358897888','1995/07/01','Thai.jpg')
insert into NhanVien values('NV003',N'Trần Quốc Vương',789,0,1,N'Tân Phú','0353060088','1996/12/07','Vuong.jpg')

--insert bang HangXe
insert into HangXe values('HD','Honda')
insert into HangXe values('YMH','Yamaha')
insert into HangXe values('SZK','Suzuki')
insert into HangXe values('PGO','Piaggio')
insert into HangXe values('SYM','SYM')
--insert LoaiXe
insert into LoaiXe values('HD001','HD','WAVE S')
insert into LoaiXe values('HD002','HD','VARIO')
insert into LoaiXe values('HD003','HD','AIR BLADE')
insert into LoaiXe values('HD004','HD','WASE RSX')
insert into LoaiXe values('SYM001','SYM','GALAXY')
insert into LoaiXe values('SZK001','SZK','AXELO')
insert into LoaiXe values('YMH001','YMH','EXICTER')
insert into LoaiXe values('YMH002','YMH','JUPITER')
insert into LoaiXe values('YMH003','YMH','SIRIUS FI')
insert into LoaiXe values('YMH004','YMH','NVX')
insert into LoaiXe values('PGO001','PGO','VESPA')




--insert Phiếu nhập hàng
insert into PhieuNhapHang values('PN001','NV002',N'Công ty Tiến Dũng','2018/03/18')
insert into PhieuNhapHang values('PN002','NV001',N'Công ty Tiến Dũng','2018/05/28')
insert into PhieuNhapHang values('PN003','NV002',N'Công ty Thành Công','2018/06/08')
insert into PhieuNhapHang values('PN004','NV002',N'Công ty Thành Công','2018/07/09')

--insert Xe
insert into Xe values('WS346785','PN001','HD001','WS153455',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',0)--0 đã bán, 1 đang bán, 2 chưa bán.
insert into Xe values('WS346786','PN001','HD001','WS153456',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',1)
insert into Xe values('WS346787','PN002','HD001','WS153457',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',1) 
insert into Xe values('WS346788','PN002','HD001','WS143458',N'Đỏ đen','110Cc',16000000,21000000,'Anh2.jpg',0)
insert into Xe values('WS346789','PN003','HD001','WS123459',N'Đỏ đen','110Cc',16000000,21000000,'Anh2.jpg',1)
insert into Xe values('VRO878122','PN003','HD002','VRO056482',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',0)
insert into Xe values('VRO878123','PN004','HD002','VRO056483',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',1)
insert into Xe values('VRO878124','PN004','HD002','VRO056484',N'Bạc','150cc',50000000,59000000,'Anh3.jpg',1)
insert into Xe values('EX456231','PN001','YMH001','EX256541',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',0)
insert into Xe values('EX456232','PN001','YMH001','EX256542',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',0)
insert into Xe values('EX456233','PN001','YMH001','EX256543',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('EX456234','PN002','YMH001','EX256544',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('EX456235','PN002','YMH001','EX256545',N'Đen Mờ','150cc',48000000,51000000,'Anh4.jpg',1)
insert into Xe values('JPT189564','PN002','YMH002','JPT789854',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',0)
insert into Xe values('JPT189565','PN003','YMH002','JPT789856',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',1)
insert into Xe values('JPT189566','PN003','YMH002','JPT789857',N'Đen Đỏ','110cc',20000000,24000000,'Anh5.jpg',1)
insert into Xe values('JPT189567','PN003','YMH002','JPT789858',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)
insert into Xe values('JPT189568','PN004','YMH002','JPT789853',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)
insert into Xe values('JPT189569','PN004','YMH002','JPT789851',N'Đen Vàng','110cc',20000000,0,'Anh6.jpg',2)



--insert Khachhang
insert into KhachHang values('KH001',N'Phạm Duy Biên',1,N'Gò Vấp','077789899')
insert into KhachHang values('KH002',N'Nguyễn Đức Tùng',1,N'Gò Vấp','0348597856')
insert into KhachHang values('KH003',N'Thạch Thị Thảo',0,N'Quận 3','0357894562')
insert into KhachHang values('KH004',N'Hoằng Tế Nhị',1,N'Quận 10','0789856213')
insert into KhachHang values('KH005',N'Đỗ Thị Quỳnh Anh',0,N'Quận Phú Nhuận','0365897852')
insert into KhachHang values('KH006',N'Lê Mạnh',1,N'Quận 1','0378988981')
insert into KhachHang values('KH007',N'Lê Hùng',1,N'Quận 3','0789563254')
insert into KhachHang values('KH008',N'Nguyễn Thị Thảo',0,N'Quận Phú Nhuận','0798563248')
insert into KhachHang values('KH009',N'Đoàn Văn Công',1,N'Quận 10','0372585855')
insert into KhachHang values('KH010',N'Lê Hữ Nghĩa',1,N'Quận Tân Bình','0384568993')

--insert HoaDon 
insert into HoaDon values('HD101','NV002','KH001','2019/02/22')
insert into HoaDon values('HD102','NV002','KH002','2019/04/23')
insert into HoaDon values('HD103','NV002','KH003','2019/05/28')
insert into HoaDon values('HD104','NV002','KH004','2019/01/15')
insert into HoaDon values('HD105','NV002','KH005','2019/02/19')

--insert into HoaDonCT
insert into HoaDonChiTiet values('HD101','WS346785',20000000)
insert into HoaDonChiTiet values('HD101','WS346788',21000000)
insert into HoaDonChiTiet values('HD102','EX456231',51000000)
insert into HoaDonChiTiet values('HD103','EX456232',51000000)
insert into HoaDonChiTiet values('HD104','JPT189564',24000000)
insert into HoaDonChiTiet values('HD104','JPT189569',23000000)
insert into HoaDonChiTiet values('HD105','VRO878122',59000000)

go







-------------------------------------------CÁC THỦ TỤC---------------------------------------------------
-----------Thống kê doanh thu theo năm.
IF OBJECT_ID ('sp_doanhthunam') is not null
DROP PROC sp_doanhthunam
GO
CREATE PROC sp_doanhthunam
@year varchar(4)
AS
BEGIN
	SELECT 
	    LX.TenLoai,
		LX.MaLX,
		COUNT(HDCT.SoKhung) AS SoLuong,
		Sum(HDCT.Gia) AS DoanhThu,
		SUM(HDCT.Gia - X.GiaNhap) AS LoiNhuan
		
	FROM HoaDonChiTiet HDCT join Xe X ON HDCT.SoKhung = X.SoKhung
							join LoaiXe LX ON X.MaLX = LX.MaLX
							join HoaDon HD ON HDCT.MaHD = HD.MaHD

	WHERE YEAR(HD.NgayBan) = @year
	GROUP BY LX.MaLX, LX.TenLoai			
END
GO
---Exec sp_doanhthunam @year=2019

---------------Thống kê doanh thu theo tháng.
IF OBJECT_ID ('sp_doanhthuthang') is not null
DROP PROC sp_doanhthuthang
GO
CREATE PROC sp_doanhthuthang
@year varchar(4),
@month varchar(2)
AS
BEGIN
    SELECT
	    LX.TenLoai,
		LX.MaLX,
		COUNT(HDCT.SoKhung) AS SoLuong,
		Sum(HDCT.Gia) AS DoanhThu,
		SUM(HDCT.Gia - X.GiaNhap) AS LoiNhuan
		
	FROM HoaDonChiTiet HDCT join Xe X ON HDCT.SoKhung = X.SoKhung
							join LoaiXe LX ON X.MaLX = LX.MaLX
							join HoaDon HD ON HDCT.MaHD = HD.MaHD
	WHERE YEAR(HD.NgayBan) = @year and MONTH(HD.NgayBan) = @month
	GROUP BY LX.MaLX, LX.TenLoai			
END
GO
---Exec sp_doanhthuthang @year=2019, @month =3

------------------Thống kê xe theo trạng thái.------
IF OBJECT_ID ('sp_xe') is not null
DROP PROC sp_xe
GO
CREATE PROC sp_xe
@TrangThai varchar(10)
AS
IF @TrangThai is null
	BEGIN
		SELECT *FROM Xe	
	END
ELSE
	BEGIN
		SELECT * FROM Xe WHERE TrangThai =@TrangThai
	END
GO

---Exec sp_xe @TrangThai=2


-----------------Xóa Hóa đơn(HoaDon, HoaDonChiTiet, Cập nhật trạng thái Xe)
IF OBJECT_ID ('sp_xoadonhang') is not null
DROP PROC sp_xoadonhang
GO
CREATE PROC sp_xoadonhang
@MaHD varchar(8)
AS
BEGIN
    DELETE FROM HoaDonChiTiet WHERE MaHD = @MaHD
	DELETE FROM HoaDon WHERE MaHD = @MaHD
END
GO

---EXEC sp_xoadonhang @MaHD='HD108' 

--------------------Thủ tục update Xe---------------
IF OBJECT_ID ('sp_updateXe') is not null
DROP PROC sp_updateXe
GO
CREATE PROC  sp_updateXe
@SoKhung varchar(50),
@MaPN varchar(8),
@MaLX varchar(10),
@SoMay varchar(50),
@MauXe nvarchar(20),
@PhanKhoi varchar(10),
@GiaNhap float,
@GiaBan float,
@Anh varchar(20),
@TrangThai int
AS
IF EXISTS (Select * From HoaDonChiTiet Where SoKhung = @SoKhung)
BEGIN
    UPDATE XE SET MaPN = @MaPN, MaLX = @MaLX, SoMay =@SoMay, MauXe = @MauXe
					,PhanKhoi = @PhanKhoi, GiaNhap = @GiaNhap, GiaBan = @GiaBan, Anh = @Anh
			WHERE SoKhung = @SoKhung
END
ELSE
BEGIN
   UPDATE XE SET MaPN = @MaPN, MaLX = @MaLX, SoMay =@SoMay, MauXe = @MauXe
					,PhanKhoi = @PhanKhoi, GiaNhap = @GiaNhap, GiaBan = @GiaBan, Anh = @Anh,
					TrangThai = @TrangThai
			WHERE SoKhung = @SoKhung
END
GO

----exec sp_updateXe 'WS346785','PN002','HD001','WS1',N'Đỏ','Cc',15000000,20000000,'.jpg',1

-----exec sp_updateXe 'WS346786','PN001','HD001','WS153456',N'Đỏ','110Cc',15000000,20000000,'Anh1.jpg',0


--select * from HoaDonChiTiet
--select * from XE WHERE SOKHUNG = 'WS346786'
--select * from KhachHang
--select * from HoaDon
---select * from nhanvien

--Insert into Xe(SoKhung, MaPN , MaLX, SoMay, MauXe, PhanKhoi, GiaNhap,GiaBan, Anh, TrangThai)
                 --values('123454','PN001','HD001','321',N'Đỏ','120CC',10000,0,'Anh1.jpg',0)

