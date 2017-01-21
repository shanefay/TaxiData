package programmingproject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cal
 */
public class PreprocessData //Used to generate the file for the MySQL database! :D
{

    ArrayList<String> medallions = new ArrayList<>();
    ArrayList<String> hacks = new ArrayList<>();
    PrintWriter out;
    PrintWriter meds, hack;

    public PreprocessData(String file, String file2)
    {
        try
        {
            out = new PrintWriter("taxi_data.csv");
            meds = new PrintWriter("meds.txt");
            hack = new PrintWriter("hacks.txt");
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(PreprocessData.class.getName()).log(Level.SEVERE, null, ex);
        }
        processData(file);
        processData(file2);
        writeAuxDataFiles();
    }

    public void processData(String dataFile)
    {
        BufferedReader buff = null;
        try
        {
            buff = new BufferedReader(new FileReader(dataFile));
            String current = "";
            int count = 0;
            buff.readLine(); //Skip the headers!
            while ((current = buff.readLine()) != null)
            {
                count++;
                if (count % 10000 == 0)
                {
                    System.out.println("Loading Line " + count + "...");
                }
                String[] currentLine = current.split(",");
                boolean found = false;
                for (int i = 0; i < medallions.size(); i++)
                {
                    if (medallions.get(i).equals(currentLine[0]))
                    {
                        currentLine[0] = "" + i;
                        found = true;
                    }
                }
                if (!found)
                {
                    medallions.add(currentLine[0]);
                    writeMed(currentLine[0]);
                    currentLine[0] = "" + medallions.size();
                }
                found = false;
                for (int i = 0; i < hacks.size(); i++)
                {
                    if (hacks.get(i).equals(currentLine[1]))
                    {
                        currentLine[1] = "" + i;
                        found = true;
                    }
                }
                if (!found)
                {
                    hacks.add(currentLine[1]);
                    writeHack(currentLine[1]);
                    currentLine[1] = "" + hacks.size();
                }
                currentLine[5] = "" + DateTime.dateTimeToSecs(currentLine[5]);
                if (currentLine[4].equals("Y"))
                {
                    currentLine[4] = "1";
                }
                else
                {
                    currentLine[4] = "0";
                }

                appendData(currentLine);
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeAuxDataFiles()
    {
        try
        {
            PrintWriter newData = new PrintWriter("meds2.txt");
            for (String med : medallions)
            {
                newData.println(med);
                newData.flush();
            }
            newData.close();
            newData = new PrintWriter("hacks2.txt");
            for (String hack : hacks)
            {
                newData.println(hack);
                newData.flush();
            }
            newData.close();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(PreprocessData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeMed(String med)
    {
        meds.println(med);
        meds.flush();
    }
    
    public void writeHack(String med)
    {
        hack.println(med);
        hack.flush();
    }

    public void appendData(String[] data)
    {
        String line = "";
        for (int i = 0; i < data.length; i++)
        {
            if (i != 6)
            {
                line += (i == 0 ? "" : ",") + data[i];
            }
        }
        out.println(line);
        out.flush();
    }
}
