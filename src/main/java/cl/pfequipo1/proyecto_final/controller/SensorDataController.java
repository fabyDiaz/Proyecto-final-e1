package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.service.SensorDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {
    @Autowired
    private SensorDataServiceImpl sensorDataService;


}
