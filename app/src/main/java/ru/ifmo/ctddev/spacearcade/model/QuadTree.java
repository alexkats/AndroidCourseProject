package ru.ifmo.ctddev.spacearcade.model;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public class QuadTree {

    private static final int MAX_QUADTREES = 12;
    private static final int MAX_OBJECTS_TO_CHECK = 8;
    private static final List<QuadTree> QUAD_TREE_POOL = new ArrayList<>();
    private final List<ScreenGameObject> gameObjects = new ArrayList<>();
    private final Rect area = new Rect();
    private final Rect tmpRect = new Rect();
    private final QuadTree[] children = new QuadTree[4];

    public static void init() {
        QUAD_TREE_POOL.clear();

        for (int i = 0; i < MAX_QUADTREES; i++) {
            QUAD_TREE_POOL.add(new QuadTree());
        }
    }

    private static boolean hasBeenDetected(List<Collision> detectedCollisions, Collision c) {
        for (Collision collision : detectedCollisions) {
            if (collision.equals(c)) {
                return true;
            }
        }

        return false;
    }

    public void setArea(Rect area) {
        this.area.set(area);
    }

    public void checkObjects(List<ScreenGameObject> gameObjects) {
        this.gameObjects.clear();

        for (ScreenGameObject gameObject : gameObjects) {
            if (Rect.intersects(gameObject.boundingRect, area)) {
                this.gameObjects.add(gameObject);
            }
        }
    }

    public void checkCollisions(GameEngine gameEngine, List<Collision> detectedCollisions) {
        int objectsQuantity = gameObjects.size();

        if (objectsQuantity > MAX_OBJECTS_TO_CHECK && QUAD_TREE_POOL.size() >= 4) {
            splitAndCheck(gameEngine, detectedCollisions);
        } else {
            for (int i = 0; i < objectsQuantity; i++) {
                ScreenGameObject objectA = gameObjects.get(i);

                for (int j = i + 1; j < objectsQuantity; j++) {
                    ScreenGameObject objectB = gameObjects.get(j);

                    if (objectA.checkCollision(objectB)) {
                        Collision c = Collision.init(objectA, objectB);

                        if (!hasBeenDetected(detectedCollisions, c)) {
                            detectedCollisions.add(c);
                            objectA.onCollision(gameEngine, objectB);
                            objectB.onCollision(gameEngine, objectA);
                        }
                    }
                }
            }
        }
    }

    private void splitAndCheck(GameEngine gameEngine, List<Collision> detectedCollisions) {
        for (int i = 0; i < 4; i++) {
            children[i] = QUAD_TREE_POOL.remove(0);
        }

        for (int i = 0; i < 4; i++) {
            children[i].setArea(getArea(i));
            children[i].checkObjects(gameObjects);
            children[i].checkCollisions(gameEngine, detectedCollisions);
            children[i].gameObjects.clear();
            QUAD_TREE_POOL.add(children[i]);
        }
    }

    private Rect getArea(int area) {
        int startX = this.area.left;
        int startY = this.area.top;
        int width = this.area.width();
        int height = this.area.height();

        switch (area) {
            case 0:
                tmpRect.set(startX, startY, startX + width / 2, startY + height / 2);
                break;
            case 1:
                tmpRect.set(startX + width / 2, startY, startX + width, startY + height / 2);
                break;
            case 2:
                tmpRect.set(startX, startY + height / 2, startX + width / 2, startY + height);
                break;
            case 3:
                tmpRect.set(startX + width / 2, startY + height / 2, startX + width, startY + height);
                break;
        }

        return tmpRect;
    }

    public void addGameObject(ScreenGameObject objectToAdd) {
        gameObjects.add(objectToAdd);
    }

    public void removeGameObject(ScreenGameObject objectToRemove) {
        gameObjects.remove(objectToRemove);
    }
}
