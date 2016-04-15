package nl.jonaskoperdraat.lichtkrantcontroller.parse;

import nl.jonaskoperdraat.lichtkrantcontroller.Application;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the {@link ShowParser} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
public class ShowParserTest {

    @Autowired
    ShowParser showParser;

    @Test
    public void testParse() throws IOException {
        String[] pages = new String[] {
                "pagina 1: eerste pagina ><.",
                "p2: ,.: [] fdjsklfjdlska;",
                "pag. 3: soiuwerjklfdsjoi oisfddsjklf jwaio fupwe fld ja120382109"
        };

        String showText = String.join("\n", pages);

        InputStream inputStream = new ByteArrayInputStream(showText.getBytes(StandardCharsets.UTF_8));

        showParser.setInputStream(inputStream);

        showParser.parse();

        Show show = showParser.getShow();

        for( int i = 0; i < pages.length; i++ ) {
            assertEquals(showParser.cleanLine(pages[i]), show.getPages().get(i).getContent());
        }
        assertEquals(0, show.getStatus().getCurrentPage());

    }

    @Test
    public void testCleanLine() {

        String accepted = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVW    1234567890!@#$^&*()_+:;?\"'.,><";
        String notAccepted = "`~{}[]|\\";

        assertEquals(accepted, showParser.cleanLine(accepted + notAccepted));
    }


}