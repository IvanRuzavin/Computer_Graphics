package ogl2.exam;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Project implements GLEventListener, KeyListener {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final int FPS = 60;

    private JFrame frame;
    private GLJPanel canvas;
    private FPSAnimator animator;

    private Camera camera;
    private BoardBox boardBox;

    private float boardRotationX = 0.0f;
    private float boardRotationY = 0.0f;
    private float boardRotationZ = 0.0f;

    public static void main(String[] args) {
        new Project();
    }

    public Project() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createWindow();
            }
        });
    }

    private void createWindow() {
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setDepthBits(24);
        capabilities.setDoubleBuffered(true);

        canvas = new GLJPanel(capabilities);
        canvas.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);
        canvas.setFocusable(true);

        frame = new JFrame("Step 1 - UNI-DS black 3D board box");
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (animator != null) {
                    animator.stop();
                }
                System.exit(0);
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        animator = new FPSAnimator(canvas, FPS, true);
        animator.start();
        canvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearColor(0.08f, 0.09f, 0.11f, 1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

        setupLight(gl);

        camera = new Camera();
        boardBox = new BoardBox();
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        camera.apply(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
        setupLight(gl);

        gl.glPushMatrix();

        gl.glRotatef(boardRotationZ, 0.0f, 0.0f, 1.0f); // blue axis
        gl.glRotatef(boardRotationY, 0.0f, 1.0f, 0.0f); // green axis
        gl.glRotatef(boardRotationX, 1.0f, 0.0f, 0.0f); // red axis

        drawAxes(gl);
        boardBox.draw(gl);

        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        drawable.getGL().getGL2().glViewport(0, 0, width, height);
    }

    private void setupLight(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] ambient = {0.25f, 0.25f, 0.25f, 1.0f};
        float[] diffuse = {0.9f, 0.9f, 0.85f, 1.0f};
        float[] specular = {0.8f, 0.8f, 0.8f, 1.0f};
        float[] position = {3.0f, 4.0f, 5.0f, 1.0f};

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);
    }

    private void drawAxes(GL2 gl) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glLineWidth(2.0f);

        gl.glBegin(GL2.GL_LINES);
        gl.glColor3f(1.0f, 0.1f, 0.1f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(2.0f, 0, 0);

        gl.glColor3f(0.1f, 1.0f, 0.1f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 2.0f, 0);

        gl.glColor3f(0.1f, 0.3f, 1.0f);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0, 2.0f);
        gl.glEnd();

        gl.glLineWidth(1.0f);
        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            // Camera Rotation.
            case KeyEvent.VK_LEFT:
                boardRotationY -= 5.0f;
                break;
            case KeyEvent.VK_RIGHT:
                boardRotationY += 5.0f;
                break;
            case KeyEvent.VK_UP:
                boardRotationX -= 5.0f;
                break;
            case KeyEvent.VK_DOWN:
                boardRotationX += 5.0f;
                break;
            case KeyEvent.VK_Q:
                boardRotationZ -= 5.0f;
                break;
            case KeyEvent.VK_E:
                boardRotationZ += 5.0f;
                break;
            case KeyEvent.VK_PAGE_UP:
                camera.zoomIn();
                break;
            case KeyEvent.VK_PAGE_DOWN:
                camera.zoomOut();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

            // Click board move.
            case KeyEvent.VK_1:
                boardBox.selectClickBoard(0);
                break;

            case KeyEvent.VK_2:
                boardBox.selectClickBoard(1);
                break;

            case KeyEvent.VK_3:
                boardBox.selectClickBoard(2);
                break;

            case KeyEvent.VK_4:
                boardBox.selectClickBoard(3);
                break;

            case KeyEvent.VK_5:
                boardBox.selectClickBoard(4);
                break;

            case KeyEvent.VK_A:
                boardBox.previousMikrobus();
                break;

            case KeyEvent.VK_D:
                boardBox.nextMikrobus();
                break;

            case KeyEvent.VK_SPACE:
                boardBox.placeSelectedClickBoard();
                break;

            case KeyEvent.VK_R:
                boardBox.removeClickBoardFromActiveMikrobus();
                break;

            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
