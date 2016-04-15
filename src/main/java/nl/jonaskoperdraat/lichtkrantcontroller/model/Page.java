package nl.jonaskoperdraat.lichtkrantcontroller.model;

/**
 * Class representing a single page of a {@link Show}.
 */
public class Page {

    private String content;

    public Page(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
