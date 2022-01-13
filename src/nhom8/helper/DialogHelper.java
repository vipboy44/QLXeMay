
package nhom8.helper;

import java.awt.Component;
import javax.swing.JOptionPane;


public class DialogHelper {
   
///hiển thị thông báo cho người dùng
///parent là cửa sổ chứa thông báo.
   public static void alert(Component parent,String message){
       JOptionPane.showMessageDialog(parent,message,"Hệ thống quản lý đào tạo",
               JOptionPane.INFORMATION_MESSAGE);
   }
   
///Hiển thị thông báo yêu cầu người dùng xác nhận
///return kết kết quả nhận dc true hoặc false.
   public static boolean confirm(Component parent, String message){
       int result = JOptionPane.showConfirmDialog(parent, message,
               "Hệ thống quản lý đào tạo",JOptionPane.YES_NO_OPTION
               ,JOptionPane.QUESTION_MESSAGE);
       return  result==JOptionPane.YES_OPTION;
   }
   
///Hiển thị thông báo yêu cầu nhập dữ liệu.
///return kết quả từ người nhập vào.
   public static String prompt(Component parent, String message){
       return JOptionPane.showInputDialog(parent, message,
               "Hệ thống quản lý đào tạo",JOptionPane.INFORMATION_MESSAGE);
   }
}
