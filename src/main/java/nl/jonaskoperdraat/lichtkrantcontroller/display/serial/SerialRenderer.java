package nl.jonaskoperdraat.lichtkrantcontroller.display.serial;

import gnu.io.NRSerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jonas on 19-4-2016.
 */
@Component
public class SerialRenderer {

    private static final Logger LOG = LoggerFactory.getLogger(SerialRenderer.class);

    @Value("${serial.port}")
    String port;

    @Value("${serial.baudrate}")
    int baudrate;

    @PostConstruct
    public void testWrite() throws IOException {

        LOG.info("Available serial ports: {}", Arrays.toString(NRSerialPort.getAvailableSerialPorts().toArray()));

        NRSerialPort nrSerialPort = new NRSerialPort(port, baudrate);
        nrSerialPort.connect();

        DataOutputStream out = new DataOutputStream(nrSerialPort.getOutputStream());
        out.write(new byte[] {0x68, 0x6F, 0x69});

        out.close();
    }

}
