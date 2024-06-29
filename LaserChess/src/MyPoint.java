class MyPoint {
    int row, column;
    /*
    *   +-------> column
    *   |
    *   |
    *   | row
    *   v
    * */
    static MyPoint up = new MyPoint(-1, 0);
    static MyPoint down = new MyPoint(1, 0);
    static MyPoint left = new MyPoint(0, -1);
    static MyPoint right = new MyPoint(0, 1);
    MyPoint(int row, int column) {
        this.row = row;
        this.column = column;
    }
    MyPoint(MyPoint myPoint) {
        row = myPoint.row;
        column = myPoint.column;
    }

    void addVector(MyPoint vector) {   //用作箭头
        row += vector.row;
        column += vector.column;
    }
    void subVector(MyPoint vector) {  //用作箭头
        row -= vector.row;
        column -= vector.column;
    }

    // angle 具体含义见 Chess.java--Chess--LaserHandler
    // 及 CellBoard.java--CellBoard--Laser
    static MyPoint angleToVector(int angle) {
        return switch (angle) {
            case 0 -> up;
            case 90 -> right;
            case 180 -> down;
            case 270 -> left;
            default -> throw new IllegalStateException("Unexpected angle: " + angle);
        };
    }
}