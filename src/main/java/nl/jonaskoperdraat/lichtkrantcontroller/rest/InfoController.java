package nl.jonaskoperdraat.lichtkrantcontroller.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonas on 2-5-2016.
 */
@RestController
public class InfoController {

    @Value("${application.version}")
    String applicationVersion = "test";

    @RequestMapping("/info")
    public Map<String, Object> getApplicationVersion() {
        Map info = new HashMap<String, Object>();
        info.put("applicationVersion", applicationVersion);
        return info;
    }

}
