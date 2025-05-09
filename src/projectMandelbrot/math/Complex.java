package projectMandelbrot.math;

/**
 * Класс для комплексных чисел
 */
public class Complex {
    private double re;
    private double im;

    /**
     * Конструктор для создания комплексного числа с указанными действительной и мнимой частями
     * @param re действительная часть
     * @param im мнимая часть
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * Конструктор для создания комплексного числа с нулевыми действительной и мнимой частями
     */
    public Complex() {
        re = 0;
        im = 0;
    }

    /**
     * Метод для сложения двух комплексных чисел
     * @param other другое комплексное число
     * @return результат сложения типа {@link Complex}
     */
    public Complex plus(Complex other) {
        return new Complex(re + other.re, im + other.im);
    }

    /**
     * Метод для вычитания одного комплексного числа из другого
     * @param other другое комплексное число
     * @return результат вычитания типа {@link Complex}
     */
    public Complex minus(Complex other) {
        return new Complex(re - other.re, im - other.im);
    }

    /**
     * Метод для умножения двух комплексных чисел
     * @param other другое комплексное число
     * @return результат умножения типа {@link Complex}
     */
    public Complex times(Complex other) {
        return new Complex(
                re * other.re - im * other.im,
                re * other.im + im * other.re
        );
    }

    /**
     * Метод для деления одного комплексного числа на другое
     * @param other другое комплексное число
     * @return результат деления типа {@link Complex}
     */
    public Complex div(Complex other) {
        return new Complex(
                re / other.re - im / other.im,
                re / other.im + im / other.re
        );
    }

    /**
     * Метод для вычисления квадрата модуля комплексного числа
     * @return квадрат модуля
     */
    public double abs2() {
        return re * re + im * im;
    }

    /**
     * Метод для вычисления модуля комплексного числа.
     * @return модуль
     */
    public double abs() {
        return Math.sqrt(re * re + im * im);
    }
}