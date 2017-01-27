package ru.ifmo.ctddev.spacearcade.model;

import android.graphics.Rect;

/**
 * @author Alexey Katsman
 * @since 27.01.17
 */

public abstract class ScreenGameObject extends GameObject {

    public double radius;
    public BodyType bodyType;
    public Rect boundingRect = new Rect(-1, -1, -1, -1);
    protected double x;
    protected double y;
    protected int height;
    protected int width;

    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }

    public void onPostUpdate(GameEngine gameEngine) {
        boundingRect.set((int) x, (int) y, (int) x + width, (int) y + height);
    }

    public boolean checkCollision(ScreenGameObject otherObject) {
        if (bodyType == BodyType.Circular && otherObject.bodyType == BodyType.Circular) {
            return checkCircularCollision(otherObject);
        } else if (bodyType == BodyType.Rectangular && otherObject.bodyType == BodyType.Rectangular) {
            return checkRectangularCollision(otherObject);
        } else {
            return checkMixedCollision(otherObject);
        }
    }

    private boolean checkMixedCollision(ScreenGameObject other) {
        ScreenGameObject circularSprite;
        ScreenGameObject rectangularSprite;
        if (bodyType == BodyType.Rectangular) {
            circularSprite = this;
            rectangularSprite = other;
        } else {
            circularSprite = other;
            rectangularSprite = this;
        }

        double circleCenterX = circularSprite.x + circularSprite.width / 2.0d;
        double positionXToCheck = circleCenterX;

        if (circleCenterX < rectangularSprite.x) {
            positionXToCheck = rectangularSprite.x;
        } else if (circleCenterX > rectangularSprite.x + rectangularSprite.width) {
            positionXToCheck = rectangularSprite.x + rectangularSprite.width;
        }

        double distanceX = circleCenterX - positionXToCheck;

        double circleCenterY = circularSprite.y + circularSprite.height / 2.0d;
        double positionYToCheck = circleCenterY;

        if (circleCenterY < rectangularSprite.y) {
            positionYToCheck = rectangularSprite.y;
        } else if (circleCenterY > rectangularSprite.y + rectangularSprite.height) {
            positionYToCheck = rectangularSprite.y + rectangularSprite.height;
        }

        double distanceY = circleCenterY - positionYToCheck;
        double squareDistance = distanceX * distanceX + distanceY * distanceY;

        return squareDistance <= circularSprite.radius * circularSprite.radius;
    }

    private boolean checkRectangularCollision(ScreenGameObject other) {
        return Rect.intersects(boundingRect, other.boundingRect);
    }

    private boolean checkCircularCollision(ScreenGameObject other) {
        double distanceX = x + width / 2.0d - (other.x + other.width / 2.0d);
        double distanceY = y + height / 2.0d - (other.y + other.height / 2.0d);
        double squareDistance = distanceX * distanceX + distanceY * distanceY;
        double collisionDistance = radius + other.radius;

        return squareDistance <= collisionDistance * collisionDistance;
    }
}