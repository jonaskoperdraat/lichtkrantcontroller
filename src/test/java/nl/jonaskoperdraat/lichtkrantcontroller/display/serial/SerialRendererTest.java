package nl.jonaskoperdraat.lichtkrantcontroller.display.serial;

import nl.jonaskoperdraat.lichtkrantcontroller.config.AppConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jonas on 29-4-2016.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(AppConfiguration.class)
public class SerialRendererTest {

    @Autowired
    SerialRenderer serialRenderer;


    // @TODO mock show and make renderer send message on notify.
//    @Test
//    @Ignore
    public void sendTestMessage() throws InterruptedException {
        serialRenderer.sendMessage("Lichtkrant test...");

        int count = 0;
        while(true) {
            Thread.sleep(2000);
            serialRenderer.sendMessage("Lichtkrant test... count = " + count++);
        }
    }

}
