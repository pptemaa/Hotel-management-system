package Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import dto.InGuestDTO;
import dto.ServiceDTO;
import jakarta.annotation.PostConstruct;
import config.Config;
import dao.*;
import dto.RoomDTO;
import dto.ServiceDTO;
import dto.GuestServiceDTO;
import jakarta.transaction.Transactional;
import model.ModelRoom;
import model.Guest;
import model.Service;
import model.ServiceRecord;
import model.Residence;
import model.Status;
import Exception.HotelException;

@org.springframework.stereotype.Service
@Transactional
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Config config;
    private final RoomDAO roomDAO;
    private final ServiceDAO serviceDAO;
    private final GuestDAO guestDAO;
    private final ResidenceDAO residenceDAO;
    private final ServiceRecordDAO serviceRecordDAO;

    public Admin(Config config, RoomDAO roomDAO, ServiceDAO serviceDAO, GuestDAO guestDAO, ResidenceDAO residenceDAO, ServiceRecordDAO serviceRecordDAO) {
        this.config = config;
        this.roomDAO = roomDAO;
        this.serviceDAO = serviceDAO;
        this.guestDAO = guestDAO;
        this.residenceDAO = residenceDAO;
        this.serviceRecordDAO = serviceRecordDAO;
    }

    private Guest creatOrFindGuest(String name) throws HotelException {
        for (Guest g : guestDAO.findAll()) {
            if (g.getName().equalsIgnoreCase(name)) {
                return g;
            }
        }
        Guest guest = new Guest(name);
        guestDAO.save(guest);
        return guest;
    }

    public void orderService(String guestName, String serviceName, LocalDate localDate) throws HotelException {
        Service service = null;
        for (Service s : serviceDAO.findAll()) {
            if (s.getName().equalsIgnoreCase(serviceName)) {
                service = s;
                break;
            }
        }
        if (service == null) {
            throw new HotelException("услуга" + serviceName + "не найдена");
        }
        Guest guest = creatOrFindGuest(guestName);
        ServiceRecord serviceRecord = new ServiceRecord(serviceName, localDate);
        serviceRecordDAO.save(serviceRecord, guest.getId(), service.getId());
    }

    public void checkIn(int number, String guestName, LocalDate checkInDate, LocalDate checkOutDate) throws HotelException {
        ModelRoom room = getRoomByNumber(number); // БЕРЕМ ИЗ БАЗЫ!

        if (room.getStatus() != Status.FREE) {
            throw new HotelException("Номер " + number + " нельзя заселить. Статус " + room.getStatus());
        }

        Guest guest = creatOrFindGuest(guestName);
        room.setStatus(Status.OCCUPIED);
        room.setGuest(guest);
        room.setCheckInDate(checkInDate);
        room.setCheckOutDate(checkOutDate);
        room.addResidence(guest, checkInDate, checkOutDate, config.getResidenceHistoryMax());
        roomDAO.setStatus(room);
        residenceDAO.save(room);
    }

    public void checkOut(int number) throws HotelException {
        ModelRoom room = getRoomByNumber(number);

        if (room.getStatus() != Status.OCCUPIED) {
            throw new HotelException("Номер " + number + " не заселен");
        }
        room.setStatus(Status.FREE);
        room.setGuest(null);
        room.setCheckInDate(null);
        room.setCheckOutDate(null);
        roomDAO.setStatus(room);
    }

    public void addRoom(int number, double price, int capicity, int stars) throws HotelException {
        if (number <= 0) {
            throw new HotelException("Номер комнаты должен быть положительным.");
        }
        if (price < 0) {
            throw new HotelException("Цена номера не может быть отрицательной.");
        }
        if (capicity <= 0) {
            throw new HotelException("Вместимость номера должна быть больше 0.");
        }
        if (stars < 0) {
            throw new HotelException("Количество звёзд не может быть отрицательным.");
        }
        boolean exists = roomDAO.findAll().stream().anyMatch(r -> r.getNumber() == number);
        if (exists) {
            throw new HotelException("Комната с номером " + number + " уже существует.");
        }
        ModelRoom room = new ModelRoom(number, price, capicity, stars);
        roomDAO.save(room);

    }

    public void addService(String name, double price) throws HotelException {
        if (price < 0) {
            throw new HotelException("Цена услуги не может быть отрицательной.");
        }
        boolean exists = serviceDAO.findAll().stream()
                .anyMatch(s -> s.getName().equalsIgnoreCase(name));

        if (exists) {
            throw new HotelException("Услуга \"" + name + "\" уже существует.");
        }

        Service service = new Service(name, price);
        serviceDAO.save(service); // Сохраняем в БД!
    }

    public void roomUpdatePrice(int number, double newPrice) throws HotelException {
        if (newPrice < 0) throw new HotelException("Цена не может быть отрицательной.");
        ModelRoom room = getRoomByNumber(number);
        room.setPrice(newPrice);
        roomDAO.setPrice(room); // Если упадет, @Transactional откатит изменение в БД
    }

    public void serviceUpdatePrice(String name, double newPrice) throws HotelException {
        if (newPrice < 0) {
            throw new HotelException("Новая цена услуги не может быть отрицательной.");
        }

        Service service = serviceDAO.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new HotelException("Услуга \"" + name + "\" не найдена."));

        service.setPrice(newPrice);
        serviceDAO.setPrice(service);
    }

    public void setRoomStatus(int number, Status status) throws HotelException {
        ModelRoom room = getRoomByNumber(number);
        room.setStatus(status);
        roomDAO.setStatus(room);
    }

    public List<ModelRoom> getFreeRoomsAtDate(LocalDate date) throws HotelException {
        if (date.isBefore(LocalDate.now())) {
            throw new HotelException("Дата должна быть в будущем.");
        }
        List<ModelRoom> result = new ArrayList<>();
        for (ModelRoom room : roomDAO.findAll()) {
            boolean occupiedOnDate = false;
            for (Residence residence : room.getResidenceHistory()) {
                LocalDate in = residence.getCheckInDate();
                LocalDate out = residence.getCheckOutDate();
                if (in != null && out != null) {
                    boolean startsBeforeOrOn = !date.isBefore(in);
                    boolean endsAfterOrOn = !date.isAfter(out);
                    if (startsBeforeOrOn && endsAfterOrOn) {
                        occupiedOnDate = true;
                        break;
                    }
                }
            }
            if (!occupiedOnDate) {
                result.add(room);
            }
        }
        return result;
    }


    private List<ModelRoom> printGuest(Comparator<ModelRoom> comparator) throws HotelException {
        return roomDAO.findAll().stream().filter(room -> Status.OCCUPIED == room.getStatus() && room.getGuest()!=null).sorted(comparator).toList();

    }

    public List<ModelRoom> printGuestByAlpha() throws HotelException {
        return printGuest(Comparator.comparing(room -> room.getGuest().getName()));
    }

    public List<ModelRoom> printGuestByTime() throws HotelException {
        return printGuest(Comparator.comparing(room -> room.getCheckOutDate()));
    }


    public List<ModelRoom> getRoomsSortedByPrice() throws HotelException {
        return roomDAO.findAll().stream()
                .sorted(Comparator.comparingDouble(ModelRoom::getPrice))
                .toList();
    }

    public List<Service> getServicesSortedByPrice() throws HotelException {
        return serviceDAO.findAll().stream()
                .sorted(Comparator.comparingDouble(Service::getPrice))
                .toList();
    }

    private RoomDTO convertToDTO(ModelRoom room) {
        return new RoomDTO(room.getId(), room.getNumber(), room.getPrice(), room.getStatus().toString(), room.getStars(), room.getCapacity());
    }

    private ServiceDTO convertToServiceDTO(Service service) {
        return new ServiceDTO(service.getId(), service.getName(), service.getPrice());
    }


    public List<RoomDTO> getAllRoomsDto() throws HotelException {
        return roomDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RoomDTO> getFreeRoomsDto() throws HotelException {
        return roomDAO.findAll().stream().filter(roomDAO -> roomDAO.getStatus() == Status.FREE).map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ServiceDTO> getAllServiceDto() throws HotelException {
        return serviceDAO.findAll().stream().map(this::convertToServiceDTO).collect(Collectors.toList());
    }

    public List<RoomDTO> getSortedRoomsDTO(String sortBy) throws HotelException {
        List<ModelRoom> rooms = roomDAO.findAll();
        switch (sortBy.trim().toLowerCase()) {
            case "stars":
                rooms.sort(Comparator.comparingDouble(ModelRoom::getStars));
                break;
            case "capacity":
                rooms.sort(Comparator.comparingInt(ModelRoom::getCapacity));
                break;
            case "price":
                rooms.sort(Comparator.comparingDouble(ModelRoom::getPrice));
                break;
            default:
                throw new HotelException("Неизвестный параметр сортировки: " + sortBy + ". Доступные параметры: price, capacity, stars.");
        }
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RoomDTO> getFreeSortedRoomsDTO(String sortBy) throws HotelException {
        List<ModelRoom> rooms = roomDAO.findAll().stream().filter(room -> room.getStatus() == Status.FREE).collect(Collectors.toList());
        switch (sortBy.trim().toLowerCase()) {
            case "stars":
                rooms.sort(Comparator.comparingDouble(ModelRoom::getStars));
                break;
            case "capacity":
                rooms.sort(Comparator.comparingInt(ModelRoom::getCapacity));
                break;
            case "price":
                rooms.sort(Comparator.comparingDouble(ModelRoom::getPrice));
                break;
            default:
                throw new HotelException("Неизвестный параметр сортировки: " + sortBy + ". Доступные параметры: price, capacity, stars.");
        }
        return rooms.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RoomDTO> getFreeRoomsAtDateDto(String dateString) throws HotelException {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString);
        } catch (Exception e) {
            throw new HotelException("Неверный формат даты. Используйте формат ГГГГ-ММ-ДД (например, 2026-03-15).");
        }
        return getFreeRoomsAtDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<dto.InGuestDTO> getGuestsByAlphaDto() throws HotelException {
        return printGuestByAlpha().stream()
                .map(room -> new dto.InGuestDTO(
                        room.getNumber(),
                        // Если гость есть - берем имя, если вдруг нет - пишем "Неизвестный"
                        room.getGuest() != null ? room.getGuest().getName() : "Неизвестный гость",
                        room.getCheckInDate() != null ? room.getCheckInDate().toString() : "",
                        room.getCheckOutDate() != null ? room.getCheckOutDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public List<dto.InGuestDTO> getGuestsByCheckoutDateDto() throws HotelException {
        return printGuestByTime().stream()
                .map(room -> new dto.InGuestDTO(
                        room.getNumber(),
                        room.getGuest() != null ? room.getGuest().getName() : "Неизвестный гость",
                        room.getCheckInDate() != null ? room.getCheckInDate().toString() : "",
                        room.getCheckOutDate() != null ? room.getCheckOutDate().toString() : ""
                ))
                .collect(Collectors.toList());
    }

    public List<GuestServiceDTO> getGuestServicesByPriceDto(String guestName) throws HotelException {
        // Берем готовый список из метода выше и просто сортируем по цене
        List<GuestServiceDTO> dtos = new ArrayList<>(getGuestServicesByDateDto(guestName));
        dtos.sort(Comparator.comparingDouble(GuestServiceDTO::getPrice));
        return dtos;
    }

    public List<GuestServiceDTO> getGuestServicesByDateDto(String guestName) throws HotelException {
        // 1. Ищем записи услуг для конкретного гостя в базе
        List<ServiceRecord> records = serviceRecordDAO.findAll().stream()
                .filter(r -> r.getGuest() != null && r.getGuest().getName().equalsIgnoreCase(guestName))
                .sorted(Comparator.comparing(ServiceRecord::getDate))
                .toList();

        if (records.isEmpty()) {
            throw new HotelException("Гость с именем " + guestName + " не найден или не заказывал услуг.");
        }

        List<Service> allServices = serviceDAO.findAll();

        return records.stream().map(record -> {
            double price = allServices.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(record.getName()))
                    .map(Service::getPrice)
                    .findFirst().orElse(0.0);

            return new GuestServiceDTO(record.getName(), record.getDate() != null ? record.getDate().toString() : "", price);
        }).collect(Collectors.toList());
    }

    public double getRoomPayment(int number) throws HotelException {
        ModelRoom room = getRoomByNumber(number);
        return room.canculate();
    }

    public List<Residence> getLastGuestsForRoom(int number) throws HotelException {
        ModelRoom room = getRoomByNumber(number);
        List<Residence> history = room.getResidenceHistory();
        return history.isEmpty() ? Collections.emptyList() : history;
    }

    public List<RoomDTO> getRoomsPriceListDto() throws HotelException {
        return getRoomsSortedByPrice().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ServiceDTO> getServicesPriceListDto() throws HotelException {
        return getServicesSortedByPrice().stream()
                .map(this::convertToServiceDTO)
                .collect(Collectors.toList());
    }

    public long getFreeRoomsCount() throws HotelException {
        return roomDAO.findAll().stream()
                .filter(room -> room.getStatus() == Status.FREE)
                .count();
    }

    public long getOccupiedRoomsCount() throws HotelException {
        return roomDAO.findAll().stream()
                .filter(room -> room.getStatus() == Status.OCCUPIED)
                .count();
    }

    private ModelRoom getRoomByNumber(int number) throws HotelException {
        return roomDAO.findAll().stream()
                .filter(r -> r.getNumber() == number)
                .findFirst()
                .orElseThrow(() -> new HotelException("Комната с номером " + number + " не найдена."));
    }

}
