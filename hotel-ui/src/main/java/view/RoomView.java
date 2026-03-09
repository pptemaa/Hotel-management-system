package view;

import model.ModelRoom;
import model.Residence;
import model.Service;
import model.ServiceRecord;

import java.util.List;

public class RoomView implements IRoomView {
    @Override
    public void displayDetailsOfRoom(ModelRoom room) {
        System.out.println(
                "Номер " + room.getNumber() + '\n'
                        + "Цена " + room.getPrice() + '\n'
                        + "Статус " + room.getStatus() + '\n'
                        + "Вместимость " + room.getCapacity() + '\n'
                        + "Звезды " + room.getStars()
        );
        if (room.getGuest() != null) {
            System.out.println("Гость " + room.getGuest().getName() + '\n'
                    + "Дата заезда " + room.getCheckInDate() + '\n'
                    + "Дата выезда " + room.getCheckOutDate() + '\n'
                    + "Всего к оплате " + room.canculate()
            );
        }
    }
    @Override
    public void dispalyAllRooms(List<ModelRoom> listRooms) {
        System.out.println("Список всех номеров");
        for (ModelRoom modelRoom : listRooms) {
            String guestName;
            if (modelRoom.getGuest() != null) {
                guestName = modelRoom.getGuest().getName();
            } else {
                guestName = "нету";
            }
            System.out.println(
                            "Номер " + modelRoom.getNumber() + '\n'
                            + "Цена " + modelRoom.getPrice() + '\n'
                            + "Статус " + modelRoom.getStatus() + '\n'
                            + "Вместимость " + modelRoom.getCapacity() + '\n'
                            + "Звезды " + modelRoom.getStars()+'\n'
                            + "Гость : " + guestName
            );

        }
    }
    @Override
    public void displayFreeRooms(List<ModelRoom> listFreeRooms){
        System.out.println("Список всех свободных номеров");
        for (ModelRoom modelRoom : listFreeRooms) {
            System.out.println(
                    "Номер " + modelRoom.getNumber() + '\n'
                            + "Цена " + modelRoom.getPrice() + '\n'
                            + "Статус " + modelRoom.getStatus() + '\n'
                            + "Вместимость " + modelRoom.getCapacity() + '\n'
                            + "Звезды " + modelRoom.getStars()+'\n'
            );

        }
    }
    @Override
    public void displayCntFreeRooms(List<ModelRoom> listFreeRooms){
        System.out.println("Всего свободных комнат: " + listFreeRooms.size());
    }
    @Override
    public void displaySumForRoom(int number, double sum,String guestName){
        System.out.println("Оплата за номер " + number + " в котором проживал " + guestName + " : " + sum + " рублей");
    }
    @Override
    public void displayThreeGuest(List<Residence> history,int number){
        System.out.println("3 последних постояльцев номера "+number + " и даты их пребывания");
        for (Residence residence:history){
            System.out.println("Гость " +residence.getGuest().getName() + "Дата пребывания" + residence.getCheckInDate());
        }
    }
    @Override
    public void displayPriceRoom(List<ModelRoom> listPrice){
        System.out.println("Цены номеров");
        for (ModelRoom room:listPrice){
            System.out.println("Номер " +room.getNumber() + " Цена: " + room.getPrice());
        }
    }
    @Override
    public void displaySortedRoomsByPrice(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные номера по цене");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " стоит " + room.getPrice());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displaySortedRoomsByCapacity(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные номера по вместимости");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " вместимость " + room.getCapacity());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displaySortedRoomsByStars(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные номера по звездам");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " звезды " + room.getStars());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displaySortedFreeRoomsByPrice(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные свободные номера по цене");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " стоит " + room.getPrice());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displaySortedFreeRoomsByCapacity(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные свободнные номера по вместимости");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " вместимость " + room.getCapacity());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displaySortedFreeRoomsByStars(List<ModelRoom> roomList){
        if (!roomList.isEmpty()){
            System.out.println("Отсортированные свободные номера по звездам");
            for (ModelRoom room:roomList){
                System.out.println("Номер " + room.getNumber() + " звезды " + room.getStars());
            }
        }else{
            System.out.println("Нету добавленных номеров");
        }
    }
    @Override
    public void displayGuestByAplpha(List<ModelRoom> list){
        if (!list.isEmpty()){
            System.out.println("Отсортированные постояльцы по алвафиту");
            for (ModelRoom room:list){
                System.out.println(room.getGuest().getName());
            }
        }else {
            System.out.println("Список постояльец пуст");
        }
    }
    @Override
    public void displayGuestByTime(List<ModelRoom> list){
        if (!list.isEmpty()){
            System.out.println("Отсортированные постояльцы по дате выселения");
            for (ModelRoom room:list){
                System.out.println("Гость " + room.getGuest().getName()+"Дата выселения " + room.getCheckOutDate());
            }
        }else {
            System.out.println("Список постояльец пуст");
        }
    }
    @Override
    public void displayThreeLastGuests(List<Residence> history){
        if (history==null){
            System.out.println("Номер не найден");
        }else if (history.isEmpty()){
            System.out.println("Истори пуста");
        }else{
            System.out.println("История: 3 последних гостя номера ");
            for (Residence residence:history){
                System.out.println(residence.getGuest().getName());
            }
        }
    }
    @Override
    public void displayPricesForRoom(List<ModelRoom> list){
        if (list.isEmpty()){
            System.out.println("Номера не найдены");
        }else{
            System.out.println("Цены на все номера");
            for (ModelRoom modelRoom:list){
                System.out.println("Номер " + modelRoom.getNumber()+" , цена " + modelRoom.getPrice());
            }
        }
    }




}
