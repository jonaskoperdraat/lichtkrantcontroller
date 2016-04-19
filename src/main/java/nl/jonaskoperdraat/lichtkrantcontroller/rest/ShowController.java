package nl.jonaskoperdraat.lichtkrantcontroller.rest;

import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import nl.jonaskoperdraat.lichtkrantcontroller.model.ShowStatus;
import nl.jonaskoperdraat.lichtkrantcontroller.parse.ShowParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Class for the rest interface for loading and controlling a {@link Show}.
 */
@RestController
@RequestMapping("/show")
public class ShowController {

    private static final Logger LOG = LoggerFactory.getLogger(ShowController.class);

    @Value("testproperty")
    String testproperty;

    @Autowired
    ShowParser showParser;

    @Autowired
    Show show;

    @PostConstruct
    void init() {
        LOG.debug("Testproperty: {}", testproperty);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    Show getShow() {
        return show;
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    void open(@RequestParam("file") MultipartFile file) throws InvalidFileFormatException, IOException {

        if (file.isEmpty()) {
            throw new InvalidFileFormatException();
        }

        showParser.setInputStream(file.getInputStream());

        // parse input file into show.
        showParser.parse(show);
    }

    @RequestMapping(value = "/goToPage/{pageNrString}")
    ShowStatus goToPage(@PathVariable String pageNrString) throws InvalidPageNumberException {

        int pageNr = 0;

        try {
            pageNr = Integer.parseInt(pageNrString);

            show.goToPage(pageNr);
        } catch (NumberFormatException e)
        {
            throw new InvalidPageNumberException();
        } catch (Show.PageNumberOutOfBoundsException e) {
            throw new InvalidPageNumberException();
        }

        return show.getStatus();
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Unable to parse the uploaded file.")
    private class InvalidFileFormatException extends Exception {
        private static final long serialVersionUID = 100L;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="The page number is either in an invalid format or the page requested does not exist.")
    private class InvalidPageNumberException extends Exception
    {
        private static final long serialVersionUID = 101L;
    }

}
