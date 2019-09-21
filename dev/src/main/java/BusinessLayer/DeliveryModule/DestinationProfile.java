package BusinessLayer.DeliveryModule;


import BusinessLayer.Enums.Area;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "DestinationProfile")
public class DestinationProfile implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "destination")
    @SequenceGenerator(name = "destination", sequenceName = "destination_seq", allocationSize = 1)
    private int destId;
    private String name;
    private String city;
    private String street;
    private int number;
    private int phoneNuber;
    private String contactName;
    private Area area;


    public DestinationProfile() {
    }

    public DestinationProfile(String name, String city, String street, int number, int phoneNuber, String contactName, Area area) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.number = number;
        this.phoneNuber = phoneNuber;
        this.contactName = contactName;
        this.area = area;
    }

    public int getDestId() {
        return destId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPhoneNuber() {
        return phoneNuber;
    }

    public void setPhoneNuber(int phoneNuber) {
        this.phoneNuber = phoneNuber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
        DestinationProfile that = (DestinationProfile) o;
        return getNumber() == that.getNumber() &&
                getPhoneNuber() == that.getPhoneNuber() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getStreet(), that.getStreet()) &&
                Objects.equals(getContactName(), that.getContactName()) &&
                getArea() == that.getArea();
    }


    @Override
    public String toString(){
        return destId + ") "  +
                name;
    }
}
