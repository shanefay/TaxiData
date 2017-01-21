package programmingproject;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.providers.OpenStreetMap;
import de.fhpotsdam.unfolding.providers.StamenMapProvider;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

/**
 *
 * @author cal
 */
public class MapGraphs
{

    RenderArea renderArea;

    UnfoldingMap map;
    ArrayList<AbstractMapProvider> mapProviders = new ArrayList<>();
    int mapWidth = 3500, mapHeight = 3500;
    int currentMap = 0;

    //Camera Rotation
    float cameraX, cameraY;
    float cameraTransX, cameraTransY;
    float zoom;
    MouseEvent lastMousePosition;
    float MOUSE_SENSITIVITY = 300f;
    boolean demoMode = true;
    AreaMapGraph areaMapGraph;

    //Graphs
    HeatMapGraph heatMapGraph;
    TripAnimator tripAnimator;
    LocationVisualization location;
    VisualComparision comparisonQuery;

    int background;
    int ambient = -1;

    public MapGraphs(RenderArea renderArea, PGraphics3D buffer)
    {
        this.renderArea = renderArea;

        mapProviders.add(new Google.GoogleMapProvider());
        mapProviders.add(new OpenStreetMap.OpenStreetMapProvider());
        mapProviders.add(new StamenMapProvider.Toner());
        mapProviders.add(new Microsoft.RoadProvider());
        mapProviders.add(new Microsoft.AerialProvider());

        resetBackground();

        //Wanna try a different map?
        //Replace the last parameter with one of these!! http://unfoldingmaps.org/javadoc/index.html?de/fhpotsdam/unfolding/providers/package-summary.html
        map = new UnfoldingMap(renderArea, -mapWidth / 2, -mapHeight / 2, mapWidth, mapHeight, mapProviders.get(0));
        map.zoomAndPanTo(12, new Location(40.731416f, -73.990667f));
        map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));

        heatMapGraph = new HeatMapGraph(renderArea, this);
        tripAnimator = new TripAnimator(renderArea, this);
        location = new LocationVisualization(renderArea, this);
        comparisonQuery = new VisualComparision(renderArea, this);
        areaMapGraph = new AreaMapGraph(renderArea, this);

        renderArea.currentVisualisation = heatMapGraph;
    }

    public void setBackground(int red, int green, int blue)
    {
        background = renderArea.color(red, green, blue);
    }

    public void setBackground(int color)
    {
        background = color;
    }

    public void resetBackground()
    {
        background = renderArea.color(179, 209, 255);
    }

    public void setAmbientLight(int color)
    {
        ambient = color;
    }

    public void resetAmbient()
    {
        ambient = -1;
    }

    public void draw(PGraphics3D buffer)
    {
        buffer.pushStyle();
        buffer.pushMatrix();
        buffer.background(background);

        if (ambient != -1)
        {
            buffer.ambientLight(renderArea.red(ambient), renderArea.green(ambient), renderArea.blue(ambient));
        }

        if (demoMode)
        {
            if (cameraY < 1)
            {
                cameraY += 0.005f;
            }
            cameraX += 0.001f;
        }

        buffer.translate(buffer.width / 2, buffer.height / 2, zoom);

        buffer.rotateX(cameraY);
        buffer.rotateZ(cameraX);

        buffer.translate(cameraTransX, cameraTransY, 0);

        buffer.pushMatrix();
        buffer.translate(-mapWidth / 2, -mapHeight / 2, 0);

        map.draw();
        buffer.image(map.mapDisplay.getOuterPG(), 0, 0);
        buffer.popMatrix();

        //Draw whichever visualisation is active
        renderArea.currentVisualisation.draw(buffer);

        buffer.popMatrix();
        buffer.pushStyle();
    }

    public void mousePressed(MouseEvent e)
    {
        demoMode = false;
    }

    public void mouseDragged(MouseEvent e)
    {
        if (lastMousePosition == null)
        {
            lastMousePosition = e;
        }

        if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK)
        {
            //excellent comments ahead, praise the sun!
            cameraTransX += Math.cos(cameraX) * (e.getXOnScreen() - lastMousePosition.getXOnScreen()); //THIS
            cameraTransY += Math.cos(cameraX) * (e.getYOnScreen() - lastMousePosition.getYOnScreen()); //TOOK
            cameraTransX += Math.sin(cameraX) * (e.getYOnScreen() - lastMousePosition.getYOnScreen()); //ME
            cameraTransY -= Math.sin(cameraX) * (e.getXOnScreen() - lastMousePosition.getXOnScreen()); //FOREVER!
        } else if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK)
        {
            cameraX -= (e.getXOnScreen() - lastMousePosition.getXOnScreen()) / MOUSE_SENSITIVITY;
            cameraY -= (e.getYOnScreen() - lastMousePosition.getYOnScreen()) / MOUSE_SENSITIVITY;
        }

        lastMousePosition = e;
    }

    public void mouseReleased(MouseEvent e)
    {
        lastMousePosition = null;
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        zoom -= e.getWheelRotation() * 20;
    }

    public void setCamera(float lat, float lon, int zoomLevel, float cameraX, float cameraY, float cameraTransX, float cameraTransY, float zoom)
    {
        map.zoomAndPanTo(zoomLevel, new Location(lat, lon));
        map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
        this.cameraX = cameraX;
        this.cameraY = cameraY;
        this.cameraTransX = cameraTransX;
        this.cameraTransY = cameraTransY;
        this.zoom = zoom;
        renderArea.currentVisualisation.reloadData();
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_EQUALS) //Zoom in
        {
            map.zoomLevelIn();
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) //Zoom out
        {
            map.zoomLevelOut();
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_I) //Pan up
        {
            map.panUp();
            map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_J) //Pan left
        {
            map.panLeft();
            map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_K) //Pan down
        {
            map.panDown();
            map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_L) //Pan right
        {
            map.panRight();
            map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
            renderArea.currentVisualisation.reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_QUOTE) //Print camera preset to console
        {
            System.out.println(map.getCenter().getLat() + "f, " + map.getCenter().getLon() + "f, " + map.getZoomLevel() + ", " + cameraX + "f, " + cameraY + "f, " + cameraTransX + "f, " + cameraTransY + "f, " + zoom + "f");
        } else if (e.getKeyCode() == KeyEvent.VK_Q) //Sample Camera Preset
        {
            //setCamera(40.770947f, -73.87256f, 16, 2.67433f, 1.0016665f, -154.64786f, 393.83865f, 1.0f);
            renderArea.currentVisualisation = tripAnimator;
            setCamera(40.731403f, -73.99066f, 14, 0.37599716f, 1.2116657f, 22.387857f, -67.63221f, 0.0f);
            renderArea.currentQuery.requestQuery("SELECT * FROM taxi_data WHERE (\n"
                    + "(pickup_datetime >= 0 AND pickup_datetime < 2678400) OR \n"
                    + "(pickup_datetime >= 2678400 AND pickup_datetime < 5097600)\n"
                    + ") AND (\n"
                    + "(pickup_datetime % 86400 >= 21600 AND pickup_datetime % 86400 < 39600)\n"
                    + ") AND (\n"
                    + "(passenger_count >= 0 AND passenger_count <= 6)\n"
                    + ") LIMIT 50000", true);
            tripAnimator.setTime(6, 240);
        } else if (e.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) //Previous map
        {
            if (currentMap == 0)
            {
                currentMap = mapProviders.size() - 1;
                map.mapDisplay.setMapProvider(mapProviders.get(currentMap));
            } else
            {
                map.mapDisplay.setMapProvider(mapProviders.get(--currentMap % mapProviders.size()));
            }
        } else if (e.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) //Next mapz
        {
            map.mapDisplay.setMapProvider(mapProviders.get(Math.abs(++currentMap % mapProviders.size())));
        } else if (e.getKeyCode() == KeyEvent.VK_R)
        {
            setCamera(40.731415f, -73.99066f, 12, 0.3229994f, 1.0049993f, 0.0f, 0.0f, 0.0f); //Reset to default view
        } else if (e.getKeyCode() == KeyEvent.VK_D)
        {
            demoMode = true;
        }
    }
}
//Default View: 40.731415f, -73.99066f, 12, 0.3229994f, 1.0049993f, 0.0f, 0.0f, 0.0f
