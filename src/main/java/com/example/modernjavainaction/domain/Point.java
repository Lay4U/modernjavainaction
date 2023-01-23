package com.example.modernjavainaction.domain;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

public record Point(int x, int y) {
    public Point moveRightBy(int x) {
        return new Point(this.x + x, this.y);
    }

    public final static Comparator<Point> compareByXAndThenY =
            comparing(Point::x).thenComparing(Point::y);

    public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
        return points.stream()
                .map(p -> new Point(p.x() + x, p.y()))
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        return y == point.y;
    }

}
