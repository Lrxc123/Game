package package1;

import javax.swing.*;

public class LoginJFrame extends JFrame {
    public LoginJFrame(){
        this.setSize(488,430);
        //设置界面的标题
        this.setTitle("拼图 登录");
        //设置页面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置游戏关闭模式 点击关闭直接退出程序
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
