package nl.jonaskoperdraat.lichtkrantcontroller.display.serial;

import gnu.io.NRSerialPort;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by jonas on 19-4-2016.
 */
@Component
public class PortsLister {

    public PortsLister() {}

    @PostConstruct
    void printPorts() {
        Set<String> ports = NRSerialPort.getAvailableSerialPorts();
        ports.forEach(System.out::println);
    }

}
