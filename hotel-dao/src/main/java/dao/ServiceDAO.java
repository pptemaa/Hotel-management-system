package dao;

import Exception.HotelException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Service;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceDAO {
    @PersistenceContext
    private EntityManager em;

    public List<Service> findAll() throws HotelException {
        try {
            return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
        } catch (Exception e) {
            throw new HotelException("Ошибка при загрузке услуг из БД", e);
        }
    }

    public void save(Service service) throws HotelException {
        try {
            em.persist(service);
        } catch (Exception e) {
            throw new HotelException("Ошибка при сохранении сервиса в БД", e);
        }
    }

    public void remove(Service service) throws HotelException {
        try {
            Service toRemove = em.find(Service.class, service.getId());
            if (toRemove == null) {
                throw new HotelException("Сервис с id " + service.getId() + " не найден");
            }
            em.remove(toRemove);
        } catch (Exception e) {
            throw new HotelException("Ошибка при удалении сервиса из БД", e);
        }
    }

    public void setPrice(Service service) throws HotelException {
        try {
            Service managed = em.find(Service.class, service.getId());
            if (managed == null) {
                throw new HotelException("Сервис с id " + service.getId() + " не найден");
            }
            managed.setPrice(service.getPrice());
        } catch (Exception e) {
            throw new HotelException("Ошибка при изменении цены сервиса в БД", e);
        }
    }

}
