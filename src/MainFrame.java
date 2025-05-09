import projectMandelbrot.gui.AreaSelector;
import projectMandelbrot.gui.FractalPainter;
import projectMandelbrot.gui.MakerColor;
import projectMandelbrot.math.DoublePoint;
import projectMandelbrot.math.Pair;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import static java.lang.System.exit;

public class MainFrame extends JFrame {

    private Stack<Pair> pointsStack = new Stack<>();
    private final AreaSelector selector = new AreaSelector();
    private final MakerColor makerColor = new MakerColor();
    private final FractalPainter fPainter = new FractalPainter(makerColor,-2.0, 1.0, -1.0, 1.0);
    private final JPanel mainPanel = new JPanel() {
        @Override
        public void paint(Graphics g) {
            fPainter.paint(g);
        }
    };

    /**
     * Метод для действий отмены
     */
    private void undoAction() {
        if (!pointsStack.isEmpty()) {
            pointsStack.pop();

            if (!pointsStack.isEmpty()) {
                makerColor.coef=-1;
//                Pair lastPair = pointsStack.peek();
//                DoublePoint[] lastRect = {lastPair.getP1(), lastPair.getP2()};
//                var xMin =(Math.min(lastRect[0].x, lastRect[1].x));
//                var xMax = (Math.max(lastRect[0].x, lastRect[1].x));
//                var yMin = (Math.min(lastRect[0].y, lastRect[1].y));
//                var yMax = (Math.max(lastRect[0].y, lastRect[1].y));

                double r[]= normal();
                fPainter.updateCoordinates(r[0], r[1], r[2], r[3],0);
                mainPanel.repaint();
            }
            else {
                System.out.println("Нет действий для отмены.");
                pointsStack.push(new Pair(new DoublePoint(-2.,-1.), new DoublePoint(1.,1.)));
            }
        } else {
            System.out.println("Нет действий для отмены.");
            pointsStack.push(new Pair(new DoublePoint(-2.,-1.), new DoublePoint(1.,1.)));
        }
    }

    public MainFrame() {
        pointsStack.push(new Pair(new DoublePoint(-2.,-1.), new DoublePoint(1.,1.)));
        createMenu();
        mainPanel.setBackground(Color.WHITE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        add(mainPanel);
        selector.setColor(Color.BLUE);

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                 double points[]= normal();

                fPainter.updateCoordinates(points[0], points[1], points[2], points[3], 1);
                mainPanel.repaint();
            }
        });


        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                selector.setGraphics(mainPanel.getGraphics());
                fPainter.setWidth(mainPanel.getWidth());
                fPainter.setHeight(mainPanel.getHeight());
            }
        });

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selector.clearSelection();
                super.mousePressed(e);
                selector.addPoint(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (selector.getRect() != null) {
                        makerColor.coef = 1;
                        selector.paint();

                        var xMin = fPainter.getConverter().xScr2Crt(selector.getRect().getStartPoint().x);
                        var xMax = fPainter.getConverter().xScr2Crt(selector.getRect().getWidth() + selector.getRect().getStartPoint().x);
                        var yMin = fPainter.getConverter().yScr2Crt(selector.getRect().getStartPoint().y);
                        var yMax = fPainter.getConverter().yScr2Crt(selector.getRect().getStartPoint().y + selector.getRect().getHeigth());

                       // System.out.println(xMin + " " + xMax + " " + yMin + " " + yMax);
                        double panelAspectRatio = (double) mainPanel.getWidth() / mainPanel.getHeight();
                        double rectWidth = xMax - xMin;
                        double rectHeight = yMax - yMin;
                        double rectAspectRatio = rectWidth / rectHeight;

                        if (rectAspectRatio > panelAspectRatio) {
                            double newHeight = rectWidth / panelAspectRatio;
                            double heightDelta = (newHeight - rectHeight) / 2;
                            yMin -= heightDelta;
                            yMax += heightDelta;
                        } else if (rectAspectRatio < panelAspectRatio) {
                            double newWidth = rectHeight * panelAspectRatio;
                            double widthDelta = (newWidth - rectWidth) / 2;
                            xMin -= widthDelta;
                            xMax += widthDelta;
                        }

                       // System.out.println(xMin + " " + xMax + " " + yMin + " " + yMax);
                        double[] point = {xMin, xMax, yMin, yMax};
                        fPainter.updateCoordinates(point[0], point[1], point[2], point[3], 0);
                        pointsStack.push(new Pair(new DoublePoint(point[1], point[3]), new DoublePoint(point[0], point[2])));
                        mainPanel.repaint();
                    }
                    selector.clearSelection();
                }

                if (SwingUtilities.isRightMouseButton(e)){
                    if (selector.getRect() != null) {
                        int panelWidth = mainPanel.getWidth();
                        int panelHeight = mainPanel.getHeight();
                        int clickX = e.getX();
                        int clickY = e.getY();
                        double shiftX = 0;
                        double shiftY = 0;
                        double shiftFactor = 0.2;

                        if (clickX < panelWidth / 2) { shiftX = shiftFactor;
                        } else if (clickX > panelWidth * 2 / 3) { shiftX = -shiftFactor;}
                        if(clickY < panelHeight / 2){ shiftY = -shiftFactor;
                        } else if(clickY > panelHeight * 2 / 3) { shiftY = +shiftFactor;
                        }
                        double currentXMin = fPainter.getConverter().getXMin();
                        double currentXMax = fPainter.getConverter().getXMax();
                        double currentYMin = fPainter.getConverter().getYMin();
                        double currentYMax = fPainter.getConverter().getYMax();

                        double xShiftAmount = (currentXMax - currentXMin) * shiftX;
                        double yShiftAmount = (currentYMax - currentYMin) * shiftY;

                        double xMin = currentXMin + xShiftAmount;
                        double xMax = currentXMax + xShiftAmount;
                        double yMin = currentYMin + yShiftAmount;
                        double yMax = currentYMax + yShiftAmount;
                        fPainter.updateCoordinates(xMin, xMax, yMin, yMax,1);
                        mainPanel.repaint();
                        pointsStack.push(new Pair(new DoublePoint(xMax, yMax), new DoublePoint(xMin, yMin)));
                    }
                }
            }
        });

        mainPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selector.paint();
                    selector.addPoint(e.getPoint());
                    selector.paint();
                }
                if (SwingUtilities.isRightMouseButton(e)){
                    selector.addPoint(e.getPoint());
                    mainPanel.repaint();
                }
            }
        });

        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                    undoAction();
                }
            }
        });
    }

    /**
     * Метод для создания меню
     */
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Открыть файл");
        JMenuItem saveFile = new JMenuItem("Cохранить файл");
        JMenuItem saveItem = new JMenuItem("Сохранить картинку");
        fileMenu.add(openItem);
        fileMenu.add(saveFile);
        fileMenu.add(saveItem);

        JMenu colorMenu = new JMenu("Цвет");
        JMenuItem color2Item = new JMenuItem("Цвет 1");
        JMenuItem color1Item = new JMenuItem("Цвет 2");
        colorMenu.add(color2Item);
        colorMenu.add(color1Item);

        JMenu actionsMenu = new JMenu("Действия");
        JMenuItem actionStart = new JMenuItem("Стартовая картинка");
        JMenuItem actionBack = new JMenuItem("Отменить действие");
        JMenuItem actionExit = new JMenuItem("Выход");
        actionsMenu.add(actionStart);
        actionsMenu.add(actionBack);
        actionsMenu.add(actionExit);

        Font font = new Font("Arial", Font.PLAIN, 30);
        fileMenu.setFont(font);
        colorMenu.setFont(font);
        actionsMenu.setFont(font);
        openItem.setFont(font);
        saveFile.setFont(font);
        saveItem.setFont(font);
        color1Item.setFont(font);
        color2Item.setFont(font);
        actionBack.setFont(font);
        actionExit.setFont(font);
        actionStart.setFont(font);

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFrameAsImage();
            }
        });
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { openFile();}
        });
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { saveFile();}
        });
        color1Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { color1();}
        });
        color2Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { color2();}
        });
        actionBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { undoAction() ;}
        });
        actionExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { exit(0) ;}
        });
        actionStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { actionStart() ;}
        });


        menuBar.add(fileMenu);
        menuBar.add(colorMenu);
        menuBar.add(actionsMenu);
        setJMenuBar(menuBar);
    }
    private double[] normal() {
        int newWidth = mainPanel.getWidth();
        int newHeight = mainPanel.getHeight();

        double currentXMin = pointsStack.getLast().p1.x;
        double currentXMax = pointsStack.getLast().p2.x;
        double currentYMin = pointsStack.getLast().p1.y;
        double currentYMax = pointsStack.getLast().p2.y;

        double aspectRatio = (currentXMax - currentXMin) / (currentYMax - currentYMin);
        double newAspectRatio = (double) newWidth / newHeight;
        if (newAspectRatio > aspectRatio) {
            double deltaX = ((currentYMax - currentYMin) * newAspectRatio - (currentXMax - currentXMin)) / 2;
            currentXMin -= deltaX;
            currentXMax += deltaX;
        } else {
            double deltaY = ((currentXMax - currentXMin) / newAspectRatio - (currentYMax - currentYMin)) / 2;
            currentYMin -= deltaY;
            currentYMax += deltaY;
        }

        return new double[]{currentXMin,currentXMax,currentYMin,currentYMax};

    }

    /**
     * Метод для сохранения картинки в формате png jpg
     */
    private void saveFrameAsImage() {
        BufferedImage image = new BufferedImage(
                mainPanel.getWidth(), mainPanel.getHeight(), BufferedImage.TYPE_INT_RGB
        );

        Graphics2D graphics = image.createGraphics();
        mainPanel.paint(graphics);
        graphics.dispose();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(800, 600));

        FileNameExtensionFilter pngFilter = new FileNameExtensionFilter("PNG Image", "png");
        FileNameExtensionFilter jpegFilter = new FileNameExtensionFilter("JPEG Image", "jpg");
        fileChooser.addChoosableFileFilter(pngFilter);
        fileChooser.addChoosableFileFilter(jpegFilter);
        fileChooser.setFileFilter(jpegFilter);


        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String extension = "";
            if (fileToSave.getName().toLowerCase().endsWith(".png")) {
                extension = ".png";
            } else if (fileToSave.getName().toLowerCase().endsWith(".jpg") || fileToSave.getName().toLowerCase().endsWith(".jpeg")) {
                extension = ".jpg";
            } else {
                extension = ".jpg";
            }

            if (!fileToSave.getPath().toLowerCase().endsWith(extension)) {
                fileToSave = new File(fileToSave.getPath() + extension);
            }

            try {
                if (extension.equalsIgnoreCase(".png")) {
                    ImageIO.write(image, "png", fileToSave);
                } else {
                    ImageIO.write(image, "jpg", fileToSave);
                }
                JOptionPane.showMessageDialog(this, "Картинка успешно сохранена!", "", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла: " + ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Метод для открытия собственного файла
     */
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line = reader.readLine();
                StringTokenizer tokenizer = new StringTokenizer(line);
                double x1 = Double.parseDouble(tokenizer.nextToken());
                double y1 = Double.parseDouble(tokenizer.nextToken());
                DoublePoint point1 = new DoublePoint(x1, y1);

                line = reader.readLine();
                tokenizer = new StringTokenizer(line);
                double x2 = Double.parseDouble(tokenizer.nextToken());
                double y2 = Double.parseDouble(tokenizer.nextToken());
                DoublePoint point2 = new DoublePoint(x2, y2);

                line = reader.readLine().trim();
                makerColor.currentColor = Integer.parseInt(line);

                pointsStack.clear();
                Pair pair = new Pair(point1, point2);
                pointsStack.push(pair);

                fPainter.updateCoordinates(x1, x2, y1, y2, pointsStack.size());
                mainPanel.repaint();

                JOptionPane.showMessageDialog(this, "Файл успешно загружен!");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ошибка при чтении файла: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Метод для установки начальной картинки фрактала
     */
    private void actionStart() {
        pointsStack.clear();
        Pair pair = new Pair(new DoublePoint(-2,-1), new DoublePoint(1,1));
        pointsStack.push(pair);
        makerColor.coef=2;
        fPainter.updateCoordinates(-2, 1, -1, 1,0);
        mainPanel.repaint();
        System.out.println("Открыто начало");
    }

    /**
     * Метод для сохранения текущего изображения в файл
     */
    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false); // Отключаем фильтр "Все файлы"
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files (*.txt)", "txt"); // Фильтр для txt-файлов
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter); // Устанавливаем фильтр по умолчанию

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Проверяем наличие расширения .txt и добавляем его, если отсутствует
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt";
            }

            try {
                Pair lastPair = pointsStack.peek();
                DoublePoint[] lastRect = {lastPair.getP1(), lastPair.getP2()};

                FileWriter writer = new FileWriter(filePath);

                writer.write(lastRect[0].x + " " + lastRect[0].y + "\n");
                writer.write(lastRect[1].x + " " + lastRect[1].y + "\n");
                writer.write("" + makerColor.currentColor);
                writer.close();
                JOptionPane.showMessageDialog(this, "Файл успешно сохранен!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Метод для установки цвета1
     */
    private void color1() {
        makerColor.currentColor = 1;
        repaint();
    }
    /**
     * Метод для установки цвета2
     */
    private void color2() {
        makerColor.currentColor = 2;
        repaint();
    }

}