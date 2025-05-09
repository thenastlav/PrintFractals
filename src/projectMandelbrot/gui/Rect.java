package projectMandelbrot.gui;

import java.awt.*;

/**
 * Класс для построения прямоугольной области по двум заданным точкам
 */
public class Rect {
    /**
     * Первая установленная точка для формирования прямоугольной области
     */
    private Point point1 = null;

    /**
     * Вторая установленная точка для формирования прямоугольной области
     */
    private Point point2 = null;

    public Point getPoint1() {
        return point1;
    }
    public Point getPoint2() {
        return point2;
    }

    /**
     * Проверка возможности построения прямоугольника
     * @return true, если заданы обе точки и прямоугльник может быть построен;
     * false - если хотя бы одна из точек, необходимых для построения прямоугольника не задана
     */
    public boolean isValid(){
        return point1 != null && point2 != null;
    }

    /**
     * Добавление точки для формирования прямоугольной области
     * При первом вызове метода, добавляется первая точка
     * При повторных вызовах метода добавляется и перезаписывается вторая точка
     * После вызова метода {@link #clearPoints()} обе точки, формирующие прямоугольную область будут очищены
     * и добавление точек будет осуществляться заново, начиная с первой.
     * @param point точка, координаты которой будут добавлены в прямоугольную область
     * @see #clearPoints()
     */
    public void addPoint(Point point){
        if (point1 != null)
            point2 = point;
        else
            point1 = point;
    }

    /**
     * Получение стартовой (левой верхней) точки прямоугольной области
     * @return Левая верхняя точка прямоугольной области
     */
    public Point getStartPoint(){
        return isValid() ? new Point(
                Math.min(point1.x,point2.x),
                Math.min(point1.y,point2.y)) : null;
    }

    /**
     * Получение ширины прямоугольной области
     * @return ширина прямоугольника
     */
    public int getWidth(){
        return isValid() ? Math.abs(point1.x-point2.x) : 0;
    }

    public int getWidthVect(){
        return isValid() ? (point1.x-point2.x) : 0;
    }
    /**
     * Получение высоты прямоугольной области
     * @return высота прямоугольника
     */
    public int getHeigth(){
        return isValid() ? Math.abs(point1.y-point2.y) : 0;
    }

    public int getHeigthVect(){
        return isValid() ? (point1.y-point2.y) : 0;
    }

    /**
     * Удаление данных о точках, формирующих прямоугольную область
     * После вызова этого метода построение прямоугольной области становится невозможным,
     * до тех пор пока не будут добавлены две новые точки
     * @see #isValid()
     * @see #addPoint(Point)
     */
    public void clearPoints(){
        point1 = point2 = null;
    }

}
