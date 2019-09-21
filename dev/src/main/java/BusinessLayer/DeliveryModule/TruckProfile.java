package BusinessLayer.DeliveryModule;

import BusinessLayer.Enums.Area;
import BusinessLayer.Enums.License;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name="TruckProfile")
public class TruckProfile implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "truck")
    @SequenceGenerator(name = "truck", sequenceName = "truck_seq", allocationSize = 1)

    private int ID;
    private String model;
    private int truckWeight;
    private int maxWeight;
    private License license;
    private Area area;

    public TruckProfile(String model, int truckWeight, int maxWeight, License license, Area area) {
        this.model = model;
        this.truckWeight = truckWeight;
        this.maxWeight = maxWeight;
        this.license = license;
        this.area = area;
    }

    public TruckProfile() {}


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getTruckWeight() {
        return truckWeight;
    }

    public void setTruckWeight(int truckWeight) {
        this.truckWeight = truckWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckProfile that = (TruckProfile) o;
        return getTruckWeight() == that.getTruckWeight() &&
                getMaxWeight() == that.getMaxWeight() &&
                Objects.equals(getModel(), that.getModel()) &&
                getLicense() == that.getLicense() &&
                getArea() == that.getArea();
    }


    @Override
    public String toString() {
        return "TruckProfile{" +
                "ID=" + ID +
                ", model='" + model + '\'' +
                ", truckWeight=" + truckWeight +
                ", maxWeight=" + maxWeight +
                ", truckLiecense=" +
                ", license=" + license +
                '}';
    }
}
