package nl.jonaskoperdraat.lichtkrantcontroller.dispaly;

import nl.jonaskoperdraat.lichtkrantcontroller.Application;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Observable;
import java.util.Observer;

/**
 * Listens to changes in the show and 'displays' the current page to the console.
 */
@Component
public class ConsoleRenderer implements Observer {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    @Autowired
    Show show;

    public ConsoleRenderer() {
        LOG.debug("ConsoleRenderer constructor.");
    }

    @PostConstruct
    void init() {
        LOG.debug("ConsoleRenderer.init()");
        show.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        LOG.debug("ConsoleRender.update({}, {})", o, arg);
        if (o instanceof Show) {
            LOG.debug("Received notification from show. Status: {}", show.getStatus());
            System.out.println(show.getPages().get(show.getStatus().getCurrentPage()).getContent());
        }
    }
}
