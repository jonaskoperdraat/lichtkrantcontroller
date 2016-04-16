package nl.jonaskoperdraat.lichtkrantcontroller.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Class representing a show. A show consists of a set of {@link Page}s.
 */
@Component
public class Show {

    // Identifier uniquely idnetifying the current show. Will be recalculated each time
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
        // check if the pagenumber is valid
        if (pageNr >= pages.size()) {
            throw new PageNumberOutOfBoundsException();
        }

        status.setCurrentPage(pageNr);

        // TODO: make display actuall display the current page.
        // (notifying listeners or something like that.
    }

    public class PageNumberOutOfBoundsException extends Exception {
    }

}
