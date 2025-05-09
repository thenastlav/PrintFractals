package projectMandelbrot.gui;
import java.awt.*;

public interface Painter {
    void paint(Graphics g);
    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);
}

