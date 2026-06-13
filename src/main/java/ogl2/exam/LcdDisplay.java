package ogl2.exam;

import com.jogamp.opengl.GL2;

public class LcdDisplay {
    private static final float CM_TO_OPENGL = 0.1f;

    private static final float LCD_WIDTH_CM = 8.0f;
    private static final float LCD_DEPTH_CM = 3.6f;
    private static final float LCD_HEIGHT_CM = 1.5f;

    private static final int PIN_COUNT = 16;

    private static final float PIN_LEFT_OFFSET_CM = 0.6f;
    private static final float PIN_TOP_OFFSET_CM = 0.2f;

    private static final float PIN_BASE_SIZE_CM = 0.2f;
    private static final float PIN_BASE_HEIGHT_CM = 0.3f;
    private static final float PIN_SIZE_CM = 0.1f;
    private static final float PIN_GAP_CM = 0.1f;

    private static final float SOCKET_HEIGHT_CM = 0.35f;

    public void draw(GL2 gl) {
        /*
         * Main LCD box.
         */
        gl.glColor3f(0.02f, 0.025f, 0.04f);

        drawLocalCuboid(gl,
                0.0f,
                SOCKET_HEIGHT_CM + LCD_HEIGHT_CM / 2.0f,
                0.0f,
                LCD_WIDTH_CM,
                LCD_HEIGHT_CM,
                LCD_DEPTH_CM);

        /*
         * LCD screen color on top.
         */
        gl.glColor3f(0.35f, 0.55f, 0.95f);

        drawLocalCuboid(gl,
                0.0f,
                SOCKET_HEIGHT_CM + LCD_HEIGHT_CM + 0.02f,
                0.0f,
                LCD_WIDTH_CM * 0.72f,
                0.04f,
                LCD_DEPTH_CM * 0.50f);

        TextUtil.drawTopText(gl,
                "LCD",
                0.0f,
                SOCKET_HEIGHT_CM + LCD_HEIGHT_CM + 0.05f,
                0.0f,
                0.0f,
                0.00075f,
                1.0f,
                1.0f,
                1.0f);

        /*
         * Bottom socket that matches LCD board pins.
         */
        drawBottomSocket(gl);
    }

    private void drawBottomSocket(GL2 gl) {
        /*
         * The board is mirrored with sceneX(...),
         * so local X positions are mirrored here too.
         */
        float logicalFirstBaseLeft =
                -LCD_WIDTH_CM / 2.0f + PIN_LEFT_OFFSET_CM;

        float pinTop =
                LCD_DEPTH_CM / 2.0f - PIN_TOP_OFFSET_CM;

        float baseCenterZ =
                pinTop - PIN_BASE_SIZE_CM / 2.0f;

        float pitch = PIN_BASE_SIZE_CM + PIN_GAP_CM;

        gl.glColor3f(0.08f, 0.08f, 0.09f);

        for (int i = 0; i < PIN_COUNT; i++) {
            float logicalBaseLeft = logicalFirstBaseLeft + i * pitch;
            float logicalCenterX = logicalBaseLeft + PIN_BASE_SIZE_CM / 2.0f;

            float x = -logicalCenterX;

            drawLocalCuboid(gl,
                    x,
                    SOCKET_HEIGHT_CM / 2.0f,
                    baseCenterZ,
                    PIN_BASE_SIZE_CM,
                    SOCKET_HEIGHT_CM,
                    PIN_BASE_SIZE_CM);
        }
    }

    private void drawLocalCuboid(GL2 gl,
                                 float xCm,
                                 float yCm,
                                 float zCm,
                                 float widthCm,
                                 float heightCm,
                                 float depthCm) {
        gl.glPushMatrix();
        gl.glTranslatef(cm(xCm), cm(yCm), cm(zCm));

        Shape.cuboid(gl,
                cm(widthCm),
                cm(heightCm),
                cm(depthCm));

        gl.glPopMatrix();
    }

    private float cm(float value) {
        return value * CM_TO_OPENGL;
    }
}