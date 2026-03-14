package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.ModelRoom;
import model.Residence;
import Exception.HotelException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ResidenceDAO {
    @PersistenceContext
    private EntityManager em;

    public void save(ModelRoom modelRoom) throws HotelException {
        List<Residence> residenceHistory = modelRoom.getResidenceHistory();
        try {
            for (Residence residence : residenceHistory) {
                em.persist(residence);
            }
        } catch (Exception e) {
            throw new HotelException("Ошибка при сохранении истории проживаний в БД", e);
        }
    }

    public List<Residence> findAll() throws HotelException {
        try {
            return em.createQuery("SELECT r FROM Residence r", Residence.class).getResultList();
        } catch (Exception e) {
            throw new HotelException("Ошибка при получении истории проживаний из БД");
        }
    }
}



