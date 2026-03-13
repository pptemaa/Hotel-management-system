package controller;

import Service.Admin;
import dto.InGuestDTO;
import dto.OutGuestDTO;
import dto.RoomCreateDTO;
import dto.RoomDTO;
import model.Residence;
import model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Exception.HotelException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {
    private final Admin admin;

    public RoomRestController(Admin admin) {
        this.admin = admin;
    }
    @GetMapping()
    public List<RoomDTO> getAllRooms(@RequestParam(value = "sort", required = false) String sort) throws HotelException {
        if (sort!=null && !sort.isBlank()){
            return admin.getSortedRoomsDTO(sort);
        }
        return admin.getAllRoomsDto();
    }

    @GetMapping(value = "/free")
    public List<RoomDTO> getAllFreeRooms(@RequestParam(value = "sort", required = false) String sort) throws HotelException {
        if (sort!=null && !sort.isBlank()){
            return admin.getFreeSortedRoomsDTO(sort);
        }
        return admin.getFreeRoomsDto();
    }

    @PostMapping(value = "/addRoom", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> addRoom(@RequestBody RoomCreateDTO newRoom) throws HotelException{
        admin.addRoom(newRoom.getNumber(),newRoom.getPrice(),newRoom.getCapacity(),newRoom.getStars());
        return ResponseEntity.status(HttpStatus.CREATED).body("Комната " + newRoom.getNumber() + " успешно добавлена!");
    }

    @PostMapping(value = "/checkIn", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> checkInGuest(@RequestBody InGuestDTO newGuest) throws HotelException {
        LocalDate checkIn = LocalDate.parse(newGuest.getCheckInDate());
        LocalDate checkOut = LocalDate.parse(newGuest.getCheckOutDate());
        admin.checkIn(newGuest.getNumber(),newGuest.getName(),checkIn,checkOut);
        return ResponseEntity.status(HttpStatus.CREATED).body("Гость " + newGuest.getName() + " заселен в комнату " + newGuest.getNumber());
    }

    @PostMapping(value = "/checkOut", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> checkOutGuest(@RequestBody OutGuestDTO guest) throws HotelException {
        admin.checkOut(guest.getNumber());
        return ResponseEntity.status(HttpStatus.OK).body("Номер " + guest.getNumber() + " освобожден");
    }

    @PutMapping(value = "/{number}/status/{status}",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> updateRoomStatus(@PathVariable("number") int number, @PathVariable("status") String status) throws HotelException{
        Status newStatus = Status.valueOf(status.toUpperCase());
        admin.setRoomStatus(number,newStatus);
        return ResponseEntity.status(HttpStatus.OK).body("Статус комнаты " + number + " успешно изменен на " + newStatus + "!");
    }

    @PutMapping(value = "/{number}/price/{price}",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> updateRoomPrice(@PathVariable("number") int number, @PathVariable("price") double price) throws HotelException{
        admin.roomUpdatePrice(number,price);
        return ResponseEntity.status(HttpStatus.OK).body("Цена комнаты " + number + " успешно изменен на " + price + "!");
    }
    @GetMapping(value = "/freeAtDate")
    public List<RoomDTO> getFreeRoomsAtDate(@RequestParam(value = "date", required = true) String date) throws HotelException {
        return admin.getFreeRoomsAtDateDto(date);
    }
    @GetMapping(value = "/guestsAlpha")
    public List<InGuestDTO> getAllGuestsByAlpha() throws HotelException {
        return admin.getGuestsByAlphaDto();
    }

    @GetMapping(value = "/guestsByCheckout")
    public List<InGuestDTO> getAllGuestsByCheckoutDate() throws HotelException {
        return admin.getGuestsByCheckoutDateDto();
    }

    @GetMapping(value = "/{number}/payment")
    public double getRoomPayment(@PathVariable("number") int number) throws HotelException {
        return admin.getRoomPayment(number);
    }

    @GetMapping(value = "/{number}/history")
    public List<Residence> getRoomHistory(@PathVariable("number") int number) throws HotelException {
        return admin.getLastGuestsForRoom(number);
    }

    @GetMapping(value = "/priceList")
    public List<RoomDTO> getRoomsPriceList() throws HotelException {
        return admin.getRoomsPriceListDto();
    }

    @GetMapping(value = "/count/free")
    public long getFreeRoomsCount() throws HotelException {
        return admin.getFreeRoomsCount();
    }

    @GetMapping(value = "/count/occupied")
    public long getOccupiedRoomsCount() throws HotelException {
        return admin.getOccupiedRoomsCount();
    }
}
