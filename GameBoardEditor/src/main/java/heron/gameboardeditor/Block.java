package heron.gameboardeditor; //Changed the package declaration -Corey
//package edu.augustana.HeronTeamProject; -old package

import javafx.scene.Parent;

public class Block extends Parent {
    public int type; //length of the block plane
    public boolean vertical = true; //orientation of the block plane

    private int health;

    public Block(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;

        /*VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }
        getChildren().add(vbox);*/
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
