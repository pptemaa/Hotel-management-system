package dao;

import Exception.HotelException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.Guest;
import model.Service;
import model.ServiceRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ServiceRecordDAO {
    @PersistenceContext
    private EntityManager em;
    public List<ServiceRecord> findAll() throws HotelException {
        try {
            return em.createQuery("SELECT sr FROM ServiceRecord sr", ServiceRecord.class)
                    .getResultList();
        } catch (Exception e) {
            throw new HotelException("Ошибка при загрузке записей услуг из БД", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void save(ServiceRecord serviceRecord, int guestId, int serviceId) throws HotelException {
        try {
            Guest guest = em.find(Guest.class, guestId);
            Service service = em.find(Service.class, serviceId);
            if (guest == null || service == null) {
                throw new HotelException("Гость или услуга не найдены при сохранении записи");
            }
            serviceRecord.setGuest(guest);
            serviceRecord.setService(service);
            em.persist(serviceRecord);
        } catch (Exception e) {
            throw new HotelException("Ошибка при сохранении записи услуги в БД", e);
        }
    }
}
