package programmingproject;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.opengl.PGraphics3D;

/**
 *
 * @author Dan
 * contains all necessary info for taxis to be drawn correctly for tripAnimator
 *
 */
public class TaxiDrawable
{

    float x, y, endx, endy, startx, starty;
    float dx, dy;
    boolean dead;
    short deathFrames;
    int startTime, tripTime;
    private final short DEATHFRAMES = 1;
    int dataSet;

    TaxiDrawable(Trip trip, UnfoldingMap map, int query)
    {
        float latitude = trip.pickupLat;
        float longitude = trip.pickupLong;
        ScreenPosition screenPosition = map.getScreenPosition(new Location(latitude, longitude));
        x = screenPosition.x;
        startx = x;
        y = screenPosition.y;
        starty = y;
        if (TripAnimator.MODE == 0)
        {
            startTime = trip.pickupTime % DateTime.SECONDS_PER_DAY;
        } else if (TripAnimator.MODE == 1)
        {
            startTime = trip.pickupTime;
        }
        latitude = trip.dropoffLat;
        longitude = trip.dropoffLong;
        screenPosition = map.getScreenPosition(new Location(latitude, longitude));
        endx = screenPosition.x;
        endy = screenPosition.y;

        tripTime = trip.time;

        dx = (endx - x) / trip.time;
        dy = (endy - y) / trip.time;

        if (query == 2)
        {
            //draws query2 as RED taxis
            dataSet = 1;
        } else
        {
            //default yellow taxis
            dataSet = 0;
        }
    }

    public boolean draw(PGraphics3D buffer, int time)
    {
        if (dead)
        {

            if (time > startTime && deathFrames < DEATHFRAMES && time < startTime + tripTime)
            {
                buffer.translate(x, y, 4);
                buffer.fill(0, 0, 0);
                buffer.box(3, 3, 8);
                deathFrames += TripAnimator.speedFactor;
            }
            if (time <= TripAnimator.speedFactor + 1)
            {
                this.resetTaxi();
            }

        } else if (time > startTime && time < startTime + tripTime)
        {

            if (dataSet == 0)
            {
                buffer.translate(x, y, 4);
                buffer.fill(255, 240, 0);
                buffer.box(3, 3, 8);
            } else if (dataSet == 1)
            {
                buffer.translate(x, y, 4);
                buffer.fill(255, 0, 0);
                buffer.box(3, 3, 8);
            }

            return true;
        }
        return false;
    }

    public void moveAndCheck(int time)
    {
        if (time > startTime)
        {
            x += TripAnimator.speedFactor * dx * TripAnimator.delta;
            y += TripAnimator.speedFactor * dy * TripAnimator.delta;
            if (endx - x < TripAnimator.speedFactor / 16 && endx - x > -1 * (TripAnimator.speedFactor / 16))
            {
                dx = 0;
                dy = 0;
                dead = true;
            }
        }

    }

    public void resetTaxi()
    {
        dead = false;
        x = startx;
        y = starty;
        dx = (endx - x) / tripTime;
        dy = (endy - y) / tripTime;
    }
}
