package programmingproject;

import java.util.ArrayList;

/**
 *
 * @author Aran Nolan <nolanar@tcd.ie>
 */
public class CurrentQuery
{

    RenderArea renderArea;
    ArrayList<Trip> queryOne, queryTwo, activeQuery;
    boolean activeQueryOne = true;

    public CurrentQuery(RenderArea renderArea)
    {
        this.renderArea = renderArea;

        queryOne = new ArrayList<>();
        queryTwo = new ArrayList<>();

        activeQuery = queryOne;
    }

    public void swap()
    {
        if (activeQueryOne)
        {
            activeQuery = queryTwo;
        } else
        {
            activeQuery = queryOne;
        }
        activeQueryOne = !activeQueryOne;
    }

    public void setQueryOneActive()
    {
        activeQueryOne = true;
        updateActiveQuery();
    }

    public void setQueryTwoActive()
    {
        activeQueryOne = false;
        updateActiveQuery();
    }
    
    public void requestQuery(String query, boolean setQueryOne)
    {

        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                System.out.println("Getting query " + (setQueryOne ? "1" : "2") + " for " + query);
                ArrayList<Trip> queryBuffer = renderArea.query.getTrips(query);

                if (setQueryOne)
                {
                    queryOne = queryBuffer;
                } else
                {
                    queryTwo = queryBuffer;
                }
                activeQueryOne = setQueryOne;
                updateActiveQuery();
                notifyVisualisation();
            }
        }, "Query Thread " + (setQueryOne ? "1" : "2")).start();
    }

    public void notifyVisualisation()
    {
        renderArea.currentVisualisation.reloadData();
    }

    public ArrayList<Trip> active()
    {
        return activeQuery;
    }

    public void updateActiveQuery()
    {
        activeQuery = (activeQueryOne ? queryOne : queryTwo);
    }

    public void setQueryOne(ArrayList<Trip> trips)
    {
        queryOne = trips;
    }

    public void setQueryTwo(ArrayList<Trip> trips)
    {
        queryTwo = trips;
    }
}
