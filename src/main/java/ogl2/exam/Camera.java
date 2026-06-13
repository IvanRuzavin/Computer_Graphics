package ogl2.exam;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
    private final GLU glu = new GLU();

    private float yaw = -35f;
    private float pitch = 35f;
    private float distance = 5.5f;

    public void apply(GL2 gl, int width, int height) {
        float aspect = height == 0 ? 1.0f : (float) width / (float) height;

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0, aspect, 0.1, 100.0);

        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        float eyeX = (float) (Math.sin(yawRad) * Math.cos(pitchRad) * distance);
        float eyeY = (float) (Math.sin(pitchRad) * distance);
        float eyeZ = (float) (Math.cos(yawRad) * Math.cos(pitchRad) * distance);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(eyeX, eyeY, eyeZ,
                0.0, 0.0, 0.0,
                0.0, 1.0, 0.0);
    }

    public void rotateLeft() {
        yaw -= 5f;
    }

    public void rotateRight() {
        yaw += 5f;
    }

    public void rotateUp() {
        pitch = Math.min(80f, pitch + 5f);
    }

    public void rotateDown() {
        pitch = Math.max(10f, pitch - 5f);
    }

    public void zoomIn() {
        distance = Math.max(3.0f, distance - 0.3f);
    }

    public void zoomOut() {
        distance = Math.min(12.0f, distance + 0.3f);
    }
}
