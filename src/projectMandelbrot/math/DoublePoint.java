package projectMandelbrot.math;

/**
 * Класс для представления точки с двумя координатами типа double
 */
public class DoublePoint {
    public double x;
    public double y;

    /**
     * Конструктор для создания точки с заданными координатами
     *
     * @param x координата X
     * @param y координата Y
     */
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}