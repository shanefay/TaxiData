package programmingproject;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import java.util.ArrayList;

/**
 *
 * @author Shane
 */
public class LocationDrawable
{

    float lat;
    float lon;
    float x;
    float y;
    String name;
    int visitors;
    SimplePointMarker marker;

    public LocationDrawable(float lat, float lon, String name, UnfoldingMap map)
    {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        ScreenPosition screenPosition = map.getScreenPosition(new Location(lat, lon));
        x = screenPosition.x;
        y = screenPosition.y;
        marker = new SimplePointMarker(new Location(lat, lon));
        map.addMarker(marker);
    }

    //for all trips  checksif the drop off point is withn +- .01 lat and long
    //and if so, increments the visitor counter 
    public void setData(ArrayList<Trip> trips)
    {
        for (Trip t : trips)
        {
            if (t.dropoffLat <= lat + .01 && t.dropoffLat >= lat - .01 && t.dropoffLong <= lon + .01 && t.dropoffLong >= lon - .01)
            {
                visitors++;
            }
        }
    }

    public void updateLocation(UnfoldingMap map)
    {
        ScreenPosition screenPosition = map.getScreenPosition(new Location(lat, lon));
        x = screenPosition.x;
        y = screenPosition.y;
    }
}
