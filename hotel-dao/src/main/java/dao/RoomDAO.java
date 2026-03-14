package dao;

import Exception.HotelException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Guest;
import model.ModelRoom;
import model.Status;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoomDAO {
    @PersistenceContext
    private EntityManager em;

    public List<ModelRoom> findAll() throws HotelException {
        List<ModelRoom> result = new ArrayList<>();
        try {
            return em.createQuery("SELECT r FROM ModelRoom r", ModelRoom.class).getResultList();
        } catch (Exception e) {
            throw new HotelException("Ошибка при загрузке комнат из БД", e);
        }
    }

    public void save(ModelRoom modelRoom) throws HotelException {
        try {
            em.persist(modelRoom);
        } catch (Exception e) {
            throw new HotelException("Ошибка при сохранении номера в БД", e);
        }
    }


    public void setStatus(ModelRoom modelRoom) throws HotelException {
        try {
            ModelRoom room = em.find(ModelRoom.class, modelRoom.getId());
            if (room == null) {
                throw new HotelException("Комната с id " + modelRoom.getId() + " не найдена");
            }
            room.setStatus(modelRoom.getStatus());
            room.setGuest(modelRoom.getGuest());
            room.setCheckInDate(modelRoom.getCheckInDate());
            room.setCheckOutDate(modelRoom.getCheckOutDate());
        } catch (Exception e) {
            throw new HotelException("Ошибка при изменении статуса комнаты в БД", e);
        }
    }

    public void setPrice(ModelRoom modelRoom) throws HotelException {
        try {
            ModelRoom room = em.find(ModelRoom.class, modelRoom.getId());
            if (room == null) {
                throw new HotelException("Комната с id " + modelRoom.getId() + " не найдена");
            }
            room.setPrice(modelRoom.getPrice());
        } catch (Exception e) {
            throw new HotelException("Ошибка при изменении цены комнаты в БД", e);
        }
    }
}
