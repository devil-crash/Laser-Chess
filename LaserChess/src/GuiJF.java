import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class GuiJF extends JFrame {
    JPanel contentPane;     //内容面板
    BoardJP boardJP;        //棋盘面板
    ControlJP controlJP;    //控制台面板
    static Color ableColor, disableColor;  //按键启用、禁用的颜色
    GuiJF() {
        super();
        GuiIni();
    }
    class BoardJP extends JPanel {  //棋盘面板
        CellJB[][] cellJBS;             //棋格按钮
        boolean isHighlight, isLaser;   //是否需要显示高亮、镭射路径
        int rowChosen, columnChosen;    //选中的第一个棋子的方位
        Color blueColor, redColor, laserColor;  //红方、蓝方以及镭射的颜色
        BoardJP() {
            super(new GridLayout(8, 10, 4, 4));
            boardIni();
        }
        class CellJB extends JButton {
            boolean isEnter;    //鼠标指针是否位于按钮内部
            CellJB(int row, int column) {   //生成位于row行 column列的棋格按钮
                super();
                isEnter = false;
                setBackground(ableColor);
                //根据CellBoard给定的棋格排布CELL_LAYOUT设定图标
                if (CellBoard.isVoid(row, column)) setIcon(Icon.getIcon(CellBoard.CELL_LAYOUT[row][column]));
                else setIcon(Icon.getIcon(CellBoard.cells[row][column].getChess()));
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        boardJP.repaint();
                    }
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isEnter = true;
                        isLaser = false;
                        boardJP.repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isEnter = false;
                        boardJP.repaint();
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isHighlight = isEnter && !isHighlight && CellBoard.isMovable(row, column);
                        if (isHighlight) {      //选中第一个棋子
                            rowChosen = row;
                            columnChosen = column;
                            if(CellBoard.isRotatable(row, column)) {    //若选中的棋子可旋转，则开启旋转按键
                                controlJP.cw.setEnabled(true);
                                controlJP.cw.setBackground(ableColor);
                                controlJP.ccw.setEnabled(true);
                                controlJP.ccw.setBackground(ableColor);
                            }
                        } else {                //取消选中的棋子，或已经选择第二个位置以移动
                            //若可移动且移动成功，则更新两个方位的图标
                            if(rowChosen >= 0 && columnChosen >= 0 && CellBoard.move(rowChosen, columnChosen, row, column)) {
                                updateIcon(rowChosen, columnChosen);
                                updateIcon(row, column);
                                rowChosen = columnChosen = -1;  //删除原先选中棋子的行列
                                isLaser = true;                 //将laser置为true，通知BoardJP绘制路径
                            }
                            controlJP.rotateDisable();
                        }
                        boardJP.repaint();
                    }
                });
            }
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            //根据当前玩家设置player和switchState的图标
            if(CellBoard.turn % 2 == CellBoard.BLUE_PLAYER) {
                controlJP.player.setIcon(Icon.iconRead("blue_player.png"));
                controlJP.switchState.setBackground(blueColor);
            }
            else {
                controlJP.player.setIcon(Icon.iconRead("red_player.png"));
                controlJP.switchState.setBackground(redColor);
            }

            if (isHighlight)    //若需要绘制高亮，则绘制
                for (MyPoint i : CellBoard.getMovablePoints()) {
                    drawHighlight(i.row, i.column, g);
                }
            if(isLaser) //若需要绘制镭射，则绘制
            {
                //根据CellBoard给定的路径，绘制镭射
                Vector<MyPoint> path = CellBoard.Laser.getPath();
                for(int i = 1; i < path.size() - 1; i++){
                    MyPoint p1 = path.get(i - 1), p2 = path.get(i);
                    drawLaser(p1.row, p1.column, p2.row, p2.column, g);
                }
                //若有棋子需要销毁，则更新此处的图标，并将CellBoard.toDestroy重新置空
                if(CellBoard.haveToDestroy()) {
                    MyPoint toDestroy = CellBoard.getToDestroy();
                    if (toDestroy != null) {
                        updateIcon(toDestroy.row, toDestroy.column);
                        CellBoard.toDestroy = null;
                    }
                    boardJP.repaint();      //摧毁棋子后需重新绘制镭射，避免被更新的图标覆盖
                    if(CellBoard.isOver()) new GameOverFrame();     //游戏结束则弹出结算窗口
                }
                controlJP.rotateDisable();
            }
        }
        public void updateIcon(int row, int column) //更新row行column列的图标
        {
            if (CellBoard.isVoid(row, column)) cellJBS[row][column].setIcon(Icon.getIcon(CellBoard.CELL_LAYOUT[row][column]));
            else cellJBS[row][column].setIcon(Icon.getIcon(CellBoard.cells[row][column].getChess()));
        }
        public void drawHighlight(int row, int column, Graphics g) {    //根据给定的行列，绘制高亮图标
            JButton cellJB = cellJBS[row][column];
            //根据此处有无棋子，绘制对应的高亮图标
            g.drawImage(Icon.getImage(CellBoard.isVoid(row, column) ? "normal_move_highlight.png" : "special_move_highlight.png"), cellJB.getX() + 5, cellJB.getY() + 5, null);
        }

        public void drawLaser(int row1, int column1, int row2, int column2, Graphics g) {   //重绘时调用，根据给定的两点行列，绘制镭射
            JButton cellJB1 = cellJBS[row1][column1], cellJB2 = cellJBS[row2][column2];
            Graphics2D g2D = (Graphics2D)g;
            g2D.setColor(laserColor);
            g2D.setStroke(new BasicStroke(10.0f));
            g2D.drawLine(cellJB1.getX() + cellJB1.getWidth() / 2, cellJB1.getY() + cellJB1.getHeight() / 2, cellJB2.getX() + cellJB1.getWidth() / 2, cellJB2.getY() + cellJB2.getHeight() / 2);
        }

        void boardIni() {
            setPreferredSize(new Dimension(1000, 800));
            setBackground(new Color(252, 240, 227));
            blueColor = new Color(167,205,255);
            redColor = new Color(255,177, 177);
            laserColor = new Color(108, 18, 232);
            //初始化成员
            cellJBS = new CellJB[8][10];
            for (int i = 0; i < 8; ++i)
                for (int ii = 0; ii < 10; ++ii) {
                    cellJBS[i][ii] = new CellJB(i, ii);
                    add(cellJBS[i][ii]);
                }
            isHighlight = isLaser = false;
            rowChosen = columnChosen = -1;
            addMouseListener(new MouseAdapter() {           //添加鼠标监听器
                @Override
                public void mousePressed(MouseEvent e) {    //面板被点击
                    isHighlight = false;            //通知擦除高亮
                    isLaser = false;                //通知擦除镭射路径
                    controlJP.rotateDisable();      //禁用旋转按钮
                    rowChosen = columnChosen = -1;  //删除原先选中棋子的行列
                    repaint();
                }
            });
        }
    }

    class ControlJP extends JPanel {    //控制台面板
        JLabel player;  //当前玩家
        OperationJB cw, ccw, switchState;   //顺逆旋转选中棋子，以及镭射棋的状态切换
        Font font;  //设置字体
        ControlJP() {
            super(new GridLayout(1, 4));
            controlIni();
        }

        class OperationJB extends JButton {
            boolean isEnter;
            OperationJB(String str) {
                super(str);
                isEnter = false;
                setBackground(disableColor);
                setFont(font);
            }
        }

        public void rotateDisable()     //禁用旋转按钮
        {
            cw.setEnabled(false);
            cw.setBackground(disableColor);
            ccw.setEnabled(false);
            ccw.setBackground(disableColor);
        }

        public void controlIni()
        {
            setPreferredSize(new Dimension(800, 80));
            setBackground(boardJP.getBackground());
            font = new Font("Times New Romen",Font.ITALIC + Font.BOLD,20);
            add(player = new JLabel(Icon.iconRead("blue_player.png")));

            add(ccw = new OperationJB(""));
            add(cw = new OperationJB(""));
            rotateDisable();
            cw.setIcon(Icon.iconRead("cwRotation.png"));
            ccw.setIcon(Icon.iconRead("ccwRotation.png"));
            cw.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    cw.isEnter = true;
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    cw.isEnter = false;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    //判断是否需要响应，若响应则恢复选中的棋子并禁用旋转按钮
                    if(cw.isEnter && cw.isEnabled()) {
                        CellBoard.cwRotate(boardJP.rowChosen, boardJP.columnChosen);
                        rotateDisable();
                        boardJP.updateIcon(boardJP.rowChosen, boardJP.columnChosen);
                        boardJP.rowChosen = boardJP.columnChosen = -1;
                        boardJP.isHighlight = false;
                        boardJP.isLaser = true;
                    }
                    boardJP.repaint();
                }
            });
            ccw.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    ccw.isEnter = true;
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    ccw.isEnter = false;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    //判断是否需要响应，若响应则恢复选中的棋子并禁用旋转按钮
                    if(ccw.isEnter && ccw.isEnabled()) {
                        CellBoard.ccwRotate(boardJP.rowChosen, boardJP.columnChosen);
                        rotateDisable();
                        boardJP.updateIcon(boardJP.rowChosen, boardJP.columnChosen);
                        boardJP.rowChosen = boardJP.columnChosen = -1;
                        boardJP.isHighlight = false;
                        boardJP.isLaser = true;
                    }
                    boardJP.repaint();
                }
            });

            add(switchState = new OperationJB("切换镭射棋方向"));
            switchState.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    switchState.isEnter = true;
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    switchState.isEnter = false;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    //判断是否需要响应，若响应则禁用旋转按钮
                    if(switchState.isEnter) {
                        CellBoard.laserRotate();
                        rotateDisable();
                        boardJP.updateIcon(0, 0);
                        boardJP.updateIcon(7, 9);
                        boardJP.rowChosen = boardJP.columnChosen = -1;
                        boardJP.isHighlight = false;
                        boardJP.isLaser = true;
                    }
                    boardJP.repaint();
                }
            });
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        boardJP.repaint();
    }

    void GuiIni() {
        ableColor = new Color(222,201,184);
        disableColor = new Color(208,208,208);
        contentPane = new JPanel(new BorderLayout());
        contentPane.add(boardJP = new BoardJP(), BorderLayout.CENTER);
        contentPane.add(controlJP = new ControlJP(), BorderLayout.SOUTH);
        setContentPane(contentPane);

        pack();
        setTitle("镭射象棋");
        setIconImage(Icon.getImage("logo.png"));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    static public void main(String[] args) {
        new CellBoard();
        new GuiJF();
    }
}

class GameOverFrame extends JFrame{
    JPanel overPanel;           //内容面板
    JButton confirmJB;          //确认按键
    JLabel infoJL, trophyJL;    //显示获胜者与对应奖杯
    final Font font;            //窗口字体
    GameOverFrame(){
        overPanel = new JPanel(new BorderLayout());
        setContentPane(overPanel);
        font = new Font("Times New Romen",Font.ITALIC + Font.BOLD,40);

        confirmJB = new JButton("确认并退出");
        confirmJB.setBorderPainted(false);
        confirmJB.addActionListener(e -> System.exit(0));
        confirmJB.setBackground(Color.WHITE);
        confirmJB.setFont(font);

        infoJL = new JLabel("",JLabel.CENTER);
        infoJL.setFont(font);
        trophyJL = new JLabel();
        //根据胜者设定对应的奖杯图标
        if(CellBoard.theWinner() == CellBoard.BLUE_PLAYER) {
            infoJL.setText("蓝方胜利！");
            infoJL.setForeground(Color.BLUE);
            trophyJL.setIcon(new ImageIcon("image/blueWins.png"));
        }
        else {
            infoJL.setText("红方胜利！");
            infoJL.setForeground(Color.RED);
            trophyJL.setIcon(new ImageIcon("image/redWins.png"));
        }
        overPanel.add(infoJL,BorderLayout.NORTH);
        overPanel.add(trophyJL,BorderLayout.CENTER);
        overPanel.add(confirmJB,BorderLayout.SOUTH);
        confirmJB.setPreferredSize(new Dimension(400, 100));

        pack();
        setTitle("胜负已分");
        setIconImage(Icon.getImage("logo.png"));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}