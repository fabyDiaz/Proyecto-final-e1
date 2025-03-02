package cl.pfequipo1.proyecto_final.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "location_country")
    private String locationCountry;

    @Column(name = "location_city")
    private String locationCity;

    @Column(name = "location_meta")
    private String locationMeta;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sensor> sensors = new ArrayList<>();

    public Location() {
    }

    public Location(String locationName, String locationCountry, String locationCity, String locationMeta, Company company) {
        this.locationName = locationName;
        this.locationCountry = locationCountry;
        this.locationCity = locationCity;
        this.locationMeta = locationMeta;
        this.company = company;
    }

    // Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationMeta() {
        return locationMeta;
    }

    public void setLocationMeta(String locationMeta) {
        this.locationMeta = locationMeta;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
        sensor.setLocation(this);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
        sensor.setLocation(null);
    }
}
