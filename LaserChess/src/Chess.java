public abstract class Chess     //棋子（抽象）
{
    int angle;          //从特定“0状态”顺时针偏转角度
    int player;         // 0 / 1（blue / red） default(空) -1
    Chess(int angle, int player) {
        this.angle = angle;
        this.player = player;
    }

    public abstract String getName();
    public int getAngle(){ return angle; }
    public int getPlayer() {
        return player;
    }

    public void cwRotate() {
        if(isRotatable()) {
            angle = (angle + 90) % 360;
        }
    }
    public void ccwRotate(){
        if(isRotatable()) {
            angle = (angle + 270) % 360;
        }
    }
    public void rotate() {}     // for LaserChess 特殊旋转

    public abstract boolean isRotatable();
    public boolean isSwappable(Chess chess) {   //是否可与特定chess棋交换
        return chess instanceof VoidChess;
    }
    boolean isSwappable(Cell cell) {     //是否可与特定cell上的棋交换
        return isSwappable(cell.getChess()) &&
                cell.getSort() != 1 - player; // 1 - player 为对手
    }

    /*处理接收到的镭射, inAngle--射向此棋的镭射相对此棋的方位
    *        | 0
    *        |
    *   ---> * <--- 90
    *  270   ^
    *        |  180
    *
    * 返回LaserResult 见CellBoard.Laser
    * */
    abstract int laserHandler(int inAngle);
}

class VoidChess extends Chess {
    VoidChess() {
        super(0, -1);
    }
    @Override
    public String getName(){ return "Void";}
    @Override
    public boolean isRotatable() {
        return false;
    }
    @Override
    public boolean isSwappable(Chess chess) {
        return false;
    }
    @Override
    int laserHandler(int inAngle) {
        return (inAngle + 180) % 360;
    }
}

/* 0状态
*   ^
*   |
*/
class LaserChess extends Chess {
    LaserChess(int angle, int player){
        super(angle, player);
    }
    public void rotate() {
        if(player == 0)
            angle = (angle == 0 ? 270 : 0);
        else if(player == 1)
            angle = (angle == 180 ? 90 : 180);
    }
    @Override
    public String getName(){ return "Laser"; }
    @Override
    public boolean isRotatable() {
        return false;
    }
    @Override
    public boolean isSwappable(Chess chess) {
        return false;
    }
    @Override
    int laserHandler(int inAngle) {     //吸收镭射
        return CellBoard.Laser.ABSORB;
    }
}

/*  0状态             angle=45
*       *               *************
*    *     *            ***         *
*   *********           ******      *
*    *******            *********   *
*       *               *************
*/
class DeflectorChess extends Chess {
    DeflectorChess(int angle, int player) {
        super(angle, player);
    }

    @Override
    public String getName(){ return "Deflector"; }

    @Override
    public boolean isRotatable(){ return true; }    //Deflector棋可旋转
    @Override
    int laserHandler(int inAngle) {     //两面反射镭射，其余判定摧毁
        if(inAngle == 0 && this.angle == 315) {
            return 270;
        }
        else if(inAngle - this.angle > 45 || inAngle - this.angle < -45)
            return CellBoard.Laser.DESTROY;
        else return (2 * (this.angle - inAngle) + inAngle) % 360;    //REFLECT_ANGLE
    }
}

/*     0状态
*   *       *
*   *       *
*   *********
* */
class DefenderChess extends Chess {
    DefenderChess(int angle, int player) {
        super(angle, player);
    }
    @Override
    public String getName(){ return "Defender"; }
    @Override
    public boolean isRotatable() { return true; }
    @Override
    int laserHandler(int inAngle) {     //一面吸收镭射，其余面判定摧毁
        if(inAngle == this.angle) return CellBoard.Laser.ABSORB;
        else return CellBoard.Laser.DESTROY;
    }
}


/*    0状态              angle=45
*                         *
*      ***                 *
*                           *
* */
class SwitchChess extends Chess {
    SwitchChess(int angle, int player) {
        super(angle, player);
    }
    @Override
    public String getName(){ return "Switch"; }
    @Override
    public boolean isRotatable(){ return true; }
    @Override
    public boolean isSwappable(Chess chess) {
        return chess instanceof DeflectorChess
                || chess instanceof DefenderChess
                || chess instanceof VoidChess;
    }
    @Override
    int laserHandler(int inAngle) {     //反射镭射
        return (2 * (this.angle - inAngle) + inAngle + 360) % 360;    //REFLECT_ANGLE
    }
}

class KingChess extends Chess {
    KingChess(int player) {
        super(0, player);
    }

    @Override
    public String getName(){ return "King"; }

    @Override
    public boolean isRotatable(){ return false; }   //King棋不可旋转
    @Override
    int laserHandler(int inAngle) {     //四面判定摧毁
        CellBoard.gameOver(1 - player);
        return CellBoard.Laser.DESTROY;
    }
}