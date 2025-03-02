package cl.pfequipo1.proyecto_final.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private Integer sensorId;

    @Column(name = "sensor_name", nullable = false)
    private String sensorName;

    @Column(name = "sensor_category")
    private String sensorCategory;  // Podrías usar un enum

    @Column(name = "sensor_meta")
    private String sensorMeta;

    @Column(name = "sensor_api_key", unique = true, nullable = false)
    private String sensorApiKey;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorData> sensorDataList = new ArrayList<>();

    public Sensor() {
    }

    public Sensor(String sensorName, String sensorCategory, String sensorMeta, String sensorApiKey) {
        this.sensorName = sensorName;
        this.sensorCategory = sensorCategory;
        this.sensorMeta = sensorMeta;
        this.sensorApiKey = sensorApiKey;
    }

    // Getters / Setters
    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorCategory() {
        return sensorCategory;
    }

    public void setSensorCategory(String sensorCategory) {
        this.sensorCategory = sensorCategory;
    }

    public String getSensorMeta() {
        return sensorMeta;
    }

    public void setSensorMeta(String sensorMeta) {
        this.sensorMeta = sensorMeta;
    }

    public String getSensorApiKey() {
        return sensorApiKey;
    }

    public void setSensorApiKey(String sensorApiKey) {
        this.sensorApiKey = sensorApiKey;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<SensorData> getSensorDataList() {
        return sensorDataList;
    }

    public void setSensorDataList(List<SensorData> sensorDataList) {
        this.sensorDataList = sensorDataList;
    }

    // Métodos utilitarios
    public void addSensorData(SensorData data) {
        sensorDataList.add(data);
        data.setSensor(this);
    }

    public void removeSensorData(SensorData data) {
        sensorDataList.remove(data);
        data.setSensor(null);
    }
}
