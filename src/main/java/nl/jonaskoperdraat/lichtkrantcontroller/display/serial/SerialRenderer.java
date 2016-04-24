package nl.jonaskoperdraat.lichtkrantcontroller.display.serial;

import gnu.io.NRSerialPort;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Page;
import nl.jonaskoperdraat.lichtkrantcontroller.model.Show;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jonas on 19-4-2016.
 */
@Component
public class SerialRenderer implements Observer {

    private static final Logger LOG = LoggerFactory.getLogger(SerialRenderer.class);

    @Value("${serial.port}")
    String port;

    @Value("${serial.baudrate}")
    int baudrate;

    @Autowired
    Show show;

    NRSerialPort nrSerialPort = null;

    @PostConstruct
    public void init() throws IOException {

        show.addObserver(this);

        LOG.info("Available serial ports: {}", Arrays.toString(NRSerialPort.getAvailableSerialPorts().toArray()));

        // Initialize the serial port.
        nrSerialPort = new NRSerialPort(port, baudrate);
        nrSerialPort.connect();

    }

    @Override
    public void update(Observable o, Object arg) {
        LOG.debug("SerialRenderer.update({}, {})", o, arg);
        if (o instanceof Show) {
            LOG.debug("Received notification from show. Status: {}", show.getStatus());
            Page page = show.getCurrentPage();
            sendMessage(page.getContent());
        }
    }

    private void sendMessage(String message){

        // Initialize byte array with length of message + 4 bytes header + 2 bytes footer.
        byte[] serialMessage = new byte[message.length() + 6];

        try {
            byte[] messageBytes = message.getBytes("IBM437");

            serialMessage[0] = 0; // NUL - Header sync.
            serialMessage[1] = 1; // Number of text lines on the sign -- 1 for now.
            serialMessage[2] = 0; // Sign address -- 0 = 'All'.
            serialMessage[3] = 3; // ETX - End of text (header)

            for (int i = 0; i < messageBytes.length; i++) {
                serialMessage[i + 4] = messageBytes[i];
            }

            serialMessage[serialMessage.length - 2] = 4; // EOT - End Of Transmission (text)

            // Calculate checksum
            int checksum = 0;
            for ( int i = 0; i < serialMessage.length-1; i++ ) {
                checksum = checksum ^ serialMessage[i];
            }
            serialMessage[serialMessage.length-1] = (byte) checksum;

            LOG.debug("Sending message: {}", Arrays.toString(serialMessage));

            // Send the message over the serial connection.
            DataOutputStream out = new DataOutputStream(nrSerialPort.getOutputStream());
            out.write(serialMessage);
            out.close();

            LOG.debug("Message sent.");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
