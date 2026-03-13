package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
@Entity
@Table(name = "residence")
public class Residence implements Serializable {
    private static final long serialVersionUID = 1L;
    @ManyToOne
    @JoinColumn(name = "guest_id",referencedColumnName = "id")
    private Guest guest;
    @Column(name = "checkInDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    @Column(name = "checkOutDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private int id;
    @ManyToOne()
    @JoinColumn(name = "room_id",referencedColumnName = "id")
    @JsonIgnore
    private ModelRoom room;
    public Residence(){}
    public Residence(Guest guest, LocalDate checkInDate, LocalDate checkOutDate) {
        this.guest = guest;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public ModelRoom getRoom() {
        return room;
    }

    public void setRoom(ModelRoom room) {
        this.room = room;
    }


    public int getId() {
        return id;
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

}
