package programmingproject;

import controlP5.*;

/**
 *
 * @author cal
 */
public class QueryGUI
{
    final int height;
    final int width;
    
    public static final String LABEL = "queryGUI";
    public static final String QUERY_ONE_LABEL = "queryOne";
    public static final String QUERY_TWO_LABEL = "queryTwo";
    public static final String[] DAYS = {"mo", "tu", "we", "th", "fr", "sa", "su"};
    
    public boolean ready;
    
    RenderArea renderArea;
    ControlP5 cp5;
    Group queryWindow;
    Button janButton, febButton;
    Slider sampleSlider;
    RadioButton sampleRadio;
    Button setQueryOne, setQueryTwo;
    CheckBox vendorID, months, days;
    Range janDate, febDate, hours, passengers;
    
    final int SAMPLE_POS = 10;
    

    public QueryGUI(RenderArea renderArea, ControlP5 cp5)
    {
        ready = false;
        this.renderArea = renderArea;
        this.cp5 = cp5;

        height = 360;
        width = 400;
        
        setup();
        ready = true;
    }

    public void setup()
    {
        queryWindow = cp5.addGroup(LABEL)
                .setLabel("Queries")
                .setBarHeight(20)
                .setPosition(0, 20)
                .setWidth(width)
                .activateEvent(false)
                .setBackgroundColor(renderArea.color(48, 196))
                .setBackgroundHeight(height)
                .close();

        // Set query 1 and 2 Buttons:
        setQueryOne = cp5.addButton(QUERY_ONE_LABEL)
                .setCaptionLabel("Set Query 1")
                .setPosition(10, height - 30)
                .setSize(100, 20)
                .moveTo(queryWindow);
        setQueryTwo = cp5.addButton(QUERY_TWO_LABEL)
                .setCaptionLabel("Set Query 2")
                .setPosition(width - 110, height - 30)
                .setSize(100, 20)
                .moveTo(queryWindow);

        // Sample size selector:
        sampleSlider = cp5.addSlider("sampleSlider")
                .setBroadcast(false) 
                .setCaptionLabel("Sample Size")
                .setPosition(10, SAMPLE_POS)
                .setSize(300, 15)
                .setRange(0, 100)
                .setDecimalPrecision(0)
                .showTickMarks(false)
                .setValue(50)
                .setBroadcast(true)
                .moveTo(queryWindow);
        sampleRadio = cp5.addRadioButton("sampleRadio")
                .setSize(15, 15)
                .setPosition(10, SAMPLE_POS + 20)
                .setItemsPerRow(6)
                .setSpacingColumn(50)
                .addItem("10", 10)
                .addItem("100", 100)
                .addItem("1k", 1000)
                .addItem("10k", 10000)
                .addItem("100k", 100000)
                .addItem("1M", 1000000)
                .moveTo(queryWindow)
                .activate(2);
                       
        // Months checkboxs:
        months = cp5.addCheckBox("months")
                .setPosition(20, 70)
                .setSize(20, 20)
                .setItemsPerRow(1)
                .setSpacingRow(5)
                .addItem("Jan", 0)
                .addItem("Feb", 0)
                .activateAll()
                .moveTo(queryWindow)
                ;
        janDate = cp5.addRange("janDate")
                .setBroadcast(false) 
                .setCaptionLabel("Date")
                .setPosition(80, 72.5f)
                .setSize(250, 15)
                .setDecimalPrecision(0)
                .setRange(1, DateTime.DAYS_IN_MONTH[0])
                .setRangeValues(1, DateTime.DAYS_IN_MONTH[0])
                .setValue(15)
                .showTickMarks(false)
                .setBroadcast(true)
                .moveTo(queryWindow)
                ;
        febDate = cp5.addRange("febDate")
                .setBroadcast(false) 
                .setCaptionLabel("Date")
                .setPosition(80, 97.5f)
                .setSize(250, 15)
                .setDecimalPrecision(0)
                .setRange(1, DateTime.DAYS_IN_MONTH[1])
                .setRangeValues(1, DateTime.DAYS_IN_MONTH[1])
                .setValue(15)
                .showTickMarks(false)
                .setBroadcast(true)
                .moveTo(queryWindow)
                ;

        // Days checkboxs:
        days = cp5.addCheckBox("days")
                .setPosition(20, 150)
                .setSize(20, 20)
                .setItemsPerRow(7)
                .setSpacingColumn(30)
                .setSpacingRow(5)
                .moveTo(queryWindow);
        for (int i = 0; i < DAYS.length; i++)
        {
            days.addItem(DAYS[i], i);
        }
        days.activateAll();

        hours = cp5.addRange("hours")
                .setCaptionLabel("Hours")
                .setPosition(10, 200)
                .setSize(300, 15)
                .setDecimalPrecision(0)
                .setRange(0, 23)
                .moveTo(queryWindow)
                ;

        passengers = cp5.addRange("passengers")
                .setCaptionLabel("Passengers")
                .setPosition(10, 240)
                .setSize(275, 15)
                .setDecimalPrecision(0)
                .setRange(0, 6)
                .moveTo(queryWindow)
                ;

        vendorID = cp5.addCheckBox("vendorID")
                .setSize(15, 15)
                .setPosition(90, 280)
                .setItemsPerRow(6)
                .setSpacingColumn(50)
                .addItem("CMT", 1)
                .addItem("VTS", 2)
                .setGroup(LABEL)
                .activate(0)
                .activate(1);
        cp5.addTextlabel("vendorIDLabel")
                .setText("Vendor ID:")
                .setPosition(10, 280)
                .moveTo(queryWindow);
    }
    
    public String getQuery()
    {
        String query = "SELECT * FROM taxi_data WHERE";
        query += " (\n" + monthRangeQuery(0, janDate) + " OR \n" + monthRangeQuery(1, febDate) + "\n)";
        query += weekDayQuery();
        query += hourQuery();
        query += vendorQuery();
        query += passengerQuery();
        query += sampleQuery();
        return query;
    }
    
    private String timeRangeQuery(long from, long to)
    {
        return "(pickup_datetime >= " + from + " AND pickup_datetime < " + to + ")";
    }
    
    private String monthRangeQuery(int index, Range dateSlider)
    {
        if (index >= 2 || months.getArrayValue(index) == 0) 
        {
            return "NULL";
        }
        long startOfMonth = DateTime.SECONDS_TILL_MONTH_STARTS[index];
        int lowerBound = ((int) dateSlider.getLowValue() - 1) * DateTime.SECONDS_PER_DAY;
        int upperBound = ((int) dateSlider.getHighValue()) * DateTime.SECONDS_PER_DAY;
        return timeRangeQuery(lowerBound + startOfMonth, upperBound + startOfMonth);
    }
    
    private String dayQuery(int index)
    {
        if (index >= 7)
        {
            return "NULL";
        }
        index = (index - DateTime.FIRST_WEEK_DAY + DateTime.DAYS_PER_WEEK) % DateTime.DAYS_PER_WEEK;
        int lowerBound = index * DateTime.SECONDS_PER_DAY;
        int upperBound = (index + 1) * DateTime.SECONDS_PER_DAY;
        
        String pickDT = "pickup_datetime % " + DateTime.SECONDS_PER_WEEK;
        return "(" + pickDT + " >= " + lowerBound + " AND " + pickDT + " < " + upperBound + ")";
    }
    
    private String weekDayQuery()
    {
        boolean allChecked = true;
        for(float value : days.getArrayValue()){
            if (value == 0)
            {
                allChecked = false;
            }
        }
        if (allChecked)
        {
            return "";
        }
        String query = "";
        query += " AND (\n";
        for (int i = 0; i < DateTime.DAYS_PER_WEEK; i++)
        {
            if (days.getArrayValue(i) == 1f)
            {
                query += dayQuery(i) + " OR \n";
            }
        }
        return query += "NULL\n)";
    }
    
    private String hourQuery()
    {
        int lowerBound = ((int) hours.getLowValue()) * DateTime.SECONDS_PER_HOUR;
        int upperBound = ((int) hours.getHighValue() + 1) * DateTime.SECONDS_PER_HOUR;
        String pickDT = "pickup_datetime % " + DateTime.SECONDS_PER_DAY;
        return " AND (\n(" + pickDT + " >= " + lowerBound + " AND " + pickDT + " < " + upperBound + ")\n)";
    }
    
    private String vendorQuery()
    {
        boolean cmt = vendorID.getArrayValue(0) == 1;
        boolean vts = vendorID.getArrayValue(1) == 1;
        if (cmt && vts)
        {
            return "";
        }
        String query = " AND (\n";
        if (!cmt && !vts) 
        {
            query += "NULL";
        }
        else if (cmt)
        {
            query += "vendor_id = 'CMT'";
        }
        else
        {
            query += "vendor_id = 'VTS'";
        }
        query += "\n)";
        return query;
    }
    
    private String passengerQuery()
    {
        int lowerBound = (int) passengers.getLowValue();
        int upperBound = (int) passengers.getHighValue();
        return " AND (\n(passenger_count >= " + lowerBound + " AND passenger_count <= " + upperBound + ")\n)";
    }
    
    private String sampleQuery()
    {
        return " LIMIT " + sampleSize();
    }
        
    private int sampleSize()
    {
        return (int) ((int) sampleSlider.getValue() * sampleRadio.getValue());
    }
    
    public void hide()
    {
        queryWindow.hide();
    }
    
    public void show()
    {
        queryWindow.show();
    }
}
