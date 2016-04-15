package nl.jonaskoperdraat.lichtkrantcontroller.rest;

import nl.jonaskoperdraat.lichtkrantcontroller.model.AutoPage;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Page;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import nl.jonaskoperdraat.lichtkrantcontroller.parse.ShowParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for the rest interface for loading and controlling a {@link Show}.
 */
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    ShowParser showParser;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    Show getShow() {
        Show show = new Show();

        List<Page> pages = new LinkedList<>();

        pages.add( new Page("Pagina 1"));
        pages.add( new Page("Pagina 2"));
        pages.add( new Page("Pagina 3"));
        pages.add( new Page("Pagina 4"));
        pages.add( new AutoPage(15, "Autopagina 5"));

        show.setPages(pages);

        return show;
    }

    @RequestMapping(value = "/open", method = RequestMethod.POST)
    Show open(@RequestParam("file") MultipartFile file) throws InvalidFileFormatException, IOException {

        if (file.isEmpty()) {
            throw new InvalidFileFormatException();
        }

        showParser.setInputStream(file.getInputStream());

        // parse input file into show.
        showParser.parse();

        // TODO store show

        // return parsed show
        return showParser.getShow();
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Unable to parse the uploaded file")
    private class InvalidFileFormatException extends Exception {
        private static final long serialVersionUID = 100L;
    }
}
