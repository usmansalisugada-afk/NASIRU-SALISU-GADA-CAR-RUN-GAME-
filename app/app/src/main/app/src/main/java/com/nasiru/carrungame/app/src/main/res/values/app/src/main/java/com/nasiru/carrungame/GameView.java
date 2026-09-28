package com.nasiru.carrungame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float carX;
    private float carY;

    private final float carWidth = 100;
    private final float carHeight = 160;

    public GameView(Context context) {
        super(context);

        paint.setStrokeWidth(8);

        post(() -> {
            carX = getWidth() / 2f - carWidth / 2f;
            carY = getHeight() - 220;
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Road
        canvas.drawColor(Color.rgb(35, 35, 35));

        // Road borders
        paint.setColor(Color.WHITE);
        canvas.drawRect(30, 0, 40, getHeight(), paint);
        canvas.drawRect(getWidth() - 40, 0, getWidth() - 30,
                getHeight(), paint);

        // Road center lines
        paint.setColor(Color.YELLOW);

        float centerX = getWidth() / 2f;

        for (int y = 0; y < getHeight(); y += 100) {
            canvas.drawRect(
                    centerX - 5,
                    y,
                    centerX + 5,
                    y + 55,
                    paint
            );
        }

        // Player car
        paint.setColor(Color.RED);
        canvas.drawRoundRect(
                carX,
                carY,
                carX + carWidth,
                carY + carHeight,
                20,
                20,
                paint
        );

        // Car windows
        paint.setColor(Color.CYAN);
        canvas.drawRoundRect(
                carX + 15,
                carY + 20,
                carX + carWidth - 15,
                carY + 65,
                10,
                10,
                paint
        );

        // Score
        paint.setColor(Color.WHITE);
        paint.setTextSize(45);
        canvas.drawText("SCORE: 0", 55, 70, paint);

        // Game title
        paint.setTextSize(28);
        canvas.drawText("CAR RUN", 55, 115, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {

            carX = event.getX() - carWidth / 2f;

            // Keep car inside road
            if (carX < 40) {
                carX = 40;
            }

            if (carX + carWidth > getWidth() - 40) {
                carX = getWidth() - 40 - carWidth;
            }

            invalidate();
            return true;
        }

        return true;
    }
            }
