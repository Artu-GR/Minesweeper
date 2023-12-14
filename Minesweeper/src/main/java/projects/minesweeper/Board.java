/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projects.minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 *
 * @author artur
 */
public class Board extends JPanel{
        
    private final int CELL_SIZE = 15;
    
    private final int EMPTY_CELL = 0;
    private final int MINE_CELL = 9;
    private final int NEW_CELL = 10;
    private final int FLAG_CELL = 11;
    
    private final int N_MINES = 49;
    private final int N_ROWS = 16;
    private final int N_COLS = 16;
    
    private boolean inGame;
    private int minesLeft;
    
    private Image[] img;
    private Cell[][] board;
    
    private final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    private final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;
    
    private final JLabel statusbar;
        
    public Board(JLabel statusbar){
        this.statusbar = statusbar;
        initBoard();
    }
    
    private void initBoard(){
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        
        img = new Image[12];
        
        for(int i = 0; i < 12; i++){
            var path = "src/main/resources/" + i + ".png";
            img[i] = (new ImageIcon(path)).getImage();
        }
        
        addMouseListener(new MinesAdapter());
        newGame();
    }
    
    private void newGame(){
        int mines_placed = 0;
        board = new Cell[N_ROWS][N_COLS];
        int row, col;
        Random rd = new Random();
        
        minesLeft = N_MINES;
        inGame = true;
        
        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLS; j++){
                board[i][j] = new Cell();
            }
        }
        
        statusbar.setText(Integer.toString(minesLeft));
        
        while(mines_placed < N_MINES){
            row = rd.nextInt(15);
            col = rd.nextInt(15);
            if(!board[row][col].isBomb()){
                board[row][col].setBomb(true);
                mines_placed += 1;
            }
        }

        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLS; j++){
                board[i][j].setSurrounding_bombs(findBombs(i, j));
                board[i][j].setImg(NEW_CELL);
            }
        }
        /* BOARD SOLUTION
        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLS; j++){
                System.out.print((board[i][j].isBomb()?1:0) + " ");
            }
            System.out.println("");
        }
        System.out.println("***");
        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLS; j++){
                System.out.print((board[i][j].getSurrounding_bombs()) + " ");
            }
            System.out.println("");
        }*/
    }
    
    @Override
    public void paintComponent(Graphics g){
               
        int uncover = 0; //cells left to guess
                     
        for(int i = 0; i < N_ROWS; i++){
            for(int j = 0; j < N_COLS; j++){
                                
                if(!inGame){ //Game Over
                    if(board[i][j].getImg() != FLAG_CELL && board[i][j].isBomb()){ //Mine not flagged
                        board[i][j].setImg(MINE_CELL);
                    }else if(board[i][j].getImg() == FLAG_CELL && board[i][j].isBomb()){ // Flagged mine
                        board[i][j].setImg(FLAG_CELL);
                    }else if(board[i][j].isVisited()){ // Seen and no mine
                        board[i][j].setImg(board[i][j].getSurrounding_bombs());
                    }else if(board[i][j].getImg() == FLAG_CELL && !board[i][j].isBomb()){
                        board[i][j].setImg(NEW_CELL);
                    }
                }else{ //Still playing
                    if(!board[i][j].isVisited() && minesLeft != 0){
                        uncover++;
                    }else{
                        if(!board[i][j].isBomb())
                            board[i][j].setImg(board[i][j].getSurrounding_bombs());
                    }
                }
                
                int index = board[i][j].getImg();
                
                g.drawImage(img[index], (j*CELL_SIZE), (i*CELL_SIZE), CELL_SIZE, CELL_SIZE, this);
            }
        }
        
        if(uncover == 0 && inGame){
            statusbar.setText("Game won");
            inGame = false;
        }else if(!inGame){
            statusbar.setText("Game over");
        }
        
    }
    
    private class MinesAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e){
            int x = e.getX();
            int y = e.getY();
            
            int COL = x / CELL_SIZE; //sets the coordinates
            int ROW = y / CELL_SIZE; // of the position
            
            boolean doRepaint = false; // know if the board's to be repainted
            
            if(!inGame){
                newGame();
                repaint();
            }
            
            if((x < N_ROWS * CELL_SIZE) && (y < N_COLS * CELL_SIZE)){
                
                if(e.getButton() == MouseEvent.BUTTON3){ // right click
                    
                    if(board[ROW][COL].getImg() == FLAG_CELL || board[ROW][COL].getImg() == NEW_CELL){
                        doRepaint = true;
                        
                        if(board[ROW][COL].getImg() != FLAG_CELL){
                            String msg;
                            if(minesLeft > 0){
                                board[ROW][COL].setImg(FLAG_CELL);
                                minesLeft--;
                                msg = Integer.toString(minesLeft);
                            }else{
                                msg = "No marks left";
                            }
                            statusbar.setText(msg);
                        }else{
                            board[ROW][COL].setImg(NEW_CELL);
                            minesLeft++;
                            String msg = Integer.toString(minesLeft);
                            statusbar.setText(msg);
                        }   
                    }
                } else if(e.getButton() == MouseEvent.BUTTON1){ //left click
                    if(board[ROW][COL].getImg() == FLAG_CELL || board[ROW][COL].isVisited()){
                        return; // Flag cell clicked or already visited cell
                    }
                    
                    if(board[ROW][COL].isBomb()){
                        inGame = false;
                        board[ROW][COL].setImg(MINE_CELL);
                    }else{
                        board[ROW][COL].setImg(board[ROW][COL].getSurrounding_bombs());
                    }
                    doRepaint = true;
                    
                    if(board[ROW][COL].getSurrounding_bombs() == 0){
                        //Busqueda de espacios en el tablero
                        findEmptyCells(ROW,COL);
                    }
                }
                    
                if(doRepaint){
                    repaint();
                }
                
            }
        }
    }
    
    private void findEmptyCells(int row, int col){ // Recursive function for every empty cell found
        // Clear empty cells adjacent to the one clicked and so on
        for(int i = -1; i < 2; i++){
            for(int j = -1; j < 2; j++){
                if(row + i >= 0 && col + j >= 0 && row + i <= 15 && col + j <= 15){ // Validates all coords are valid for the 16x16 board
                    if(!board[row+i][col+j].isVisited() && !board[row+i][col+j].isBomb()){
                        if(board[row+i][col+j].getSurrounding_bombs() == 0){
                            board[row+i][col+j].setImg(EMPTY_CELL);
                            board[row+i][col+j].setVisited(true);
                            findEmptyCells(row+i, col+j);
                        }else{
                            board[row+i][col+j].setImg(board[row+i][col+j].getSurrounding_bombs());
                        }
                    }
                    
                }
            }
        }
        
        
    }
    
    private int findBombs(int x, int y){
        int bombs = 0;
        if(x-1 >= 0){
            if(y-1 >= 0)
                if(board[x-1][y-1].isBomb()) bombs++;
            if(board[x-1][y].isBomb()) bombs++;
            if(y+1 <= 15)
                if(board[x-1][y+1].isBomb()) bombs++;
        }
        
        if(y-1 >= 0)
            if(board[x][y-1].isBomb()) bombs++;
        if(y+1 <= 15)    
            if(board[x][y+1].isBomb()) bombs++;
        
        if(x+1 <= 15){
            if(y-1 >= 0)
                if(board[x+1][y-1].isBomb()) bombs++;
            if(board[x+1][y].isBomb()) bombs++;
            if(y+1 <= 15)
                if(board[x+1][y+1].isBomb()) bombs++;
        }
        
        return bombs;
    }
    
}