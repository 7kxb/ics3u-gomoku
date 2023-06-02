// imports
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.sound.sampled.*;
// our class, gomoku
public class gomoku {
    // initialise
    static JFrame gameWindow;
    static JPanel panel;
    static int size = 15;
    static JButton[][] gameButtons = new JButton[size+10][size+10];
    static int [][] gameGrid = new int[size+10][size+10];
    static int turn = 1;
    static Boolean check = false;
    static int[] c = new int[8];
    static int[] temp1 = new int[2];
    static JFrame popup;
    static JFrame mainMenu;
    static int players = 0;
    static int bB = 0;
    static int wW = 0;
    // main
    public static void main(String[] args) {
        menu();
    }
    // menu screen/display
    public static void menu() {
        mainMenu = new JFrame("Gomoku");
        mainMenu.setSize(240,240);
        mainMenu.setLocation(220,40);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel();
        label.setText("Gomoku");
        panel2.add(label);
        JButton single = new JButton();
        JButton multi = new JButton();
        single.setText("Singleplayer");
        multi.setText("Multiplayer");
        single.addActionListener(new clickedButton());
        multi.addActionListener(new clickedButton());
        panel2.add(single);
        panel2.add(multi);
        mainMenu.add(panel2);
        mainMenu.setVisible(true);
        mainMenu.requestFocus();
    }
    // sets up the game
    public static void setup() {
        gameWindow = new JFrame("Gomoku");
        gameWindow.setSize(680,680);
        gameWindow.setLocation(220,40);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel();
        panel.setLayout(new GridLayout(size,size));
        gameWindow.add(panel);
        for (int row = 0; row < size+10; row++) {
            for (int col = 0; col < size+10; col++) {
                gameButtons[row][col] = new JButton("");
                if ((row >= 5 && row < size+5) && (col >= 5 && col < size+5)) panel.add(gameButtons[row][col]);
            }
        }
        init();
        gameWindow.setVisible(true);
        gameWindow.requestFocus();
    }
    // initalises board and logic
    public static void init() {
        Font font1 = new Font("SansSerif", Font.PLAIN, 8);
        for (int row = 5; row < size+5; row++) for (int col = 5; col < size+5; col++) gameGrid[row][col] = 0;
        for (int row = 5; row < size+5; row++) {
            for (int col = 5; col < size+5; col++) {
                gameButtons[row][col].setBackground(Color.orange);
                gameButtons[row][col].addActionListener(new clickedButton());
                gameButtons[row][col].setFont(font1);
            }
        }
        gameButtons[7+5][7+5].setText("X");
        gameButtons[3+5][3+5].setText("X");
        gameButtons[3+5][11+5].setText("X");
        gameButtons[11+5][3+5].setText("X");
        gameButtons[11+5][11+5].setText("X");
        gameButtons[0+5][0+5].setText("A1");
        for (int i = 1; i <= 15; i++) gameButtons[0+5][i+5].setText(i+1+"");
        for (int i = 1; i <= 15; i++) gameButtons[i+5][0+5].setText(""+(char)(i+65));
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(gomoku.class.getResource("RUecLQZ2lbw.wav"));
            Clip sound = AudioSystem.getClip();
            sound.open(audioInputStream);
            sound.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
    // when a button is clicked
    static class clickedButton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String s = event.getActionCommand();
            if (s.equals("Back")) {
                gameWindow.dispose();
                popup.dispose();
                menu();
            }
            if (check == false) {
                if (s.equals("Singleplayer")) {
                    check = true;
                    players = 1;
                    mainMenu.dispose();
                    setup();
                }
                if (s.equals("Multiplayer")) {
                    check = true;
                    players = 2;
                    mainMenu.dispose();
                    setup();
                }
            }
            if (check == true) {
                check = false;
                for (int row = 5; row < size+5; row++) {
                    for (int col = 5; col < size+5; col++) {
                        if (event.getSource() == gameButtons[row][col] && (gameButtons[row][col].getBackground() == Color.orange || gameButtons[row][col].getBackground() == Color.red || gameButtons[row][col].getBackground() == Color.yellow)) {
                            try {
                                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(gomoku.class.getResource("rp1pcf_oIcU.wav"));
                                Clip sound = AudioSystem.getClip();
                                sound.open(audioInputStream);
                                sound.start();
                            } catch (Exception e) {
                                System.out.println("Error with playing sound.");
                                e.printStackTrace();
                            }
                            if (turn == 1) gameButtons[row][col].setBackground(Color.black);
                            if (players == 2) {
                                if (turn == -1) gameButtons[row][col].setBackground(Color.white);
                                turn *= -1;
                            }
                            for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) checkIfWin(r,co);
                            if (check == true) {
                                for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) highlightThree(r,co);
                                for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) highlightFour(r,co);
                            }
                            else for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) if (gameButtons[r][co].getBackground() != Color.white && gameButtons[r][co].getBackground() != Color.black) gameButtons[r][co].setBackground(Color.orange);
                            if (players == 1) {
                                aiMove();
                                gameButtons[temp1[0]][temp1[1]].setBackground(Color.white);
                            }
                            for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) checkIfWin(r,co);
                            if (check == true) {
                                for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) highlightThree(r,co);
                                for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) highlightFour(r,co);
                            }
                            else for (int r = 5; r < size+5; r++) for (int co = 5; co < size+5; co++) if (gameButtons[r][co].getBackground() != Color.white && gameButtons[r][co].getBackground() != Color.black) gameButtons[r][co].setBackground(Color.orange);
                        }
                        if (gameButtons[row][col].getBackground() == Color.orange) check = true;
                    }
                }
            }
        }
    }
    // checks if 5 in a row
    public static void checkIfWin(int x, int y) {
        Arrays.fill(c,0);
        for (int i = 0; i < 5; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.black) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.black) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.black) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.black) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.black) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.black) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.black) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.black) c[7]++;
        }
        for (int i = 0; i < c.length; i++) if (c[i] >= 5) bB++;
        if (bB > 2) blackWin();
        Arrays.fill(c,0);
        for (int i = 0; i < 5; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.white) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.white) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.white) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.white) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.white) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.white) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.white) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.white) c[7]++;
        }
        for (int i = 0; i < c.length; i++) if (c[i] >= 5) wW++;
        if (wW > 2) whiteWin();
    }
    // panel when black wins
    public static void blackWin() {
        bB = 0;
        check = false;
        popup = new JFrame("black won");
        popup.setSize(200,100);
        popup.setLocation(400,300);
        JButton back = new JButton("Back");
        back.addActionListener(new clickedButton());
        popup.add(back);
        popup.setVisible(true);
    }
    // panel when white wins
    public static void whiteWin() {
        wW = 0;
        check = false;
        popup = new JFrame("white won");
        popup.setSize(200,100);
        popup.setLocation(400,300);
        JButton back = new JButton("Back");
        back.addActionListener(new clickedButton());
        popup.add(back);
        popup.setVisible(true);
    }
    // highlights four in a row warning
    public static void highlightFour(int x, int y) {
        Arrays.fill(c,0);
        for (int i = 0; i < 5; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.black) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.black) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.black) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.black) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.black) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.black) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.black) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.black) c[7]++;
        }
        for (int i = 0; i < 5; i++) {
            if (c[0] == 4) if (gameButtons[x][y-i].getBackground() != Color.white && gameButtons[x][y-i].getBackground() != Color.black) gameButtons[x][y-i].setBackground(Color.red);
            if (c[1] == 4) if (gameButtons[x+i][y-i].getBackground() != Color.white && gameButtons[x+i][y-i].getBackground() != Color.black) gameButtons[x+i][y-i].setBackground(Color.red);
            if (c[2] == 4) if (gameButtons[x+i][y].getBackground() != Color.white && gameButtons[x+i][y].getBackground() != Color.black) gameButtons[x+i][y].setBackground(Color.red);
            if (c[3] == 4) if (gameButtons[x+i][y+i].getBackground() != Color.white && gameButtons[x+i][y+i].getBackground() != Color.black) gameButtons[x+i][y+i].setBackground(Color.red);
            if (c[4] == 4) if (gameButtons[x][y+i].getBackground() != Color.white && gameButtons[x][y+i].getBackground() != Color.black) gameButtons[x][y+i].setBackground(Color.red);
            if (c[5] == 4) if (gameButtons[x-i][y+i].getBackground() != Color.white && gameButtons[x-i][y+i].getBackground() != Color.black) gameButtons[x-i][y+i].setBackground(Color.red);
            if (c[6] == 4) if (gameButtons[x-i][y].getBackground() != Color.white && gameButtons[x-i][y].getBackground() != Color.black) gameButtons[x-i][y].setBackground(Color.red);
            if (c[7] == 4) if (gameButtons[x-i][y-i].getBackground() != Color.white && gameButtons[x-i][y-i].getBackground() != Color.black) gameButtons[x-i][y-i].setBackground(Color.red);
        }
        Arrays.fill(c,0);
        for (int i = 0; i < 5; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.white) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.white) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.white) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.white) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.white) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.white) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.white) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.white) c[7]++;
        }
        for (int i = 0; i < 5; i++) {
            if (c[0] == 4) if (gameButtons[x][y-i].getBackground() != Color.white && gameButtons[x][y-i].getBackground() != Color.black) gameButtons[x][y-i].setBackground(Color.red);
            if (c[1] == 4) if (gameButtons[x+i][y-i].getBackground() != Color.white && gameButtons[x+i][y-i].getBackground() != Color.black) gameButtons[x+i][y-i].setBackground(Color.red);
            if (c[2] == 4) if (gameButtons[x+i][y].getBackground() != Color.white && gameButtons[x+i][y].getBackground() != Color.black) gameButtons[x+i][y].setBackground(Color.red);
            if (c[3] == 4) if (gameButtons[x+i][y+i].getBackground() != Color.white && gameButtons[x+i][y+i].getBackground() != Color.black) gameButtons[x+i][y+i].setBackground(Color.red);
            if (c[4] == 4) if (gameButtons[x][y+i].getBackground() != Color.white && gameButtons[x][y+i].getBackground() != Color.black) gameButtons[x][y+i].setBackground(Color.red);
            if (c[5] == 4) if (gameButtons[x-i][y+i].getBackground() != Color.white && gameButtons[x-i][y+i].getBackground() != Color.black) gameButtons[x-i][y+i].setBackground(Color.red);
            if (c[6] == 4) if (gameButtons[x-i][y].getBackground() != Color.white && gameButtons[x-i][y].getBackground() != Color.black) gameButtons[x-i][y].setBackground(Color.red);
            if (c[7] == 4) if (gameButtons[x-i][y-i].getBackground() != Color.white && gameButtons[x-i][y-i].getBackground() != Color.black) gameButtons[x-i][y-i].setBackground(Color.red);
        }
    }
    // highlights 3 in a row warning for ai
    public static void highlightThree(int x, int y) {
        Arrays.fill(c,0);
        for (int i = 0; i <= 3; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.black) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.black) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.black) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.black) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.black) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.black) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.black) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.black) c[7]++;
            //
            if (gameButtons[x][y-i].getBackground() == Color.white) c[0]--;
            if (gameButtons[x+i][y-i].getBackground() == Color.white) c[1]--;
            if (gameButtons[x+i][y].getBackground() == Color.white) c[2]--;
            if (gameButtons[x+i][y+i].getBackground() == Color.white) c[3]--;
            if (gameButtons[x][y+i].getBackground() == Color.white) c[4]--;
            if (gameButtons[x-i][y+i].getBackground() == Color.white) c[5]--;
            if (gameButtons[x-i][y].getBackground() == Color.white) c[6]--;
            if (gameButtons[x-i][y-i].getBackground() == Color.white) c[7]--;
        }
        for (int i = 0; i <= 3; i++) {
            if (c[0] == 3) if (gameButtons[x][y-i].getBackground() != Color.white && gameButtons[x][y-i].getBackground() != Color.black) gameButtons[x][y-i].setBackground(Color.yellow);
            if (c[1] == 3) if (gameButtons[x+i][y-i].getBackground() != Color.white && gameButtons[x+i][y-i].getBackground() != Color.black) gameButtons[x+i][y-i].setBackground(Color.yellow);
            if (c[2] == 3) if (gameButtons[x+i][y].getBackground() != Color.white && gameButtons[x+i][y].getBackground() != Color.black) gameButtons[x+i][y].setBackground(Color.yellow);
            if (c[3] == 3) if (gameButtons[x+i][y+i].getBackground() != Color.white && gameButtons[x+i][y+i].getBackground() != Color.black) gameButtons[x+i][y+i].setBackground(Color.yellow);
            if (c[4] == 3) if (gameButtons[x][y+i].getBackground() != Color.white && gameButtons[x][y+i].getBackground() != Color.black) gameButtons[x][y+i].setBackground(Color.yellow);
            if (c[5] == 3) if (gameButtons[x-i][y+i].getBackground() != Color.white && gameButtons[x-i][y+i].getBackground() != Color.black) gameButtons[x-i][y+i].setBackground(Color.yellow);
            if (c[6] == 3) if (gameButtons[x-i][y].getBackground() != Color.white && gameButtons[x-i][y].getBackground() != Color.black) gameButtons[x-i][y].setBackground(Color.yellow);
            if (c[7] == 3) if (gameButtons[x-i][y-i].getBackground() != Color.white && gameButtons[x-i][y-i].getBackground() != Color.black) gameButtons[x-i][y-i].setBackground(Color.yellow);
        }
        Arrays.fill(c,0);
        for (int i = 0; i <= 3; i++) {
            if (gameButtons[x][y-i].getBackground() == Color.white) c[0]++;
            if (gameButtons[x+i][y-i].getBackground() == Color.white) c[1]++;
            if (gameButtons[x+i][y].getBackground() == Color.white) c[2]++;
            if (gameButtons[x+i][y+i].getBackground() == Color.white) c[3]++;
            if (gameButtons[x][y+i].getBackground() == Color.white) c[4]++;
            if (gameButtons[x-i][y+i].getBackground() == Color.white) c[5]++;
            if (gameButtons[x-i][y].getBackground() == Color.white) c[6]++;
            if (gameButtons[x-i][y-i].getBackground() == Color.white) c[7]++;
            //
            if (gameButtons[x][y-i].getBackground() == Color.black) c[0]--;
            if (gameButtons[x+i][y-i].getBackground() == Color.black) c[1]--;
            if (gameButtons[x+i][y].getBackground() == Color.black) c[2]--;
            if (gameButtons[x+i][y+i].getBackground() == Color.black) c[3]--;
            if (gameButtons[x][y+i].getBackground() == Color.black) c[4]--;
            if (gameButtons[x-i][y+i].getBackground() == Color.black) c[5]--;
            if (gameButtons[x-i][y].getBackground() == Color.black) c[6]--;
            if (gameButtons[x-i][y-i].getBackground() == Color.black) c[7]--;
        }
        for (int i = 0; i <= 3; i++) {
            if (c[0] == 3) if (gameButtons[x][y-i].getBackground() != Color.white && gameButtons[x][y-i].getBackground() != Color.black) gameButtons[x][y-i].setBackground(Color.yellow);
            if (c[1] == 3) if (gameButtons[x+i][y-i].getBackground() != Color.white && gameButtons[x+i][y-i].getBackground() != Color.black) gameButtons[x+i][y-i].setBackground(Color.yellow);
            if (c[2] == 3) if (gameButtons[x+i][y].getBackground() != Color.white && gameButtons[x+i][y].getBackground() != Color.black) gameButtons[x+i][y].setBackground(Color.yellow);
            if (c[3] == 3) if (gameButtons[x+i][y+i].getBackground() != Color.white && gameButtons[x+i][y+i].getBackground() != Color.black) gameButtons[x+i][y+i].setBackground(Color.yellow);
            if (c[4] == 3) if (gameButtons[x][y+i].getBackground() != Color.white && gameButtons[x][y+i].getBackground() != Color.black) gameButtons[x][y+i].setBackground(Color.yellow);
            if (c[5] == 3) if (gameButtons[x-i][y+i].getBackground() != Color.white && gameButtons[x-i][y+i].getBackground() != Color.black) gameButtons[x-i][y+i].setBackground(Color.yellow);
            if (c[6] == 3) if (gameButtons[x-i][y].getBackground() != Color.white && gameButtons[x-i][y].getBackground() != Color.black) gameButtons[x-i][y].setBackground(Color.yellow);
            if (c[7] == 3) if (gameButtons[x-i][y-i].getBackground() != Color.white && gameButtons[x-i][y-i].getBackground() != Color.black) gameButtons[x-i][y-i].setBackground(Color.yellow);
        }
    }
    // ai movement logic
    public static int[] aiMove() {
        for (int row = 5; row < size+5; row++) {
            for (int col = 5; col < size+5; col++) {
                if (gameButtons[row][col].getBackground() == Color.red) {
                    temp1[0] = row;
                    temp1[1] = col;
                    return temp1;
                }
            }
        }
        for (int row = 5; row < size+5; row++) {
            for (int col = 5; col < size+5; col++) {
                if (gameButtons[row][col].getBackground() == Color.yellow) {
                    temp1[0] = row;
                    temp1[1] = col;
                    return temp1;
                }
            }
        }
        for (int row = 6; row < size+4; row++) {
            for (int col = 6; col < size+4; col++) {
                if (gameButtons[row][col].getBackground() == Color.black || gameButtons[row][col].getBackground() == Color.white) {
                    if (gameButtons[row][col-1].getBackground() != Color.black && gameButtons[row][col-1].getBackground() != Color.white) {
                        temp1[0] = row;
                        temp1[1] = col-1;
                        return temp1;
                    }
                    if (gameButtons[row+1][col-1].getBackground() != Color.black && gameButtons[row+1][col-1].getBackground() != Color.white) {
                        temp1[0] = row+1;
                        temp1[1] = col-1;
                        return temp1;
                    }
                    if (gameButtons[row+1][col].getBackground() != Color.black && gameButtons[row+1][col].getBackground() != Color.white) {
                        temp1[0] = row+1;
                        temp1[1] = col;
                        return temp1;
                    }
                    if (gameButtons[row+1][col+1].getBackground() != Color.black && gameButtons[row+1][col+1].getBackground() != Color.white) {
                        temp1[0] = row+1;
                        temp1[1] = col+1;
                        return temp1;
                    }
                    if (gameButtons[row][col+1].getBackground() != Color.black && gameButtons[row][col+1].getBackground() != Color.white) {
                        temp1[0] = row;
                        temp1[1] = col+1;
                        return temp1;
                    }
                    if (gameButtons[row-1][col+1].getBackground() != Color.black && gameButtons[row-1][col+1].getBackground() != Color.white) {
                        temp1[0] = row-1;
                        temp1[1] = col+1;
                        return temp1;
                    }
                    if (gameButtons[row-1][col].getBackground() != Color.black && gameButtons[row-1][col].getBackground() != Color.white) {
                        temp1[0] = row-1;
                        temp1[1] = col;
                        return temp1;
                    }
                    if (gameButtons[row-1][col-1].getBackground() != Color.black && gameButtons[row-1][col-1].getBackground() != Color.white) {
                        temp1[0] = row-1;
                        temp1[1] = col-1;
                        return temp1;
                    }
                }
            }
        }
        return temp1;
    }
}