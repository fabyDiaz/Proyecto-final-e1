package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "position")
    private String position;  // Podrías usar un JSON o campos separados para lat/lon

    @Column(name = "voltage")
    private Double voltage;

    @Column(name = "current")
    private Double currentValue;  // "current" no se usa directamente como nombre de variable en Java

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    public SensorData() {
    }

    // Constructor con todos los campos
    public SensorData(Double temperature, Double humidity, Double speed, String position,
                      Double voltage, Double currentValue, LocalDateTime createdAt) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.speed = speed;
        this.position = position;
        this.voltage = voltage;
        this.currentValue = currentValue;
        this.createdAt = createdAt;
    }

    // GETTERS y SETTERS
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
}
