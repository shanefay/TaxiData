package programmingproject;

import ddf.minim.analysis.FFT;
import de.fhpotsdam.unfolding.geo.Location;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import processing.core.PVector;
import processing.opengl.PGraphics3D;

/**
 *
 * @author cal
 */
public class Credits extends AbstractVisualisation
{

    RenderArea renderArea;
    MapGraphs mapGraphs;

    //Constants
    final int GRID_WIDTH = 100;
    final int GRID_HEIGHT = 100;
    final float ABS_SCALE = 1E6f;
    float relScale = 1;

    Gradient gradient;

    ArrayList<Trip> trips = new ArrayList<>();
    ArrayList<Trip> queuedTrips = new ArrayList<>();

    Tower[][] gridOfTowers;
    Random random = new Random();
    boolean minimize = false;
    private FFT fft;

    int y = 0;
    int frames = 0;

    public Credits(RenderArea renderArea, MapGraphs mapGraphs)
    {
        this.renderArea = renderArea;
        this.mapGraphs = mapGraphs;

        gradient = new Gradient(renderArea);
        gradient.addColor(renderArea.color(0, 0, 0));
        gradient.addColor(renderArea.color(102, 0, 102));
        gradient.addColor(renderArea.color(0, 144, 255));
        gradient.addColor(renderArea.color(0, 255, 207));
        gradient.addColor(renderArea.color(51, 204, 102));
        gradient.addColor(renderArea.color(111, 255, 0));
        gradient.addColor(renderArea.color(191, 255, 0));
        gradient.addColor(renderArea.color(255, 240, 0));
        gradient.addColor(renderArea.color(255, 153, 102));
        gradient.addColor(renderArea.color(204, 51, 0));
        gradient.addColor(renderArea.color(153, 0, 0));

        gridOfTowers = new Tower[GRID_WIDTH][GRID_HEIGHT];

        renderArea.audio.loadClip("res/credits.mp3", "credits", 0, 0.23, 0.5);
        resetTowers();
    }

    public void resetTowers()
    {
        for (int i = 0; i < GRID_WIDTH; i++)
        {
            for (int ii = 0; ii < GRID_HEIGHT; ii++)
            {
                gridOfTowers[i][ii] = new Tower(0);
            }
        }
    }

    public void start()
    {
        mapGraphs.map.zoomAndPanTo(6, new Location(-19.485040, 46.614733));
        mapGraphs.map.mapDisplay.setInnerTransformationCenter(new PVector(0, 0));
        renderArea.audio.playClip("credits");
        fft = new FFT(renderArea.audio.clips.get(0).clip.bufferSize(), renderArea.audio.clips.get(0).clip.sampleRate());
        frames = 0;
    }

    public void setScale(int sampleSize)
    {
        relScale = ABS_SCALE / sampleSize;
    }

    @Override
    public void reloadData()
    {
        resetTowers();
    }

    @Override
    public void draw(PGraphics3D buffer)
    {
        fft.forward(renderArea.audio.clips.get(0).clip.mix);
        buffer.pushStyle();
        buffer.pushMatrix();

        buffer.translate(-mapGraphs.mapWidth / 2, -mapGraphs.mapHeight / 2, 0);

        buffer.fill(255, 0, 0, 100f);

        
        if (renderArea.audio.getCurrentLevel()*2 > renderArea.audio.getAudioEdge())
        {
            y++;
            if (y >= GRID_HEIGHT)
            {
                y = 0;
            }
        }
        
        mapGraphs.setBackground(0, 0, (int) (200 * renderArea.audio.clips.get(renderArea.audio.currentClip).clip.mix.level()));

        for (int n = 0; n < fft.specSize() / 5; n++)
        {
            int i = (int) RenderArea.map(n, 0, fft.specSize() / 5, 0, GRID_WIDTH);

            gridOfTowers[i][y].height = fft.getBand(n) * 2;
            gridOfTowers[i][(y + GRID_HEIGHT - 10) % GRID_HEIGHT].height = 0;

            gridOfTowers[i][(y + 33) % GRID_HEIGHT].height = fft.getBand(n) * 2;
            gridOfTowers[i][(y + GRID_HEIGHT + 23) % GRID_HEIGHT].height = 0;

            gridOfTowers[i][(y + 66) % GRID_HEIGHT].height = fft.getBand(n) * 2;
            gridOfTowers[i][(y + GRID_HEIGHT + 56) % GRID_HEIGHT].height = 0;
        }

        for (int i = 0; i < GRID_WIDTH; i++)
        {
            for (int ii = 0; ii < GRID_HEIGHT; ii++)
            {
                if (gridOfTowers[i][ii].height != 0)
                {
                    buffer.pushMatrix();

                    buffer.translate((float) i * (mapGraphs.mapWidth / (float) GRID_WIDTH), (float) ii * (mapGraphs.mapHeight / (float) GRID_HEIGHT), (float) ((gridOfTowers[i][ii].height)) * relScale / 2);

                    buffer.fill(gradient.getGradient((float) Math.log10((gridOfTowers[i][ii].height) * 500f) * 1.8f));

                    buffer.box(mapGraphs.mapWidth / GRID_WIDTH, mapGraphs.mapHeight / GRID_HEIGHT, (float) ((double) (gridOfTowers[i][ii].height)) * relScale);
                    buffer.popMatrix();

                }
            }
        }
        
        buffer.fill((1000 * renderArea.audio.clips.get(renderArea.audio.currentClip).clip.mix.level()));
        buffer.textSize(80);
        buffer.text("Created by...", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 -600, 100);
        buffer.text("Cal Martin", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 - 100, 100);
        buffer.text("Daniel Crawford", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 - 200, 100);
        buffer.text("Shane Fay", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 - 300, 100);
        buffer.text("Aran Nolan", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 - 400, 100);
        buffer.text("John Milsom", (mapGraphs.mapWidth / 2) - 300, mapGraphs.mapHeight - frames/2 - 500, 100);
                
        buffer.popMatrix();
        buffer.popStyle();
        frames++;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }
}
