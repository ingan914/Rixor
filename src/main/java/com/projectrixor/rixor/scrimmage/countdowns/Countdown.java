package com.projectrixor.rixor.scrimmage.countdowns;

public abstract class Countdown
{
    public void onStart(int secondsLeft)
    {
    }

    public void onTick(int secondsLeft, int starttime)
    {
    }

    public void onCancel()
    {
    }

    public abstract void onEnd();
}
