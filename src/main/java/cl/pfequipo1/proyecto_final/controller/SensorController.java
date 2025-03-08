package cl.pfequipo1.proyecto_final.controller;

import cl.pfequipo1.proyecto_final.dto.LocationDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDTO;
import cl.pfequipo1.proyecto_final.service.LocationServiceImpl;
import cl.pfequipo1.proyecto_final.service.SensorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor")
public class SensorController {

    @Autowired
    private SensorServiceImpl sensorService;
/*
    @PostMapping
    public ResponseEntity<SensorDTO> createSensor(@RequestBody LocationDTO locationDTO, @RequestHeader("company-api-key") String companyApiKey) {

        SensorDTO createdSensor = sensorService.create(locationDTO, companyApiKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSensor);
    }
    */
}
