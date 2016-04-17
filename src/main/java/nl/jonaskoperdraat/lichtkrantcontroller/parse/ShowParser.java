package nl.jonaskoperdraat.lichtkrantcontroller.parse;

import nl.jonaskoperdraat.lichtkrantcontroller.model.Page;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for parsing text files into a {@link Show} model.
 */
@Component
public class ShowParser {

    private static final Logger LOG = LoggerFactory.getLogger(ShowParser.class);

    private BufferedReader reader;

    public void parse(Show show) throws IOException {

        LOG.debug("Parsing input stream.");

        show.reset();

        if (reader == null) {
            throw new IOException("No input stream provided!");
        }

        List<Page> pages = new LinkedList<>();

        String line;
        while((line = reader.readLine()) != null) {

            LOG.debug("Adding line:");
            LOG.debug("Input: {}", line);

            // Strip any non text characters from the line
            line = cleanLine(line);

            LOG.debug("Clean: {}", line);

            pages.add(new Page(line));
        }

        show.setPages(pages);

        try {
            show.goToPage(0);
        } catch (Show.PageNumberOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    String cleanLine(String line) {
        return line.replaceAll("[^\\w\\s!@#$^&*()_\\-\\+:;?\"'.,><]", "");
    }

    public void setInputStream(InputStream inputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

}
