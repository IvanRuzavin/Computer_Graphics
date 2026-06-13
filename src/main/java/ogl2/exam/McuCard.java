package ogl2.exam;

import com.jogamp.opengl.GL2;

public class McuCard {
    private static final float CM_TO_OPENGL = 0.1f;

    private static final float CARD_SIZE_CM = 6.0f;
    private static final float CARD_HEIGHT_CM = 0.2f; // 2 mm

    private static final float LEG_WIDTH_CM = 0.4f;
    private static final float LEG_DEPTH_CM = 4.3f;
    private static final float LEG_HEIGHT_CM = 0.3f; // matches socket height

    private static final float LEG_SIDE_OFFSET_CM = 0.3f;

    private static final float CONTACT_WIDTH_CM = 0.02f;
    private static final float CONTACT_DEPTH_CM = 0.1f;
    private static final float CONTACT_HEIGHT_CM = 0.1f;
    private static final float CONTACT_GAP_CM = 0.1f;

    public void draw(GL2 gl) {
        /*
         * Two white legs that fit into the two MCU socket connectors.
         */
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

        /*
         * Contacts go on both long sides of the white leg.
         */
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