package ogl2.exam;

import com.jogamp.opengl.GL2;

public class TftDisplay {
    private static final float CM_TO_OPENGL = 0.1f;

    private static final float TFT_WIDTH_CM = 12.0f;
    private static final float TFT_DEPTH_CM = 7.5f;
    private static final float TFT_HEIGHT_CM = 1.0f;

    private static final float SOCKET_WIDTH_CM = 0.4f;
    private static final float SOCKET_DEPTH_CM = 5.5f;
    private static final float SOCKET_HEIGHT_CM = 0.35f;
    private static final float SOCKET_WALL_CM = 0.1f;

    private static final float SOCKET_LEFT_OFFSET_CM = 0.6f;
    private static final float SOCKET_TOP_OFFSET_CM = 0.3f;

    private static final int PIN_ROWS = 20;
    private static final int PIN_COLS = 2;
    private static final float PIN_SIZE_CM = 0.08f;
    private static final float PIN_GAP_CM = 0.1f;
    private static final float PIN_HEIGHT_CM = 0.25f;

    public void draw(GL2 gl) {
        /*
         * Main TFT body.
         */
        gl.glColor3f(0.015f, 0.018f, 0.022f);

        drawLocalCuboid(gl,
                0.0f,
                SOCKET_HEIGHT_CM + TFT_HEIGHT_CM / 2.0f,
                0.0f,
                TFT_WIDTH_CM,
                TFT_HEIGHT_CM,
                TFT_DEPTH_CM);

        /*
         * Blue screen surface.
         */
        gl.glColor3f(0.0f, 0.25f, 0.75f);

        drawLocalCuboid(gl,
                0.0f,
                SOCKET_HEIGHT_CM + TFT_HEIGHT_CM + 0.02f,
                0.0f,
                TFT_WIDTH_CM * 0.78f,
                0.04f,
                TFT_DEPTH_CM * 0.72f);

        /*
         * Bottom socket matching the TFT connector on the board.
         * X is mirrored inside the component so it matches the board orientation.
         */
        float logicalSocketX =
                -TFT_WIDTH_CM / 2.0f
                        + SOCKET_LEFT_OFFSET_CM
                        + SOCKET_WIDTH_CM / 2.0f;

        float socketX = -logicalSocketX;

        float socketTop =
                TFT_DEPTH_CM / 2.0f - SOCKET_TOP_OFFSET_CM;

        float socketZ =
                socketTop - SOCKET_DEPTH_CM / 2.0f;

        drawBottomSocket(gl, socketX, socketZ);
    }

    private void drawBottomSocket(GL2 gl, float socketX, float socketZ) {
        gl.glColor3f(0.08f, 0.08f, 0.09f);

        // Left wall
        drawLocalCuboid(gl,
                socketX - SOCKET_WIDTH_CM / 2.0f + SOCKET_WALL_CM / 2.0f,
                SOCKET_HEIGHT_CM / 2.0f,
                socketZ,
                SOCKET_WALL_CM,
                SOCKET_HEIGHT_CM,
                SOCKET_DEPTH_CM);

        // Right wall
        drawLocalCuboid(gl,
                socketX + SOCKET_WIDTH_CM / 2.0f - SOCKET_WALL_CM / 2.0f,
                SOCKET_HEIGHT_CM / 2.0f,
                socketZ,
                SOCKET_WALL_CM,
                SOCKET_HEIGHT_CM,
                SOCKET_DEPTH_CM);

        // Top wall
        drawLocalCuboid(gl,
                socketX,
                SOCKET_HEIGHT_CM / 2.0f,
                socketZ + SOCKET_DEPTH_CM / 2.0f - SOCKET_WALL_CM / 2.0f,
                SOCKET_WIDTH_CM,
                SOCKET_HEIGHT_CM,
                SOCKET_WALL_CM);

        // Bottom wall
        drawLocalCuboid(gl,
                socketX,
                SOCKET_HEIGHT_CM / 2.0f,
                socketZ - SOCKET_DEPTH_CM / 2.0f + SOCKET_WALL_CM / 2.0f,
                SOCKET_WIDTH_CM,
                SOCKET_HEIGHT_CM,
                SOCKET_WALL_CM);

        drawSocketPins(gl, socketX, socketZ);
    }

    private void drawSocketPins(GL2 gl, float socketX, float socketZ) {
        gl.glColor3f(0.87f, 0.70f, 0.20f);

        float zPatternLength = PIN_ROWS * PIN_SIZE_CM + (PIN_ROWS - 1) * PIN_GAP_CM;
        float zMargin = (SOCKET_DEPTH_CM - zPatternLength) / 2.0f;
        float firstZ = socketZ + SOCKET_DEPTH_CM / 2.0f - zMargin - PIN_SIZE_CM / 2.0f;

        float xPatternWidth = PIN_COLS * PIN_SIZE_CM + (PIN_COLS - 1) * PIN_GAP_CM;
        float firstX = socketX - xPatternWidth / 2.0f + PIN_SIZE_CM / 2.0f;

        for (int row = 0; row < PIN_ROWS; row++) {
            float z = firstZ - row * (PIN_SIZE_CM + PIN_GAP_CM);

            for (int col = 0; col < PIN_COLS; col++) {
                float x = firstX + col * (PIN_SIZE_CM + PIN_GAP_CM);

                drawLocalCuboid(gl,
                        x,
                        PIN_HEIGHT_CM / 2.0f,
                        z,
                        PIN_SIZE_CM,
                        PIN_HEIGHT_CM,
                        PIN_SIZE_CM);
            }
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