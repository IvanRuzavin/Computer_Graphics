package ogl2.exam;

import com.jogamp.opengl.GL2;

public final class Shape {
    private Shape() {
    }

    public static void cuboid(GL2 gl, float width, float height, float depth) {
        float x = width / 2.0f;
        float y = height / 2.0f;
        float z = depth / 2.0f;

        gl.glBegin(GL2.GL_QUADS);

        // Top
        gl.glNormal3f(0, 1, 0);
        gl.glVertex3f(-x, y, -z);
        gl.glVertex3f(-x, y, z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(x, y, -z);

        // Bottom
        gl.glNormal3f(0, -1, 0);
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(x, -y, -z);
        gl.glVertex3f(x, -y, z);
        gl.glVertex3f(-x, -y, z);

        // Front
        gl.glNormal3f(0, 0, 1);
        gl.glVertex3f(-x, -y, z);
        gl.glVertex3f(x, -y, z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(-x, y, z);

        // Back
        gl.glNormal3f(0, 0, -1);
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(-x, y, -z);
        gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, -y, -z);

        // Right
        gl.glNormal3f(1, 0, 0);
        gl.glVertex3f(x, -y, -z);
        gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(x, -y, z);

        // Left
        gl.glNormal3f(-1, 0, 0);
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(-x, -y, z);
        gl.glVertex3f(-x, y, z);
        gl.glVertex3f(-x, y, -z);

        gl.glEnd();
    }

    public static void wireCuboid(GL2 gl, float width, float height, float depth) {
        float x = width / 2.0f;
        float y = height / 2.0f;
        float z = depth / 2.0f;

        gl.glBegin(GL2.GL_LINES);

        gl.glVertex3f(-x, -y, -z); gl.glVertex3f(x, -y, -z);
        gl.glVertex3f(x, -y, -z); gl.glVertex3f(x, -y, z);
        gl.glVertex3f(x, -y, z); gl.glVertex3f(-x, -y, z);
        gl.glVertex3f(-x, -y, z); gl.glVertex3f(-x, -y, -z);

        gl.glVertex3f(-x, y, -z); gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, y, -z); gl.glVertex3f(x, y, z);
        gl.glVertex3f(x, y, z); gl.glVertex3f(-x, y, z);
        gl.glVertex3f(-x, y, z); gl.glVertex3f(-x, y, -z);

        gl.glVertex3f(-x, -y, -z); gl.glVertex3f(-x, y, -z);
        gl.glVertex3f(x, -y, -z); gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, -y, z); gl.glVertex3f(x, y, z);
        gl.glVertex3f(-x, -y, z); gl.glVertex3f(-x, y, z);

        gl.glEnd();
    }

    public static void roundedCuboid(GL2 gl,
                                     float width,
                                     float height,
                                     float depth,
                                     float radius,
                                     int segments) {
        if (radius <= 0.0f) {
            cuboid(gl, width, height, depth);
            return;
        }

        float maxRadius = Math.min(width, depth) / 2.0f;
        float r = Math.min(radius, maxRadius);

        float[][] points = roundedRectanglePoints(width, depth, r, segments);

        float yTop = height / 2.0f;
        float yBottom = -height / 2.0f;

        // Top face
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3f(0, 1, 0);
        gl.glVertex3f(0, yTop, 0);

        for (int i = 0; i < points.length; i++) {
            gl.glVertex3f(points[i][0], yTop, points[i][1]);
        }

        gl.glVertex3f(points[0][0], yTop, points[0][1]);
        gl.glEnd();

        // Bottom face
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3f(0, -1, 0);
        gl.glVertex3f(0, yBottom, 0);

        for (int i = points.length - 1; i >= 0; i--) {
            gl.glVertex3f(points[i][0], yBottom, points[i][1]);
        }

        gl.glVertex3f(points[points.length - 1][0], yBottom, points[points.length - 1][1]);
        gl.glEnd();

        // Side faces
        gl.glBegin(GL2.GL_QUADS);

        for (int i = 0; i < points.length; i++) {
            int next = (i + 1) % points.length;

            float x1 = points[i][0];
            float z1 = points[i][1];

            float x2 = points[next][0];
            float z2 = points[next][1];

            float dx = x2 - x1;
            float dz = z2 - z1;

            float nx = dz;
            float nz = -dx;
            float len = (float) Math.sqrt(nx * nx + nz * nz);

            if (len != 0.0f) {
                nx /= len;
                nz /= len;
            }

            gl.glNormal3f(nx, 0, nz);

            gl.glVertex3f(x1, yBottom, z1);
            gl.glVertex3f(x2, yBottom, z2);
            gl.glVertex3f(x2, yTop, z2);
            gl.glVertex3f(x1, yTop, z1);
        }

        gl.glEnd();
    }

    public static void wireRoundedCuboid(GL2 gl,
                                         float width,
                                         float height,
                                         float depth,
                                         float radius,
                                         int segments) {
        if (radius <= 0.0f) {
            wireCuboid(gl, width, height, depth);
            return;
        }

        float maxRadius = Math.min(width, depth) / 2.0f;
        float r = Math.min(radius, maxRadius);

        float[][] points = roundedRectanglePoints(width, depth, r, segments);

        float yTop = height / 2.0f;
        float yBottom = -height / 2.0f;

        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i < points.length; i++) {
            gl.glVertex3f(points[i][0], yTop, points[i][1]);
        }
        gl.glEnd();

        gl.glBegin(GL2.GL_LINE_LOOP);
        for (int i = 0; i < points.length; i++) {
            gl.glVertex3f(points[i][0], yBottom, points[i][1]);
        }
        gl.glEnd();

        gl.glBegin(GL2.GL_LINES);
        for (int i = 0; i < points.length; i++) {
            gl.glVertex3f(points[i][0], yBottom, points[i][1]);
            gl.glVertex3f(points[i][0], yTop, points[i][1]);
        }
        gl.glEnd();
    }

    public static void roundedRectangleLine(GL2 gl,
                                            float width,
                                            float depth,
                                            float radius,
                                            int segments) {
        if (radius <= 0.0f) {
            float x = width / 2.0f;
            float z = depth / 2.0f;

            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glVertex3f(-x, 0.0f, -z);
            gl.glVertex3f(x, 0.0f, -z);
            gl.glVertex3f(x, 0.0f, z);
            gl.glVertex3f(-x, 0.0f, z);
            gl.glEnd();
            return;
        }

        float maxRadius = Math.min(width, depth) / 2.0f;
        float r = Math.min(radius, maxRadius);

        float[][] points = roundedRectanglePoints(width, depth, r, segments);

        gl.glBegin(GL2.GL_LINE_LOOP);

        for (int i = 0; i < points.length; i++) {
            gl.glVertex3f(points[i][0], 0.0f, points[i][1]);
        }

        gl.glEnd();
    }

    private static float[][] roundedRectanglePoints(float width,
                                                    float depth,
                                                    float radius,
                                                    int segments) {
        int safeSegments = Math.max(3, segments);

        float halfWidth = width / 2.0f;
        float halfDepth = depth / 2.0f;

        float[][] points = new float[(safeSegments + 1) * 4][2];

        float[][] centers = {
                {halfWidth - radius, halfDepth - radius},
                {-halfWidth + radius, halfDepth - radius},
                {-halfWidth + radius, -halfDepth + radius},
                {halfWidth - radius, -halfDepth + radius}
        };

        float[] startAngles = {
                0.0f,
                90.0f,
                180.0f,
                270.0f
        };

        int index = 0;

        for (int corner = 0; corner < 4; corner++) {
            for (int i = 0; i <= safeSegments; i++) {
                float angle = startAngles[corner] + (90.0f * i / safeSegments);
                double radians = Math.toRadians(angle);

                float x = centers[corner][0] + (float) Math.cos(radians) * radius;
                float z = centers[corner][1] + (float) Math.sin(radians) * radius;

                points[index][0] = x;
                points[index][1] = z;
                index++;
            }
        }

        return points;
    }
}