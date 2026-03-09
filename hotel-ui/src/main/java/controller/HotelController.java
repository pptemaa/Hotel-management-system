package controller;

import Exception.HotelException;
import Service.Admin;
import config.Config;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import view.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
@Component
public class HotelController {
    private static final Logger log = LoggerFactory.getLogger(HotelController.class);
    @Autowired
    private  Admin admin;
    @Autowired
    private  IRoomView roomView;
    private  final Scanner scanner;
    @Autowired
    private  IServiceView serviceView;
    @Autowired
    private  Config config;
    public HotelController() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextInt()) {
                log.error("Ошибка обработки ввода: введено не числовое значение");
                System.out.println("Ошибка: введите числовое значение!");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очистка буфера

            switch (choice) {
                case 1 -> addRoom();
                case 2 -> addService();
                case 3 -> checkInGuest();
                case 4 -> checkOutGuest();
                case 5 -> chekOrderService();
                case 6 -> setRoomStatus();
                case 7 -> updatePriceRoom();
                case 8 -> updatePriceService();

                case 9 -> showAllRooms();
                case 10 -> printAllServices();
                case 11 -> printSortedRoomsByPrice();
                case 12 -> printSortedRoomsByCapacity();
                case 13 -> printSortedRoomsByStars();

                case 14 -> printSortedFreeRoomsByPrice();
                case 15 -> printSortedFreeRoomsByCapacity();
                case 16 -> printSortedFreeRoomsByStars();
                case 17 -> printRoomAvailableByDate();

                case 18 -> printGuestsByAlphabet();
                case 19 -> printGuestsByCheckOutDate();
                case 20 -> printGuestServicesByPrice();
                case 21 -> printGuestServicesByDate();

                case 22 -> printPaymentForRoom();
                case 23 -> printThreeLastGuests();
                case 24 -> printPricesForRoom();
                case 25 -> showRoomDetails();
                case 26 -> printPricesForService();

                case 27 -> printFreeCntRoom();
                case 28 -> printOcupiedCntRoom();

                case 0 -> {
                    log.info("Начало обработки команды: Выход");
                    System.out.println("Выход из системы. До свидания!");
                    running = false;
                    log.info("Команда выполнена успешно: Выход");
                }
                default -> {
                    log.error("Ошибка обработки команды: неверный пункт меню {}", choice);
                    System.out.println("Ошибка: пункта с номером " + choice + " не существует.");
                }
            }
        }
    }

    private void printMenu() {
        System.out.println("\n========= УПРАВЛЕНИЕ ОТЕЛЕМ =========");
        System.out.println("1.  Добавить номер");
        System.out.println("2.  Добавить услугу");
        System.out.println("3.  Заселить гостя");
        System.out.println("4.  Выселить гостя");
        System.out.println("5.  Заказать услугу гостю");
        System.out.println("6.  Изменить статус номера");
        System.out.println("7.  Обновить цену номера");
        System.out.println("8.  Обновить цену услуги");
        System.out.println("-------------------------------------");
        System.out.println("9.  Просмотреть ВСЕ номера");
        System.out.println("10. Просмотреть ВСЕ услуги");
        System.out.println("11. Номера (сорт. по ЦЕНЕ)");
        System.out.println("12. Номера (сорт. по ВМЕСТИМОСТИ)");
        System.out.println("13. Номера (сорт. по ЗВЕЗДАМ)");
        System.out.println("14. Свободные номера (сорт. по ЦЕНЕ)");
        System.out.println("15. Свободные номера (сорт. по ВМЕСТИМОСТИ)");
        System.out.println("16. Свободные номера (сорт. по ЗВЕЗДАМ)");
        System.out.println("17. Свободные номера к определенной дате");
        System.out.println("-------------------------------------");
        System.out.println("18. Просмотреть постояльцев (по алфавиту)");
        System.out.println("19. Постояльцы (по дате выселения)");
        System.out.println("20. Просмотреть услуги гостя (по цене)");
        System.out.println("21. Просмотреть услуги гостя (по дате)");
        System.out.println("22. Рассчитать оплату за номер");
        System.out.println("23. История: 3 последних гостя номера");
        System.out.println("24. Прайс-лист на номера");
        System.out.println("25. Детальная информация о номере");
        System.out.println("26. Прайс-лист на сервисы");
        System.out.println("-------------------------------------");
        System.out.println("27. Количество свободных номеров");
        System.out.println("28. Количество занятых номеров");
        System.out.println("-------------------------------------");
        System.out.println("0.  Выход");
        System.out.print("\nВыберите пункт: ");
    }
    
    private void showRoomDetails() {
        log.info("Начало обработки команды: Детальная информация о номере");
        try {
            System.out.println("Введите номер комнаты для просмотра деталей");
            int number = scanner.nextInt();
            scanner.nextLine();
            ModelRoom room = admin.printDetailsRoom(number);
            roomView.displayDetailsOfRoom(room);
            log.info("Команда выполнена успешно: Детальная информация о номере");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Детальная информация о номере", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Детальная информация о номере", e);
            System.out.println(e.getMessage());
        }
    }

    private void showAllRooms() {
        log.info("Начало обработки команды: Просмотреть все номера");
        System.out.println("Все номера в отеле: ");
        List<ModelRoom> listModelRoom = admin.printRooms();
        if (listModelRoom.isEmpty()) {
            System.out.println("Нету номеров");
        } else {
            roomView.dispalyAllRooms(listModelRoom);
        }
        log.info("Команда выполнена успешно: Просмотреть все номера");
    }

    private void addRoom() {
        log.info("Начало обработки команды: Добавить номер");
        try {
            System.out.println("Введите данные номера:");
            System.out.println("Введите номер");
            int number = scanner.nextInt();
            System.out.println("Введите цену номера ");
            int price = scanner.nextInt();
            System.out.println("Введите вместимость номера ");
            int capacity = scanner.nextInt();
            System.out.println("Введите звезды номера ");
            int stars = scanner.nextInt();
            scanner.nextLine();
            admin.addRoom(number, price, capacity, stars);
            System.out.println("Комната с номером " + number + " добавлена");
            log.info("Команда выполнена успешно: Добавить номер");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Добавить номер", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Добавить номер", e);
            System.out.println(e.getMessage());
        }
    }

    private void addService() {
        log.info("Начало обработки команды: Добавить услугу");
        try {
            System.out.println("Введите название услуги");
            String name = scanner.nextLine();
            System.out.println("Введите цену услуги ");
            int price = scanner.nextInt();
            scanner.nextLine();
            admin.addService(name, price);
            System.out.println("Услуга " + name + " добавлена");
            log.info("Команда выполнена успешно: Добавить услугу");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Добавить услугу", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Добавить услугу", e);
            System.out.println(e.getMessage());
        }
    }

    private void checkInGuest() {
        log.info("Начало обработки команды: Заселить гостя");
        try {
            System.out.println("Чтобы заселить гостя введите его данные ");
            System.out.println("Введите номер комнаты ");
            int number = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введите имя гостя ");
            String guestName = scanner.nextLine();
            System.out.println("Введите дату заселения (гггг-мм-дд): ");
            LocalDate checkInDate = LocalDate.parse(scanner.nextLine());
            System.out.println("Введите дату выселения (гггг-мм-дд): ");
            LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());
            if (checkOutDate.isBefore(checkInDate)) {
                System.out.println("Ошибка: Дата выезда не может быть раньше заезда!");
                log.error("Ошибка обработки команды: Заселить гостя - дата выезда раньше заезда");
                return;
            }
            admin.checkIn(number, guestName, checkInDate, checkOutDate);
            System.out.println("Гость " + guestName + " заселен в номер " + number + " c " + checkInDate + " по " + checkOutDate);
            log.info("Команда выполнена успешно: Заселить гостя");
        } catch (DateTimeParseException e) {
            log.error("Ошибка обработки команды: Заселить гостя", e);
            System.out.println("Ошибка: Неверный формат даты! Используйте ГГГГ-ММ-ДД.");
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Заселить гостя", e);
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Заселить гостя", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        }
    }

    private void checkOutGuest() {
        log.info("Начало обработки команды: Выселить гостя");
        try {
            System.out.println("Чтобы выcелить гостя введите номер его комнаты ");
            int number = scanner.nextInt();
            scanner.nextLine();
            admin.checkOut(number);
            System.out.println("номер " + number + " освобожден");
            log.info("Команда выполнена успешно: Выселить гостя");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Выселить гостя", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Выселить гостя", e);
            System.out.println(e.getMessage());
        }
    }

    private void chekOrderService() {
        log.info("Начало обработки команды: Заказать услугу гостю");
        try {
            System.out.println("Введите имя гостя");
            String name = scanner.nextLine();
            System.out.println("Введите название услуги");
            String serviceName = scanner.nextLine();
            System.out.println("Введите дату услуги (гггг-мм-дд): ");
            LocalDate date = LocalDate.parse(scanner.nextLine());
            admin.orderService(name, serviceName, date);
            Service service = admin.printDetailService(serviceName);
            System.out.println("Гость " + name + " заказал услугу " + serviceName + " на " + date + " по цене " + service.getPrice());
            log.info("Команда выполнена успешно: Заказать услугу гостю");
        } catch (DateTimeParseException e) {
            log.error("Ошибка обработки команды: Заказать услугу гостю", e);
            System.out.println("Ошибка: Неверный формат даты! Используйте ГГГГ-ММ-ДД.");
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Заказать услугу гостю", e);
            System.out.println(e.getMessage());
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Заказать услугу гостю", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        }
    }

    private void setRoomStatus() {
        log.info("Начало обработки команды: Изменить статус номера");
        if (!config.isRoomStatusChangeEnabled()) {
            System.out.println("Изменение статуса номера отключено в настройках.");
            log.error("Ошибка обработки команды: Изменить статус номера - отключено в настройках");
            return;
        }
        try {
            System.out.println("Введите номер комнаты ");
            int number = scanner.nextInt();
            scanner.nextLine();
            Status status = null;
            while (true) {
                System.out.println("Введите новый статус:(FREE,OCCUPIED,REPAIR)");
                String inputStatus = scanner.nextLine();
                try {
                    status = Status.valueOf(inputStatus);
                    admin.setRoomStatus(number, status);
                    System.out.println("Статус комнаты " + number + " изменен на " + status);
                    log.info("Команда выполнена успешно: Изменить статус номера");
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка: тип '" + inputStatus + "' не найден!");
                    System.out.println("Доступные типы: " + Arrays.toString(Status.values()));
                }
            }
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Изменить статус номера", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Изменить статус номера", e);
            System.out.println(e.getMessage());
        }
    }
    private void updatePriceRoom() {
        log.info("Начало обработки команды: Обновить цену номера");
        try {
            System.out.println("Введите номер комнаты ");
            int number = scanner.nextInt();
            System.out.println("Введите новую цену");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            admin.roomUpdatePrice(number, newPrice);
            System.out.println("Стоимость номера " + number + " изменена на: " + newPrice);
            log.info("Команда выполнена успешно: Обновить цену номера");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Обновить цену номера", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Обновить цену номера", e);
            System.out.println(e.getMessage());
        }
    }

    private void updatePriceService() {
        log.info("Начало обработки команды: Обновить цену услуги");
        try {
            System.out.println("Введите название услуги ");
            String nameService = scanner.nextLine();
            System.out.println("Введите новую цену");
            double newPrice = scanner.nextDouble();
            scanner.nextLine();
            admin.serviceUpdatePrice(nameService, newPrice);
            System.out.println("Стоимость услуги " + nameService + " изменена на: " + newPrice);
            log.info("Команда выполнена успешно: Обновить цену услуги");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Обновить цену услуги", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Обновить цену услуги", e);
            System.out.println(e.getMessage());
        }
    }

    private void printAllServices() {
        log.info("Начало обработки команды: Просмотреть все услуги");
        List<Service> serviceList = admin.printService();
        serviceView.displayAllService(serviceList);
        log.info("Команда выполнена успешно: Просмотреть все услуги");
    }
    private void printSortedRoomsByPrice() {
        log.info("Начало обработки команды: Номера по цене");
        List<ModelRoom> list = admin.printSortedRoomsByPrice();
        roomView.displaySortedRoomsByPrice(list);
        log.info("Команда выполнена успешно: Номера по цене");
    }
    private void printSortedRoomsByCapacity() {
        log.info("Начало обработки команды: Номера по вместимости");
        List<ModelRoom> list = admin.printSortedRoomsByCapacity();
        roomView.displaySortedRoomsByCapacity(list);
        log.info("Команда выполнена успешно: Номера по вместимости");
    }
    private void printSortedRoomsByStars() {
        log.info("Начало обработки команды: Номера по звездам");
        List<ModelRoom> list = admin.printSortedRoomsByStars();
        roomView.displaySortedRoomsByStars(list);
        log.info("Команда выполнена успешно: Номера по звездам");
    }

    private void printSortedFreeRoomsByPrice() {
        log.info("Начало обработки команды: Свободные номера по цене");
        List<ModelRoom> list = admin.printSortedFreeByPrice();
        roomView.displaySortedFreeRoomsByPrice(list);
        log.info("Команда выполнена успешно: Свободные номера по цене");
    }
    private void printSortedFreeRoomsByCapacity() {
        log.info("Начало обработки команды: Свободные номера по вместимости");
        List<ModelRoom> list = admin.printSortedFreeByCapacity();
        roomView.displaySortedFreeRoomsByCapacity(list);
        log.info("Команда выполнена успешно: Свободные номера по вместимости");
    }
    private void printSortedFreeRoomsByStars() {
        log.info("Начало обработки команды: Свободные номера по звездам");
        List<ModelRoom> list = admin.printSortedFreeByStars();
        roomView.displaySortedFreeRoomsByStars(list);
        log.info("Команда выполнена успешно: Свободные номера по звездам");
    }

    private void printRoomAvailableByDate() {
        log.info("Начало обработки команды: Свободные номера к дате");
        try {
            System.out.println("Введите дату (гггг-мм-дд), на которую нужно найти свободные номера:");
            String input = scanner.nextLine();
            LocalDate date = LocalDate.parse(input);
            List<ModelRoom> rooms = admin.getFreeRoomsAtDate(date);
            if (rooms.isEmpty()) {
                System.out.println("На дату " + date + " нет свободных номеров.");
            } else {
                System.out.println("Свободные номера на дату " + date + ":");
                roomView.dispalyAllRooms(rooms);
            }
            log.info("Команда выполнена успешно: Свободные номера к дате");
        } catch (DateTimeParseException e) {
            log.error("Ошибка обработки команды: Свободные номера к дате", e);
            System.out.println("Ошибка: Неверный формат даты! Используйте ГГГГ-ММ-ДД.");
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Свободные номера к дате", e);
            System.out.println(e.getMessage());
        }
    }

    private void printGuestsByAlphabet() {
        log.info("Начало обработки команды: Постояльцы по алфавиту");
        List<ModelRoom> list = admin.printGuestByAlpha();
        roomView.displayGuestByAplpha(list);
        log.info("Команда выполнена успешно: Постояльцы по алфавиту");
    }
    private void printGuestsByCheckOutDate() {
        log.info("Начало обработки команды: Постояльцы по дате выселения");
        List<ModelRoom> list = admin.printGuestByTime();
        roomView.displayGuestByTime(list);
        log.info("Команда выполнена успешно: Постояльцы по дате выселения");
    }
    private void printGuestServicesByPrice() {
        log.info("Начало обработки команды: Услуги гостя по цене");
        System.out.println("Введите имя гостя");
        String guestName = scanner.nextLine();
        List<ServiceRecord> result = admin.printGuestServicesByPrice(guestName);
        if (result == null) {
            System.out.println("Ошибка: Гость с именем " + guestName + " не найден.");
            log.error("Ошибка обработки команды: Услуги гостя по цене - гость не найден: " + guestName);
        } else if (result.isEmpty()) {
            System.out.println("У гостя " + guestName + " пока нет заказанных услуг.");
            log.info("Команда выполнена успешно: Услуги гостя по цене");
        } else {
            serviceView.displayGuestServiceByPrice(result);
            log.info("Команда выполнена успешно: Услуги гостя по цене");
        }
    }

    private void printGuestServicesByDate() {
        log.info("Начало обработки команды: Услуги гостя по дате");
        System.out.println("Введите имя гостя");
        String guestName = scanner.nextLine();
        List<ServiceRecord> result = admin.printGuestServicesByDate(guestName);
        if (result == null) {
            System.out.println("Ошибка: Гость с именем " + guestName + " не найден.");
            log.error("Ошибка обработки команды: Услуги гостя по дате - гость не найден: " + guestName);
        } else if (result.isEmpty()) {
            System.out.println("У гостя " + guestName + " пока нет заказанных услуг.");
            log.info("Команда выполнена успешно: Услуги гостя по дате");
        } else {
            serviceView.displayGuestServiceByDate(result);
            log.info("Команда выполнена успешно: Услуги гостя по дате");
        }
    }
    private void printPaymentForRoom() {
        log.info("Начало обработки команды: Оплата за номер");
        try {
            System.out.println("Введите номер");
            int number = scanner.nextInt();
            scanner.nextLine();
            double res = admin.printSumForRoom(number);
            System.out.println("Оплата за комнату " + number + " = " + res);
            log.info("Команда выполнена успешно: Оплата за номер");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Оплата за номер", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Оплата за номер", e);
            System.out.println(e.getMessage());
        }
    }

    private void printThreeLastGuests() {
        log.info("Начало обработки команды: Три последних гостя номера");
        try {
            System.out.println("Введите номер");
            int number = scanner.nextInt();
            scanner.nextLine();
            List<Residence> list = admin.printThreeGuest(number);
            roomView.displayThreeLastGuests(list);
            log.info("Команда выполнена успешно: Три последних гостя номера");
        } catch (InputMismatchException e) {
            log.error("Ошибка обработки команды: Три последних гостя номера", e);
            System.out.println("Ошибка при вводе данных");
            scanner.nextLine();
        } catch (HotelException e) {
            log.error("Ошибка обработки команды: Три последних гостя номера", e);
            System.out.println(e.getMessage());
        }
    }

    private void printPricesForRoom() {
        log.info("Начало обработки команды: Прайс-лист номера");
        List<ModelRoom> list = admin.getRoomsSortedByPrice();
        roomView.displayPricesForRoom(list);
        log.info("Команда выполнена успешно: Прайс-лист номера");
    }

    private void printPricesForService() {
        log.info("Начало обработки команды: Прайс-лист сервисы");
        List<Service> list = admin.getServicesSortedByPrice();
        serviceView.displayPricesForService(list);
        log.info("Команда выполнена успешно: Прайс-лист сервисы");
    }

    private void printFreeCntRoom() {
        log.info("Начало обработки команды: Количество свободных номеров");
        admin.printFreeCntRoom();
        log.info("Команда выполнена успешно: Количество свободных номеров");
    }

    private void printOcupiedCntRoom() {
        log.info("Начало обработки команды: Количество занятых номеров");
        admin.printOcupiedCntRoom();
        log.info("Команда выполнена успешно: Количество занятых номеров");
    }

}

