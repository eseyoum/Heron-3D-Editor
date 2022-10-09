package heron.gameboardeditor;

import javafx.scene.Parent;

public class Block extends Parent {
    public int type; //length of the block plane
    public boolean vertical = true; //orientation of the block plane

    private int health;

    public Block(int type) {
        this.type = type;
        health = type;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
