package programmingproject;

import processing.opengl.PGraphics3D;

/**
 *
 * @author John Milsom
 */
public class VerticalLine
{

    int xPos;
    int yPos;
    int height;
    float buildStage;
    boolean built;

    public VerticalLine(int xPos, int yPos, int height)
    {
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
        buildStage = 0;
        built = false;
    }

    public void draw(PGraphics3D buffer)
    {
        if (!built)
        {
            buildLines(buffer);
        } else
        {
            buffer.pushStyle();
            buffer.pushMatrix();
            buffer.fill(0, 120, 255);
            buffer.translate(xPos, yPos, height / 2);
            buffer.box(1, 1, height);
            buffer.popMatrix();
            buffer.popStyle();
        }
    }

    public void buildLines(PGraphics3D buffer)
    {
        if (buildStage < 100)
        {
            float currentHeight = height * buildStage / 100;
            buffer.pushStyle();
            buffer.pushMatrix();
            buffer.fill(0, 120, 255);
            buffer.translate(xPos, yPos, currentHeight / 2);
            buffer.box(1, 1, currentHeight);
            buffer.popMatrix();
            buffer.popStyle();
            buildStage += 0.7;
        } else
        {
            built = true;
            draw(buffer);
        }
    }
}
