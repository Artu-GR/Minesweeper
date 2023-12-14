/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projects.minesweeper;

/**
 *
 * @author artur
 */
public class Cell {
    private int surrounding_bombs;
    private boolean visited;
    private boolean bomb;
    private int img;

    /**
     * @return the surrounding_bombs
     */
    public int getSurrounding_bombs() {
        return surrounding_bombs;
    }

    /**
     * @param surrounding_bombs the surrounding_bombs to set
     */
    public void setSurrounding_bombs(int surrounding_bombs) {
        this.surrounding_bombs = surrounding_bombs;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the bomb
     */
    public boolean isBomb() {
        return bomb;
    }

    /**
     * @param bomb the bomb to set
     */
    public void setBomb(boolean bomb) {
        this.bomb = bomb;
    }

    /**
     * @return the img
     */
    public int getImg() {
        return img;
    }

    /**
     * @param img the img to set
     */
    public void setImg(int img) {
        this.img = img;
    }
}
