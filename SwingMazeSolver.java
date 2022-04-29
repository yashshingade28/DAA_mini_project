import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.*;

public class SwingMazeSolver extends JFrame implements ActionListener {
    JButton submit, reset, presets, preset1, preset2, preset3, empty, close;
    JDialog pathError;
    JLabel messageToUser = new JLabel();
    JLabel mazeSolverDescription;
    JFrame presetWindow;
    Color background = new Color(50, 50, 63);
    static int x = 0, xend;
    static int y = 0, yend;
    static boolean mouseHeld = false;
    private static final long serialVersionUID = 1L;

    SwingMazeSolver() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Backtracking Maze Solver");
        setLocation(0, 0);
        setSize(1080, 1920);
        setLayout(null);
        createPresetWindow();
        addButtons(this);
        addTiles(this);
        setVisible(true);
    }

    public void createPresetWindow() {
        presetWindow = new JFrame("Maze Presets");
        presetWindow.setSize(400, 470);
        presetWindow.setLayout(null);
        addPresetButtons(presetWindow);
    }

    pictureclass[][] tileset = new pictureclass[N][N];

    public void addTiles(JFrame swingMazeSolver) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tileset[i][j] = new pictureclass(i, j);
                swingMazeSolver.add(tileset[i][j]);
            }
        }
        tileset[0][0].setBackground(Color.RED);
        tileset[N - 1][N - 1].setBackground(Color.YELLOW);
        swingMazeSolver.add(new pictureclass(410, 20, 620, 620));
    }

    public void redrawTiles() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (maze[i][j] == 0) {
                    tileset[j][i].setBackground(Color.DARK_GRAY);
                } else {
                    tileset[j][i].setBackground(Color.LIGHT_GRAY);
                }
            }
        }
        tileset[0][0].setBackground(Color.MAGENTA);
        tileset[N - 1][N - 1].setBackground(Color.PINK);
        repaint();
    }

    class pictureclass extends JPanel {
        int id1, id2;
        private static final long serialVersionUID = 1L;

        public pictureclass(int i, int j) {
            id1 = (i);
            id2 = (j);
            setBounds(420 + (60 * i), 30 + (60 * j), 60, 60);
            if (maze[i][j] == 0) {
                setBackground(Color.DARK_GRAY);
            } else {
                setBackground(Color.LIGHT_GRAY);
            }
            this.addMouseListener(new MouseEventListener());
        }

        pictureclass(int x, int y, int width, int height) {
            setBounds(x, y, width, height);
            setBackground(background);
        }

        public int getid1() {
            return id1;
        }

        public int getid2() {
            return id2;
        }
    }


    public void addButtons(JFrame swingMazeSolver) {
        submit = new JButton("Submit");
        submit.addActionListener(this);
        submit.setBounds(50, 20 + 280, 300, 75);
        submit.setFont(submit.getFont().deriveFont(35.0f));
        swingMazeSolver.add(submit);

        reset = new JButton("Reset");
        reset.addActionListener(this);
        reset.setBounds(50, 100 + 280, 300, 75);
        reset.setFont(reset.getFont().deriveFont(35.0f));
        swingMazeSolver.add(reset);

        presets = new JButton("Presets");
        presets.addActionListener(this);
        presets.setBounds(50, 180 + 280, 300, 75);
        presets.setFont(presets.getFont().deriveFont(35.0f));
        swingMazeSolver.add(presets);
    }

    public void addPresetButtons(JFrame presetWindow) {
        preset1 = new JButton("Preset 1");
        preset1.addActionListener(this);
        preset1.setBounds(50, 20, 300, 75);
        preset1.setFont(preset1.getFont().deriveFont(35.0f));
        presetWindow.add(preset1);

        preset2 = new JButton("Preset 2");
        preset2.addActionListener(this);
        preset2.setBounds(50, 100, 300, 75);
        preset2.setFont(preset2.getFont().deriveFont(35.0f));
        presetWindow.add(preset2);

        preset3 = new JButton("Preset 3");
        preset3.addActionListener(this);
        preset3.setBounds(50, 180, 300, 75);
        preset3.setFont(preset3.getFont().deriveFont(35.0f));
        presetWindow.add(preset3);

        empty = new JButton("Empty Maze");
        empty.addActionListener(this);
        empty.setBounds(50, 260, 300, 75);
        empty.setFont(empty.getFont().deriveFont(35.0f));
        presetWindow.add(empty);

        close = new JButton("Close");
        close.addActionListener(this);
        close.setBounds(125, 340, 150, 35);
        presetWindow.add(close);
    }

    public void addLabels(JFrame swingMazeSolver, String messageToUserText) {
        messageToUser.setBounds(60, 225, 300, 100);
        messageToUser.setText(messageToUserText);
        messageToUser.setForeground(Color.RED);
        messageToUser.setFont(messageToUser.getFont().deriveFont(17.0f));
        swingMazeSolver.add(messageToUser);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            boolean vis[][] = new boolean[N][N];
            boolean isPath = solveMaze(maze, solution, x, y, "down", vis);
            printSolution(isPath);
        }
        if (e.getSource() == empty || e.getSource() == reset) {
            fillMaze(1);
            redrawTiles();
            messageToUser.setText("");
        }
        if (e.getSource() == presets) {
            presetWindow.setVisible(true);
        }
        if (e.getSource() == preset1 || e.getSource() == preset2 || e.getSource() == preset3) {
            solution = new int[N][N];
            if (e.getSource() == preset1) {
                int newMaze[][] = { { 1, 0, 1, 0, 1, 0, 1, 1, 1, 1 }, { 1, 1, 1, 1, 1, 1, 1, 0, 0, 0 },
                    { 0, 0, 0, 0, 1, 0, 0, 0, 1, 0 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    { 1, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
                    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, { 1, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
                    { 1, 0, 1, 0, 1, 1, 0, 1, 0, 0 }, { 1, 0, 0, 0, 0, 1, 0, 1, 1, 1 },
                    { 1, 1, 1, 1, 0, 1, 1, 1, 0, 1 }
                };
                maze = newMaze;
            }
            if (e.getSource() == preset2) {
                int newMaze[][] = { { 1, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, { 1, 0, 1, 0, 1, 0, 0, 0, 0, 1 },
                    { 1, 0, 1, 0, 1, 0, 1, 1, 1, 1 }, { 1, 0, 1, 1, 1, 0, 1, 0, 0, 1 },
                    { 1, 0, 0, 0, 1, 0, 1, 0, 1, 1 },
                    { 1, 1, 1, 1, 1, 0, 1, 0, 0, 0 }, { 0, 0, 1, 0, 1, 0, 1, 1, 1, 1 },
                    { 1, 1, 1, 0, 1, 1, 1, 0, 0, 1 }, { 0, 0, 1, 0, 1, 0, 0, 1, 0, 1 },
                    { 1, 1, 1, 0, 1, 1, 1, 1, 0, 1 }
                };
                maze = newMaze;
            }
            if (e.getSource() == preset3) {
                Random rand = new Random();
                int newMaze[][] = new int[N][N];
                for (int i = 0 ; i < N ; i++) {
                    for (int j = 0 ; j < N ; j++) {
                        newMaze[i][j] = rand.nextBoolean() ? 1 : 0;
                    }
                }
                maze = newMaze;
            }
            redrawTiles();
        }
        if (e.getSource() == close) {
            presetWindow.setVisible(false);
        }
    }

    public void addWall(pictureclass pressedButton) {
        maze[pressedButton.getid2()][pressedButton.getid1()] = 0;
    }

    public class MouseEventListener implements MouseListener {
        public void mouseClicked(MouseEvent arg0) {

        }

        public void mouseEntered(MouseEvent arg0) {
            if (mouseHeld) {
                pictureclass pressedButton = (pictureclass) arg0.getSource();
                pressedButton.setBackground(Color.DARK_GRAY);
                addWall(pressedButton);
            }
        }

        public void mouseExited(MouseEvent arg0) {

        }

        public void mousePressed(MouseEvent arg0) {
            mouseHeld = true;
        }

        public void mouseReleased(MouseEvent arg0) {
            mouseHeld = false;
        }
    }

    public void drawThoughts() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (solution[i][j] == 1) {
                    tileset[j][i].setBackground(Color.GRAY);
                    repaint();
                }
            }
        }
    }

    static void fillMaze(int value) {
        solution = new int[N][N];
        maze = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                maze[i][j] = value;
            }
        }
    }
    static int solution[][];
    static int visited[][];
    static int N = 10;
    static int maze[][];
    static String direction;

    boolean safe(int maze[][], int x, int y) {
        if (x >= 0 && y >= 0 && x < N && y < N && maze[x][y] != 0) {

            return true;
        } else {
            return false;
        }
    }

    boolean solveMaze(int maze[][], int solution[][], int x, int y, String direction, boolean vis[][])
    throws StackOverflowError {
        try {
            if (x == N - 1 && y == N - 1 && safe(maze, x, y)) {
                solution[x][y] = 1;
                return true;
            }
            System.out.println(x + " " + y + " " + direction);
            if (safe(maze, x, y) && !vis[x][y]) {
                solution[x][y] = 1;
                if (!direction.equals("up")) {
                    vis[x][y] = true;
                    if (solveMaze(maze, solution, x + 1, y, "down", vis)) {
                        return true;
                    } else {
                        vis[x][y] = false;
                    }
                }
                if (!direction.equals("left")) {
                    vis[x][y] = true;
                    if (solveMaze(maze, solution, x, y + 1, "right", vis)) {
                        return true;
                    } else {
                        vis[x][y] = false;
                    }
                }
                if (!direction.equals("down")) {
                    vis[x][y] = true;
                    if (solveMaze(maze, solution, x - 1, y, "up", vis)) {
                        return true;
                    } else {
                        vis[x][y] = false;
                    }
                }
                if (!direction.equals("right")) {
                    vis[x][y] = true;
                    if (solveMaze(maze, solution, x, y - 1, "left", vis)) {
                        return true;
                    } else {
                        vis[x][y] = false;
                    }
                }
                solution[x][y] = 0;
                return false;
            }
            return false;
        } catch (StackOverflowError e) {
            fillMaze(0);
            addLabels(this, "NO PATH FOUND, PLEASE RESET!");
            return false;
        }
    }

    void printSolution(boolean isPath) {
        if (isPath == true) {
            drawThoughts();
        } else {
            addLabels(this, "START BLOCKED, PLEASE RESET!");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        fillMaze(1);
        new SwingMazeSolver();
    }
}
