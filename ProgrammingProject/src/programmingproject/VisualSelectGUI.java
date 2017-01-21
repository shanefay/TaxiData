package programmingproject;

import controlP5.ControlP5;
import controlP5.DropdownList;

/**
 *
 * @author cal
 */
public class VisualSelectGUI
{

    RenderArea renderArea;
    ControlP5 cp5;

    public static final int WIDTH = 200;
    
    DropdownList visList;
    public static final String VIS_LIST_LABEL = "visList";
    public static final String[] VISUAL_LABELS = new String[]
    {
        "Heat Map",
        "Taxi Animator",
        "Area Map Graph",
        "Location Popularity",
        "Query comparison",
        "Line Pie Chart",
        "Stats Visual",
    };

    public VisualSelectGUI(RenderArea renderArea, ControlP5 cp5)
    {
        this.renderArea = renderArea;
        this.cp5 = cp5;

        setup();
    }

    public void setup()
    {
        visList = cp5.addDropdownList(VIS_LIST_LABEL)
                .setCaptionLabel("Visualisations")
                .setSize(WIDTH, 400)
                .setBarHeight(20)
                .setItemHeight(20)
                .setPosition(renderArea.width - WIDTH, 20)
                .addItems(VISUAL_LABELS);
    }

    public void resize()
    {
        visList.setPosition(renderArea.width - WIDTH, 20);
    }
}
