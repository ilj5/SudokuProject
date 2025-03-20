package com.example.sudokuproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Board {
    private float x,y; // upper left corner
    private float w, h; // width and height
    private float cell; //width and height of each cell on the board
    private float selCellX, selCellY;
    private boolean isSelected;

    public Board(float x, float y, float w, float h, float cell) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.cell = cell;
        isSelected = false;
        this.selCellX = -1;
        this.selCellY = -1;
    }

    public void setSelCell(float selCellX, float selCellY) {
        float newSelCellX;
        float newSelCellY;
        newSelCellX = selCellX - x;
        newSelCellY = selCellY - y;
        newSelCellX = newSelCellX - (newSelCellX % cell) + x;
        newSelCellY = newSelCellY - (newSelCellY % cell) + y;
        if (this.selCellX == newSelCellX && this.selCellY == newSelCellY) {
            this.selCellX = -1;
            this.selCellY= -1;
            isSelected = false;
        }else if (newSelCellX >= x && newSelCellX <= x + w && newSelCellY >= y && newSelCellY <= y + h) {
            this.selCellX = newSelCellX;
            this.selCellY = newSelCellY;
            isSelected = true;
        }
    }

    public void draw(Canvas canvas, int[][] puzzleBoard, int[][] lockedPuzzleBoard) {
        Paint p = new Paint();
        p.setTextSize(75);
        p.setTextAlign(Paint.Align.CENTER);

        if (selCellX != -1 && selCellY != -1) {
            p.setStyle(Paint.Style.FILL);
            if (lockedPuzzleBoard[(int) ((selCellX - x) / cell)][(int) ((selCellY - y) / cell)] != 0) {
                p.setColor(Color.RED);
                canvas.drawRect(selCellX, selCellY, selCellX + cell, selCellY + cell, p);
            } else {
                p.setColor(Color.CYAN);
                canvas.drawRect(selCellX, selCellY, selCellX + cell, selCellY + cell, p);
            }
        }

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);

        canvas.drawRect( x, y, x + w, y + h, p );

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(x + (i * cell), y + (j * cell), x + (i * cell) + cell, y + (j * cell) + cell, p);
                p.setStyle(Paint.Style.FILL);
                if (puzzleBoard[i][j] != 0) {
                    if (lockedPuzzleBoard[i][j] != 0) {
                        p.setColor(Color.YELLOW);
                    } else {
                        p.setColor(Color.WHITE);
                    }
                    canvas.drawText(String.valueOf(puzzleBoard[i][j]), x + (i * cell) + (cell / 2), y + (j * cell) + (cell - 20), p);
                    p.setColor(Color.WHITE);
                }
            }
        }

        p.setColor(Color.RED);

        for (int i = 3; i < 9; i += 3) {
            canvas.drawLine(x + (i * cell), y, x + (i * cell), y + h, p);
            canvas.drawLine(x, y + (i * cell), x + w, y + (i * cell), p);
        }
    }

    public  boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + w && y >= this.y && y <= this.y + h;
    }

    public float getSelCellX() {
        return selCellX - x;
    }

    public float getSelCellY() {
        return selCellY - y;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
