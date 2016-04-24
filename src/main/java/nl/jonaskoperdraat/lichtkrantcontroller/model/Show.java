package nl.jonaskoperdraat.lichtkrantcontroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.jonaskoperdraat.lichtkrantcontroller.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Class representing a show. A show consists of a set of {@link Page}s.
 */
@Component
public class Show extends Observable {

    private static Logger LOG = LoggerFactory.getLogger(Show.class);

    // Identifier uniquely identifying the current show. Will be recalculated each time
    // a show is loaded. A reference will be stored in showstatus aswell so a status object
    // can be matched against a show.
    private UUID uuid;

    private List<Page> pages;

    private ShowStatus status = new ShowStatus();

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void reset() {
        status = new ShowStatus();
        pages = null;
        uuid = UUID.randomUUID();
        status.setShowUUID(uuid);
    }

    public void goToPage(int pageNr) throws PageNumberOutOfBoundsException {
        LOG.debug("Show.goToPage({})", pageNr);
        // check if the pagenumber is valid
        if (pageNr >= pages.size()) {
            throw new PageNumberOutOfBoundsException();
        }

        status.setCurrentPage(pageNr);

        // Notify any observers that might be interested in the change.

        LOG.debug("Notifying observers...");
        setChanged();
        notifyObservers();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        LOG.debug("Show.addObserver({})", o);
        super.addObserver(o);
    }

    @JsonIgnore
    public Page getCurrentPage() {
        return pages.get(status.getCurrentPage());
    }

    public class PageNumberOutOfBoundsException extends Exception {
    }

}
