package projectMandelbrot.fractal;
import projectMandelbrot.math.Complex;

/**
 * Класс для множества Мандельброта
 */
public class Mandelbrot {
    private double r;
    private int maxIter;

    /**
     * Конструктор класса Mandelbrot с параметрами по умолчанию
     */
    public Mandelbrot() {
       this(200, 2);
    }

    /**
     * Конструктор класса Mandelbrot с заданными значениями максимального количества итераций и радиуса
     * @param maxIterations максимальное количество итераций
     * @param r             радиус
     */
    public Mandelbrot(int maxIterations, double r) {
        this.maxIter = maxIterations;
        this.r = r;
    }

    /**
     * Метод для получения значения максимального количества итераций
     * @return максимальное количество итераций
     */
    public int getMaxIter() {
        return maxIter;
    }

    /**
     * Метод для установки нового значения максимального количества итераций
     * @param value новое значение максимального количества итераций
     */
    public void setMaxIter(int value) {
        maxIter = Math.max(Math.abs(value), 200);
    }

    /**
     * Метод для получения значения радиуса
     * @return радиус
     */
    public double getR() {
        return r;
    }

    /**
     * Метод для установки нового значения радиуса
     * @param value новое значение радиуса
     */
    public void setR(double value) {
        r = Math.max(Math.abs(value), 2);
    }

    /**
     * Метод для проверки, принадлежит ли комплексная точка множеству Мандельброта
     * @param c комплексная точка
     * @return (i / maxIter) находится ли точка во множестве
     */
    public double isInSet(Complex c) {
        Complex z = new Complex();
        int i = 0;
        double r2 = r * r;
        while (z.abs2() < r2 && i < maxIter) {
           z = z.times(z).plus(c);
           i++;
        }
        return ((double) i / maxIter);
    }
}