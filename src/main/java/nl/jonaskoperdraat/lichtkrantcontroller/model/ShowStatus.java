package nl.jonaskoperdraat.lichtkrantcontroller.model;

import java.util.UUID;

/**
 * Class representing the current status of a {@link Show}
 */
public class ShowStatus {

    private int currentPage = 0;
    private UUID showUUID;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setShowUUID(UUID uuid) {
        this.showUUID = uuid;
    }

    public UUID getShowUUID() {
        return showUUID;
    }
}
