package programmingproject;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author cal
 */
public class Data
{
    String[] meds, hacks;
    RenderArea renderArea;

    //these are no fun, anyone able to check? it wasnt a leap year!
    public static final int[] SECONDS_TILL_MONTH_STARTS =
    {
        0, 2678400, 5097600, 7776000, 10368000, 13046400, 15638400,
        18316800, 20995200, 23587200, 26265600, 28857600
    };

    //these are used so we can use our sensible names to refer to the original column names
    static final String MEDALLION = "medallion";
    static final String HACK = "hack_license";
    static final String VENDORID = "vendor_id";
    static final String RATECODE = "rate_code";
    static final String STOREANDFWDFLAG = "store_and_fwd_flag";
    static final String PICKUPTIME = "pickup_datetime";
    static final String DROPOFFTIME = "dropoff_datetime";
    static final String PASSENGER = "passenger_count";
    static final String TIME = "trip_time_in_secs";
    static final String TRIPDISTANCE = "trip_distance";
    static final String PICKUPLONG = "pickup_longitude";
    static final String PICKUPLAT = "pickup_latitude";
    static final String DROPOFFLONG = "dropoff_longitude";
    static final String DROPOFFLAT = "dropoff_latitude";
    
    // vendorID string in file
    public static final String VENDOR_CMT = "CMT";  // Trip.vendorID = True
    public static final String VENDOR_VTS = "VTS";  // Trip.vendorID = False

    public Data(String filename, RenderArea renderArea)
    {
        this.renderArea = renderArea;
        
        loadMeds("res/meds.txt");
        loadHacks("res/hacks.txt");
    }

    public void loadMeds(String file)
    {
        meds = renderArea.loadStrings(file);
    }

    public void loadHacks(String file)
    {
        hacks = renderArea.loadStrings(file);
    }
}
