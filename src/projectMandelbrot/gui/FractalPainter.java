package projectMandelbrot.gui;
import projectMandelbrot.fractal.Mandelbrot;
import projectMandelbrot.math.Complex;
import projectMandelbrot.math.Converter;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Класс для рисования фрактала Мандельброта
 */
public class FractalPainter implements Painter {
    private final Mandelbrot mandelbrot = new Mandelbrot();
    private final Converter converter;
    private final MakerColor makerColor;

    /**
     * Конструктор класса FractalPainter
     *
     * @param makerColor объект класса MakerColor
     * @param xMin       минимальное значение X на экране
     * @param xMax       максимальное значение X на экране
     * @param yMin       минимальное значение Y на экране
     * @param yMax       максимальное значение Y на экране
     */
    public FractalPainter(MakerColor makerColor, double xMin, double xMax, double yMin, double yMax) {
        converter = new Converter(xMin, xMax, yMin, yMax, 0, 0);
        this.makerColor = makerColor;
    }

    /**
     * Метод для обновления координат экрана и изменения максимального
     * количества итераций в зависимости от типа операции (масштабирование или сдвиг).
     *
     * @param xMin   новое минимальное значение X на экране
     * @param xMax   новое максимальное значение X на экране
     * @param yMin   новое минимальное значение Y на экране
     * @param yMax   новое максимальное значение Y на экране
     * @param type   тип операции (0 - масштабирование, 1 - сдвиг)
     */
    public void updateCoordinates(double xMin, double xMax, double yMin, double yMax, int type) {
        converter.setXShape(xMin, xMax);
        converter.setYShape(yMin, yMax);

        if (makerColor.coef == 2 || (xMax==1. && xMin== -2.)) {
            mandelbrot.setMaxIter(200);
        } else if (type == 0) {
            mandelbrot.setMaxIter((int) (mandelbrot.getMaxIter() + 200* makerColor.coef));
        }
        //System.out.println(mandelbrot.getMaxIter());
    }

    /**
     * Метод для рисования фрактала Мандельброта.
     *
     * @param g объект класса Graphics, используемый для рисования
     */
    @Override
    public void paint(Graphics g) {
        int threadCount = Runtime.getRuntime().availableProcessors();
        int blockSize = getWidth() / threadCount;

        Thread[] threads = new Thread[threadCount];
        BufferedImage[] images = new BufferedImage[threadCount];

        for (int i = 0; i < threadCount; i++) {
            final int startX = i * blockSize;
            final int endX = (i == threadCount - 1) ? getWidth() : (i + 1) * blockSize;

            int finalI = i;
            threads[i] = new Thread(() -> {
                BufferedImage image = new BufferedImage(endX - startX, getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();

                for (int x = startX; x < endX; x++) {
                    for (int y = 0; y < getHeight(); y++) {
                        var cx = converter.xScr2Crt(x);
                        var cy = converter.yScr2Crt(y);

                        var color = (makerColor.currentColor == 1)
                                ? makerColor.getColor1(mandelbrot.isInSet(new Complex(cx, cy)))
                                : makerColor.getColor2(mandelbrot.isInSet(new Complex(cx, cy)));
                        g2d.setColor(color);
                        g2d.fillRect(x - startX, y, 1, 1);
                    }
                }
                synchronized (images) {
                    images[finalI] = image;
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.err.println("Поток был прерван");
            }
        }
        for (int i = 0; i < threadCount; i++) {
            if (images[i] != null) {
                g.drawImage(images[i], i * blockSize, 0, null);
            }
        }
    }

    /**
     * Метод для получения ширины области рисования
     *
     * @return ширина области рисования
     */
    @Override
    public int getWidth() {
        return converter.getWidth();
    }

    /**
     * Метод для установки новой ширины области рисования
     *
     * @param width новая ширина области рисования
     */
    @Override
    public void setWidth(int width) {
        converter.setWidth(width);
    }

    /**
     * Метод для получения высоты области рисования.
     *
     * @return высота области рисования
     */
    @Override
    public int getHeight() {
        return converter.getHeight();
    }

    /**
     * Метод для установки новой высоты области рисования.
     *
     * @param height новая высота области рисования
     */
    @Override
    public void setHeight(int height) {
        converter.setHeight(height);
    }

    /**
     * Метод для получения объекта класса Converter.
     *
     * @return объект класса Converter
     */
    public Converter getConverter() {
        return converter;
    }
}