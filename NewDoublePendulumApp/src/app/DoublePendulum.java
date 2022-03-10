package app;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class DoublePendulum {
    private double x0, y0, x1, x2, y1, y2;

    public double getA2_v() {
        return a2_v;
    }

    public void setA2_v(double a2_v) {
        this.a2_v = a2_v;
    }

    public double getA2_a() {
        return a2_a;
    }

    public void setA2_a(double a2_a) {
        this.a2_a = a2_a;
    }

    private double a1, a1_v, a1_a, a2, a2_v, a2_a;

    private Line line1, line2;
    private Circle circle1, circle2;

    public DoublePendulum(double x0, double y0, double x1, double y1, double x2, double y2) {
        line1 = new Line(x0, y0, x1, y1);
        line2 = new Line(x1, y1, x2, y2);
        circle1 = new Circle(x1, y1, 5);
        circle2 = new Circle(x2, y2, 5);
    }

    public void setAngles(double a1, double a1_v,double a1_a, double a2, double a2_v, double a2_a) {
        this.a1 = a1;
        this.a1_v = a1_v;
        this.a1_a = a1_a;
        this.a2 = a2;
        this.a2_v = a2_v;
        this.a2_a = a2_a;

    }

    public Circle getCircle2() {
        return circle2;
    }

    public void setCircle2(Circle circle2) {
        this.circle2 = circle2;
    }

    public void setCircles(double r1, double r2) {
        circle1.setRadius(r1);
        circle2.setRadius(r2);
    }

    public void setPosition(double nx0, double ny0, double nx1, double ny1, double nx2, double ny2) {
        x0 = nx0;
        y0 = ny0;
        x1 = nx1;
        y1 = ny1;
        x2 = nx2;
        y2 = ny2;

        line2.setStartX(nx1);
        line2.setStartY(ny1);
        line2.setEndX(nx2);
        line2.setEndY(ny2);

        line1.setStartX(nx0);
        line1.setStartY(ny0);
        line1.setEndX(nx1);
        line1.setEndY(ny1);

        circle1.setCenterX(nx1);
        circle1.setCenterY(ny1);
        circle2.setCenterX(nx2);
        circle2.setCenterY(ny2);
    }

    public double getX0() {
        return x0;
    }

    public void setX0(double x0) {
        this.x0 = x0;
    }

    public double getY0() {
        return y0;
    }

    public void setY0(double y0) {
        this.y0 = y0;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getA1() {
        return a1;
    }

    public void setA1(double a1) {
        this.a1 = a1;
    }

    public double getA2() {
        return a2;
    }

    public void setA2(double a2) {
        this.a2 = a2;
    }

    public double getA1_v() {
        return a1_v;
    }

    public void setA1_v(double a1_v) {
        this.a1_v = a1_v;
    }

    public double getA1_a() {
        return a1_a;
    }

    public void setA1_a(double a1_a) {
        this.a1_a = a1_a;
    }

    public Line getLine1() {
        return line1;
    }

    public void setLine1(Line line1) {
        this.line1 = line1;
    }

    public Line getLine2() {
        return line2;
    }

    public void setLine2(Line line2) {
        this.line2 = line2;
    }

    public Circle getCircle1() {
        return circle1;
    }

    public void setCircle1(Circle circle1) {
        this.circle1 = circle1;
    }
}
