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

    public void sendMessage(String message){

        // Initialize byte array with length of message + 4 bytes header + 2 bytes footer.
        char[] serialMessage = new char[message.length() + 13];

        try {
            byte[] messageBytes = message.getBytes("IBM437");

            int i = 0;

            serialMessage[i++] = 0; // NUL - Header sync.
            serialMessage[i++] = 1; // Number of text lines on the sign -- 1 for now.
            serialMessage[i++] = 0; // Sign address -- 0 = 'All'.
            serialMessage[i++] = 3; // ETX - End of text (header)

            serialMessage[i++] = (char)(Short.parseShort("11000000", 2)); // Serial status flag
            serialMessage[i++] = 48; // Page number x100 (ASCII)
            serialMessage[i++] = 48; // Page number x10  (ASCII)
            serialMessage[i++] = 49; // Page number x1   (ASCII)

            serialMessage[i++] = (char)Short.parseShort("11100000", 2); // Tempo
            serialMessage[i++] = (char)Short.parseShort("11000001", 2); // Function
            serialMessage[i++] = (char)Short.parseShort("10000000", 2); // Page status

            for (int j = 0; j < messageBytes.length; j++) {
                serialMessage[j + i] = (char)messageBytes[j];
            }

            serialMessage[serialMessage.length - 2] = 4; // EOT - End Of Transmission (text)

            // Calculate checksum
            int checksum = 0;
            for ( int j = 0; j < serialMessage.length-1; j++ ) {
                checksum = checksum ^ serialMessage[j];
            }
            serialMessage[serialMessage.length-1] = (char) checksum;

            LOG.debug("Sending message: {}", Arrays.toString(serialMessage));

            byte[] serialByteMessage = new byte[serialMessage.length];
            for( int j = 0; j < serialByteMessage.length; j++) {
                serialByteMessage[j] = (byte)serialMessage[j];
            }

            // Send the message over the serial connection.
            DataOutputStream out = new DataOutputStream(nrSerialPort.getOutputStream());
            out.write(serialByteMessage);
            out.close();

            LOG.debug("Message sent.");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
