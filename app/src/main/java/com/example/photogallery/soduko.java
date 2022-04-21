package com.example.photogallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class soduko extends View {
    private final int boardColor;
    private final int cellFillColor;
    private final int cellHighlightColor;

    private final int letterColor;
    private final int letterColorSolve;

    private int cellSize;

    private final Paint boardColorPaint = new Paint();
    private final Paint cellFillColorPaint = new Paint();
    private final Paint cellHighlightColorPaint = new Paint();

    private final Paint letterPaint = new Paint();
    private final Rect letterPaintBounds = new Rect();
    private final solver solver = new solver();

    public soduko(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.soduko, 0,0);
        try {
            boardColor = array.getInteger(R.styleable.soduko_boardColor, 0);
            cellFillColor = array.getInteger(R.styleable.soduko_cellFillColor, 0);
            cellHighlightColor = array.getInteger(R.styleable.soduko_cellHighlightColor, 0);

            letterColor = array.getInteger(R.styleable.soduko_letterColor, 0);
            letterColorSolve = array.getInteger(R.styleable.soduko_letterColorSolve, 0);
        }finally {
            array.recycle();
        }

    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        int dimension = Math.min(this.getMeasuredWidth(), this.getMeasuredHeight());
        cellSize = dimension / 9;
        setMeasuredDimension(dimension, dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(16);
        boardColorPaint.setColor(boardColor);
        boardColorPaint.setAntiAlias(true);

        cellFillColorPaint.setStyle(Paint.Style.FILL);

        cellFillColorPaint.setColor(cellFillColor);

        cellHighlightColorPaint.setStyle(Paint.Style.FILL);
        cellHighlightColorPaint.setColor(cellHighlightColor);

        letterPaint.setStyle(Paint.Style.FILL);
        letterPaint.setAntiAlias(true);
        letterPaint.setColor(letterColor);

        ColorCell(canvas, solver.getSelectedRow(), solver.getSelectedCol());
        canvas.drawRect(0, 0, getWidth(), getHeight(), boardColorPaint);
        drawBoard(canvas);
        drawNumbers(canvas);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isValid;
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            solver.setSelectedRow((int)Math.ceil(y/cellSize));
            solver.setSelectedCol((int)Math.ceil(x/cellSize));
            isValid = true;
        }
        else {
            isValid = false;
        }

        return isValid;
    }

    private void drawNumbers(Canvas canvas){
        letterPaint.setTextSize(cellSize);
            for (int r = 0; r < 9; r++){
                for (int c = 0; c < 9; c++){
                    if(solver.getBoard()[r][c] != 0){
                        String text = Integer.toString(solver.getBoard()[r][c]);
                        float width, height;
                        letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                        width = letterPaint.measureText(text);
                        height = letterPaintBounds.height();

                        canvas.drawText(text, (c*cellSize) + ((cellSize - width)/2),
                                (r*cellSize+cellSize)- ((cellSize-height)/2), letterPaint);
                    }

                }
            }
            letterPaint.setColor(letterColorSolve);
            for(ArrayList<Object> letter : solver.getEmptyBoxIndex()){
                int r = (int) letter.get(0);
                int c = (int) letter.get(1);

                String text = Integer.toString(solver.getBoard()[r][c]);
                float width, height;
                letterPaint.getTextBounds(text, 0, text.length(), letterPaintBounds);
                width = letterPaint.measureText(text);
                height = letterPaintBounds.height();

                canvas.drawText(text, (c*cellSize)+ ((cellSize - width)/2),
                        (r*cellSize+cellSize)- ((cellSize-height)/2), letterPaint);
            }
    }

    private  void ColorCell(Canvas canvas, int row, int col){
        if(solver.getSelectedCol() != -1 && solver.getSelectedRow() != -1){
            canvas.drawRect((col-1)* cellSize, 0, col*cellSize,
                    cellSize*9, cellFillColorPaint);

            canvas.drawRect(0, (row-1)*cellSize, cellSize*9,
                    row*cellSize, cellFillColorPaint);

            canvas.drawRect((col-1)* cellSize, (row-1)*cellSize,
                    col*cellSize, row*cellSize, cellHighlightColorPaint);
        }
        invalidate();
    }


    private void drawThickLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(10);
        boardColorPaint.setColor(boardColor);
    }

    private void drawThinLine(){
        boardColorPaint.setStyle(Paint.Style.STROKE);
        boardColorPaint.setStrokeWidth(4);
        boardColorPaint.setColor(boardColor);
    }

    public solver getSolver() {
        return this.solver;
    }

    public void drawBoard(Canvas canvas){
        for (int c = 0; c < 10; c++){
            if(c%3==0)
                drawThickLine();
            else
                drawThinLine();
            canvas.drawLine(cellSize * c, 0,
                    cellSize * c, getHeight(), boardColorPaint);
        }
        for (int r = 0; r < 10; r++){
            if(r%3==0)
                drawThickLine();
            else
                drawThinLine();
            canvas.drawLine( 0, cellSize * r,
                    getWidth(), cellSize * r, boardColorPaint);
        }
    }


}
