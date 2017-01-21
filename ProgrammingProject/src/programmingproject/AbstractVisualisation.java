package programmingproject;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import processing.opengl.PGraphics3D;

/**
 *
 * @author Cal
 */
public class AbstractVisualisation
{

    private CurrentQuery currentQuery;

    public void reloadData()
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("ERROR - reloadData() not defined in this class!");
        }
    }

    public void clearMemory()
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("ERROR - clearMemory() not defined in this class!");
        }
    }

    public void draw(PGraphics3D buffer)
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("ERROR - draw(PGraphics3D buffer) not defined in this class!");
        }
    }

    public void setCurrentQuery(CurrentQuery currentQuery)
    {
        this.currentQuery = currentQuery;
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_1)
        {
            currentQuery.setQueryOneActive();
            reloadData();
        } else if (e.getKeyCode() == KeyEvent.VK_2)
        {
            currentQuery.setQueryTwoActive();
            reloadData();
        }
    }

    public void mousePressed(MouseEvent e)
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("INFO - mousePressed(MouseEvent e) not defined in this class!");
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("INFO - mouseDragged(MouseEvent e) not defined in this class!");
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("INFO - mousePressed(MouseEvent e) not defined in this class!");
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (ProgrammingProject.DEBUG)
        {
            System.out.println("INFO - mouseWheelMoved(MouseWheelEvent e) not defined in this class!");
        }
    }
}
