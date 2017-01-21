package programmingproject;

import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import processing.core.PGraphics;
import processing.opengl.PGraphics3D;

/**
 *
 * @author cal
 *
 * Adapting HeatMapGraph.java 2015-03-18
 */
public class VisualComparision extends AbstractVisualisation
{

    RenderArea renderArea;
    MapGraphs mapGraphs;

    //Constants
    final int GRID_WIDTH = 300;
    final int GRID_HEIGHT = 300;
    final float ABS_SCALE = 50000f;
    
    float relScale = 1;

    // 0 = telescope,   1 = side by side
    int visualStyle = 1;

    ArrayList<Trip> trips1 = new ArrayList<>();
    ArrayList<Trip> trips2 = new ArrayList<>();
    ArrayList<Trip> queuedTrips1 = new ArrayList<>();
    ArrayList<Trip> queuedTrips2 = new ArrayList<>();
    Tower[][] gridOfTowers1;
    Tower[][] gridOfTowers2;

    float percent = 0f;
    Random random = new Random();
    boolean minimize = false;

    PGraphics buffer;

    public VisualComparision(RenderArea renderArea, MapGraphs mapGraphs)
    {
        this.renderArea = renderArea;
        this.mapGraphs = mapGraphs;
        buffer = renderArea.createGraphics(renderArea.width, renderArea.height, RenderArea.P3D);
        
        super.setCurrentQuery(renderArea.currentQuery);

        gridOfTowers1 = new Tower[GRID_WIDTH][GRID_HEIGHT];
        gridOfTowers2 = new Tower[GRID_WIDTH][GRID_HEIGHT];

        resetTowers(gridOfTowers1);
        resetTowers(gridOfTowers2);
    }

    public void resetTowers(Tower[][] gridOfTowers)
    {
        for (int i = 0; i < GRID_WIDTH; i++)
        {
            for (int ii = 0; ii < GRID_HEIGHT; ii++)
            {
                gridOfTowers[i][ii] = new Tower(0);
            }
        }
    }

    public void calculateTowers(ArrayList<Trip> trips, Tower[][] gridOfTowers)
    {
        for (Trip trip : trips)
        {
            float latitude = trip.dropoffLat;
            float longitude = trip.dropoffLong;
            ScreenPosition pos = mapGraphs.map.getScreenPosition(new Location(latitude, longitude));
            int relX = (int) (pos.x);
            int relY = (int) (pos.y);

            int x = (int) RenderArea.map(relX, -mapGraphs.mapWidth / 2, mapGraphs.mapWidth / 2, 0, GRID_WIDTH);
            int y = (int) RenderArea.map(relY, -mapGraphs.mapHeight / 2, mapGraphs.mapHeight / 2, 0, GRID_HEIGHT);

            if (x < GRID_WIDTH && x > 0 && y < GRID_HEIGHT && y > 0)
            {
                gridOfTowers[x][y].height++;
            } else
            {
//                System.out.println("GRID ERROR - OUT OF BOUNDS");
//                System.out.println("relX = " + relX + " relY = " + relY);
//                System.out.println("x = " + x + " y = " + y);
            }
        }
    }

    public void setScale(int sampleSize) {
        relScale = ABS_SCALE / sampleSize;
    }
    
    public void setData(ArrayList<Trip> data1, ArrayList<Trip> data2)
    {
        minimize = true;
        queuedTrips1 = data1;
        queuedTrips2 = data2;
    }

    public void switchData()
    {
        trips1 = queuedTrips1;
        trips2 = queuedTrips2;
        setScale(trips1.size() + trips1.size());
        resetTowers(gridOfTowers1);
        resetTowers(gridOfTowers2);
        calculateTowers(trips1, gridOfTowers1);
        calculateTowers(trips2, gridOfTowers2);
        System.out.println("TRIP SIZE: " + trips1.size());
    }
    
    @Override
    public void reloadData()
    {
        setData(renderArea.currentQuery.queryOne, renderArea.currentQuery.queryTwo);
        resetTowers(gridOfTowers1);
        resetTowers(gridOfTowers2);
        calculateTowers(trips1, gridOfTowers1);
        calculateTowers(trips2, gridOfTowers2);
    }

    @Override
    public void draw(PGraphics3D buffer)
    {
        buffer.pushStyle();
        buffer.pushMatrix();

        if (minimize)
        {
            if (percent > 0f)
            {
                percent -= 0.05f;
            } else
            {
                minimize = false;
                switchData();
            }
        }
        if (!minimize && percent < 1)
        {
            percent += 0.05;
        }

        buffer.translate(-mapGraphs.mapWidth / 2, -mapGraphs.mapHeight / 2, 0);

        buffer.fill(255, 0, 0, 100f);
        
        for (int i = 0; i < GRID_WIDTH; i++)
        {
            for (int ii = 0; ii < GRID_HEIGHT; ii++)
            {
                switch (visualStyle)
                {
                    case 0:
                        if (gridOfTowers1[i][ii].height == gridOfTowers2[i][ii].height)
                        {
                            drawTower(buffer, gridOfTowers1[i][ii], i, ii, 255, 63, 255, 0.75f, 0.75f, 0);
                        } else
                        {
                            if (gridOfTowers1[i][ii].height > gridOfTowers2[i][ii].height)
                            {
                                drawTower(buffer, gridOfTowers1[i][ii], i, ii, 63, 63, 255, 0.5f, 0.5f, 0f);
                                drawTower(buffer, gridOfTowers2[i][ii], i, ii, 255, 63, 63, 1f, 1f, 0f);
                            } else
                            {
                                drawTower(buffer, gridOfTowers1[i][ii], i, ii, 63, 63, 255, 1f, 1f, 0f);
                                drawTower(buffer, gridOfTowers2[i][ii], i, ii, 255, 63, 63, 0.5f, 0.5f, 0f);
                            }
                        }
                        break;
                    case 1:
                        drawTower(buffer, gridOfTowers1[i][ii], i, ii, 63, 63, 255, 0.4f, 0.8f, -0.2f);
                        drawTower(buffer, gridOfTowers2[i][ii], i, ii, 255, 63, 63, 0.4f, 0.8f, 0.2f);
                        break;
                }
            }
        }

        buffer.popMatrix();
        buffer.popStyle();
    }

    public void drawTower(PGraphics3D buffer, Tower tower, int x, int y, int red, int green, int blue, float baseX, float baseY, float baseOffset)
    {
        if (tower.height == 0)
        {
            return;
        }
        buffer.pushMatrix();
        buffer.translate((float) (x + baseOffset) * (mapGraphs.mapWidth / (float) GRID_WIDTH), (float) y * (mapGraphs.mapHeight / (float) GRID_HEIGHT), (float) ((tower.height)) * relScale * percent / 2);

        // towers:
        buffer.fill(red, green, blue);
        buffer.box(mapGraphs.mapWidth / GRID_WIDTH * baseX, mapGraphs.mapHeight / GRID_HEIGHT * baseY, (float) ((double) (tower.height)) * relScale * percent);

        buffer.popMatrix();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        super.keyPressed(e);
        if (e.getKeyCode() == KeyEvent.VK_M)
        {
            visualStyle = (visualStyle == 0 ? 1 : 0);
        }
    }
}
