import java.util.Vector;

public class CellBoard {
    static final int BLUE_PLAYER = 0, RED_PLAYER = 1;   //玩家
    static final int[][] CELL_LAYOUT = new int[][]      //棋格布局
        {
                {Cell.RED_RESERVED, Cell.BLUE_RESERVED,     Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.RED_RESERVED,  Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLANK,             Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK,         Cell.BLUE_RESERVED,},
                {Cell.RED_RESERVED, Cell.BLUE_RESERVED,     Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.BLANK, Cell.RED_RESERVED,  Cell.BLUE_RESERVED,},
        };

    static final String[][][] CHESS_LAYOUT = {
            {{"laser", "red", "180"},       {"void", "", ""},       {"void", "", ""},               {"void", "", ""},               {"defender", "red", "180"},     {"king", "red", ""},        {"defender", "red", "180"},     {"deflector", "red", "135"},    {"void", "", ""},       {"void", "", ""}},
            {{"void", "", ""},              {"void", "", ""},       {"deflector", "red", "225"},    {"void", "", ""},               {"void", "", ""},               {"void", "", ""},           {"void", "", ""},               {"void", "", ""},               {"void", "", ""},       {"void", "", ""}},
            {{"void", "", ""},              {"void", "", ""},       {"void", "", ""},               {"deflector", "blue", "315"},   {"void", "", ""},               {"void", "", ""},           {"void", "", ""},               {"void", "", ""},               {"void", "", ""},       {"void", "", ""}},
            {{"deflector", "red", "45"},    {"void", "", ""},       {"deflector", "blue", "225"},   {"void", "", ""},               {"switch", "red", "45"},        {"switch", "red", "135"},   {"void", "", ""},               {"deflector", "red", "135"},    {"void", "", ""},       {"deflector", "blue", "315"}},
            {{"deflector", "red", "135"},   {"void", "", ""},       {"deflector", "blue", "315"},   {"void", "", ""},               {"switch", "blue", "135"},      {"switch", "blue", "45"},   {"void", "", ""},               {"deflector", "red", "45"},     {"void", "", ""},       {"deflector", "blue", "225"}},
            {{"void", "", ""},              {"void", "", ""},       {"void", "", ""},               {"void", "", ""},               {"void", "", ""},               {"void", "", ""},           {"deflector", "red", "135"},    {"void", "", ""},               {"void", "", ""},       {"void", "", ""}},
            {{"void", "", ""},              {"void", "", ""},       {"void", "", ""},               {"void", "", ""},               {"void", "", ""},               {"void", "", ""},           {"void", "", ""},               {"deflector", "blue", "45"},    {"void", "", ""},       {"void", "", ""}},
            {{"void", "", ""},              {"void", "", ""},       {"deflector", "blue", "315"},   {"defender", "blue", "0"},      {"king", "blue", ""},           {"defender", "blue", "0"},  {"void", "", ""},               {"void", "", ""},               {"void", "", ""},       {"laser", "blue", "0"}}
    };
    static Cell[][] cells;      //棋格
    static final MyPoint blueLaserMyPoint = new MyPoint(7, 9);    //蓝方Laser棋所在棋格
    static final MyPoint redLaserMyPoint = new MyPoint(0, 0);     //红方Laser棋所在棋格
    static Vector<MyPoint> movableMyPoints;     // 指定棋子可移动范围
    static MyPoint toDestroy = null;            // 镭射摧毁棋子
    static int turn;   // odd-blue; even-red
    static int winner;

    CellBoard() {
        winner = -1;
        movableMyPoints = new Vector<>();
        turn = 0;
        cells = new Cell[8][10];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                cells[row][col] = new Cell(CELL_LAYOUT[row][col], null);

                switch (CHESS_LAYOUT[row][col][0]) {
                    case "void" -> {
                        cells[row][col].setChess(new VoidChess());
                    }
                    case "king" -> {
                        if(CHESS_LAYOUT[row][col][1].equals("blue"))
                            cells[row][col].setChess(new KingChess(BLUE_PLAYER));
                        else
                            cells[row][col].setChess(new KingChess(RED_PLAYER));
                    }
                    case "laser" -> {
                        if(CHESS_LAYOUT[row][col][1].equals("blue"))
                            cells[row][col].setChess(new LaserChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), BLUE_PLAYER));
                        else
                            cells[row][col].setChess(new LaserChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), RED_PLAYER));
                    }
                    case "deflector" -> {
                        if(CHESS_LAYOUT[row][col][1].equals("blue"))
                            cells[row][col].setChess(new DeflectorChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), BLUE_PLAYER));
                        else
                            cells[row][col].setChess(new DeflectorChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), RED_PLAYER));
                    }
                    case "defender" -> {
                        if(CHESS_LAYOUT[row][col][1].equals("blue"))
                            cells[row][col].setChess(new DefenderChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), BLUE_PLAYER));
                        else
                            cells[row][col].setChess(new DefenderChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), RED_PLAYER));
                    }
                    case "switch" -> {
                        if(CHESS_LAYOUT[row][col][1].equals("blue"))
                            cells[row][col].setChess(new SwitchChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), BLUE_PLAYER));
                        else
                            cells[row][col].setChess(new SwitchChess(Integer.parseInt(CHESS_LAYOUT[row][col][2]), RED_PLAYER));
                    }
                }
            }
        }
    }

    static boolean isInBoard(int row, int col) {
        return (row >= 0 && row <= 7)
                && (col >= 0 && col <= 9);
    }
    static boolean isInBoard(MyPoint myPoint) {
        return (myPoint.row >= 0 && myPoint.row <= 7)
                && (myPoint.column >= 0 && myPoint.column <= 9);
    }
    static boolean isVoid(int row, int col) {       // 指定位置棋格上是否为空棋
        return cells[row][col].getChess() instanceof VoidChess;
    }

    static Cell getCell(MyPoint position) {
        return cells[position.row][position.column];
    }

    // 前端所选棋子isMovable()时，将调用该方法更新可移动方位
    static Vector<MyPoint> getMovablePoints() {
        return movableMyPoints;
    }

    // 指定棋子当前能否移动，同时更新可移动范围
    static boolean isMovable(int row, int col) {
        movableMyPoints.clear();

        if ((turn % 2) != cells[row][col].getChess().getPlayer()) return false;

        MyPoint[] around = {
                new MyPoint(row - 1, col - 1), new MyPoint(row - 1, col),
                new MyPoint(row - 1, col + 1), new MyPoint(row, col - 1),
                new MyPoint(row, col + 1), new MyPoint(row + 1, col - 1),
                new MyPoint(row + 1, col), new MyPoint(row + 1, col + 1)
        };

        boolean isMovable = false;
        for (MyPoint myPoint : around)
            if (isInBoard(myPoint.row, myPoint.column)
                    && isMovable(row, col, myPoint.row, myPoint.column)) {
                movableMyPoints.add(myPoint);
                isMovable = true;
            }
        return isMovable;
    }

    // (row1, col1)的棋子能否移动到(row2, col2)
    public static boolean isMovable(int row1, int col1, int row2, int col2) {
        return row2 - row1 >= -1 && row2 - row1 <= 1
                && col2 - col1 >= -1 && col2 - col1 <= 1
                && cells[row1][col1].getChess().isSwappable(cells[row2][col2]);
    }

    static boolean isRotatable(int row, int col) {
        return cells[row][col].getChess().getPlayer() == (turn % 2)
                && cells[row][col].getChess().isRotatable();
    }

    //  是否有棋被摧毁
    static boolean haveToDestroy() {
        return toDestroy != null;
    }

    // 调用后重置 toDestroy
    static MyPoint getToDestroy() {
        if(haveToDestroy()) {
            MyPoint ret = toDestroy;
            toDestroy = null;
            return ret;
        }

        return null;
    }

    // king被摧毁时调用
    static void gameOver(int player) {
        winner = player;
    }

    //  游戏是否结束
    static boolean isOver() {
        return winner != -1;
    }

    static int theWinner() {
        return winner;
    }

    //row1,col1为起点，row2,col2为终点，移动棋子，成功返回true，反之false
    public static boolean move(int row1, int col1, int row2, int col2) {
        if (isMovable(row1, col1, row2, col2)) {
            swap(row1, col1, row2, col2);
            Laser.LaserUpdate(turn % 2);
            ++turn;
            return true;
        }
        return false;
    }

    //  移动(交换棋子)
    private static void swap(int row1, int col1, int row2, int col2)
    {
        Chess tmp = cells[row1][col1].getChess();
        cells[row1][col1].setChess(cells[row2][col2].getChess());
        cells[row2][col2].setChess(tmp);
    }

    public static void cwRotate(int row, int col) {     //顺时针旋转棋子
        if (isRotatable(row, col)) {
            cells[row][col].getChess().cwRotate();
            Laser.LaserUpdate(turn % 2);
            ++turn;
        }
    }

    public static void ccwRotate(int row, int col) {    //逆时针旋转棋子
        if (isRotatable(row, col)) {
            cells[row][col].getChess().ccwRotate();
            Laser.LaserUpdate(turn % 2);
            ++turn;
        }
    }

    //旋转镭射发射器
    public static void laserRotate() {
        if (turn % 2 == BLUE_PLAYER) getCell(blueLaserMyPoint).getChess().rotate();
        else getCell(redLaserMyPoint).getChess().rotate();
        Laser.LaserUpdate(turn % 2);
        ++turn;
    }

    static class Laser {
        //LaserResult
        static final int ABSORB = -1, DESTROY = -2;
        /*  outAngle
         *        ^
         *        | 0
         *        |
         *   <--- * ---> 90
         *  270   |
         *        |  180
         *        v
         * */
        static Vector<MyPoint> path = new Vector<>();  // (-1, -1)表示激光路径的结束
        static MyPoint end = new MyPoint(-1, -1);

        public static Vector<MyPoint> getPath() {
            return path;
        }

        //  玩家操作结束后更新镭射
        public static void LaserUpdate(int player) {
            path.clear();
            MyPoint laserPosition = player == BLUE_PLAYER ?
                    new MyPoint(blueLaserMyPoint) : new MyPoint(redLaserMyPoint);   //设定初始镭射方位

            int outAngle = getCell(laserPosition).getChess().getAngle();    //当前镭射朝向角度
            MyPoint forwardVector = MyPoint.angleToVector(outAngle);        //当前镭射朝向向量，用于改变镭射方位
            path.add(new MyPoint(laserPosition));                           //将起始方位添加至路径
            for( ; ; ) {                                        //持续调用laserHandler，直至镭射超出范围、棋子吸收镭射、棋子被镭射摧毁时为止
                laserPosition.addVector(forwardVector);         //依据当前镭射朝向向量，改变镭射方位

                if (!CellBoard.isInBoard(laserPosition)) {      //镭射超出范围，保存路径后停止循环
                    laserPosition.subVector(forwardVector);
                    path.add(new MyPoint(laserPosition));
                    path.add(end);
                    break;
                }

                path.add(new MyPoint(laserPosition));                       //更新镭射路径
                outAngle = getCell(laserPosition).getChess().laserHandler   //更新镭射朝向角度
                        ((outAngle + 180) % 360);
                if (outAngle < 0) {                 //镭射朝向角度为负数，说明棋子吸收镭射或棋子被镭射摧毁，应保存路径并停止循环
                    path.add(end);
                    if (outAngle == DESTROY) {      //棋子被镭射摧毁，则将该棋改为空棋，并保存其方位供gui更新
                        toDestroy = new MyPoint(laserPosition);
                        getCell(laserPosition).setChess(new VoidChess());
                    }
                    break;
                }
                forwardVector = MyPoint.angleToVector(outAngle);    //更新镭射朝向向量
            }
        }
    }
}

class Cell {
    static final int BLUE_RESERVED = 0,
            RED_RESERVED = 1,
            BLANK = -1;
    private final int sort; //棋格种类
    private Chess chess;    //容纳的棋子

    Cell(int sort, Chess chess) {
        this.sort = sort;
        this.chess = chess;
    }

    public int getSort() {
        return sort;
    }

    public Chess getChess() {
        return chess;
    }

    public void setChess(Chess chess) {
        this.chess = chess;
    }
}