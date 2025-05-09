package projectMandelbrot.math;
import static java.lang.Math.abs;

/**
 * Класс для конвертации координат между декартовой и экранной системами координат
 */
public class Converter {
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private int width;
    private int height;

    /**
     * Конструктор класса Converter.
     *
     * @param xMin   минимальное значение X в декартовой системе координат
     * @param xMax   максимальное значение X в декартовой системе координат
     * @param yMin   минимальное значение Y в декартовой системе координат
     * @param yMax   максимальное значение Y в декартовой системе координат
     * @param width  ширина области отображения в экранной системе координат
     * @param height высота области отображения в экранной системе координат
     */
    public Converter(
            double xMin,
            double xMax,
            double yMin,
            double yMax,
            int width,
            int height
    ) {
        setXShape(xMin, xMax);
        setYShape(yMin, yMax);
        setWidth(width);
        setHeight(height);
    }

    /**
     * Метод для получения минимального значения X в декартовой системе координат
     *
     * @return минимальное значение X
     */
    public double getXMin() {
        return xMin;
    }

    /**
     * Метод для получения максимального значения X в декартовой системе координат
     *
     * @return максимальное значение X
     */
    public double getXMax() {
        return xMax;
    }

    /**
     * Метод для установки новых границ по оси X в декартовой системе координат
     *
     * @param xMin минимальное значение X
     * @param xMax максимальное значение X
     */
    public void setXShape(double xMin, double xMax) {
        this.xMin = Math.min(xMin, xMax);
        this.xMax = Math.max(xMin, xMax);
        if (abs(xMin - xMax) < 1e-1) {
            this.xMin -= 0.05;
            this.xMax += 0.05;
        }
    }

    /**
     * Метод для получения минимального значения Y в декартовой системе координат
     *
     * @return минимальное значение Y
     */
    public double getYMin() {
        return yMin;
    }

    /**
     * Метод для получения максимального значения Y в декартовой системе координат
     *
     * @return максимальное значение Y
     */
    public double getYMax() {
        return yMax;
    }

    /**
     * Метод для установки новых границ по оси Y в декартовой системе координат
     *
     * @param yMin минимальное значение Y
     * @param yMax максимальное значение Y
     */
    public void setYShape(double yMin, double yMax) {
        this.yMin = Math.min(yMin, yMax);
        this.yMax = Math.max(yMin, yMax);
    }

    /**
     * Метод для получения ширины области отображения в экранной системе координат
     *
     * @return ширина области отображения
     */
    public int getWidth() {
        return width - 1;
    }

    /**
     * Метод для установки новой ширины области отображения в экранной системе координат
     *
     * @param width новая ширина области отображения
     */
    public void setWidth(int width) {
        this.width = abs(width);
    }

    /**
     * Метод для получения высоты области отображения в экранной системе координат
     *
     * @return высота области отображения
     */
    public int getHeight() {
        return height - 1;
    }

    /**
     * Метод для установки новой высоты области отображения в экранной системе координат
     *
     * @param height новая высота области отображения
     */
    public void setHeight(int height) {
        this.height = abs(height);
    }

    /**
     * Метод для получения коэффициента пересчета по оси X
     *
     * @return коэффициент пересчета по оси X
     */
    public double getXDen() {
        return width / (xMax - xMin);
    }

    /**
     * Метод для получения коэффициента пересчета по оси Y
     *
     * @return коэффициент пересчета по оси Y
     */
    public double getYDen() {
        return height / (yMax - yMin);
    }

    /**
     * Метод для преобразования координаты из декартовой системы в экранную систему координат по оси X
     *
     * @param x координата в декартовой системе
     * @return координата в экранной системе координат
     */
    public int xCrt2Scr(double x) {
        var v = ((x - xMin) * getXDen());
        if (v < -width) v = -width;
        if (v > 2 * width) v = 2 * width;
        return (int) v;
    }

    /**
     * Метод для преобразования координаты из декартовой системы в экранную систему координат по оси Y
     *
     * @param y координата в декартовой системе
     * @return координата в экранной системе координат
     */
    public int yCrt2Scr(double y) {
        var v = ((yMax - y) * getYDen());
        if (v < -height) v = -height;
        if (v > 2 * height) v = 2 * height;
        return (int) v;
    }

    /**
     * Метод для преобразования координаты из экранной системы в декартовую систему координат по оси X
     *
     * @param x координата в экранной системе
     * @return координата в декартовой системе координат
     */
    public double xScr2Crt(int x) {
        return (double) x / getXDen() + xMin;
    }

    /**
     * Метод для преобразования координаты из экранной системы в декартовую систему координат по оси Y
     *
     * @param y координата в экранной системе
     * @return координата в декартовой системе координат
     */
    public double yScr2Crt(int y) {
        return yMax - (double) y / getYDen();
    }
}