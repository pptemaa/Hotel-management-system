package Service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import dto.ServiceDTO;
import jakarta.annotation.PostConstruct;
import config.Config;
import dao.*;
import dto.RoomDTO;
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
    private final Map<Integer, ModelRoom> roomsById = new HashMap<>();
    private final Map<Integer, ModelRoom> roomsByNumber = new HashMap<>();

    private final Map<String, Guest> guestMap = new HashMap<>();
    private final Map<Integer, Guest> guestsById = new HashMap<>();

    private final Map<String, Service> serviceMap = new HashMap<>();
    private final Map<Integer, Service> servicesById = new HashMap<>();

    private final Map<Guest, List<ServiceRecord>> serviceRecordsMap = new HashMap<>();
    private transient Config config;
    private static Admin instanse;
    private int roomCounter = 1;

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

    @PostConstruct
    public void initFromDb() throws HotelException {
        // гости
        for (Guest guest : guestDAO.findAll()) {
            guestsById.put(guest.getId(), guest);
            guestMap.put(guest.getName(), guest);
        }

        // услуги
        for (Service service : serviceDAO.findAll()) {
            servicesById.put(service.getId(), service);
            serviceMap.put(service.getName(), service);
        }

        // комнаты
        for (ModelRoom room : roomDAO.findAll()) {
            roomsById.put(room.getId(), room);
            roomsByNumber.put(room.getNumber(), room);

        }
        for (Residence residence : residenceDAO.findAll()) {
            if (residence.getRoom() != null) {
                ModelRoom modelRoom = roomsById.get(residence.getRoom().getId());
                if (modelRoom != null) {
                    modelRoom.getResidenceHistory().add(residence);
                }
            }
        }

        // записи услуг гостей
        for (ServiceRecord record : serviceRecordDAO.findAll()) {
            Guest g = record.getGuest();
            if (g != null) {
                serviceRecordsMap.computeIfAbsent(g, k -> new ArrayList<>()).add(record);
            }
        }

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

    public void checkIn(int number, String guestName, LocalDate checkInDate, LocalDate checkOutDate) throws
            HotelException {
        ModelRoom room = null;
        for (ModelRoom r : roomDAO.findAll()) {
            if (r.getNumber() == number) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new HotelException("Номер не найден");
        }
        if (room.getStatus() != Status.FREE) {
            throw new HotelException("номер " + number + " нельзя заселить. Статус " + room.getStatus());
        }
        Guest guest = creatOrFindGuest(guestName);
        room.setStatus(Status.OCCUPIED);
        room.setGuest(guest);
        room.setCheckInDate(checkInDate);
        room.setCheckOutDate(checkOutDate);
        room.addResidence(guest, checkInDate, checkOutDate, config.getResidenceHistoryMax());
    }

    public void checkOut(int number) throws HotelException {
        ModelRoom room = null;
        for (ModelRoom r : roomDAO.findAll()) {
            if (r.getNumber() == number) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new HotelException("Номер " + number + " не найден");
        }
        if (room.getStatus() != Status.OCCUPIED) {
            throw new HotelException("номер " + number + " не заселен");
        }
        room.setStatus(Status.FREE);
        room.setGuest(null);
        room.setCheckInDate(null);
        room.setCheckOutDate(null);
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
        for (ModelRoom r : roomsByNumber.values()) {
            if (r.getNumber() == number) {
                throw new HotelException("Комната с номером " + number + " уже существует.");
            }
        }
        ModelRoom room = new ModelRoom(number, price, capicity, stars);
        try {
            roomsById.put(room.getId(), room);
            roomsByNumber.put(number, room);
            roomDAO.save(room);
        } catch (HotelException e) {
            roomsById.remove(room.getId());
            roomsByNumber.remove(number);
            roomCounter--;
            throw e;
        }
    }

    public void addService(String name, double price) throws HotelException {
        if (price < 0) {
            throw new HotelException("Цена услуги не может быть отрицательной.");
        }
        if (serviceMap.containsKey(name)) {
            throw new HotelException("Услуга \"" + name + "\" уже существует.");
        }
        Service service = new Service(name, price);
        serviceDAO.save(service);
    }

    public void roomUpdatePrice(int number, double newPrice) throws HotelException {
        if (newPrice < 0) {
            throw new HotelException("Новая цена номера не может быть отрицательной.");
        }
        ModelRoom room = null;
        for (ModelRoom r : roomDAO.findAll()) {
            if (r.getNumber() == number) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new HotelException("Комната с номером " + number + " не найдена.");
        }
        room.setPrice(newPrice);
    }

    public void serviceUpdatePrice(String name, double newPrice) throws HotelException {
        if (newPrice < 0) {
            throw new HotelException("Новая цена услуги не может быть отрицательной.");
        }
        Service service = null;
        for (Service s : serviceDAO.findAll()) {
            if (s.getName().equalsIgnoreCase(name)) {
                service = s;
                break;
            }
        }
        if (service == null) {
            throw new HotelException("Услуга \"" + name + "\" не найдена.");
        }
        service.setPrice(newPrice);
    }

    public void setRoomStatus(int number, Status status) throws HotelException {
        ModelRoom room = null;
        for (ModelRoom r : roomDAO.findAll()) {
            if (r.getNumber() == number) {
                room = r;
                break;
            }
        }
        if (room == null) {
            throw new HotelException("Комната с номером " + number + " не найдена.");
        }
        room.setStatus(status);
    }

    public List<ModelRoom> getFreeRoomsAtDate(LocalDate date) throws HotelException {
        if (date.isBefore(LocalDate.now())) {
            throw new HotelException("Дата должна быть в будущем.");
        }

        List<ModelRoom> result = new ArrayList<>();
        for (ModelRoom room : roomsByNumber.values()) {
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

    public List<ModelRoom> printRooms() {
        return roomsByNumber.values().stream().toList();
    }

    public List<Service> printService() {
        return serviceMap.values().stream().toList();
    }

    public List<ModelRoom> printSortedRooms(Comparator<ModelRoom> comparator) {
        return roomsByNumber.values().stream().sorted(comparator).toList();
    }

    public List<ModelRoom> printSortedRoomsByPrice() {
        return printSortedRooms(Comparator.comparingDouble(ModelRoom::getPrice));
    }

    public List<ModelRoom> printSortedRoomsByCapacity() {
        return printSortedRooms(Comparator.comparingDouble(ModelRoom::getCapacity));
    }

    public List<ModelRoom> printSortedRoomsByStars() {
        return printSortedRooms(Comparator.comparingDouble(ModelRoom::getStars));
    }

    public List<ModelRoom> printFreeRooms(Comparator<ModelRoom> comparator) {
        return roomsByNumber.values().stream().filter(room -> room.getStatus() == Status.FREE).sorted(comparator).toList();
    }

    public List<ModelRoom> printSortedFreeByPrice() {
        return printFreeRooms(Comparator.comparingDouble(ModelRoom::getPrice));
    }

    public List<ModelRoom> printSortedFreeByCapacity() {
        return printFreeRooms(Comparator.comparingDouble(ModelRoom::getCapacity));
    }

    public List<ModelRoom> printSortedFreeByStars() {
        return printFreeRooms(Comparator.comparingDouble(ModelRoom::getStars));
    }

    private List<ModelRoom> printGuest(Comparator<ModelRoom> comparator) {
        return roomsByNumber.values().stream().filter(room -> Status.OCCUPIED == room.getStatus()).sorted(comparator).toList();

    }

    public List<ModelRoom> printGuestByAlpha() {
        return printGuest(Comparator.comparing(room -> room.getGuest().getName()));
    }

    public List<ModelRoom> printGuestByTime() {
        return printGuest(Comparator.comparing(room -> room.getCheckOutDate()));
    }

    public void printFreeCntRoom() {
        long cnt = roomsByNumber.values().stream().filter(room -> room.getStatus() == Status.FREE).count();
        System.out.println("Общее количество свободных номеров " + cnt);
    }

    public void printOcupiedCntRoom() {
        long cnt = roomsByNumber.values().stream().filter(room -> room.getStatus() == Status.OCCUPIED).count();
        System.out.println("Общее количество жильцов " + cnt);
    }

    public double printSumForRoom(int number) throws HotelException {
        if (!roomsByNumber.containsKey(number)) {
            throw new HotelException("Комната с номером " + number + " не найдена.");
        }
        ModelRoom room = roomsByNumber.get(number);
        return room.canculate();
    }

    public List<Residence> printThreeGuest(int number) throws HotelException {
        if (!roomsByNumber.containsKey(number)) {
            throw new HotelException("Комната с номером " + number + " не найдена.");
        }
        ModelRoom room = roomsByNumber.get(number);
        List<Residence> history = room.getResidenceHistory();
        if (history.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return history;
    }

    private List<ServiceRecord> printGuestServices(Comparator<ServiceRecord> comparator, String guestName) {
        Guest guest = guestMap.get(guestName);
        if (guest == null) {
            return null;
        }
        List<ServiceRecord> records = serviceRecordsMap.get(guest);
        if (records == null || records.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        records = records.stream().sorted(comparator).toList();
        return records;
    }

    public List<ServiceRecord> printGuestServicesByPrice(String guestName) {
        return printGuestServices(Comparator.comparingDouble(serviceRecord -> serviceMap.get(serviceRecord.getName()).getPrice()), guestName);
    }

    public List<ServiceRecord> printGuestServicesByDate(String guestName) {
        return printGuestServices(Comparator.comparing(ServiceRecord::getDate), guestName);
    }

    public List<ModelRoom> getRoomsSortedByPrice() {
        return roomsByNumber.values().stream()
                .sorted(Comparator.comparingDouble(ModelRoom::getPrice))
                .toList();
    }

    public List<Service> getServicesSortedByPrice() {
        return serviceMap.values().stream()
                .sorted(Comparator.comparingDouble(Service::getPrice))
                .toList();
    }


    public ModelRoom printDetailsRoom(int number) throws HotelException {
        ModelRoom room = roomsByNumber.get(number);
        if (room == null) {
            throw new HotelException("Номер " + number + " не найден.");
        }
        return room;
    }

    public Service printDetailService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    private RoomDTO convertToDTO(ModelRoom room) {
        return new RoomDTO(room.getId(), room.getNumber(), room.getPrice(), room.getStatus().toString(),room.getStars(),room.getCapacity());
    }

    private ServiceDTO convertToServiceDTO(Service service) {
        return new ServiceDTO(service.getId(), service.getName(), service.getPrice());
    }


    public List<RoomDTO> getAllRoomsDto() throws HotelException {
        return roomDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<RoomDTO> getFreeRoomsDto() throws HotelException {
        return roomDAO.findAll().stream().filter(roomDAO->roomDAO.getStatus()==Status.FREE).map(this::convertToDTO).collect(Collectors.toList());
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
        List<ModelRoom> rooms = roomDAO.findAll().stream().filter(room->room.getStatus()==Status.FREE).collect(Collectors.toList());
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





}
