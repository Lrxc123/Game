package package1;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.*;
import java.util.Random;
import java.net.URL;
import javax.swing.ImageIcon;

public class GameJFrame extends JFrame{
    //管理数据
    int[][] date=new int[4][4];
    //记录空白方块
    int x=0,y=0;
    int[][] win={
            {1,2,3,4},
            {5,6,7,8},
            {9,10,11,12},
            {13,14,15,0}
    };
    int step=0;
    public GameJFrame(){
        //初始化界面
        createJFrame();
        //初始化菜单
        createMenu();
        //打乱图片
        chaosImage();
        //初始化图片
        createImage();
        //让界面显示
        this.setVisible(true);
    }
    //初始化菜单
    private void createMenu(){
        //1.创建整个菜单
        JMenuBar jMenuBar=new JMenuBar();
        //2.创建菜单上面的选项
        JMenu function=new JMenu("功能");
        JMenu about=new JMenu("关于我们");
        //3.创建选项下面的条目
        JMenuItem rePlay=new JMenuItem("重新游戏");
        JMenuItem reLogin=new JMenuItem("重新登录");
        JMenuItem closeGame=new JMenuItem("关闭游戏");
        JMenuItem account=new JMenuItem("公众号");
        //给条目增加事件
        rePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chaosImage();
                step=0;
                createImage();
            }
        });
        reLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new LoginJFrame();
            }
        });
        closeGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //直接关闭虚拟机
                System.exit(0);
            }
        });
        account.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹窗
                JDialog jDialog=new JDialog();
                //创建管理图片的容器
                URL url = getClass().getClassLoader().getResource("image/about.png");
                if(url!=null){
                    ImageIcon icon = new ImageIcon(url);
                    JLabel jLabel = new JLabel(icon);
                    jLabel.setBounds(0,0,258,258);
                    jDialog.add(jLabel);
                    jDialog.setSize(344,344);
                    jDialog.setAlwaysOnTop(true);
                    jDialog.setLocationRelativeTo(null);
                    //弹窗不关闭 无法进行下面操作
                    jDialog.setModal(true);
                    jDialog.setVisible(true);
                }
            }
        });
        //将每个选项下面的条目添加
        function.add(rePlay);
        function.add(reLogin);
        function.add(closeGame);
        about.add(account);
        //将选项添加到菜单
        jMenuBar.add(function);
        jMenuBar.add(about);
        this.setJMenuBar(jMenuBar);
    }
    //打乱图片
    public void chaosImage(){
        int[] tempArr={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        Random random=new Random();
        for(int i=0;i<tempArr.length;i++){
            int index=random.nextInt(tempArr.length);
            int temp=tempArr[i];
            tempArr[i]=tempArr[index];
            tempArr[index]=temp;
        }
        for(int i=0;i<tempArr.length;i++){
            if(tempArr[i]==0){
                x=i/4;
                y=i%4;
            }
            date[i/4][i%4]=tempArr[i];
        }
    }
    //初始化图片
    private void createImage(){
        //删除所有的图片
        this.getContentPane().removeAll();
        if(victory()){
            URL url = getClass().getClassLoader().getResource("image/victory.jpg");
            if(url!=null){
                ImageIcon win = new ImageIcon(url);
                JLabel winJLabel = new JLabel(win);
                winJLabel.setBounds(203,283,197,73);
                this.getContentPane().add(winJLabel);
                this.getContentPane().repaint();
            }
        }
        //计数 JLabel既可以管理图片也可以管理文字
        JLabel stepCount=new JLabel("步数："+step);
        stepCount.setBounds(50,30,100,20);
        this.getContentPane().add(stepCount);
        //路径分为绝对路径从盘符开始 相对路径从当前项目开始
        //设置游戏图片
        for(int i=0;i<16;i++){
            if(date[i/4][i%4]!=0){
                URL url = getClass().getClassLoader().getResource("image/"+date[i/4][i%4]+".png");
                if(url!=null){
                    ImageIcon icon = new ImageIcon(url);
                    JLabel jLabel = new JLabel(icon);
                    //指定图片的位置
                    jLabel.setBounds(105*(i%4)+83,105*(i/4)+134,105,105);
                    //给图片添加边框
                    //BevelBorder.RAISED：表示凸起的边框（看起来像从界面上凸起来）
                    //BevelBorder.LOWERED：表示凹陷的边框（看起来像向界面内凹陷）
                    jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    //把管理容器添加到界面中 界面有一个默认容器
                    this.getContentPane().add(jLabel);
                }
            }
        }
        //刷新一下页面
        this.getContentPane().repaint();
    }
    //初始化界面
    private void createJFrame(){
        //设置宽高
        this.setSize(603,688);
        //设置界面的标题
        this.setTitle("拼图单机版 v1.0");
        //设置页面置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置游戏关闭模式 点击关闭直接退出程序
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //取消默认居中放置
        this.setLayout(null);
        //鼠标移动
        this.addMouseListener(new MouseListener(){
            //记录鼠标按下的位置
            int startX,startY;
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                startX=e.getX();
                startY=e.getY();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(victory()){
                    return;
                }
                //记录鼠标移动到那个位置
                int endX=e.getX();
                int endY=e.getY();
                int dx=endX-startX;
                int dy=endY-startY;
                if(Math.abs(dx)>Math.abs(dy)&&Math.abs(dx)>20){
                    if(dx>0) moveLeft(); else moveRight();
                }
                else if(Math.abs(dy)>Math.abs(dx)&&Math.abs(dy)>20){
                    if(dy>0) moveUp(); else moveDown();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        //显示原图片
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                int code=e.getKeyCode();
                if(code==65){
                    //删除所有图片
                    getContentPane().removeAll();
                    //加载完整图片
                    URL url = getClass().getClassLoader().getResource("image/all.png");
                    if(url!=null){
                        ImageIcon icon = new ImageIcon(url);
                        JLabel all = new JLabel(icon);
                        all.setBounds(83,134,420,420);
                        getContentPane().add(all);
                        //刷新页面
                        getContentPane().repaint();
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                //重新加载图片
                int code=e.getKeyCode();
                if(code==65){
                    createImage();
                }
                else if(code==87){
                    date=new int[][]{
                            {1,2,3,4},
                            {5,6,7,8},
                            {9,10,11,12},
                            {13,14,15,0}
                    };
                    x=3;
                    y=3;
                    createImage();
                }
            }
        });
    }
    //向左移动
    private void moveLeft() {
        if (y>0){
            date[x][y]=date[x][y-1];
            date[x][y-1]=0;
            y--;
            step++;
            createImage();
        }
    }
    // 向右移动
    private void moveRight(){
        if (y<date[0].length-1) {
            date[x][y]=date[x][y+1];
            date[x][y+1]=0;
            y++;
            step++;
            createImage();
        }
    }
    // 向上移动
    private void moveUp() {
        if (x>0) {
            date[x][y]=date[x-1][y];
            date[x-1][y]=0;
            x--;
            step++;
            createImage();
        }
    }
    // 向下移动
    private void moveDown() {
        if (x<date.length-1) {
            date[x][y]=date[x+1][y];
            date[x+1][y]=0;
            x++;
            step++;
            createImage();
        }
    }
    //判断是否胜利
    public boolean victory(){
        for(int i=0;i<16;i++){
            if(date[i/4][i%4]!=win[i/4][i%4]){
                return false;
            }
        }
        return true;
    }
}
