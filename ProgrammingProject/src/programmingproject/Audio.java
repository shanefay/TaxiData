package programmingproject;

import ddf.minim.Minim;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Audio //Cal'd Own Tried and Tested Audio Playing Package!
{

    ArrayList<SoundClip> clips = new ArrayList<SoundClip>();
    Minim minim;
    int currentClip = -1;

    public Audio(RenderArea renderArea)
    {
        minim = new Minim(renderArea);
    }

    public void loadClip(String file, String name, int offset, double audioEdge, double shakeValue)
    {
        System.out.println("Loading " + file);
        clips.add(new SoundClip(minim.loadFile(file), name, audioEdge, shakeValue));
    }

    public void playClip(String name)
    {
        for (SoundClip clip : clips)
        {
            clip.clip.pause();
        }
        for (int i = 0; i < clips.size(); i++)
        {
            SoundClip clip = clips.get(i);
            if (clip.name.equals(name))
            {
                currentClip = i;
                clip.clip.rewind();
                clip.clip.play();
            }
        }
    }

    public void playClip(int clipNum)
    {
        for (SoundClip clip : clips)
        {
            clip.clip.pause();
        }
        if (clipNum < clips.size())
        {
            SoundClip clip = clips.get(clipNum);
            currentClip = clipNum;
            clip.clip.rewind();
            clip.clip.play();
        }

    }

    public int getCurrentPosition()
    {
        if (currentClip != -1)
        {
            return clips.get(currentClip).clip.position();
        } else
        {
            return -1;
        }
    }

    public double getAudioShake()
    {
        if (currentClip != -1)
        {
            return clips.get(currentClip).shakeValue;
        }
        return 0.0;
    }

    public double getAudioEdge()
    {
        if (currentClip != -1)
        {
            return clips.get(currentClip).audioEdge;
        }
        return 0.3;
    }

    public float getCurrentLevel()
    {
        if (currentClip != -1)
        {
            return ((clips.get(currentClip).clip.mix.level()));
        }
        return 0;
    }
    
    
    public void setGain(float gain, int clip)
    {
            clips.get(clip).clip.setGain(gain);
    }
    
        public void shiftGain(float gain, float dunno, int time, int clip)
    {
            clips.get(clip).clip.shiftGain(gain, dunno, time);
    }

    public void stopAllClips()
    {
        for (SoundClip clip : clips)
        {
            clip.clip.pause();
        }
    }

    public void playClip(String name, float volume)
    {
        for (int i = 0; i < clips.size(); i++)
        {
            SoundClip clip = clips.get(i);
            if (clip.name.equals(name))
            {
                currentClip = i;
                clip.clip.rewind();
                float maxGain = clip.clip.gain().getMaximum();
                float minGain = clip.clip.gain().getMinimum();
                float gain = (maxGain + minGain) * (volume / 100);
                System.out.println(gain);
                clip.clip.setGain(gain);
                clip.clip.play();
            }
        }
    }
}
