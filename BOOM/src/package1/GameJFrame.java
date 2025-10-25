package package1;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameJFrame extends JFrame {
    String[][] data = new String[16][16];
    private boolean gameOver = false;
    private int remainingCells = 216; // 16*16 - 40 = 216个非雷格子
    private JLabel statusLabel;
    private boolean[][] flagged; // 标记数组，记录哪些格子被标记了
    private int flagCount = 0;   // 标记数量
    private final int TOTAL_MINES = 40; // 总雷数
    public GameJFrame() {
        createJFrame();
        createMenu();
        createBoom();
        flagged = new boolean[16][16]; // 初始化标记数组
        showBoom();
        this.setVisible(true);
    }
    // 创建一个窗口
    private void createJFrame() {
        this.setSize(580, 630); // 增加高度以显示状态栏
        this.setTitle("扫雷单机版 v1.0");
        // 置顶
        this.setAlwaysOnTop(true);
        // 居中
        this.setLocationRelativeTo(null);
        // 关闭直接退出程序
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 固定窗口大小，禁止拉伸
        this.setResizable(false);
    }
    // 功能栏
    public void createMenu() {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu function = new JMenu("功能");
        JMenuItem rePlay = new JMenuItem("重新游戏");
        JMenuItem closeGame = new JMenuItem("关闭游戏");
        rePlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        closeGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        function.add(rePlay);
        function.add(closeGame);
        jMenuBar.add(function);
        this.setJMenuBar(jMenuBar);
    }
    // 创建雷区
    public void createBoom() {
        // 初始化雷区
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                data[i][j] = "0";
            }
        }
        // 随机生成40个雷
        Random random = new Random();
        int[] boom = new int[40];
        for (int i = 0; i < 40; i++) {
            int boom1;
            boolean isDuplicate;
            do {
                boom1 = random.nextInt(256);
                isDuplicate = false;
                // 检查是否与已生成的雷重复
                for (int j = 0; j < i; j++) {
                    if (boom[j] == boom1) {
                        isDuplicate = true;
                        break;
                    }
                }
            } while (isDuplicate);
            boom[i] = boom1;
            int x = boom1 / 16;
            int y = boom1 % 16;
            data[x][y] = "💣";
        }
        SetBoom();
    }
    // 设置雷九宫格内的属性
    private void SetBoom() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if ("💣".equals(data[i][j])) {
                    continue;
                }
                int count = 0;
                for (int x = Math.max(0, i - 1); x <= Math.min(15, i + 1); x++) {
                    for (int y = Math.max(0, j - 1); y <= Math.min(15, j + 1); y++) {
                        if (x == i && y == j) continue;
                        if ("💣".equals(data[x][y])) {
                            count++;
                        }
                    }
                }
                if (count > 0) {
                    data[i][j] = String.valueOf(count);
                }
            }
        }
    }
    // 显示雷区
    private void showBoom() {
        this.getContentPane().removeAll();
        this.getContentPane().setLayout(new BorderLayout());
        // 创建状态栏
        statusLabel = new JLabel("剩余标记: " + (TOTAL_MINES - flagCount) + " | 剩余格子: " + remainingCells);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        this.getContentPane().add(statusLabel, BorderLayout.SOUTH);
        // 创建分层面板
        JLayeredPane jLayeredPane = new JLayeredPane();
        jLayeredPane.setBounds(0, 0, 580, 580);
        this.getContentPane().add(jLayeredPane, BorderLayout.CENTER);
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                JLabel jLabel = new JLabel(data[i][j], SwingConstants.CENTER);
                jLabel.setBounds(30 * j + 40, 30 * i + 40, 30, 30);
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                jLabel.setFont(jLabel.getFont().deriveFont(16f));
                jLabel.setOpaque(true);
                jLabel.setBackground(Color.WHITE);
                // 设置数字颜色
                if ("1".equals(data[i][j])) jLabel.setForeground(Color.BLUE);
                else if ("2".equals(data[i][j])) jLabel.setForeground(Color.GREEN);
                else if ("3".equals(data[i][j])) jLabel.setForeground(Color.RED);
                else if ("4".equals(data[i][j])) jLabel.setForeground(Color.MAGENTA);
                else if ("5".equals(data[i][j])) jLabel.setForeground(Color.ORANGE);
                else if ("6".equals(data[i][j])) jLabel.setForeground(Color.CYAN);
                else if ("7".equals(data[i][j])) jLabel.setForeground(Color.PINK);
                else if ("8".equals(data[i][j])) jLabel.setForeground(Color.GRAY);
                jLayeredPane.add(jLabel, JLayeredPane.DEFAULT_LAYER);
                // 盖子
                JButton cover = new JButton("");
                cover.setBounds(30 * j + 40, 30 * i + 40, 30, 30);
                cover.setMargin(new Insets(0, 0, 0, 0));
                cover.setFocusPainted(false);
                cover.setOpaque(true);
                cover.setBackground(Color.LIGHT_GRAY);
                final int row = i;
                final int col = j;
                // 添加鼠标监听器来处理右键点击
                cover.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;
                        if (SwingUtilities.isRightMouseButton(e)) {
                            // 右键点击 - 标记/取消标记
                            if (!flagged[row][col]) {
                                // 标记
                                if (flagCount < TOTAL_MINES) {
                                    flagged[row][col] = true;
                                    cover.setText("🚩");
                                    cover.setForeground(Color.RED);
                                    flagCount++;
                                    updateStatusLabel();
                                }
                            } else {
                                // 取消标记
                                flagged[row][col] = false;
                                cover.setText("");
                                flagCount--;
                                updateStatusLabel();
                            }
                            e.consume(); // 消耗事件，防止其他处理
                        }
                    }
                });
                // 原有的左键点击监听器
                cover.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver || flagged[row][col]) return; // 如果已标记，不响应左键

                        jLayeredPane.remove(cover);
                        jLayeredPane.repaint();

                        if ("💣".equals(data[row][col])) {
                            // 踩到地雷，游戏结束
                            gameOver = true;
                            revealAllMines(jLayeredPane);
                            showGameOverDialog(false);
                        } else {
                            // 安全格子
                            remainingCells--;
                            // 如果是空白格子，自动展开周围格子
                            if ("0".equals(data[row][col])) {
                                autoRevealEmptyCells(row, col, jLayeredPane);
                            }
                            // 检查是否获胜
                            if (remainingCells == 0) {
                                gameOver = true;
                                showGameOverDialog(true);
                            }
                            updateStatusLabel();
                        }
                    }
                });
                jLayeredPane.add(cover, JLayeredPane.PALETTE_LAYER);
            }
        }
        this.revalidate();
        this.repaint();
    }
    // 更新状态标签
    private void updateStatusLabel() {
        statusLabel.setText("剩余标记: " + (TOTAL_MINES - flagCount) + " | 剩余格子: " + remainingCells);
    }
    // 自动展开空白格子
    private void autoRevealEmptyCells(int row, int col, JLayeredPane layeredPane) {
        for (int x = Math.max(0, row - 1); x <= Math.min(15, row + 1); x++) {
            for (int y = Math.max(0, col - 1); y <= Math.min(15, col + 1); y++) {
                Component[] components = layeredPane.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JButton) {
                        JButton button = (JButton) comp;
                        if (button.getBounds().x == 30 * y + 40 &&
                                button.getBounds().y == 30 * x + 40) {
                            // 检查是否被标记，如果标记了就不展开
                            Point pos = button.getLocation();
                            int currentCol = (pos.x - 40) / 30;
                            int currentRow = (pos.y - 40) / 30;
                            if (flagged[currentRow][currentCol]) {
                                continue;
                            }
                            layeredPane.remove(button);
                            layeredPane.repaint();
                            if ("0".equals(data[x][y])) {
                                // 递归展开连续的空白格子
                                autoRevealEmptyCells(x, y, layeredPane);
                            }
                            if (!"💣".equals(data[x][y])) {
                                remainingCells--;
                            }
                        }
                    }
                }
            }
        }
        updateStatusLabel();
    }
    // 显示所有地雷
    private void revealAllMines(JLayeredPane layeredPane) {
        Component[] components = layeredPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                Point pos = button.getLocation();
                int col = (pos.x - 40) / 30;
                int row = (pos.y - 40) / 30;
                if ("💣".equals(data[row][col])) {
                    layeredPane.remove(button);
                    layeredPane.repaint();
                } else {
                    button.setEnabled(false);
                }
            }
        }
    }
    // 显示游戏结束对话框
    private void showGameOverDialog(boolean isWin) {
        String message = isWin ? "恭喜你！你赢了！" : "游戏结束，你踩到地雷了！";
        // 创建自定义对话框
        Object[] options = {"重新游戏", "退出游戏"};
        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "游戏结束",
                JOptionPane.YES_NO_OPTION,
                isWin ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0] // 默认选择重新游戏
        );
        // 处理用户选择
        if (option == JOptionPane.YES_OPTION) {
            // 重新游戏
            restartGame();
        } else if (option == JOptionPane.NO_OPTION) {
            // 退出游戏
            System.exit(0);
        }
    }
    // 重新开始游戏
    private void restartGame() {
        this.dispose();
        new GameJFrame();
    }
}