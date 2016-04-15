package nl.jonaskoperdraat.lichtkrantcontroller.model;

import java.util.List;

/**
 * Class representing a show. A show consists of a set of {@link Page}s.
 */
public class Show {

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

}
