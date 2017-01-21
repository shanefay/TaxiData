package programmingproject;

import controlP5.ControlEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import processing.core.PApplet;
import processing.opengl.PGraphics3D;

/**
 *
 * @author cal
 */
public class RenderArea extends PApplet
{

    HeatMapGraph heightMapGraph;
    MapGraphs mapGraphs;
    Data data;
    LinePieChart linePieChart;
    StatsVisual statsVisual;
    
    CurrentQuery currentQuery;

    AbstractVisualisation currentVisualisation;

    PGraphics3D buffer;
    
    Audio audio;
    
    Credits credits;

    GUI gui;

    Query query;

    @Override
    public void setup()
    {
        size(width, height, P2D);

        buffer = (PGraphics3D) createGraphics(width, height, P3D);
        buffer.textFont(createFont("Calibri", 30, false));

        gui = new GUI(this);
        query = new Query();
        
        currentQuery = new CurrentQuery(this);

        mapGraphs = new MapGraphs(this, buffer);
        linePieChart = new LinePieChart(this);
        statsVisual = new StatsVisual(this);
        
        audio = new Audio(this);
        
        credits = new Credits(this, mapGraphs);

        currentVisualisation = mapGraphs.heatMapGraph;
    }

    @Override
    public void draw()
    {
        buffer.beginDraw();

        if (currentVisualisation == linePieChart || currentVisualisation == statsVisual)
        {
            currentVisualisation.draw(buffer);
        } else
        {
            mapGraphs.draw(buffer);
        }

        buffer.endDraw();
        image(buffer, 0, 0); //Draw 3D offscreen buffer onto 2D onscreen buffer!

        draw2DGUI();
    }

    public void draw2DGUI()
    {
        if (mapGraphs.heatMapGraph.labelX != -1)
        {
            pushStyle();
            pushMatrix();
            translate(mapGraphs.heatMapGraph.labelX, mapGraphs.heatMapGraph.labelY);
            fill(0, 255, 0);
            rect(0, 0, 100, 50);

            int desiredID = mapGraphs.heatMapGraph.currentID - 1;
            int row = (desiredID / mapGraphs.heatMapGraph.GRID_WIDTH);
            int column = desiredID - (row * mapGraphs.heatMapGraph.GRID_WIDTH);

            fill(0);
            text((int) (mapGraphs.heatMapGraph.gridOfTowers[row][column].height / 10) + " taxis", 5, 20);

            popMatrix();
            popStyle();
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        super.mousePressed(e);
        if (!gui.cp5.isMouseOver())
        {
            currentVisualisation.mousePressed(e);
            mapGraphs.mousePressed(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        super.mouseDragged(e);
        if (!gui.cp5.isMouseOver())
        {
            currentVisualisation.mouseDragged(e);
            mapGraphs.mouseDragged(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        super.mouseReleased(e);
        if (!gui.cp5.isMouseOver())
        {
            currentVisualisation.mouseReleased(e);
            mapGraphs.mouseReleased(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        super.mouseWheelMoved(e);
        if (!gui.cp5.isMouseOver())
        {
            currentVisualisation.mouseWheelMoved(e);
            mapGraphs.mouseWheelMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_O)
        {
            currentVisualisation = credits;
            credits.start();
        } else if (e.getKeyCode() == KeyEvent.VK_H)
        {
            gui.hotkeyGUI.toggleDisplay();
        }
        currentVisualisation.keyPressed(e);
        mapGraphs.keyPressed(e);
    }

    public void controlEvent(ControlEvent theEvent)
    {
        if (theEvent.isFrom(VisualSelectGUI.VIS_LIST_LABEL))
        {
            mapGraphsGUIEvent(theEvent);
        } else if (theEvent.isFrom(QueryGUI.QUERY_ONE_LABEL))
        {
            currentQuery.requestQuery(gui.queryGUI.getQuery(), true);
        } else if (theEvent.isFrom(QueryGUI.QUERY_TWO_LABEL))
        {
            currentQuery.requestQuery(gui.queryGUI.getQuery(), false);
        }
    }

    private void mapGraphsGUIEvent(ControlEvent theEvent)
    {
        switch ((int) theEvent.getValue())
        {
            case 0: //"Heat Map":
                currentVisualisation = mapGraphs.heatMapGraph;
                break;
            case 1: //"Taxi Animator":
                currentVisualisation = mapGraphs.tripAnimator;
                break;
            case 2: //"Area Map Graph":
                currentVisualisation = mapGraphs.areaMapGraph;
                break;
            case 3: //"Location Popularity":
                currentVisualisation = mapGraphs.location;
                break;
            case 4: //"Query comparison":
                currentVisualisation = mapGraphs.comparisonQuery;
                break;
            case 5: //"Line Pie Chart":
                currentVisualisation = linePieChart;
                break;
            case 6: //"Stats Visual":
                currentVisualisation = statsVisual;
                break;
            default:
                break;
        }
        mapGraphs.resetAmbient();
        mapGraphs.resetBackground();
        currentVisualisation.reloadData();
    }

    @Override
    protected void resizeRenderer(int newWidth, int newHeight) //When the window is resized, adjust all buffers accordingly!
    {
        super.resizeRenderer(newWidth, newHeight);
        if (mapGraphs != null)
        {
            mapGraphs.heatMapGraph.buffer = createGraphics(newWidth, newHeight, RenderArea.P3D);
        }
        if (buffer != null)
        {
            buffer = (PGraphics3D) createGraphics(newWidth, newHeight, RenderArea.P3D);
        }
        if (gui != null)
        {
            gui.resize();
        }
    }
}
