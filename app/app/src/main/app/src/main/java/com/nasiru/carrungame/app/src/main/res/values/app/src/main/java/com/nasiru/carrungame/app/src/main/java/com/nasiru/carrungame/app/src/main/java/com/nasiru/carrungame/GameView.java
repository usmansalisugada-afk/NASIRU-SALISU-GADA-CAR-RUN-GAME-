package com.nasiru.carrungame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();

    private float carX;
    private float carY;

    private final float carWidth = 100;
    private final float carHeight = 160;

    private float obstacleX;
    private float obstacleY;

    private final float obstacleWidth = 100;
    private final float obstacleHeight = 150;

    private float obstacleSpeed = 10;

    private int score = 0;
    private boolean gameOver = false;

    public GameView(Context context) {
        super(context);

        post(() -> {
            carX = getWidth() / 2f - carWidth / 2f;
            carY = getHeight() - 230;

            createObstacle();

            invalidate();
        });
    }

    private void createObstacle() {
        float roadWidth = getWidth() - 80;

        obstacleX = 40 +
                random.nextFloat() * (roadWidth - obstacleWidth);

        obstacleY = -obstacleHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Background
        canvas.drawColor(Color.rgb(35, 35, 35));

        // Road borders
        paint.setColor(Color.WHITE);

        canvas.drawRect(
                30,
                0,
                40,
                getHeight(),
                paint
        );

        canvas.drawRect(
                getWidth() - 40,
                0,
                getWidth() - 30,
                getHeight(),
                paint
        );

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

        // Player car window
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

        // Obstacle car
        paint.setColor(Color.BLUE);

        canvas.drawRoundRect(
                obstacleX,
                obstacleY,
                obstacleX + obstacleWidth,
                obstacleY + obstacleHeight,
                20,
                20,
                paint
        );

        // Score
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);

        canvas.drawText(
                "SCORE: " + score,
                55,
                65,
                paint
        );

        if (!gameOver) {

            // Move obstacle
            obstacleY += obstacleSpeed;

            // Obstacle passed
            if (obstacleY > getHeight()) {

                score++;

                createObstacle();

                // Increase difficulty
                if (score % 5 == 0) {
                    obstacleSpeed += 1.5f;
                }
            }

            // Collision
            if (carX < obstacleX + obstacleWidth &&
                    carX + carWidth > obstacleX &&
                    carY < obstacleY + obstacleHeight &&
                    carY + carHeight > obstacleY) {

                gameOver = true;
            }

            postInvalidateDelayed(30);

        } else {

            paint.setColor(Color.RED);
            paint.setTextSize(60);

            canvas.drawText(
                    "GAME OVER",
                    getWidth() / 2f - 180,
                    getHeight() / 2f,
                    paint
            );

            paint.setColor(Color.WHITE);
            paint.setTextSize(35);

            canvas.drawText(
                    "Tap to Restart",
                    getWidth() / 2f - 125,
                    getHeight() / 2f + 60,
                    paint
            );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (gameOver) {

                score = 0;
                obstacleSpeed = 10;
                gameOver = false;

                carX = getWidth() / 2f - carWidth / 2f;
                carY = getHeight() - 230;

                createObstacle();

                invalidate();

                return true;
            }
        }

        if (!gameOver &&
                (event.getAction() == MotionEvent.ACTION_DOWN ||
                 event.getAction() == MotionEvent.ACTION_MOVE)) {

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
