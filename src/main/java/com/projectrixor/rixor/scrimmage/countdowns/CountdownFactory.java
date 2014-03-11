package com.projectrixor.rixor.scrimmage.countdowns;

import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CountdownFactory {
    protected final Plugin plugin;
    protected final Set<CountdownController> controllers = new HashSet();

    public CountdownFactory(Plugin plugin) {
        this.plugin = plugin;
    }

    public void start(Countdown countdown, int seconds) {
        this.controllers.add(new CountdownController(this.plugin, countdown).start(seconds));
    }

    public void cancel(Countdown countdown) {
        for (Iterator it = this.controllers.iterator(); it.hasNext(); ) {
            CountdownController runner = (CountdownController)it.next();
            if (runner.getCountdown() == countdown) {
                runner.cancel();
                it.remove();
            }
        }
    }

    public Set<Countdown> getAll() {
        Set result = new HashSet(this.controllers.size());
        for (CountdownController runner : this.controllers) {
            result.add(runner.getCountdown());
        }
        return result;
    }

    public Set<Countdown> getAll(Class<? extends Countdown> countdownClass) {
        Set result = new HashSet();
        for (CountdownController runner : this.controllers) {
            if (countdownClass.isInstance(runner.getCountdown())) {
                result.add(runner.getCountdown());
            }
        }
        return result;
    }

    public void cancelAll() {
        for (CountdownController runner : this.controllers) {
            runner.cancel();
        }
        this.controllers.clear();
    }

    public void cancelAll(Class<? extends Countdown> countdownClass) {
        for (Iterator it = this.controllers.iterator(); it.hasNext(); ) {
            CountdownController runner = (CountdownController)it.next();
            if (countdownClass.isInstance(runner.getCountdown())) {
                runner.cancel();
                it.remove();
            }
        }
    }
}
