package programmingproject;

import java.util.ArrayList;

/**
 *
 * @author http://www.openprocessing.org/sketch/46554
 */
public class Gradient
{

    ArrayList colors;
    RenderArea renderArea;

    // Constructor
    public Gradient(RenderArea renderArea)
    {
        colors = new ArrayList();
        this.renderArea = renderArea;
    }

    public void addColor(int c)
    {
        colors.add(c);
    }

    public int getGradient(float value)
    {
        // make sure there are colors to use
        if (colors.isEmpty())
        {
            return 0;
        }

        // if its too low, use the lowest value
        if (value <= 0.0)
        {
            return (int) (Integer) colors.get(0);
        }

        // if its too high, use the highest value
        if (value >= colors.size() - 1)
        {
            return (int) (Integer) colors.get(colors.size() - 1);
        }

        // lerp between the two needed colors
        int color_index = (int) value;
        int c1 = (int) (Integer) colors.get(color_index);
        int c2 = (int) (Integer) colors.get(color_index + 1);

        return renderArea.lerpColor(c1, c2, value - color_index);
    }
}
