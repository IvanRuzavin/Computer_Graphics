package ogl2.exam;

import com.jogamp.opengl.GL2;

public class BoardBox {
    /*
     * Real board body dimensions in centimeters.
     */
    private static final float FULL_WIDTH_CM = 29.5f;
    private static final float FULL_DEPTH_CM = 23.7f;
    private static final float FULL_HEIGHT_CM = 1.0f;

    private static final float EDGE_DEPTH_CM = 2.5f;

    private static final float MIDDLE_WIDTH_CM = 28.5f;
    private static final float MIDDLE_HEIGHT_CM = 0.4f; // 4 mm

    /*
     * Rounded corner radius.
     */
    private static final float CORNER_RADIUS_CM = 0.6f;
    private static final int CORNER_SEGMENTS = 12;

    /*
     * Marking dimensions.
     */
    private static final float YELLOW_PERIMETER_WIDTH_CM = 28.5f;
    private static final float YELLOW_PERIMETER_DEPTH_CM = 22.7f;

    private static final float OFFSET_2MM_CM = 0.2f;
    private static final float OFFSET_1MM_CM = 0.1f;
    private static final float OFFSET_3MM_CM = 0.3f;
    private static final float OFFSET_4MM_CM = 0.4f;
    private static final float OFFSET_6MM_CM = 0.6f;

    private static final float BUTTON_RECT_WIDTH_CM = 10.8f;
    private static final float BUTTON_RECT_DEPTH_CM = 8.4f;

    private static final float PIN_RECT_WIDTH_CM = 10.8f;
    private static final float PIN_RECT_DEPTH_CM = 8.4f;

    private static final float MCU_SOCKET_SIZE_CM = 6.0f;
    private static final float MCU_FROM_YELLOW_BOTTOM_CM = 1.3f;

    private static final float TFT_LEFT_DISTANCE_FROM_YELLOW_CM = 7.8f;
    private static final float TFT_WIDTH_CM = 12.0f;
    private static final float TFT_DEPTH_CM = 7.5f;

    private static final float LCD_WIDTH_CM = 8.0f;
    private static final float LCD_DEPTH_CM = 3.6f;

    /*
     * Target configuration.
     * These are the correct Click boards for mikroBUS 1-5.
     */
    private static final ClickBoardType[] TARGET_CLICK_BOARDS = {
            ClickBoardType.WIFI_CLICK,
            ClickBoardType.OLED_CLICK,
            ClickBoardType.RS232_CLICK,
            ClickBoardType.THUNDER_CLICK,
            ClickBoardType.TEMP_CLICK
    };

    private ClickBoardType selectedClickBoardType = ClickBoardType.WIFI_CLICK;
    private final ClickBoardType[] placedClickBoards = new ClickBoardType[5];
    private int activeMikrobusIndex = 0;

    private int activePlacementIndex = 0;

    /*
     * 0 = Click board
     * 1 = MCU card
     * 2 = TFT display
     * 3 = LCD display
     */
    private int selectedPlacementType = 0;

    private boolean mcuCardPlaced = false;
    private boolean tftDisplayPlaced = false;
    private boolean lcdDisplayPlaced = false;

    /*
     * Top-left small boxes.
     */
    private static final float SMALL_BOX_WIDTH_CM = 6.3f;
    private static final float SMALL_BOX_DEPTH_CM = 5.8f;
    private static final float SMALL_BOX_HEIGHT_CM = 0.6f; // 6 mm

    /*
     * mikroBUS dimensions.
     */
    private static final float MIKROBUS_WIDTH_CM = 2.44f;
    private static final float MIKROBUS_DEPTH_CM = 2.4f;

    /*
     * mikroBUS row spacing.
     * The row should fill the area from the second top-left box
     * toward the yellow line on the right.
     */
    private static final float MIKROBUS_START_OFFSET_CM = 0.6f; // 6 mm from second box
    private static final float MIKROBUS_GAP_CM = 0.6f;          // 6 mm between each mikroBUS

    private static final float MIKROBUS_SIDE_HEIGHT_CM = 0.7f;   // 7 mm
    private static final float MIKROBUS_BOTTOM_HEIGHT_CM = 0.9f; // 2 mm higher
    private static final float MIKROBUS_TOP_HEIGHT_CM = 0.3f;    // floating top part height
    private static final float MIKROBUS_TOP_BOTTOM_GAP_CM = 0.2f; // 2 mm gap above board
    private static final float MIKROBUS_WALL_WIDTH_CM = 0.3f;    // 3 mm

    /*
     * Hole dimensions for mikroBUS.
     */
    private static final float HOLE_SIZE_CM = 0.1f;     // 1 mm x 1 mm
    private static final float HOLE_GAP_CM = 0.1f;      // 1 mm
    private static final int HOLES_PER_SIDE = 8;
    private static final float HOLE_DEPTH_CM = 0.1f;    // visual recessed depth

    /*
     * mikroBUS yellow guide lines
     */
    private static final float MIKROBUS_GUIDE_TOP_OFFSET_CM = 0.1f; // 1 mm from top yellow line

    /*
     * TFT header connector
     */
    private static final float TFT_HEADER_LEFT_OFFSET_CM = 0.6f;  // 6 mm from TFT left line
    private static final float TFT_HEADER_TOP_OFFSET_CM = 0.3f;   // 3 mm from TFT top line
    private static final float TFT_HEADER_WIDTH_CM = 0.4f;        // 4 mm
    private static final float TFT_HEADER_DEPTH_CM = 5.5f;        // 5.5 cm
    private static final float TFT_HEADER_HEIGHT_CM = 0.8f;       // 8 mm

    private static final float TFT_HEADER_KEY_WIDTH_CM = 0.1f;    // 1 mm
    private static final float TFT_HEADER_KEY_DEPTH_CM = 0.2f;    // 2 mm
    private static final float TFT_HEADER_KEY_HEIGHT_CM = TFT_HEADER_HEIGHT_CM;   // visual height

    private static final int TFT_HEADER_PIN_ROWS = 20;
    private static final int TFT_HEADER_PIN_COLS = 2;
    private static final float TFT_HEADER_HOLE_SIZE_CM = 0.1f;    // 1 mm
    private static final float TFT_HEADER_HOLE_GAP_CM = 0.1f;     // 1 mm
    private static final float TFT_HEADER_HOLE_VISUAL_HEIGHT_CM = 0.02f;

    /*
     * LCD header connector
     */
    private static final int LCD_PIN_COUNT = 16;
    private static final float LCD_PIN_LEFT_OFFSET_CM = 0.6f;     // 6 mm from LCD left line
    private static final float LCD_PIN_TOP_OFFSET_CM = 0.2f;      // 2 mm from LCD top line

    private static final float LCD_PIN_SIZE_CM = 0.1f;            // 1 mm square
    private static final float LCD_PIN_HEIGHT_CM = 0.8f;          // 8 mm

    private static final float LCD_PIN_BASE_SIZE_CM = 0.2f;       // 2 mm x 2 mm
    private static final float LCD_PIN_BASE_HEIGHT_CM = 0.3f;     // 3 mm
    private static final float LCD_PIN_GAP_CM = 0.1f;             // 1 mm between pins

    /*
     * MCU card socket internal connectors
     */
    private static final float MCU_CONNECTOR_SIDE_OFFSET_CM = 0.3f;   // 3 mm from left/right MCU rectangle lines
    private static final float MCU_CONNECTOR_WIDTH_CM = 0.4f;         // 4 mm
    private static final float MCU_CONNECTOR_DEPTH_CM = 4.3f;         // 4.3 cm
    private static final float MCU_CONNECTOR_HEIGHT_CM = 0.3f;        // 3 mm high walls
    private static final float MCU_CONNECTOR_WALL_CM = 0.1f;          // 1 mm wall thickness

    private static final float MCU_CONTACT_THICKNESS_CM = 0.02f;      // thin gold contact thickness
    private static final float MCU_CONTACT_HEIGHT_CM = 0.12f;         // gold contact height
    private static final float MCU_CONTACT_LENGTH_CM = 0.1f;          // 1 mm
    private static final float MCU_CONTACT_GAP_CM = 0.1f;             // 1 mm between contacts

    /*
     * Scale:
     * 1 OpenGL unit = 10 centimeters.
     */
    private static final float CM_TO_OPENGL = 0.1f;

    /*
     * Small Y offset so the lines do not flicker with the top surface.
     */
    private static final float LINE_Y_OFFSET = 0.004f;

    public void draw(GL2 gl) {
        float fullWidth = cm(FULL_WIDTH_CM);
        float fullDepth = cm(FULL_DEPTH_CM);
        float fullHeight = cm(FULL_HEIGHT_CM);

        float edgeDepth = cm(EDGE_DEPTH_CM);

        float middleWidth = cm(MIDDLE_WIDTH_CM);
        float middleDepth = cm(FULL_DEPTH_CM - 2.0f * EDGE_DEPTH_CM);
        float middleHeight = cm(MIDDLE_HEIGHT_CM);

        float cornerRadius = cm(CORNER_RADIUS_CM);

        /*
         * Align all parts by TOP.
         */
        float topY = fullHeight / 2.0f;

        float edgeCenterY = topY - fullHeight / 2.0f;
        float middleCenterY = topY - middleHeight / 2.0f;

        float topEdgeZ = fullDepth / 2.0f - edgeDepth / 2.0f;
        float bottomEdgeZ = -fullDepth / 2.0f + edgeDepth / 2.0f;

        /*
         * Board body.
         */
        drawBoardPart(gl,
                0.0f,
                edgeCenterY,
                topEdgeZ,
                fullWidth,
                fullHeight,
                edgeDepth,
                cornerRadius,
                true);

        drawBoardPart(gl,
                0.0f,
                middleCenterY,
                0.0f,
                middleWidth,
                middleHeight,
                middleDepth,
                0.0f,
                false);

        drawBoardPart(gl,
                0.0f,
                edgeCenterY,
                bottomEdgeZ,
                fullWidth,
                fullHeight,
                edgeDepth,
                cornerRadius,
                true);

        /*
         * Surface markings.
         */
        drawTopMarkings(gl, topY + LINE_Y_OFFSET);
        drawMikrobusGuideLines(gl, topY + LINE_Y_OFFSET);

        /*
         * 3D objects on the top.
         */
        drawTopLeftBoxes(gl, topY);
        drawMikrobusRow(gl, topY);
        drawTftHeaderConnector(gl, topY);
        drawLcdHeaderConnector(gl, topY);
        drawMcuCardConnectors(gl, topY);

        /*
         * Transparent target hints.
         */
        drawTargetHints(gl, topY);

        /*
         * User-placed objects.
         */
        drawPlacedClickBoards(gl, topY);
        drawPlacedLargeModules(gl, topY);

        /*
         * Preview and active marker.
         */
        drawSelectedObjectPreview(gl, topY);
        drawActivePlacementSelector(gl, topY + LINE_Y_OFFSET + 0.003f);
    }

    private void drawTargetHints(GL2 gl, float surfaceY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        /*
         * Do not write transparent hints into depth buffer.
         * This prevents hints from hiding real objects.
         */
        gl.glDepthMask(false);

        drawTargetClickBoardHints(gl, surfaceY);
        drawTargetMcuHint(gl, surfaceY);
        drawTargetTftHint(gl, surfaceY);
        drawTargetLcdHint(gl, surfaceY);

        gl.glDepthMask(true);
        gl.glDisable(GL2.GL_BLEND);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private void drawTargetClickBoardHints(GL2 gl, float surfaceY) {
        for (int i = 0; i < TARGET_CLICK_BOARDS.length; i++) {
            ClickBoardType targetType = TARGET_CLICK_BOARDS[i];

            float centerX = getMikrobusCenterX(i);
            float centerZ = getMikrobusCenterZ();

            float guideDepth = getMikrobusGuideDepth();

            float boardDepth = getTargetClickBoardDepth(targetType, guideDepth);
            float boardCenterZ = (boardDepth - MIKROBUS_DEPTH_CM) / 2.0f;

            /*
             * Same vertical positioning as ClickBoard.java.
             */
            float pcbThicknessCm = 0.12f;
            float legBottomYcm = -0.05f;
            float legHeightCm = 0.8f;

            float pcbBottomYcm = legBottomYcm + legHeightCm;
            float pcbCenterYcm = pcbBottomYcm + pcbThicknessCm / 2.0f;

            gl.glPushMatrix();
            gl.glTranslatef(sceneX(centerX), surfaceY, cm(centerZ));

            /*
             * Almost invisible colored Click board hint.
             */
            gl.glColor4f(
                    targetType.getRed(),
                    targetType.getGreen(),
                    targetType.getBlue(),
                    0.22f
            );

            gl.glPushMatrix();
            gl.glTranslatef(0.0f, cm(pcbCenterYcm), cm(boardCenterZ));

            Shape.cuboid(gl,
                    cm(MIKROBUS_WIDTH_CM),
                    cm(pcbThicknessCm),
                    cm(boardDepth));

            gl.glPopMatrix();

            gl.glPopMatrix();
        }
    }

    private float getTargetClickBoardDepth(ClickBoardType type, float guideDepth) {
        if (type.getSize() == ClickBoardSize.LONG) {
            return guideDepth;
        }

        if (type.getSize() == ClickBoardSize.MEDIUM) {
            return guideDepth / 2.0f;
        }

        return MIKROBUS_DEPTH_CM;
    }

    private void drawTargetMcuHint(GL2 gl, float surfaceY) {
        gl.glPushMatrix();
        gl.glTranslatef(sceneX(0.0f), surfaceY, cm(getMcuCenterZ()));

        /*
         * MCU target hint:
         * transparent grey card + transparent white legs.
         */

        float cardSizeCm = MCU_SOCKET_SIZE_CM;
        float cardHeightCm = 0.2f; // 2 mm

        float legWidthCm = MCU_CONNECTOR_WIDTH_CM;
        float legDepthCm = MCU_CONNECTOR_DEPTH_CM;
        float legHeightCm = MCU_CONNECTOR_HEIGHT_CM;

        float leftLegX =
                -MCU_SOCKET_SIZE_CM / 2.0f
                        + MCU_CONNECTOR_SIDE_OFFSET_CM
                        + MCU_CONNECTOR_WIDTH_CM / 2.0f;

        float rightLegX =
                MCU_SOCKET_SIZE_CM / 2.0f
                        - MCU_CONNECTOR_SIDE_OFFSET_CM
                        - MCU_CONNECTOR_WIDTH_CM / 2.0f;

        /*
         * Transparent white legs.
         */
        gl.glColor4f(0.95f, 0.95f, 0.95f, 0.28f);

        gl.glPushMatrix();
        gl.glTranslatef(cm(leftLegX), cm(legHeightCm / 2.0f), 0.0f);
        Shape.cuboid(gl,
                cm(legWidthCm),
                cm(legHeightCm),
                cm(legDepthCm));
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glTranslatef(cm(rightLegX), cm(legHeightCm / 2.0f), 0.0f);
        Shape.cuboid(gl,
                cm(legWidthCm),
                cm(legHeightCm),
                cm(legDepthCm));
        gl.glPopMatrix();

        /*
         * Transparent MCU card body.
         */
        gl.glColor4f(0.55f, 0.55f, 0.60f, 0.32f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f,
                cm(legHeightCm + cardHeightCm / 2.0f),
                0.0f);

        Shape.cuboid(gl,
                cm(cardSizeCm),
                cm(cardHeightCm),
                cm(cardSizeCm));

        gl.glPopMatrix();

        /*
         * Thin visible outline so the hint is easy to notice.
         */
        gl.glColor4f(0.8f, 0.8f, 0.85f, 0.55f);
        gl.glLineWidth(1.5f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f,
                cm(legHeightCm + cardHeightCm / 2.0f),
                0.0f);

        Shape.wireCuboid(gl,
                cm(cardSizeCm),
                cm(cardHeightCm + 0.02f),
                cm(cardSizeCm));

        gl.glPopMatrix();

        gl.glLineWidth(1.0f);

        gl.glPopMatrix();
    }

    private void drawTargetTftHint(GL2 gl, float surfaceY) {
        gl.glPushMatrix();
        gl.glTranslatef(sceneX(getTftCenterX()), surfaceY, cm(getTftCenterZ()));

        /*
         * TFT target ghost.
         */
        gl.glColor4f(0.0f, 0.35f, 0.85f, 0.18f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, cm(0.85f), 0.0f);

        Shape.cuboid(gl,
                cm(TFT_WIDTH_CM),
                cm(1.0f),
                cm(TFT_DEPTH_CM));

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private void drawTargetLcdHint(GL2 gl, float surfaceY) {
        gl.glPushMatrix();
        gl.glTranslatef(sceneX(getLcdCenterX()), surfaceY, cm(getLcdCenterZ()));

        /*
         * LCD target ghost.
         */
        gl.glColor4f(0.4f, 0.55f, 1.0f, 0.18f);

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, cm(1.1f), 0.0f);

        Shape.cuboid(gl,
                cm(LCD_WIDTH_CM),
                cm(1.5f),
                cm(LCD_DEPTH_CM));

        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    public String checkSolution() {
        int score = 0;
        int total = 8;

        /*
         * Check mikroBUS Click boards.
         */
        for (int i = 0; i < TARGET_CLICK_BOARDS.length; i++) {
            if (placedClickBoards[i] == TARGET_CLICK_BOARDS[i]) {
                score++;
            }
        }

        /*
         * Check MCU/TFT/LCD.
         */
        if (mcuCardPlaced) {
            score++;
        }

        if (tftDisplayPlaced) {
            score++;
        }

        if (lcdDisplayPlaced) {
            score++;
        }

        if (score == total) {
            return "SUCCESS! All modules are placed correctly. Score: "
                    + score + "/" + total;
        }

        return "Current score: " + score + "/" + total
                + ". Some modules are missing or placed incorrectly.";
    }

    private void drawActivePlacementSelector(GL2 gl, float lineY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_LINE_STIPPLE);

        gl.glColor3f(0.0f, 1.0f, 0.35f);
        gl.glLineWidth(2.5f);

        if (activePlacementIndex < 5) {
            drawRoundedLine(gl,
                    getMikrobusCenterX(activePlacementIndex),
                    getMikrobusGuideCenterZ(),
                    MIKROBUS_WIDTH_CM,
                    getMikrobusGuideDepth(),
                    cm(CORNER_RADIUS_CM),
                    lineY);
        } else if (activePlacementIndex == 5) {
            drawRoundedLine(gl,
                    0.0f,
                    getMcuCenterZ(),
                    MCU_SOCKET_SIZE_CM,
                    MCU_SOCKET_SIZE_CM,
                    cm(CORNER_RADIUS_CM),
                    lineY);
        } else if (activePlacementIndex == 6) {
            drawRoundedLine(gl,
                    getTftCenterX(),
                    getTftCenterZ(),
                    TFT_WIDTH_CM,
                    TFT_DEPTH_CM,
                    cm(CORNER_RADIUS_CM),
                    lineY);
        } else if (activePlacementIndex == 7) {
            drawRoundedLine(gl,
                    getLcdCenterX(),
                    getLcdCenterZ(),
                    LCD_WIDTH_CM,
                    LCD_DEPTH_CM,
                    cm(CORNER_RADIUS_CM),
                    lineY);
        }

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private float getMikrobusGuideCenterZ() {
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float boxTop = yellowTop - OFFSET_1MM_CM;
        float boxBottom = boxTop - SMALL_BOX_DEPTH_CM;

        float guideTop = yellowTop - MIKROBUS_GUIDE_TOP_OFFSET_CM;

        return (guideTop + boxBottom) / 2.0f;
    }

    private float getMcuCenterZ() {
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        return yellowBottom + MCU_FROM_YELLOW_BOTTOM_CM + MCU_SOCKET_SIZE_CM / 2.0f;
    }

    private float getTftCenterX() {
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;

        float tftLeft = yellowLeft + TFT_LEFT_DISTANCE_FROM_YELLOW_CM;

        return tftLeft + TFT_WIDTH_CM / 2.0f;
    }

    private float getTftCenterZ() {
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float buttonCenterZ = yellowBottom + OFFSET_2MM_CM + BUTTON_RECT_DEPTH_CM / 2.0f;
        float lowerRectTop = buttonCenterZ + BUTTON_RECT_DEPTH_CM / 2.0f;

        float tftBottom = lowerRectTop + OFFSET_1MM_CM;

        return tftBottom + TFT_DEPTH_CM / 2.0f;
    }

    private float getLcdCenterX() {
        float yellowRight = YELLOW_PERIMETER_WIDTH_CM / 2.0f;

        float lcdRight = yellowRight - OFFSET_2MM_CM;

        return lcdRight - LCD_WIDTH_CM / 2.0f;
    }

    private float getLcdCenterZ() {
        float tftTop = getTftCenterZ() + TFT_DEPTH_CM / 2.0f;

        return tftTop - LCD_DEPTH_CM / 2.0f;
    }

    private void drawPlacedLargeModules(GL2 gl, float surfaceY) {
        if (mcuCardPlaced) {
            gl.glPushMatrix();
            gl.glTranslatef(sceneX(0.0f), surfaceY, cm(getMcuCenterZ()));

            new McuCard().draw(gl);

            gl.glPopMatrix();
        }

        if (tftDisplayPlaced) {
            gl.glPushMatrix();
            gl.glTranslatef(sceneX(getTftCenterX()), surfaceY, cm(getTftCenterZ()));

            new TftDisplay().draw(gl);

            gl.glPopMatrix();
        }

        if (lcdDisplayPlaced) {
            gl.glPushMatrix();
            gl.glTranslatef(sceneX(getLcdCenterX()), surfaceY, cm(getLcdCenterZ()));

            new LcdDisplay().draw(gl);

            gl.glPopMatrix();
        }
    }

    public void selectMcuCard() {
        selectedPlacementType = 1;
        System.out.println("Selected: MCU Card");
    }

    public void selectTftDisplay() {
        selectedPlacementType = 2;
        System.out.println("Selected: TFT Display");
    }

    public void selectLcdDisplay() {
        selectedPlacementType = 3;
        System.out.println("Selected: LCD Display");
    }

    private float getMikrobusGuideDepth() {
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float boxTop = yellowTop - OFFSET_1MM_CM;
        float boxBottom = boxTop - SMALL_BOX_DEPTH_CM;

        float guideTop = yellowTop - MIKROBUS_GUIDE_TOP_OFFSET_CM;

        return guideTop - boxBottom;
    }

    private void drawSelectedObjectPreview(GL2 gl, float surfaceY) {
        /*
         * Preview of the currently selected object.
         * It appears next to the board box.
         */
        float previewCenterX = -FULL_WIDTH_CM / 2.0f - 3.5f;
        float previewCenterZ = 0.0f;

        gl.glPushMatrix();
        gl.glTranslatef(sceneX(previewCenterX), surfaceY, cm(previewCenterZ));

        /*
         * 0 = Click board
         * 1 = MCU card
         * 2 = TFT display
         * 3 = LCD display
         */
        if (selectedPlacementType == 0) {
            ClickBoard preview = new ClickBoard(selectedClickBoardType);

            preview.draw(gl,
                    MIKROBUS_WIDTH_CM,
                    MIKROBUS_DEPTH_CM,
                    MIKROBUS_WALL_WIDTH_CM,
                    getMikrobusGuideDepth());
        } else if (selectedPlacementType == 1) {
            /*
             * MCU Card preview.
             * Slightly scaled down so it fits nicely next to the board.
             */
            gl.glPushMatrix();
            gl.glScalef(0.75f, 0.75f, 0.75f);

            new McuCard().draw(gl);

            gl.glPopMatrix();
        } else if (selectedPlacementType == 2) {
            /*
             * TFT preview.
             * TFT is large, so preview is scaled down.
             */
            gl.glPushMatrix();
            gl.glScalef(0.45f, 0.45f, 0.45f);

            new TftDisplay().draw(gl);

            gl.glPopMatrix();
        } else if (selectedPlacementType == 3) {
            /*
             * LCD preview.
             */
            gl.glPushMatrix();
            gl.glScalef(0.60f, 0.60f, 0.60f);

            new LcdDisplay().draw(gl);

            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }

    private void drawPlacedClickBoards(GL2 gl, float surfaceY) {
        for (int i = 0; i < 5; i++) {
            ClickBoardType type = placedClickBoards[i];

            if (type == null) {
                continue;
            }

            float centerX = getMikrobusCenterX(i);
            float centerZ = getMikrobusCenterZ();

            gl.glPushMatrix();
            gl.glTranslatef(sceneX(centerX), surfaceY, cm(centerZ));

            ClickBoard clickBoard = new ClickBoard(type);
            clickBoard.draw(gl,
                    MIKROBUS_WIDTH_CM,
                    MIKROBUS_DEPTH_CM,
                    MIKROBUS_WALL_WIDTH_CM,
                    getMikrobusGuideDepth());

            gl.glPopMatrix();
        }

        drawActiveMikrobusSelector(gl, surfaceY + LINE_Y_OFFSET + 0.003f);
    }

    private void drawActiveMikrobusSelector(GL2 gl, float lineY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_LINE_STIPPLE);

        gl.glColor3f(0.0f, 1.0f, 0.35f);
        gl.glLineWidth(2.5f);

        drawRoundedLine(gl,
                getMikrobusCenterX(activeMikrobusIndex),
                getMikrobusCenterZ(),
                MIKROBUS_WIDTH_CM,
                MIKROBUS_DEPTH_CM,
                cm(CORNER_RADIUS_CM),
                lineY);

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private float getMikrobusCenterX(int index) {
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;

        float box1Left = yellowLeft + OFFSET_3MM_CM;
        float box2Left = box1Left + SMALL_BOX_WIDTH_CM + OFFSET_2MM_CM;
        float box2Right = box2Left + SMALL_BOX_WIDTH_CM;

        float firstMikroLeft = box2Right + MIKROBUS_START_OFFSET_CM;

        float currentLeft = firstMikroLeft + index * (MIKROBUS_WIDTH_CM + MIKROBUS_GAP_CM);

        return currentLeft + MIKROBUS_WIDTH_CM / 2.0f;
    }

    private float getMikrobusCenterZ() {
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float boxTop = yellowTop - OFFSET_1MM_CM;
        float boxBottom = boxTop - SMALL_BOX_DEPTH_CM;

        float mikroBottom = boxBottom;

        return mikroBottom + MIKROBUS_DEPTH_CM / 2.0f;
    }

    public void selectClickBoard(int index) {
        ClickBoardType[] boards = ClickBoardType.values();

        if (index >= 0 && index < boards.length) {
            selectedClickBoardType = boards[index];

            /*
             * Important:
             * 0 means the selected object is a Click board.
             * Without this, preview stays stuck on MCU/TFT/LCD.
             */
            selectedPlacementType = 0;

            System.out.println("Selected Click board: " + selectedClickBoardType.getDisplayName());
        }
    }

    public void nextMikrobus() {
        activePlacementIndex++;

        if (activePlacementIndex >= 8) {
            activePlacementIndex = 0;
        }

        syncActiveMikrobusIndex();
        printActivePlacement();
    }

    public void previousMikrobus() {
        activePlacementIndex--;

        if (activePlacementIndex < 0) {
            activePlacementIndex = 7;
        }

        syncActiveMikrobusIndex();
        printActivePlacement();
    }

    private void syncActiveMikrobusIndex() {
        if (activePlacementIndex >= 0 && activePlacementIndex < 5) {
            activeMikrobusIndex = activePlacementIndex;
        }
    }

    private void printActivePlacement() {
        if (activePlacementIndex < 5) {
            System.out.println("Active placement: mikroBUS " + (activePlacementIndex + 1));
        } else if (activePlacementIndex == 5) {
            System.out.println("Active placement: MCU Card rectangle");
        } else if (activePlacementIndex == 6) {
            System.out.println("Active placement: TFT rectangle");
        } else if (activePlacementIndex == 7) {
            System.out.println("Active placement: LCD rectangle");
        }
    }

    public void placeSelectedClickBoard() {
        if (activePlacementIndex < 5) {
            if (selectedPlacementType == 0) {
                placedClickBoards[activePlacementIndex] = selectedClickBoardType;

                System.out.println("Placed "
                        + selectedClickBoardType.getDisplayName()
                        + " on mikroBUS "
                        + (activePlacementIndex + 1));
            } else {
                System.out.println("Selected object cannot be placed on mikroBUS.");
            }

            return;
        }

        if (activePlacementIndex == 5) {
            if (selectedPlacementType == 1) {
                mcuCardPlaced = true;
                System.out.println("Placed MCU card.");
            } else {
                System.out.println("Only MCU card can be placed here.");
            }

            return;
        }

        if (activePlacementIndex == 6) {
            if (selectedPlacementType == 2) {
                tftDisplayPlaced = true;
                System.out.println("Placed TFT display.");
            } else {
                System.out.println("Only TFT display can be placed here.");
            }

            return;
        }

        if (activePlacementIndex == 7) {
            if (selectedPlacementType == 3) {
                lcdDisplayPlaced = true;
                System.out.println("Placed LCD display.");
            } else {
                System.out.println("Only LCD display can be placed here.");
            }
        }
    }

    public void removeClickBoardFromActiveMikrobus() {
        if (activePlacementIndex < 5) {
            placedClickBoards[activePlacementIndex] = null;

            System.out.println("Removed Click board from mikroBUS "
                    + (activePlacementIndex + 1));

            return;
        }

        if (activePlacementIndex == 5) {
            mcuCardPlaced = false;
            System.out.println("Removed MCU card.");
            return;
        }

        if (activePlacementIndex == 6) {
            tftDisplayPlaced = false;
            System.out.println("Removed TFT display.");
            return;
        }

        if (activePlacementIndex == 7) {
            lcdDisplayPlaced = false;
            System.out.println("Removed LCD display.");
        }
    }

    private void drawMcuCardConnectors(GL2 gl, float surfaceY) {
        /*
         * MCU placement rectangle is already defined as:
         * 6 cm x 6 cm
         * centered horizontally
         * offset 1.3 cm from yellow bottom line
         */
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float mcuCenterX = 0.0f;
        float mcuCenterZ = yellowBottom + MCU_FROM_YELLOW_BOTTOM_CM + MCU_SOCKET_SIZE_CM / 2.0f;

        float mcuLeft = mcuCenterX - MCU_SOCKET_SIZE_CM / 2.0f;
        float mcuRight = mcuCenterX + MCU_SOCKET_SIZE_CM / 2.0f;

        float leftConnectorCenterX =
                mcuLeft + MCU_CONNECTOR_SIDE_OFFSET_CM + MCU_CONNECTOR_WIDTH_CM / 2.0f;

        float rightConnectorCenterX =
                mcuRight - MCU_CONNECTOR_SIDE_OFFSET_CM - MCU_CONNECTOR_WIDTH_CM / 2.0f;

        drawSingleMcuConnector(gl, leftConnectorCenterX, mcuCenterZ, surfaceY);
        drawSingleMcuConnector(gl, rightConnectorCenterX, mcuCenterZ, surfaceY);
    }

    private void drawSingleMcuConnector(GL2 gl,
                                        float centerXcm,
                                        float centerZcm,
                                        float surfaceY) {
        gl.glPushMatrix();
        gl.glTranslatef(sceneX(centerXcm), surfaceY, cm(centerZcm));

        float outerW = MCU_CONNECTOR_WIDTH_CM;
        float outerD = MCU_CONNECTOR_DEPTH_CM;
        float h = MCU_CONNECTOR_HEIGHT_CM;
        float wall = MCU_CONNECTOR_WALL_CM;

        /*
         * White connector frame with recess inside.
         */
        gl.glColor3f(0.96f, 0.96f, 0.96f);

        // Left wall
        drawLocalCuboid(gl,
                -outerW / 2.0f + wall / 2.0f,
                h / 2.0f,
                0.0f,
                wall,
                h,
                outerD);

        // Right wall
        drawLocalCuboid(gl,
                outerW / 2.0f - wall / 2.0f,
                h / 2.0f,
                0.0f,
                wall,
                h,
                outerD);

        // Top wall
        drawLocalCuboid(gl,
                0.0f,
                h / 2.0f,
                outerD / 2.0f - wall / 2.0f,
                outerW,
                h,
                wall);

        // Bottom wall
        drawLocalCuboid(gl,
                0.0f,
                h / 2.0f,
                -outerD / 2.0f + wall / 2.0f,
                outerW,
                h,
                wall);

        /*
         * Optional dark recessed floor to make the cavity visible.
         */
        gl.glColor3f(0.08f, 0.08f, 0.09f);
        drawLocalCuboid(gl,
                0.0f,
                0.01f,
                0.0f,
                outerW - 2.0f * wall,
                0.02f,
                outerD - 2.0f * wall);

        /*
         * Gold contact rows on the INNER faces of left/right walls.
         */
        drawMcuConnectorContacts(gl, outerW, outerD, h, wall);

        gl.glPopMatrix();
    }

    private void drawMcuConnectorContacts(GL2 gl,
                                          float outerW,
                                          float outerD,
                                          float h,
                                          float wall) {
        gl.glColor3f(0.87f, 0.70f, 0.20f);

        float usableDepth = outerD - 2.0f * wall;

        int contactCount = (int) ((usableDepth + MCU_CONTACT_GAP_CM)
                / (MCU_CONTACT_LENGTH_CM + MCU_CONTACT_GAP_CM));

        if (contactCount < 1) {
            contactCount = 1;
        }

        float usedDepth = contactCount * MCU_CONTACT_LENGTH_CM
                + (contactCount - 1) * MCU_CONTACT_GAP_CM;

        float firstContactCenterZ = usedDepth / 2.0f - MCU_CONTACT_LENGTH_CM / 2.0f;

        /*
         * Gold contacts sit on inner faces of the two long side walls.
         */
        float innerLeftX =
                -outerW / 2.0f + wall + MCU_CONTACT_THICKNESS_CM / 2.0f;

        float innerRightX =
                outerW / 2.0f - wall - MCU_CONTACT_THICKNESS_CM / 2.0f;

        float contactCenterY = h / 2.0f;

        for (int i = 0; i < contactCount; i++) {
            float z = firstContactCenterZ
                    - i * (MCU_CONTACT_LENGTH_CM + MCU_CONTACT_GAP_CM);

            // Left inner contact row
            drawLocalCuboid(gl,
                    innerLeftX,
                    contactCenterY,
                    z,
                    MCU_CONTACT_THICKNESS_CM,
                    MCU_CONTACT_HEIGHT_CM,
                    MCU_CONTACT_LENGTH_CM);

            // Right inner contact row
            drawLocalCuboid(gl,
                    innerRightX,
                    contactCenterY,
                    z,
                    MCU_CONTACT_THICKNESS_CM,
                    MCU_CONTACT_HEIGHT_CM,
                    MCU_CONTACT_LENGTH_CM);
        }
    }

    private void drawMikrobusGuideLines(GL2 gl, float lineY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(1.0f, 0.82f, 0.0f);
        gl.glLineWidth(1.5f);
        gl.glEnable(GL2.GL_LINE_STIPPLE);
        gl.glLineStipple(1, (short) 0x00FF);

        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float box1Left = yellowLeft + OFFSET_3MM_CM;
        float box2Left = box1Left + SMALL_BOX_WIDTH_CM + OFFSET_2MM_CM;
        float box2Right = box2Left + SMALL_BOX_WIDTH_CM;

        float boxTop = yellowTop - OFFSET_1MM_CM;
        float boxBottom = boxTop - SMALL_BOX_DEPTH_CM;

        float mikroBottom = boxBottom;
        float guideTop = yellowTop - MIKROBUS_GUIDE_TOP_OFFSET_CM;

        float guideDepth = guideTop - mikroBottom;
        float guideCenterZ = (guideTop + mikroBottom) / 2.0f;

        float firstMikroLeft = box2Right + MIKROBUS_START_OFFSET_CM;

        for (int i = 0; i < 5; i++) {
            float currentLeft = firstMikroLeft + i * (MIKROBUS_WIDTH_CM + MIKROBUS_GAP_CM);
            float centerX = currentLeft + MIKROBUS_WIDTH_CM / 2.0f;

            drawRoundedLine(gl,
                    centerX,
                    guideCenterZ,
                    MIKROBUS_WIDTH_CM,
                    guideDepth,
                    cm(CORNER_RADIUS_CM),
                    lineY);
        }

        gl.glDisable(GL2.GL_LINE_STIPPLE);
        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private void drawTftHeaderConnector(GL2 gl, float surfaceY) {
        /*
         * Recompute TFT rectangle position.
         */
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float buttonCenterZ = yellowBottom + OFFSET_2MM_CM + BUTTON_RECT_DEPTH_CM / 2.0f;
        float lowerRectTop = buttonCenterZ + BUTTON_RECT_DEPTH_CM / 2.0f;

        float tftLeft = yellowLeft + TFT_LEFT_DISTANCE_FROM_YELLOW_CM;
        float tftBottom = lowerRectTop + OFFSET_1MM_CM;
        float tftTop = tftBottom + TFT_DEPTH_CM;

        /*
         * TFT header placement.
         */
        float headerLeft = tftLeft + TFT_HEADER_LEFT_OFFSET_CM;
        float headerTop = tftTop - TFT_HEADER_TOP_OFFSET_CM;

        float headerCenterX = headerLeft + TFT_HEADER_WIDTH_CM / 2.0f;
        float headerCenterZ = headerTop - TFT_HEADER_DEPTH_CM / 2.0f;

        gl.glPushMatrix();
        gl.glTranslatef(sceneX(headerCenterX), surfaceY, cm(headerCenterZ));

        // Main connector body
        gl.glColor3f(0.14f, 0.14f, 0.16f);
        drawLocalCuboid(gl,
                0.0f,
                TFT_HEADER_HEIGHT_CM / 2.0f,
                0.0f,
                TFT_HEADER_WIDTH_CM,
                TFT_HEADER_HEIGHT_CM,
                TFT_HEADER_DEPTH_CM);

        // Direction key on the VISUAL right side.
        // Because the board layout is mirrored with sceneX(...),
        // this piece must use negative local X here.
        gl.glColor3f(0.10f, 0.10f, 0.12f);
        drawLocalCuboid(gl,
                -TFT_HEADER_WIDTH_CM / 2.0f - TFT_HEADER_KEY_WIDTH_CM / 2.0f,
                TFT_HEADER_HEIGHT_CM / 2.0f,
                0.0f,
                TFT_HEADER_KEY_WIDTH_CM,
                TFT_HEADER_KEY_HEIGHT_CM,
                TFT_HEADER_KEY_DEPTH_CM);

        // 40 holes: 20 rows x 2 columns on top
        gl.glColor3f(0.02f, 0.02f, 0.02f);

        float zPatternLength =
                TFT_HEADER_PIN_ROWS * TFT_HEADER_HOLE_SIZE_CM
                        + (TFT_HEADER_PIN_ROWS - 1) * TFT_HEADER_HOLE_GAP_CM;

        float zMargin = (TFT_HEADER_DEPTH_CM - zPatternLength) / 2.0f;
        float firstHoleZ = TFT_HEADER_DEPTH_CM / 2.0f
                - zMargin
                - TFT_HEADER_HOLE_SIZE_CM / 2.0f;

        float xPatternWidth =
                TFT_HEADER_PIN_COLS * TFT_HEADER_HOLE_SIZE_CM
                        + (TFT_HEADER_PIN_COLS - 1) * TFT_HEADER_HOLE_GAP_CM;

        float xStart = -xPatternWidth / 2.0f + TFT_HEADER_HOLE_SIZE_CM / 2.0f;
        float topHoleY = TFT_HEADER_HEIGHT_CM + TFT_HEADER_HOLE_VISUAL_HEIGHT_CM / 2.0f;

        for (int row = 0; row < TFT_HEADER_PIN_ROWS; row++) {
            float z = firstHoleZ - row * (TFT_HEADER_HOLE_SIZE_CM + TFT_HEADER_HOLE_GAP_CM);

            for (int col = 0; col < TFT_HEADER_PIN_COLS; col++) {
                float x = xStart + col * (TFT_HEADER_HOLE_SIZE_CM + TFT_HEADER_HOLE_GAP_CM);

                drawLocalCuboid(gl,
                        x,
                        topHoleY,
                        z,
                        TFT_HEADER_HOLE_SIZE_CM,
                        TFT_HEADER_HOLE_VISUAL_HEIGHT_CM,
                        TFT_HEADER_HOLE_SIZE_CM);
            }
        }

        gl.glPopMatrix();
    }

    private void drawLcdHeaderConnector(GL2 gl, float surfaceY) {
        /*
         * Recompute LCD rectangle position.
         */
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowRight = YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        float buttonCenterZ = yellowBottom + OFFSET_2MM_CM + BUTTON_RECT_DEPTH_CM / 2.0f;
        float lowerRectTop = buttonCenterZ + BUTTON_RECT_DEPTH_CM / 2.0f;

        float tftLeft = yellowLeft + TFT_LEFT_DISTANCE_FROM_YELLOW_CM;
        float tftBottom = lowerRectTop + OFFSET_1MM_CM;
        float tftTop = tftBottom + TFT_DEPTH_CM;

        float lcdRight = yellowRight - OFFSET_2MM_CM;
        float lcdLeft = lcdRight - LCD_WIDTH_CM;
        float lcdTop = tftTop;

        /*
         * Single horizontal row of 16 pins.
         */
        float firstBaseLeft = lcdLeft + LCD_PIN_LEFT_OFFSET_CM;
        float pinTop = lcdTop - LCD_PIN_TOP_OFFSET_CM;

        float baseCenterZ = pinTop - LCD_PIN_BASE_SIZE_CM / 2.0f;
        float pitch = LCD_PIN_BASE_SIZE_CM + LCD_PIN_GAP_CM;

        gl.glPushMatrix();
        gl.glTranslatef(0.0f, surfaceY, 0.0f);

        for (int i = 0; i < LCD_PIN_COUNT; i++) {
            float baseLeft = firstBaseLeft + i * pitch;
            float centerX = baseLeft + LCD_PIN_BASE_SIZE_CM / 2.0f;

            // Base block at bottom of each pin
            gl.glColor3f(0.16f, 0.16f, 0.18f);
            gl.glPushMatrix();
            gl.glTranslatef(sceneX(centerX), 0.0f, cm(baseCenterZ));
            drawLocalCuboid(gl,
                    0.0f,
                    LCD_PIN_BASE_HEIGHT_CM / 2.0f,
                    0.0f,
                    LCD_PIN_BASE_SIZE_CM,
                    LCD_PIN_BASE_HEIGHT_CM,
                    LCD_PIN_BASE_SIZE_CM);
            gl.glPopMatrix();

            // Pin centered on top of the base
            gl.glColor3f(0.75f, 0.75f, 0.78f);
            gl.glPushMatrix();
            gl.glTranslatef(sceneX(centerX), 0.0f, cm(baseCenterZ));
            drawLocalCuboid(gl,
                    0.0f,
                    LCD_PIN_BASE_HEIGHT_CM + LCD_PIN_HEIGHT_CM / 2.0f,
                    0.0f,
                    LCD_PIN_SIZE_CM,
                    LCD_PIN_HEIGHT_CM,
                    LCD_PIN_SIZE_CM);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }

    private void drawBoardPart(GL2 gl,
                               float x,
                               float y,
                               float z,
                               float width,
                               float height,
                               float depth,
                               float cornerRadius,
                               boolean rounded) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        gl.glColor3f(0.01f, 0.012f, 0.016f);

        if (rounded) {
            Shape.roundedCuboid(gl, width, height, depth, cornerRadius, CORNER_SEGMENTS);
        } else {
            Shape.cuboid(gl, width, height, depth);
        }

        gl.glPopMatrix();
    }

    private void drawTopMarkings(GL2 gl, float lineY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glDisable(GL2.GL_LIGHTING);

        float radius = cm(CORNER_RADIUS_CM);

        /*
         * Yellow perimeter:
         * 28.5 cm x 22.7 cm
         */
        gl.glColor3f(1.0f, 0.82f, 0.0f);
        gl.glLineWidth(3.0f);

        drawRoundedLine(gl,
                0.0f,
                0.0f,
                YELLOW_PERIMETER_WIDTH_CM,
                YELLOW_PERIMETER_DEPTH_CM,
                radius,
                lineY);

        /*
         * Helper values.
         */
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowRight = YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowBottom = -YELLOW_PERIMETER_DEPTH_CM / 2.0f;
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        /*
         * Lower-left white rectangle:
         * 10.8 cm x 8.4 cm
         * 2 mm offset from left and bottom yellow line.
         */
        float buttonCenterX = yellowLeft + OFFSET_2MM_CM + BUTTON_RECT_WIDTH_CM / 2.0f;
        float buttonCenterZ = yellowBottom + OFFSET_2MM_CM + BUTTON_RECT_DEPTH_CM / 2.0f;

        /*
         * Lower-right white rectangle:
         * 10.8 cm x 8.4 cm
         * 2 mm offset from right and bottom yellow line.
         */
        float pinCenterX = yellowRight - OFFSET_2MM_CM - PIN_RECT_WIDTH_CM / 2.0f;
        float pinCenterZ = yellowBottom + OFFSET_2MM_CM + PIN_RECT_DEPTH_CM / 2.0f;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glLineWidth(2.0f);

        drawRoundedLine(gl,
                buttonCenterX,
                buttonCenterZ,
                BUTTON_RECT_WIDTH_CM,
                BUTTON_RECT_DEPTH_CM,
                radius,
                lineY);

        drawRoundedLine(gl,
                pinCenterX,
                pinCenterZ,
                PIN_RECT_WIDTH_CM,
                PIN_RECT_DEPTH_CM,
                radius,
                lineY);

        /*
         * MCU socket:
         * 6 cm x 6 cm
         */
        float mcuCenterX = 0.0f;
        float mcuCenterZ = yellowBottom + MCU_FROM_YELLOW_BOTTOM_CM + MCU_SOCKET_SIZE_CM / 2.0f;

        gl.glEnable(GL2.GL_LINE_STIPPLE);
        gl.glLineStipple(1, (short) 0x00FF);

        drawRoundedLine(gl,
                mcuCenterX,
                mcuCenterZ,
                MCU_SOCKET_SIZE_CM,
                MCU_SOCKET_SIZE_CM,
                radius,
                lineY);

        gl.glDisable(GL2.GL_LINE_STIPPLE);

        /*
         * TFT rectangle:
         * 12 cm x 7.5 cm
         */
        float lowerRectTop = buttonCenterZ + BUTTON_RECT_DEPTH_CM / 2.0f;

        float tftLeft = yellowLeft + TFT_LEFT_DISTANCE_FROM_YELLOW_CM;
        float tftBottom = lowerRectTop + OFFSET_1MM_CM;

        float tftCenterX = tftLeft + TFT_WIDTH_CM / 2.0f;
        float tftCenterZ = tftBottom + TFT_DEPTH_CM / 2.0f;

        drawRoundedLine(gl,
                tftCenterX,
                tftCenterZ,
                TFT_WIDTH_CM,
                TFT_DEPTH_CM,
                radius,
                lineY);

        /*
         * LCD rectangle:
         * 8 cm x 3.6 cm
         */
        float tftTop = tftCenterZ + TFT_DEPTH_CM / 2.0f;

        float lcdRight = yellowRight - OFFSET_2MM_CM;
        float lcdCenterX = lcdRight - LCD_WIDTH_CM / 2.0f;
        float lcdCenterZ = tftTop - LCD_DEPTH_CM / 2.0f;

        drawRoundedLine(gl,
                lcdCenterX,
                lcdCenterZ,
                LCD_WIDTH_CM,
                LCD_DEPTH_CM,
                radius,
                lineY);

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    private void drawTopLeftBoxes(GL2 gl, float surfaceY) {
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        /*
         * Box 1:
         * 3 mm from left yellow line
         * 1 mm from top yellow line
         */
        float box1Left = yellowLeft + OFFSET_3MM_CM;
        float boxTop = yellowTop - OFFSET_1MM_CM;

        float box1CenterX = box1Left + SMALL_BOX_WIDTH_CM / 2.0f;
        float boxCenterZ = boxTop - SMALL_BOX_DEPTH_CM / 2.0f;

        drawTransparentRoundedBox(gl,
                box1CenterX,
                boxCenterZ,
                SMALL_BOX_WIDTH_CM,
                SMALL_BOX_DEPTH_CM,
                SMALL_BOX_HEIGHT_CM,
                CORNER_RADIUS_CM,
                surfaceY);

        /*
         * Box 2:
         * 2 mm to the right of box 1
         */
        float box2Left = box1Left + SMALL_BOX_WIDTH_CM + OFFSET_2MM_CM;
        float box2CenterX = box2Left + SMALL_BOX_WIDTH_CM / 2.0f;

        drawTransparentRoundedBox(gl,
                box2CenterX,
                boxCenterZ,
                SMALL_BOX_WIDTH_CM,
                SMALL_BOX_DEPTH_CM,
                SMALL_BOX_HEIGHT_CM,
                CORNER_RADIUS_CM,
                surfaceY);
    }

    private void drawTransparentRoundedBox(GL2 gl,
                                           float centerXcm,
                                           float centerZcm,
                                           float widthCm,
                                           float depthCm,
                                           float heightCm,
                                           float radiusCm,
                                           float surfaceY) {
        boolean lightingWasEnabled = gl.glIsEnabled(GL2.GL_LIGHTING);

        gl.glPushMatrix();
        gl.glTranslatef(sceneX(centerXcm),
                surfaceY + cm(heightCm / 2.0f),
                cm(centerZcm));

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDepthMask(false);

        gl.glColor4f(0.18f, 0.18f, 0.20f, 0.55f);
        Shape.roundedCuboid(gl,
                cm(widthCm),
                cm(heightCm),
                cm(depthCm),
                cm(radiusCm),
                CORNER_SEGMENTS);

        gl.glDepthMask(true);
        gl.glDisable(GL2.GL_BLEND);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }

        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor3f(0.35f, 0.35f, 0.38f);
        gl.glLineWidth(1.5f);

        Shape.wireRoundedCuboid(gl,
                cm(widthCm),
                cm(heightCm) + 0.001f,
                cm(depthCm),
                cm(radiusCm),
                CORNER_SEGMENTS);

        gl.glLineWidth(1.0f);

        if (lightingWasEnabled) {
            gl.glEnable(GL2.GL_LIGHTING);
        }

        gl.glPopMatrix();
    }

    private void drawMikrobusRow(GL2 gl, float surfaceY) {
        float yellowLeft = -YELLOW_PERIMETER_WIDTH_CM / 2.0f;
        float yellowTop = YELLOW_PERIMETER_DEPTH_CM / 2.0f;

        /*
         * Recompute second top-left box position.
         */
        float box1Left = yellowLeft + OFFSET_3MM_CM;
        float box2Left = box1Left + SMALL_BOX_WIDTH_CM + OFFSET_2MM_CM;
        float box2Right = box2Left + SMALL_BOX_WIDTH_CM;

        /*
         * Align mikroBUS sockets with the LOWER line of the small boxes.
         */
        float boxTop = yellowTop - OFFSET_1MM_CM;
        float boxBottom = boxTop - SMALL_BOX_DEPTH_CM;

        float mikroBottom = boxBottom;
        float mikroCenterZ = mikroBottom + MIKROBUS_DEPTH_CM / 2.0f;

        /*
         * Place 5 mikroBUS sockets in the area after the second box.
         */
        float firstMikroLeft = box2Right + MIKROBUS_START_OFFSET_CM;

        for (int i = 0; i < 5; i++) {
            float currentLeft = firstMikroLeft + i * (MIKROBUS_WIDTH_CM + MIKROBUS_GAP_CM);
            float centerX = currentLeft + MIKROBUS_WIDTH_CM / 2.0f;

            drawMikrobus(gl, centerX, mikroCenterZ, surfaceY);
        }
    }

    private void drawMikrobus(GL2 gl, float centerXcm, float centerZcm, float surfaceY) {
        gl.glPushMatrix();
        gl.glTranslatef(sceneX(centerXcm), surfaceY, cm(centerZcm));

        /*
         * Local mikroBUS model:
         * local Y=0 is board top surface.
         */
        float outerW = MIKROBUS_WIDTH_CM;
        float outerD = MIKROBUS_DEPTH_CM;
        float wall = MIKROBUS_WALL_WIDTH_CM;

        /*
         * Dark socket color.
         */
        gl.glColor3f(0.16f, 0.16f, 0.18f);

        // Left wall
        drawLocalCuboid(gl,
                -outerW / 2.0f + wall / 2.0f,
                MIKROBUS_SIDE_HEIGHT_CM / 2.0f,
                0.0f,
                wall,
                MIKROBUS_SIDE_HEIGHT_CM,
                outerD);

        // Right wall
        drawLocalCuboid(gl,
                outerW / 2.0f - wall / 2.0f,
                MIKROBUS_SIDE_HEIGHT_CM / 2.0f,
                0.0f,
                wall,
                MIKROBUS_SIDE_HEIGHT_CM,
                outerD);

        // Bottom wall (2 mm higher than left/right walls)
        drawLocalCuboid(gl,
                0.0f,
                MIKROBUS_BOTTOM_HEIGHT_CM / 2.0f,
                -outerD / 2.0f + wall / 2.0f,
                outerW,
                MIKROBUS_BOTTOM_HEIGHT_CM,
                wall);

        // Top wall (2 mm lower, floating 2 mm above board, only 3 mm high)
        drawLocalCuboid(gl,
                0.0f,
                MIKROBUS_TOP_BOTTOM_GAP_CM + MIKROBUS_TOP_HEIGHT_CM / 2.0f,
                outerD / 2.0f - wall / 2.0f,
                outerW,
                MIKROBUS_TOP_HEIGHT_CM,
                wall);

        /*
         * Holes on left and right inner sides.
         */
        drawMikrobusHoles(gl, outerW, outerD, wall);

        gl.glPopMatrix();
    }

    private void drawMikrobusHoles(GL2 gl, float outerW, float outerD, float wall) {
        /*
         * Holes should be on TOP of the left/right walls,
         * not on the vertical side faces.
         */
        gl.glColor3f(0.01f, 0.01f, 0.01f);

        float patternLength = HOLES_PER_SIDE * HOLE_SIZE_CM + (HOLES_PER_SIDE - 1) * HOLE_GAP_CM;
        float margin = (outerD - patternLength) / 2.0f;

        float firstHoleCenterZ = outerD / 2.0f - margin - HOLE_SIZE_CM / 2.0f;

        /*
         * Hole centers are placed on the top surface of left/right rails.
         */
        float leftHoleX = -outerW / 2.0f + wall / 2.0f;
        float rightHoleX = outerW / 2.0f - wall / 2.0f;

        /*
         * Thin black squares slightly above the side walls.
         */
        float holeVisualHeightCm = 0.02f;
        float holeCenterY = MIKROBUS_SIDE_HEIGHT_CM + holeVisualHeightCm / 2.0f;

        for (int i = 0; i < HOLES_PER_SIDE; i++) {
            float z = firstHoleCenterZ - i * (HOLE_SIZE_CM + HOLE_GAP_CM);

            // Left rail top holes
            drawLocalCuboid(gl,
                    leftHoleX,
                    holeCenterY,
                    z,
                    HOLE_SIZE_CM,
                    holeVisualHeightCm,
                    HOLE_SIZE_CM);

            // Right rail top holes
            drawLocalCuboid(gl,
                    rightHoleX,
                    holeCenterY,
                    z,
                    HOLE_SIZE_CM,
                    holeVisualHeightCm,
                    HOLE_SIZE_CM);
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
        Shape.cuboid(gl, cm(widthCm), cm(heightCm), cm(depthCm));
        gl.glPopMatrix();
    }

    private void drawRoundedLine(GL2 gl,
                                 float centerXcm,
                                 float centerZcm,
                                 float widthCm,
                                 float depthCm,
                                 float radius,
                                 float lineY) {
        gl.glPushMatrix();

        gl.glTranslatef(sceneX(centerXcm), lineY, cm(centerZcm));

        Shape.roundedRectangleLine(gl,
                cm(widthCm),
                cm(depthCm),
                radius,
                CORNER_SEGMENTS);

        gl.glPopMatrix();
    }

    private float sceneX(float logicalXcm) {
        /*
         * Mirror X so the visual board orientation matches the UNI-DS layout.
         */
        return cm(-logicalXcm);
    }

    private float cm(float value) {
        return value * CM_TO_OPENGL;
    }
}