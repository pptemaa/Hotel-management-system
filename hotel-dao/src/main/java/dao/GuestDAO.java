package dao;

import Exception.HotelException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import model.Guest;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GuestDAO {
    @PersistenceContext
    private EntityManager em;

    public List<Guest> findAll() throws HotelException {
        try {
            return em.createQuery("SELECT g FROM Guest g", Guest.class).getResultList();
        } catch (Exception e) {
            throw new HotelException("Ошибка при получении списка гостей в БД", e);
        }
    }

    public void save(Guest guest) throws HotelException {
        try {
            em.persist(guest);
        } catch (Exception e) {
            throw new HotelException("Ошибка при сохранении гостя в БД", e);
        }
    }

    public void remove(Guest guest) throws HotelException {
        Guest toRemove = em.find(Guest.class, guest.getId());
        if (toRemove == null) {
            throw new HotelException("Ошибка при удалении гостя из БД");
        }
        try {
            em.remove(toRemove);
        } catch (Exception e) {
            throw new HotelException("Ошибка при удалении гостя из БД", e);
        }
    }
}
