package controller;

import Service.Admin;
import dto.OrderServiceDTO;
import dto.ServiceCreateDTO;
import dto.ServiceDTO;
import dto.GuestServiceDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import Exception.HotelException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceRestController {
    private final Admin admin;

    public ServiceRestController(Admin admin) {
        this.admin = admin;
    }
    @PostMapping(produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> addService(@RequestBody ServiceCreateDTO newService) throws HotelException {
        admin.addService(newService.getName(), newService.getPrice());
        return ResponseEntity.status(HttpStatus.CREATED).body("Сервис " + newService.getName() + " Успешно добавлен");
    }
    @PostMapping(value = "/order", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> orderService(@RequestBody OrderServiceDTO service) throws HotelException{
        LocalDate date = LocalDate.parse(service.date());
        admin.orderService(service.guestName(),service.serviceName(),date);
        return ResponseEntity.status(HttpStatus.CREATED).body("Гость  " + service.guestName() + " заказал услугу " + service.serviceName() + " на число " + service.date());
    }

    @PutMapping(value = "/{name}/price/{price}",produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> updateServicePrice(@PathVariable("name") String name, @PathVariable("price") double price) throws HotelException{
        admin.serviceUpdatePrice(name,price);
        return ResponseEntity.status(HttpStatus.OK).body("Цена услуги " + name + " успешно изменен на " + price + "!");
    }

    @GetMapping()
    public List<ServiceDTO> getAllServices() throws HotelException {
        return admin.getAllServiceDto();
    }

    @GetMapping(value = "/guestServices/price")
    public List<GuestServiceDTO> getGuestServicesByPrice(@RequestParam("guestName") String guestName) throws HotelException {
        return admin.getGuestServicesByPriceDto(guestName);
    }

    @GetMapping(value = "/guestServices/date")
    public List<GuestServiceDTO> getGuestServicesByDate(@RequestParam("guestName") String guestName) throws HotelException {
        return admin.getGuestServicesByDateDto(guestName);
    }

    @GetMapping(value = "/priceList")
    public List<ServiceDTO> getServicesPriceList() throws HotelException {
        return admin.getServicesPriceListDto();
    }
}
