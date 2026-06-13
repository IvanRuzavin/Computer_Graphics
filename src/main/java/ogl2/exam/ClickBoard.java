package ogl2.exam;

import com.jogamp.opengl.GL2;

public class ClickBoard {
    private static final float CM_TO_OPENGL = 0.1f;

    /*
     * The board itself should not be wider than the mikroBUS.
     */
    private static final float PCB_THICKNESS_CM = 0.12f;

    /*
     * Legs:
     * 8 mm long legs.
     */
    private static final float LEG_HEIGHT_CM = 0.8f;
    private static final float LEG_WIDTH_CM = 0.045f;

    /*
     * End rectangles:
     * 3 mm long small rectangles at the bottom of legs.
     */
    private static final float LEG_END_WIDTH_CM = 0.08f;
    private static final float LEG_END_HEIGHT_CM = 0.04f;
    private static final float LEG_END_DEPTH_CM = 0.30f;

    /*
     * The bottom of the legs reaches mikroBUS holes.
     */
    private static final float LEG_BOTTOM_Y_CM = 0.72f;

    /*
     * Small pins visible on TOP of the Click board.
     * These mark where each leg is on the other side.
     */
    private static final float TOP_PIN_SIZE_CM = 0.06f;
    private static final float TOP_PIN_HEIGHT_CM = 0.10f; // 1 mm

    private static final int LEGS_PER_SIDE = 8;

    private static final float HOLE_SIZE_CM = 0.1f;
    private static final float HOLE_GAP_CM = 0.1f;

    private final ClickBoardType type;

    public ClickBoard(ClickBoardType type) {
        this.type = type;
    }

    public void draw(GL2 gl,
                     float mikrobusWidthCm,
                     float mikrobusDepthCm,
                     float mikrobusWallCm,
                     float mikrobusGuideDepthCm) {
        float pcbWidthCm = mikrobusWidthCm;
        float pcbDepthCm = getBoardDepth(mikrobusDepthCm, mikrobusGuideDepthCm);

        /*
         * Boards are aligned by their lower side.
         * SMALL board sits exactly on mikroBUS.
         * MEDIUM and LONG extend upward toward the yellow guide area.
         */
        float boardCenterZcm = (pcbDepthCm - mikrobusDepthCm) / 2.0f;

        float pcbBottomYcm = LEG_BOTTOM_Y_CM + LEG_HEIGHT_CM;
        float pcbCenterYcm = pcbBottomYcm + PCB_THICKNESS_CM / 2.0f;
        float pcbTopYcm = pcbBottomYcm + PCB_THICKNESS_CM;

        /*
         * PCB body.
         */
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, cm(pcbCenterYcm), cm(boardCenterZcm));

        gl.glColor3f(0.05f, 0.45f, 0.16f);
        Shape.cuboid(gl,
                cm(pcbWidthCm),
                cm(PCB_THICKNESS_CM),
                cm(pcbDepthCm));

        gl.glPopMatrix();

        /*
         * Legs go down into the mikroBUS holes.
         */
        drawLegs(gl, mikrobusWidthCm, mikrobusDepthCm, mikrobusWallCm);

        /*
         * Small top pins show where the legs are from above.
         */
        drawTopPins(gl, mikrobusWidthCm, mikrobusDepthCm, mikrobusWallCm, pcbTopYcm);

        /*
         * Distinct detail per board type.
         */
        drawTopDetail(gl, pcbTopYcm, pcbWidthCm, pcbDepthCm, boardCenterZcm);
    }

    private float getBoardDepth(float mikrobusDepthCm, float mikrobusGuideDepthCm) {
        if (type.getSize() == ClickBoardSize.LONG) {
            return mikrobusGuideDepthCm;
        }

        if (type.getSize() == ClickBoardSize.MEDIUM) {
            return mikrobusGuideDepthCm / 2.0f;
        }

        return mikrobusDepthCm;
    }

    private void drawLegs(GL2 gl,
                          float mikrobusWidthCm,
                          float mikrobusDepthCm,
                          float mikrobusWallCm) {
        gl.glColor3f(0.85f, 0.68f, 0.18f);

        float patternLength = LEGS_PER_SIDE * HOLE_SIZE_CM + (LEGS_PER_SIDE - 1) * HOLE_GAP_CM;
        float margin = (mikrobusDepthCm - patternLength) / 2.0f;

        float firstLegCenterZ = mikrobusDepthCm / 2.0f - margin - HOLE_SIZE_CM / 2.0f;

        /*
         * Same X positions as the mikroBUS holes.
         */
        float leftLegX = -mikrobusWidthCm / 2.0f + mikrobusWallCm / 2.0f;
        float rightLegX = mikrobusWidthCm / 2.0f - mikrobusWallCm / 2.0f;

        for (int i = 0; i < LEGS_PER_SIDE; i++) {
            float z = firstLegCenterZ - i * (HOLE_SIZE_CM + HOLE_GAP_CM);

            drawSingleLeg(gl, leftLegX, z);
            drawSingleLeg(gl, rightLegX, z);
        }
    }

    private void drawSingleLeg(GL2 gl, float xCm, float zCm) {
        /*
         * 8 mm vertical leg.
         */
        drawLocalCuboid(gl,
                xCm,
                LEG_BOTTOM_Y_CM + LEG_HEIGHT_CM / 2.0f,
                zCm,
                LEG_WIDTH_CM,
                LEG_HEIGHT_CM,
                LEG_WIDTH_CM);

        /*
         * 3 mm long rectangle at the bottom.
         */
        drawLocalCuboid(gl,
                xCm,
                LEG_BOTTOM_Y_CM + LEG_END_HEIGHT_CM / 2.0f,
                zCm,
                LEG_END_WIDTH_CM,
                LEG_END_HEIGHT_CM,
                LEG_END_DEPTH_CM);
    }

    private void drawTopPins(GL2 gl,
                             float mikrobusWidthCm,
                             float mikrobusDepthCm,
                             float mikrobusWallCm,
                             float pcbTopYcm) {
        gl.glColor3f(0.85f, 0.68f, 0.18f);

        float patternLength = LEGS_PER_SIDE * HOLE_SIZE_CM + (LEGS_PER_SIDE - 1) * HOLE_GAP_CM;
        float margin = (mikrobusDepthCm - patternLength) / 2.0f;

        float firstPinCenterZ = mikrobusDepthCm / 2.0f - margin - HOLE_SIZE_CM / 2.0f;

        float leftPinX = -mikrobusWidthCm / 2.0f + mikrobusWallCm / 2.0f;
        float rightPinX = mikrobusWidthCm / 2.0f - mikrobusWallCm / 2.0f;

        float pinCenterY = pcbTopYcm + TOP_PIN_HEIGHT_CM / 2.0f;

        for (int i = 0; i < LEGS_PER_SIDE; i++) {
            float z = firstPinCenterZ - i * (HOLE_SIZE_CM + HOLE_GAP_CM);

            drawLocalCuboid(gl,
                    leftPinX,
                    pinCenterY,
                    z,
                    TOP_PIN_SIZE_CM,
                    TOP_PIN_HEIGHT_CM,
                    TOP_PIN_SIZE_CM);

            drawLocalCuboid(gl,
                    rightPinX,
                    pinCenterY,
                    z,
                    TOP_PIN_SIZE_CM,
                    TOP_PIN_HEIGHT_CM,
                    TOP_PIN_SIZE_CM);
        }
    }

    private void drawTopDetail(GL2 gl,
                               float pcbTopYcm,
                               float pcbWidthCm,
                               float pcbDepthCm,
                               float boardCenterZcm) {
        float detailY = pcbTopYcm + 0.08f;

        if (type == ClickBoardType.WIFI_CLICK) {
            gl.glColor3f(type.getRed(), type.getGreen(), type.getBlue());
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.50f,
                    0.16f,
                    pcbDepthCm * 0.18f);
        } else if (type == ClickBoardType.OLED_CLICK) {
            gl.glColor3f(0.02f, 0.02f, 0.025f);
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.72f,
                    0.10f,
                    pcbDepthCm * 0.45f);

            gl.glColor3f(type.getRed(), type.getGreen(), type.getBlue());
            drawLocalCuboid(gl,
                    0.0f,
                    detailY + 0.07f,
                    boardCenterZcm,
                    pcbWidthCm * 0.55f,
                    0.04f,
                    pcbDepthCm * 0.30f);
        } else if (type == ClickBoardType.RS232_CLICK) {
            gl.glColor3f(type.getRed(), type.getGreen(), type.getBlue());
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.55f,
                    0.16f,
                    pcbDepthCm * 0.16f);

            gl.glColor3f(0.02f, 0.02f, 0.025f);
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm + pcbDepthCm * 0.18f,
                    pcbWidthCm * 0.35f,
                    0.10f,
                    pcbDepthCm * 0.10f);
        } else if (type == ClickBoardType.THUNDER_CLICK) {
            gl.glColor3f(0.94f, 0.94f, 0.90f);
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.65f,
                    0.10f,
                    pcbDepthCm * 0.40f);

            gl.glColor3f(type.getRed(), type.getGreen(), type.getBlue());
            drawLocalCuboid(gl,
                    0.0f,
                    detailY + 0.08f,
                    boardCenterZcm,
                    pcbWidthCm * 0.20f,
                    0.05f,
                    pcbDepthCm * 0.25f);
        } else if (type == ClickBoardType.TEMP_CLICK) {
            gl.glColor3f(type.getRed(), type.getGreen(), type.getBlue());
            drawLocalCuboid(gl,
                    0.0f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.42f,
                    0.14f,
                    pcbDepthCm * 0.32f);

            gl.glColor3f(0.02f, 0.02f, 0.02f);
            drawLocalCuboid(gl,
                    pcbWidthCm * 0.25f,
                    detailY,
                    boardCenterZcm,
                    pcbWidthCm * 0.15f,
                    0.10f,
                    pcbDepthCm * 0.12f);
        }
    }

    private void drawLocalCuboid(GL2 gl,
                                 float localXcm,
                                 float localYcm,
                                 float localZcm,
                                 float widthCm,
                                 float heightCm,
                                 float depthCm) {
        gl.glPushMatrix();
        gl.glTranslatef(cm(localXcm), cm(localYcm), cm(localZcm));
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