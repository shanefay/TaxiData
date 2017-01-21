package programmingproject;

import javax.swing.JFrame;
import processing.core.PApplet;


/**
 *
 * @author cal
 */
public class ProgrammingProject extends PApplet
{
    public static final boolean DEBUG = false;
    
    RenderArea renderArea;
    
    public ProgrammingProject()
    {
        createUserInterface();
    }
    
    public void createUserInterface()
    {
        JFrame mainWindow = new JFrame("Programming Project - TOTALLY AWESOME EDITION!");
        
        renderArea = new RenderArea();
        
        mainWindow.setSize(1000, 600);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.add(renderArea);
        
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);
        //mainWindow.setExtendedState(mainWindow.MAXIMIZED_BOTH);
        
        renderArea.init();
    }
    
    public static void main(String[] args)
    {
        new ProgrammingProject();
        //new PreprocessData("/home/cal/NetBeansProjects/programming-project-group-3/ProgrammingProject/res/trip_data_1.csv",
        //                   "/home/cal/NetBeansProjects/programming-project-group-3/ProgrammingProject/res/trip_data_2.csv");
    }
}
