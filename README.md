# Step 1 - UNI-DS black 3D board box

This is the first clean step of the UNI-DS Computer Graphics project.
No image or texture is used.

The board is a real 3D cuboid with dimensions:

- Width: 29.5 cm
- Depth: 23.7 cm
- Height: 1 cm

Scale used in OpenGL:

```text
1 OpenGL unit = 10 cm
```

So the model is drawn as:

```text
2.95 x 2.37 x 0.10 OpenGL units
```

## Run

Open the folder in IntelliJ as a Gradle project and run:

```text
ogl2.exam.Project
```

## Controls

| Key | Action |
|---|---|
| Left arrow | Rotate camera left |
| Right arrow | Rotate camera right |
| Up arrow | Rotate camera up |
| Down arrow | Rotate camera down |
| Page Up | Zoom in |
| Page Down | Zoom out |
| Esc | Exit |
