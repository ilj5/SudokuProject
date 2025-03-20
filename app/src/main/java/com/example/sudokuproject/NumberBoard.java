package com.example.sudokuproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class NumberBoard {
    protected float x,y; // upper left corner
    protected float w, h; // width and height
    protected float cell; //width and height of each cell on the board
    private float selCellX;
    protected Bitmap pic; // trashcan picture
    private boolean isSelected;

    public NumberBoard(float x, float y, float w, float h, float cell, Bitmap p) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.cell = cell;
        isSelected = false;
        this.selCellX = -1;
        if (p != null) {
            pic = Bitmap.createScaledBitmap(p, (int) cell, (int) h, true);
        }
    }

    public void setSelCell(float selCellX) {
        float newSelCellX;
        newSelCellX = selCellX - x;
        newSelCellX = newSelCellX - (newSelCellX % cell) + x;
        if (this.selCellX == newSelCellX || selCellX == -1) {
            this.selCellX = -1;
            isSelected = false;
        }else if (newSelCellX >= x && newSelCellX <= x + w) {
            this.selCellX = newSelCellX;
            isSelected = true;
        }
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setTextSize(75);
        p.setTextAlign(Paint.Align.CENTER);

        if (selCellX != -1) {
            p.setColor(Color.CYAN);
            p.setStyle(Paint.Style.FILL);
            canvas.drawRect(selCellX, y, selCellX + cell, y + cell, p);
        }

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);

        canvas.drawRect( x, y, x + w, y + h, p );

        for (int i = 0; i < 10; i++) {
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x + (i * cell), y, x + (i * cell) + cell, y + cell, p);
            p.setStyle(Paint.Style.FILL);
            if (i != 9) {
                canvas.drawText(String.valueOf(i + 1), x + (i * cell) + (cell / 2), y + (cell - 20), p);
            }
            else if (pic != null) {
                canvas.drawBitmap(pic, x + (i * cell), y, null);
            }

        }
    }

    public  boolean contains(float x, float y) {
        return x >= this.x && x <= this.x + w && y >= this.y && y <= this.y + h;
    }

    public float getSelCellX() {
        return selCellX - x;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
