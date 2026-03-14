package model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "guest")
public class Guest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "name")
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int IdGuest;

    public Guest() {
    }

    public Guest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return IdGuest;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}

