// Imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

// GUI - Class (public)
public class GUI implements ActionListener {
    // > GUI: Fields
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openFileMenuItem;
    private JMenu commandsMenu;
    private JMenuItem originalMenuItem;
    private JMenuItem grayscaleMenuItem;
    private JMenuItem transposeMenuItem;
    private JMenuItem zoomInMenuItem;
    private JMenuItem rotateLeftMenuItem;
    private JMenuItem rotateRightMenuItem;
    private JMenuItem blurMenuItem;
    private Canvas canvas;

    // > GUI: Default Constructor
    public GUI() {
        // >> GUI: Default Constructor: frame - JFrame
        frame = new JFrame("Drawing Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        // >> GUI: Default Constructor: menuBar - JMenuBar
        menuBar = new JMenuBar();

        // >> GUI: Default Constructor: fileMenu - JMenu
        fileMenu = new JMenu("File");

        // >> GUI: Default Constructor: openFileMenuItem - JMenuItem
        openFileMenuItem = new JMenuItem("Open File");
        openFileMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: commandsMenu - JMenu
        commandsMenu = new JMenu("Commands");

        // >> GUI: Default Constructor: originalMenuItem - JMenuItem
        originalMenuItem = new JMenuItem("Original");
        originalMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: grayscaleMenuItem - JMenuItem
        grayscaleMenuItem = new JMenuItem("Grayscale");
        grayscaleMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: transposeMenuItem - JMenuItem
        transposeMenuItem = new JMenuItem("Transpose");
        transposeMenuItem.addActionListener(this); 

        // >> GUI: Default Constructor: zoomInMenuItem - JMenuItem
        zoomInMenuItem = new JMenuItem("Zoom In");
        zoomInMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: rotateLeftMenuItem - JMenuItem
        rotateLeftMenuItem = new JMenuItem("Rotate Left");
        rotateLeftMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: rotateRightMenuItem - JMenuItem
        rotateRightMenuItem = new JMenuItem("Rotate Right");
        rotateRightMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: blurMenuItem - JMenuItem
        blurMenuItem = new JMenuItem("Blur");
        blurMenuItem.addActionListener(this);

        // >> GUI: Default Constructor: canvas - Canvas
        try {
            canvas = new Canvas(ImageIO.read(new File("shrek.jpg")));
        } catch(IOException e) {
            e.printStackTrace();
        }  

        // >> GUI: Default Constructor: "basically setting up menu stuff"
        frame.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(openFileMenuItem);
        menuBar.add(commandsMenu);
        commandsMenu.add(originalMenuItem);
        commandsMenu.add(grayscaleMenuItem);
        commandsMenu.add(transposeMenuItem);
        commandsMenu.add(zoomInMenuItem);
        commandsMenu.add(rotateLeftMenuItem);
        commandsMenu.add(rotateRightMenuItem);
        commandsMenu.add(blurMenuItem);

        // >> GUI: Default Constructor: "finalizing"
        frame.add(canvas);
        frame.setVisible(true);
    }

    // > GUI: Other Methods
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == openFileMenuItem) {

            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG, GIF Formats", "png", "jpg", "gif");
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    canvas.setImage(ImageIO.read(new File(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if(event.getSource() == originalMenuItem) {
            canvas.original();
        } 
        if(event.getSource() == grayscaleMenuItem) {
            canvas.grayscale();
        }
        if(event.getSource() == transposeMenuItem) {
            canvas.transpose();
        }
        if(event.getSource() == zoomInMenuItem) {
            canvas.zoomIn();
        }
        if(event.getSource() == rotateLeftMenuItem) {
            canvas.rotateLeft();
        }
        if(event.getSource() == rotateRightMenuItem) {
            canvas.rotateRight();
        }
        if(event.getSource() == blurMenuItem) {
            canvas.blur();
        }
    }

    //  > GUI: Canvas - Class (private)
    class Canvas extends JPanel {
        // >> GUI: Canvas: Fields
        private BufferedImage img;
        private int originalImgWidth;
        private int originalImgHeight;
        private Color[][] originalRGBArray;
        private Color[][] currentRGBArray;

        // >> GUI: Canvas: Default Constructor
        public Canvas(BufferedImage image) {
            img = image;
            originalImgWidth = img.getWidth();
            originalImgHeight = img.getHeight();
            originalRGBArray = getRGBArray();
            currentRGBArray = originalRGBArray;
            repaint();
        }

        // >> GUI: Canvas: Other Methods
        public void setImage(BufferedImage image) {
            img = image;
            originalImgWidth = img.getWidth();
            originalImgHeight = img.getHeight();
            originalRGBArray = getRGBArray();
            currentRGBArray = originalRGBArray;
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, canvas);
        }

        public Color[][] getRGBArray() {
            Color[][] colorArray = new Color[img.getWidth()][img.getHeight()];
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    colorArray[x][y] = new Color(img.getRGB(x, y));
                }
            }
            return colorArray;
        }

        public void setRGBArray(Color[][] rgbArray) {
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    img.setRGB(x, y, rgbArray[x][y].getRGB());
                }
            }
        }

        public void original() {
            img = new BufferedImage(originalImgWidth, originalImgHeight, img.getType());
            currentRGBArray = originalRGBArray;
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    img.setRGB(x, y, currentRGBArray[x][y].getRGB());
                }
            }
            repaint();
        }

        public void grayscale() {
            Color[][] grayRGBArray = new Color[img.getWidth()][img.getHeight()];
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    Color c = currentRGBArray[x][y];
                    int avg = ((c.getRed() + c.getGreen() + c.getBlue()) / 3);
                    grayRGBArray[x][y] = new Color(avg, avg, avg);
                }
            }
            
            currentRGBArray = grayRGBArray;
            setRGBArray(currentRGBArray);
            repaint();
        }

        public void transpose() {
            Color[][] transposedRGBArray = new Color[img.getWidth()][img.getHeight()];
            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {
                    transposedRGBArray[x][y] = currentRGBArray[img.getWidth()-x-1][img.getHeight()-y-1];
                }
            }

            currentRGBArray = transposedRGBArray;
            setRGBArray(currentRGBArray);
            repaint();
        }

        public void zoomIn() {
            int tempWidth = img.getWidth();
            int tempHeight = img.getHeight();
            img = new BufferedImage(img.getWidth()*2, img.getHeight()*2, img.getType());
            Color[][] zoomedInRGBArray = new Color[img.getWidth()][img.getHeight()];
            for(int x = 0; x < tempWidth; x++) {
                for(int y = 0; y < tempHeight; y++) {
                    Color c = currentRGBArray[x][y];
                    zoomedInRGBArray[x*2][y*2] = c;
                    zoomedInRGBArray[x*2+1][y*2] = c;
                    zoomedInRGBArray[x*2][y*2+1] = c;
                    zoomedInRGBArray[x*2+1][y*2+1] = c;
                }
            }

            currentRGBArray = zoomedInRGBArray;
            setRGBArray(currentRGBArray);
            repaint();
        }

        public void rotateLeft() {
            rotateRight();
            rotateRight();
            rotateRight();
        }

        public void rotateRight() {
            int tempWidth = img.getWidth();
            int tempHeight = img.getHeight();
            img = new BufferedImage(img.getHeight(), img.getWidth(), img.getType());
            Color[][] rotatedRightRGBArray = new Color[img.getWidth()][img.getHeight()];
            for(int x = 0; x < tempWidth; x++) {
                for(int y = 0; y < tempHeight; y++) {
                    rotatedRightRGBArray[y][tempWidth-x-1] = currentRGBArray[x][y];
                }
            }
            currentRGBArray = rotatedRightRGBArray;
            setRGBArray(currentRGBArray);
            repaint();
        }

        public void blur() {
            Color[][] blurredRGBArray = new Color[img.getWidth()][img.getHeight()];

            for(int x = 0; x < img.getWidth(); x++) {
                for(int y = 0; y < img.getHeight(); y++) {

                    if( ((x > 0) && (x < img.getWidth()-1)) && ((y > 0) && (y < img.getHeight()-1)) ) {
                        Color c1 = currentRGBArray[x-1][y-1];
                        Color c2 = currentRGBArray[x][y-1];
                        Color c3 = currentRGBArray[x+1][y-1];
                        Color c4 = currentRGBArray[x-1][y];
                        Color c5 = currentRGBArray[x][y];
                        Color c6 = currentRGBArray[x+1][y];
                        Color c7 = currentRGBArray[x-1][y+1];
                        Color c8 = currentRGBArray[x][y+1];
                        Color c9 = currentRGBArray[x+1][y+1];
                        int avgRed = ( (c1.getRed() + c2.getRed() + c3.getRed() + c4.getRed() + c5.getRed() + c6.getRed() + c7.getRed() + c8.getRed() + c9.getRed()) / 9);
                        int avgBlue = ( (c1.getBlue() + c2.getBlue() + c3.getBlue() + c4.getBlue() + c5.getBlue() + c6.getBlue() + c7.getBlue() + c8.getBlue() + c9.getBlue()) / 9);
                        int avgGreen = ( (c1.getGreen() + c2.getGreen() + c3.getGreen() + c4.getGreen() + c5.getGreen() + c6.getGreen() + c7.getGreen() + c8.getGreen() + c9.getGreen()) / 9);
                        blurredRGBArray[x][y] = new Color(avgRed, avgBlue, avgGreen);
                    } else {
                        blurredRGBArray[x][y] = currentRGBArray[x][y];
                    }
                }
            }

            currentRGBArray = blurredRGBArray;
            setRGBArray(currentRGBArray);
            repaint();
        }
    }

    // > Main
    public static void main(String[] args) {
        GUI gui = new GUI();
    }
}