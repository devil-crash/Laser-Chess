import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

class Icon {
    static final String IMAGE_PATH = "image/";
    static final ImageIcon BLUE_RESERVED, RED_RESERVED,
            LASER_BLUE, LASER_BLUE_90, LASER_BLUE_180, LASER_BLUE_270, LASER_RED, LASER_RED_90, LASER_RED_180, LASER_RED_270,
            DEFLECTOR_BLUE_45, DEFLECTOR_BLUE_135, DEFLECTOR_BLUE_225, DEFLECTOR_BLUE_315, DEFLECTOR_RED_45, DEFLECTOR_RED_135, DEFLECTOR_RED_225, DEFLECTOR_RED_315,
            DEFENDER_BLUE, DEFENDER_BLUE_90, DEFENDER_BLUE_180, DEFENDER_BLUE_270, DEFENDER_RED, DEFENDER_RED_90, DEFENDER_RED_180, DEFENDER_RED_270,
            SWITCH_BLUE_45, SWITCH_BLUE_135, SWITCH_BLUE_225, SWITCH_BLUE_315, SWITCH_RED_45, SWITCH_RED_135, SWITCH_RED_225, SWITCH_RED_315, KING_BLUE, KING_RED;

    static {
        BLUE_RESERVED = iconRead("blue_reserved_cell.png");
        RED_RESERVED = iconRead("red_reserved_cell.png");

        LASER_BLUE = iconRead("blue_l.png");
        LASER_BLUE_90 = iconRead("blue_l_90.png");
        LASER_BLUE_180 = iconRead("blue_l_180.png");
        LASER_BLUE_270 = iconRead("blue_l_270.png");

        LASER_RED = iconRead("red_l.png");
        LASER_RED_90 = iconRead("red_l_90.png");
        LASER_RED_180 = iconRead("red_l_180.png");
        LASER_RED_270 = iconRead("red_l_270.png");

        DEFLECTOR_BLUE_45 = iconRead("blue_b_45.png");
        DEFLECTOR_BLUE_135 = iconRead("blue_b_135.png");
        DEFLECTOR_BLUE_225 = iconRead("blue_b_225.png");
        DEFLECTOR_BLUE_315 = iconRead("blue_b_315.png");

        DEFLECTOR_RED_45 = iconRead("red_b_45.png");
        DEFLECTOR_RED_135 = iconRead("red_b_135.png");
        DEFLECTOR_RED_225 = iconRead("red_b_225.png");
        DEFLECTOR_RED_315 = iconRead("red_b_315.png");

        DEFENDER_BLUE = iconRead("blue_d.png");
        DEFENDER_BLUE_90 = iconRead("blue_d_90.png");
        DEFENDER_BLUE_180 = iconRead("blue_d_180.png");
        DEFENDER_BLUE_270 = iconRead("blue_d_270.png");

        DEFENDER_RED = iconRead("red_d.png");
        DEFENDER_RED_90 = iconRead("red_d_90.png");
        DEFENDER_RED_180 = iconRead("red_d_180.png");
        DEFENDER_RED_270 = iconRead("red_d_270.png");

        SWITCH_BLUE_45 = iconRead("blue_s_45.png");
        SWITCH_BLUE_135 = iconRead("blue_s_135.png");
        SWITCH_BLUE_225 = iconRead("blue_s_225.png");
        SWITCH_BLUE_315 = iconRead("blue_s_315.png");

        SWITCH_RED_45 = iconRead("red_s_45.png");
        SWITCH_RED_135 = iconRead("red_s_135.png");
        SWITCH_RED_225 = iconRead("red_s_225.png");
        SWITCH_RED_315 = iconRead("red_s_315.png");

        KING_BLUE = iconRead("blue_k.png");
        KING_RED = iconRead("red_k.png");
    }

    static ImageIcon iconRead(String str) {
        try {
            return new ImageIcon(ImageIO.read(new File(IMAGE_PATH + str)).getScaledInstance(85, 85, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static ImageIcon getIcon(int cellSort) {
        return switch (cellSort) {
            case Cell.BLUE_RESERVED -> BLUE_RESERVED;
            case Cell.RED_RESERVED -> RED_RESERVED;
            default -> null;
        };
    }

    static ImageIcon getIcon(Chess chess) {
        switch (chess.getName()) {
            case "Laser":
                switch (chess.player) {
                    case CellBoard.BLUE_PLAYER -> {
                        return switch (chess.angle) {
                            case 0 -> LASER_BLUE;
                            case 90 -> LASER_BLUE_90;
                            case 180 -> LASER_BLUE_180;
                            case 270 -> LASER_BLUE_270;
                            default -> null;
                        };
                    }
                    case CellBoard.RED_PLAYER -> {
                        return switch (chess.angle) {
                            case 0 -> LASER_RED;
                            case 90 -> LASER_RED_90;
                            case 180 -> LASER_RED_180;
                            case 270 -> LASER_RED_270;
                            default -> null;
                        };
                    }
                }
            case "Deflector":
                switch (chess.player) {
                    case CellBoard.BLUE_PLAYER -> {
                        return switch (chess.angle) {
                            case 45 -> DEFLECTOR_BLUE_45;
                            case 135 -> DEFLECTOR_BLUE_135;
                            case 225 -> DEFLECTOR_BLUE_225;
                            case 315 -> DEFLECTOR_BLUE_315;
                            default -> null;
                        };
                    }
                    case CellBoard.RED_PLAYER -> {
                        return switch (chess.angle) {
                            case 45 -> DEFLECTOR_RED_45;
                            case 135 -> DEFLECTOR_RED_135;
                            case 225 -> DEFLECTOR_RED_225;
                            case 315 -> DEFLECTOR_RED_315;
                            default -> null;
                        };
                    }
                }
            case "Defender":
                switch (chess.player) {
                    case CellBoard.BLUE_PLAYER -> {
                        return switch (chess.angle) {
                            case 0 -> DEFENDER_BLUE;
                            case 90 -> DEFENDER_BLUE_90;
                            case 180 -> DEFENDER_BLUE_180;
                            case 270 -> DEFENDER_BLUE_270;
                            default -> null;
                        };
                    }
                    case CellBoard.RED_PLAYER -> {
                        return switch (chess.angle) {
                            case 0 -> DEFENDER_RED;
                            case 90 -> DEFENDER_RED_90;
                            case 180 -> DEFENDER_RED_180;
                            case 270 -> DEFENDER_RED_270;
                            default -> null;
                        };
                    }
                }
            case "Switch":
                switch (chess.player) {
                    case CellBoard.BLUE_PLAYER -> {
                        return switch (chess.angle) {
                            case 45 -> SWITCH_BLUE_45;
                            case 135 -> SWITCH_BLUE_135;
                            case 225 -> SWITCH_BLUE_225;
                            case 315 -> SWITCH_BLUE_315;
                            default -> null;
                        };
                    }
                    case CellBoard.RED_PLAYER -> {
                        return switch (chess.angle) {
                            case 45 -> SWITCH_RED_45;
                            case 135 -> SWITCH_RED_135;
                            case 225 -> SWITCH_RED_225;
                            case 315 -> SWITCH_RED_315;
                            default -> null;
                        };
                    }
                }
            case "King":
                switch (chess.player) {
                    case CellBoard.BLUE_PLAYER -> {
                        return KING_BLUE;
                    }
                    case CellBoard.RED_PLAYER -> {
                        return KING_RED;
                    }
                }
            default:
                return null;
        }
    }

    static Image getImage(String str) {
        return iconRead(str).getImage();
    }
}