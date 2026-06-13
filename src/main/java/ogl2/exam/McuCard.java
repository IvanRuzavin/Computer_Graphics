package ogl2.exam;

import com.jogamp.opengl.GL2;

public class McuCard {
    private static final float CM_TO_OPENGL = 0.1f;

    private static final float CARD_SIZE_CM = 6.0f;
    private static final float CARD_HEIGHT_CM = 0.2f; // 2 mm

    private static final float LEG_WIDTH_CM = 0.4f;
    private static final float LEG_DEPTH_CM = 4.3f;
    private static final float LEG_HEIGHT_CM = 0.3f;
    private static final float LEG_SIDE_OFFSET_CM = 0.3f;

    private static final float CONTACT_WIDTH_CM = 0.02f;
    private static final float CONTACT_DEPTH_CM = 0.1f;
    private static final float CONTACT_HEIGHT_CM = 0.1f;
    private static final float CONTACT_GAP_CM = 0.1f;

    /*
     * Central MCU chip.
     */
    private static final float CHIP_SIZE_CM = 1.7f;
    private static final float CHIP_HEIGHT_CM = 0.2f; // 2 mm
    private static final float CHIP_PIN_WIDTH_CM = 0.1f;
    private static final float CHIP_PIN_LENGTH_CM = 0.18f;
    private static final float CHIP_PIN_HEIGHT_CM = 0.05f;
    private static final float CHIP_PIN_GAP_CM = 0.1f;
    private static final int CHIP_PINS_PER_SIDE = 8;

    /*
     * Label rectangles.
     */
    private static final float LABEL_LONG_CM = 2.4f;
    private static final float LABEL_SHORT_CM = 1.0f;
    private static final float LABEL_EDGE_INSET_CM = 0.7f;

    public void draw(GL2 gl) {
        float leftLegX = -CARD_SIZE_CM / 2.0f + LEG_SIDE_OFFSET_CM + LEG_WIDTH_CM / 2.0f;
        float rightLegX = CARD_SIZE_CM / 2.0f - LEG_SIDE_OFFSET_CM - LEG_WIDTH_CM / 2.0f;

        drawLeg(gl, leftLegX);
        drawLeg(gl, rightLegX);

        /*
         * Main MCU card body.
         */
        gl.glColor3f(0.02f, 0.025f, 0.03f);

        drawLocalCuboid(gl,
                0.0f,
                LEG_HEIGHT_CM + CARD_HEIGHT_CM / 2.0f,
                0.0f,
                CARD_SIZE_CM,
                CARD_HEIGHT_CM,
                CARD_SIZE_CM);

        drawTopPerimeter(gl);
        drawMcuChip(gl);
        drawTopLabels(gl);
    }

    private void drawLeg(GL2 gl, float xCm) {
        gl.glColor3f(0.96f, 0.96f, 0.96f);

        drawLocalCuboid(gl,
                xCm,
                LEG_HEIGHT_CM / 2.0f,
                0.0f,
                LEG_WIDTH_CM,
                LEG_HEIGHT_CM,
                LEG_DEPTH_CM);

        drawGoldContacts(gl, xCm);
    }

    private void drawGoldContacts(GL2 gl, float legCenterXcm) {
        gl.glColor3f(0.87f, 0.70f, 0.20f);

        float usableDepth = LEG_DEPTH_CM;
        int contactCount = (int) ((usableDepth + CONTACT_GAP_CM)
                / (CONTACT_DEPTH_CM + CONTACT_GAP_CM));

        float usedDepth = contactCount * CONTACT_DEPTH_CM
                + (contactCount - 1) * CONTACT_GAP_CM;

        float firstZ = usedDepth / 2.0f - CONTACT_DEPTH_CM / 2.0f;

        float leftContactX = legCenterXcm - LEG_WIDTH_CM / 2.0f + CONTACT_WIDTH_CM / 2.0f;
        float rightContactX = legCenterXcm + LEG_WIDTH_CM / 2.0f - CONTACT_WIDTH_CM / 2.0f;

        for (int i = 0; i < contactCount; i++) {
            float z = firstZ - i * (CONTACT_DEPTH_CM + CONTACT_GAP_CM);

            drawLocalCuboid(gl,
                    leftContactX,
                    LEG_HEIGHT_CM / 2.0f,
                    z,
                    CONTACT_WIDTH_CM,
                    CONTACT_HEIGHT_CM,
                    CONTACT_DEPTH_CM);

            drawLocalCuboid(gl,
                    rightContactX,
                    LEG_HEIGHT_CM / 2.0f,
                    z,
                    CONTACT_WIDTH_CM,
                    CONTACT_HEIGHT_CM,
                    CONTACT_DEPTH_CM);
        }
    }

    private void drawTopPerimeter(GL2 gl) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(1.0f, 0.82f, 0.0f);
        gl.glLineWidth(2.0f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, cm(LEG_HEIGHT_CM + CARD_HEIGHT_CM + 0.01f), 0.0f);

        Shape.roundedRectangleLine(gl,
                cm(CARD_SIZE_CM - 0.15f),
                cm(CARD_SIZE_CM - 0.15f),
                0.0f,
                4);

        gl.glPopMatrix();

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private void drawMcuChip(GL2 gl) {
        float chipCenterY = LEG_HEIGHT_CM + CARD_HEIGHT_CM + CHIP_HEIGHT_CM / 2.0f;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, cm(chipCenterY), 0.0f);
        gl.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);

        /*
         * MCU square.
         */
        gl.glColor3f(0.14f, 0.14f, 0.16f);
        Shape.cuboid(gl,
                cm(CHIP_SIZE_CM),
                cm(CHIP_HEIGHT_CM),
                cm(CHIP_SIZE_CM));

        drawChipPins(gl);

        gl.glPopMatrix();
    }

    private void drawChipPins(GL2 gl) {
        gl.glColor3f(0.55f, 0.55f, 0.58f);

        float usedLength = CHIP_PINS_PER_SIDE * CHIP_PIN_WIDTH_CM
                + (CHIP_PINS_PER_SIDE - 1) * CHIP_PIN_GAP_CM;

        float first = -usedLength / 2.0f + CHIP_PIN_WIDTH_CM / 2.0f;
        float pinY = CHIP_HEIGHT_CM / 2.0f + CHIP_PIN_HEIGHT_CM / 2.0f;

        for (int i = 0; i < CHIP_PINS_PER_SIDE; i++) {
            float pos = first + i * (CHIP_PIN_WIDTH_CM + CHIP_PIN_GAP_CM);

            // Top side
            drawLocalCuboid(gl,
                    pos,
                    pinY,
                    CHIP_SIZE_CM / 2.0f + CHIP_PIN_LENGTH_CM / 2.0f,
                    CHIP_PIN_WIDTH_CM,
                    CHIP_PIN_HEIGHT_CM,
                    CHIP_PIN_LENGTH_CM);

            // Bottom side
            drawLocalCuboid(gl,
                    pos,
                    pinY,
                    -CHIP_SIZE_CM / 2.0f - CHIP_PIN_LENGTH_CM / 2.0f,
                    CHIP_PIN_WIDTH_CM,
                    CHIP_PIN_HEIGHT_CM,
                    CHIP_PIN_LENGTH_CM);

            // Left side
            drawLocalCuboid(gl,
                    -CHIP_SIZE_CM / 2.0f - CHIP_PIN_LENGTH_CM / 2.0f,
                    pinY,
                    pos,
                    CHIP_PIN_LENGTH_CM,
                    CHIP_PIN_HEIGHT_CM,
                    CHIP_PIN_WIDTH_CM);

            // Right side
            drawLocalCuboid(gl,
                    CHIP_SIZE_CM / 2.0f + CHIP_PIN_LENGTH_CM / 2.0f,
                    pinY,
                    pos,
                    CHIP_PIN_LENGTH_CM,
                    CHIP_PIN_HEIGHT_CM,
                    CHIP_PIN_WIDTH_CM);
        }
    }

    private void drawTopLabels(GL2 gl) {
        float outlineY = LEG_HEIGHT_CM + CARD_HEIGHT_CM + 0.012f;
        float textY = LEG_HEIGHT_CM + CARD_HEIGHT_CM + 0.02f;

        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(1.0f, 0.82f, 0.0f);
        gl.glLineWidth(1.5f);

        /*
         * Left side: MikroBUS 1 and MikroBUS 2
         */
        drawTopLabelRect(gl,
                -CARD_SIZE_CM / 2.0f + LABEL_EDGE_INSET_CM,
                1.35f,
                LABEL_SHORT_CM,
                LABEL_LONG_CM,
                outlineY);

        drawTopLabelRect(gl,
                -CARD_SIZE_CM / 2.0f + LABEL_EDGE_INSET_CM,
                -1.35f,
                LABEL_SHORT_CM,
                LABEL_LONG_CM,
                outlineY);

        /*
         * Top side: MikroBUS 3
         */
        drawTopLabelRect(gl,
                0.0f,
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                LABEL_LONG_CM,
                LABEL_SHORT_CM,
                outlineY);

        /*
         * Right side: MikroBUS 4 and MikroBUS 5
         */
        drawTopLabelRect(gl,
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                1.35f,
                LABEL_SHORT_CM,
                LABEL_LONG_CM,
                outlineY);

        drawTopLabelRect(gl,
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                -1.35f,
                LABEL_SHORT_CM,
                LABEL_LONG_CM,
                outlineY);

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }

        /*
         * Text on top face.
         */
        TextUtil.drawTopText(gl,
                "MCU CARD",
                0.3f,
                textY,
                -2.2f,
                0.0f,
                0.00042f,
                1.0f,
                0.86f,
                0.10f);

        TextUtil.drawTopText(gl,
                "MikroBUS 1",
                -CARD_SIZE_CM / 2.0f + LABEL_EDGE_INSET_CM,
                textY,
                1.35f,
                90.0f,
                0.00027f,
                1.0f,
                0.86f,
                0.10f);

        TextUtil.drawTopText(gl,
                "MikroBUS 2",
                -CARD_SIZE_CM / 2.0f + LABEL_EDGE_INSET_CM,
                textY,
                -1.35f,
                90.0f,
                0.00027f,
                1.0f,
                0.86f,
                0.10f);

        TextUtil.drawTopText(gl,
                "MikroBUS 3",
                0.0f,
                textY,
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                0.0f,
                0.00027f,
                1.0f,
                0.86f,
                0.10f);

        TextUtil.drawTopText(gl,
                "MikroBUS 4",
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                textY,
                1.35f,
                90.0f,
                0.00027f,
                1.0f,
                0.86f,
                0.10f);

        TextUtil.drawTopText(gl,
                "MikroBUS 5",
                CARD_SIZE_CM / 2.0f - LABEL_EDGE_INSET_CM,
                textY,
                -1.35f,
                90.0f,
                0.00027f,
                1.0f,
                0.86f,
                0.10f);
    }

    private void drawTopLabelRect(GL2 gl,
                                  float centerXcm,
                                  float centerZcm,
                                  float widthCm,
                                  float depthCm,
                                  float yCm) {
        gl.glPushMatrix();
        gl.glTranslatef(cm(centerXcm), cm(yCm), cm(centerZcm));

        Shape.roundedRectangleLine(gl,
                cm(widthCm),
                cm(depthCm),
                0.0f,
                4);

        gl.glPopMatrix();
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