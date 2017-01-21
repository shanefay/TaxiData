package programmingproject;

import controlP5.*;

/**
 *
 * @author Aran Nolan <nolanar@tcd.ie>
 */
public class HotkeyGUI
{

    RenderArea renderArea;
    ControlP5 cp5;

    public static final int MAIN_WIDTH = 380;
    public static final int MAIN_HEIGHT = 400;
    public static final int MINIM_WIDTH = 125;
    public static final int MINIM_HEIGHT = 20;

    private Textarea mainTextarea;
    private Textarea minimisedTextarea;

    private boolean minimised;

    public HotkeyGUI(RenderArea renderArea, ControlP5 cp5)
    {
        this.renderArea = renderArea;
        this.cp5 = cp5;
        setup();
    }

    private void setup()
    {
        mainTextarea = cp5.addTextarea("mainTextarea")
                .setPosition(renderArea.width - MAIN_WIDTH, renderArea.height - MAIN_HEIGHT)
                .setSize(MAIN_WIDTH, MAIN_HEIGHT)
                .setFont(renderArea.createFont("consolas", 12))
                .setLineHeight(14)
                .setColorBackground(renderArea.color(48, 220))
                .hideScrollbar()
                .setText(
                        "General controls:\n"
                        + "  '1'                 Display query one\n"
                        + "  '2'                 Display query two\n"
                        + "  '['  ']'            Cycle through maps\n"
                        + "  '+'  '-'            Zoom map in/out\n"
                        + "  'R'                 Reset camera view\n"
                        + "  'I' 'J' 'K' 'L'     Move around map\n"
                        + "  Left click          Pan\n"
                        + "  Right click         Rotate\n"
                        + "  Scroll              Zoom\n"
                        + "  'O'                 Easter Egg\n"
                        + "\n"
                        + "Heat Map:\n"
                        + "  Hover over towers to display data\n"
                        + "\n"
                        + "Query Comparison:\n"
                        + "  'M'                 Switch pair/telescoped modes\n"
                        + "\n"
                        + "Taxi Animator:\n"
                        + "  'M'                 Toggle 'separate by date'\n"
                        + "  'UP'                Move forward hour\n"
                        + "  'DOWN               Move backward hour\n"
                        + "  'SHIFT + UP'        Move forward day\n"
                        + "  'SHIFT + DOWN'      Move backward day\n"
                        + "  'LEFT' (+ SHIFT)    Speed up time (by 10)\n"
                        + "  'RIGHT' (+ SHIFT)   Slow down time (by 10)\n"
                        + "\n"
                        + "                                         'H' to hide"
                )
                .hide();
        minimisedTextarea = cp5.addTextarea("minimisedTextarea")
                .setPosition(renderArea.width - MINIM_WIDTH, renderArea.height - MINIM_HEIGHT)
                .setSize(MINIM_WIDTH, MINIM_HEIGHT)
                .setFont(renderArea.createFont("consolas", 12))
                .setColorBackground(renderArea.color(48, 220))
                .hideScrollbar()
                .setText("'H' for hotkeys");

        minimised = true;
    }

    public void resize()
    {
        mainTextarea.setPosition(renderArea.width - MAIN_WIDTH, renderArea.height - MAIN_HEIGHT);
        minimisedTextarea.setPosition(renderArea.width - MINIM_WIDTH, renderArea.height - MINIM_HEIGHT);
    }

    public void toggleDisplay()
    {
        if (minimised)
        {
            mainTextarea.show();
            minimisedTextarea.hide();
            minimised = false;
        } else
        {
            mainTextarea.hide();
            minimisedTextarea.show();
            minimised = true;
        }
    }
}
