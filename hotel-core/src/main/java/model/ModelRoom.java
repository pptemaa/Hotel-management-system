package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
@Entity
@Table(name = "room")
public class ModelRoom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "price")
    private double price;
    @Column(name = "number")
    private int number;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int Id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "stars")
    private int stars;
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;
    @Column(name = "checkindate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    @Column(name = "checkoutdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Residence> residenceHistory = new ArrayList<>();
    public ModelRoom(){}
    public ModelRoom(int number, double price, int capicity, int stars) {
        this.price = price;
        this.number = number;
        this.status = Status.FREE;
        this.capacity = capicity;
        this.stars = stars;
    }

    public int getId() {
        return Id;
    }

    public void addResidence(Guest guest, LocalDate checkInDate, LocalDate checkOutDate, int maxHistorySize) {
        Residence newResidence = new Residence(guest, checkInDate, checkOutDate);
        newResidence.setRoom(this);
        residenceHistory.add(newResidence);
        if (residenceHistory.size() > maxHistorySize) {
            residenceHistory.remove(0);
        }
    }

    public List<Residence> getResidenceHistory() {
        return residenceHistory;
    }

    public double canculate() {
        if (checkInDate == null || checkOutDate == null) {
            return 0.0;
        }
        return price * ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }


    @Override
    public String toString() {
        String guestName;
        if (guest != null) {
            guestName = guest.getName();
        } else {
            guestName = "None";
        }
        return "Номер: " + number + "\n" +
                "Кол-во звезд: " + stars + "\n" +
                "Вместительность: " + capacity + "\n" +
                "Стоимость: " + price + "\n" +
                "Статус номера: " + status + "\n" +
                "Имя гостя: " + guestName;

    }
}
