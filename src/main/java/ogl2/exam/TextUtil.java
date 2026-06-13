package ogl2.exam;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public final class TextUtil {
    private static final float CM_TO_OPENGL = 0.1f;
    private static final GLUT GLUT_HELPER = new GLUT();

    private TextUtil() {
    }

    public static void drawTopText(GL2 gl,
                                   String text,
                                   float xCm,
                                   float yCm,
                                   float zCm,
                                   float rotationDeg,
                                   float scale,
                                   float r,
                                   float g,
                                   float b) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(r, g, b);

        gl.glPushMatrix();
        gl.glTranslatef(cm(xCm), cm(yCm), cm(zCm));

        /*
         * Put text flat onto the top surface (XZ plane).
         */
        gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(rotationDeg + 180.0f, 0.0f, 0.0f, 1.0f);

        gl.glScalef(scale, scale, scale);

        /*
         * Approximate centering.
         */
        float approxWidth = text.length() * 72.0f;
        gl.glTranslatef(-approxWidth / 2.0f, 0.0f, 0.0f);

        GLUT_HELPER.glutStrokeString(GLUT.STROKE_ROMAN, text);

        gl.glPopMatrix();

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private static float cm(float value) {
        return value * CM_TO_OPENGL;
    }
}