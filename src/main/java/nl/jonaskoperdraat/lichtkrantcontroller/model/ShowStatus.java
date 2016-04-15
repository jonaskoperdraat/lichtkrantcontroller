package nl.jonaskoperdraat.lichtkrantcontroller.model;

/**
 * Class representing the current status of a {@link Show}
 */
public class ShowStatus {

    private int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
