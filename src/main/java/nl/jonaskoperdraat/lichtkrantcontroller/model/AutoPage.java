package nl.jonaskoperdraat.lichtkrantcontroller.model;

/**
 * Created by jonas on 14-4-2016.
 */
public class AutoPage extends Page {

    private int duration;

    public AutoPage(int duration, String content) {
        super(content);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
