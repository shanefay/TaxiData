package programmingproject;

import ddf.minim.*;

public class SoundClip //Cal'd Own Tried and Tested Audio Playing Package!
{
    AudioPlayer clip;
    String name;
    double audioEdge = 0.45;
    double shakeValue = 0.0;
    
    public SoundClip(AudioPlayer clip, String name, double audioEdge, double shakeValue)
    {
        this.clip = clip;
        this.name = name;
        this.audioEdge = audioEdge;
        this.shakeValue = shakeValue;
    }
}
