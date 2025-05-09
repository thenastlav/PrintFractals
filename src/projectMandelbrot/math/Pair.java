package projectMandelbrot.math;

/**
 * Класс для хранения пары точек типа {@link DoublePoint}
 */
public class Pair {
    public DoublePoint p1;
    public DoublePoint p2;

    /**
     * Конструктор для создания пары точек
     *
     * @param p1 первая точка
     * @param p2 вторая точка
     */
    public Pair(DoublePoint p1, DoublePoint p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Метод для получения первой точки в паре
     *
     * @return первая точка
     */
    public DoublePoint getP1() {
        return p1;
    }

    /**
     * Метод для получения второй точки в паре
     *
     * @return вторая точка
     */
    public DoublePoint getP2() {
        return p2;
    }
}