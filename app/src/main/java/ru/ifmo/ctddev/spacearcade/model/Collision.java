package ru.ifmo.ctddev.spacearcade.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class Collision {

    private static final List<Collision> COLLISTION_POOL = new ArrayList<>();
    public ScreenGameObject objectA;
    public ScreenGameObject objectB;

    public Collision(ScreenGameObject objectA, ScreenGameObject objectB) {
        this.objectA = objectA;
        this.objectB = objectB;
    }

    public static Collision init(ScreenGameObject objectA, ScreenGameObject objectB) {
        if (COLLISTION_POOL.isEmpty()) {
            return new Collision(objectA, objectB);
        }

        Collision c = COLLISTION_POOL.remove(0);
        c.objectA = objectA;
        c.objectB = objectB;

        return c;
    }

    public static void release(Collision c) {
        c.objectA = null;
        c.objectB = null;
        COLLISTION_POOL.add(c);
    }

    @SuppressWarnings({"ObjectEquality", "CovariantEquals"})
    public boolean equals(Collision c) {
        return objectA == c.objectA && objectB == c.objectB
                || objectA == c.objectB && objectB == c.objectA;
    }
}
