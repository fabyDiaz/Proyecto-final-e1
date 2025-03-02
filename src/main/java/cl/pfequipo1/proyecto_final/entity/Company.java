package cl.pfequipo1.proyecto_final.entity;


import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_api_key", nullable = false, unique = true)
    private String companyApiKey;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations = new ArrayList<>();

    public Company() {
    }

    public Company(String companyName, String companyApiKey) {
        this.companyName = companyName;
        this.companyApiKey = companyApiKey;
    }

    // Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyApiKey() {
        return companyApiKey;
    }

    public void setCompanyApiKey(String companyApiKey) {
        this.companyApiKey = companyApiKey;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
        location.setCompany(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.setCompany(null);
    }
}
