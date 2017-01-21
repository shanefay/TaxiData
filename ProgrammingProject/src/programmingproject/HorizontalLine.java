package programmingproject;

import processing.opengl.PGraphics3D;

/**
 *
 * @author John Milsom
 */
public class HorizontalLine {
    
    int startX;
    int startY;
    boolean built;
    float buildStage;
    float invBuildStage;
    int height;
    
    HorizontalLine(int startX,int startY, int height)
    {
        this.startX = startX;
        this.startY = startY;
        built = false;
        buildStage = 100;
        invBuildStage = 0;
        this.height = height;
    }
    
    public void draw(PGraphics3D buffer)
    {
        if(!built)
        {
            buildLines(buffer);
        }
        else
        {
            buffer.stroke(0, 120, 255);
            buffer.pushMatrix();
            buffer.fill(0, 120, 255);
            buffer.translate(buffer.width / 2, buffer.height / 2, 34);
            buffer.box(3, 3, 68);
            buffer.popMatrix();
            buffer.line(startX, startY, height, buffer.width / 2, buffer.height / 2, height);
        }
    }
    
    public void buildLines(PGraphics3D buffer)
    {
        if(buildStage > 0 )
        {
            float endX = startX * buildStage/100 + buffer.width/2 * invBuildStage/100;
            float endY = startY * buildStage/100 + buffer.height/2 * invBuildStage/100;
            buffer.pushStyle();
            buffer.pushMatrix();
            buffer.fill(0, 120, 255);
            buffer.stroke(0, 120, 255);
            buffer.line(startX, startY, height, endX, endY, height);
            buffer.popMatrix();
            buffer.popStyle();
            buildStage -= 0.5;
            invBuildStage += 0.5;
        }
        else
        {
            built = true;
            draw(buffer);
        }
    }
}
