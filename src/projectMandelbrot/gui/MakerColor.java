package projectMandelbrot.gui;
import java.awt.Color;

/**
 * Класс цветовой палитры фрактала Мандельброта
 */
public class MakerColor {
    public int currentColor = 1;
    public int coef = 1;

    /**
     * Метод для получения цвета из первой палитры.
     *
     * @param value Значение, определяющее оттенок цвета.
     * @return Цвет типа {@link Color}, соответствующий переданному значению.
     */
    public Color getColor1(double value) {
        if (value == 1.0) {
            return Color.BLACK;
        }
        float r = (float) (Math.abs(Math.sin(101 * value)));
        float g = (float) (Math.abs(Math.cos(10 * value)));
        float b = (float) (1 - 0.2 * (Math.abs(Math.sin(25 * value)) + Math.abs(Math.cos(17 * value))));
        return new Color(r, g, b);
    }

    /**
     * Метод для получения цвета из второй палитры.
     *
     * @param value Значение, определяющее оттенок цвета.
     * @return Цвет {@link Color}, соответствующий переданному значению.
     */
    public Color getColor2(double value) {
        if (value == 1.0) {
            return Color.BLACK;
        }
        float r = (float) (Math.abs(Math.sin(71 * value)));
        float g = (float) (Math.abs(Math.cos(28 * value)));
        float b = (float) (1 - 0.2 * (Math.abs(Math.sin(71 * value)) + Math.abs(Math.cos(1 * value))));
        return new Color(r, g, b);
    }
}