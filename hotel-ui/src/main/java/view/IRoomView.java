package view;

import model.ModelRoom;
import model.Residence;

import java.util.List;

public interface IRoomView {
    void displayDetailsOfRoom(ModelRoom room);
    void dispalyAllRooms(List<ModelRoom> listRooms);
    void displayFreeRooms(List<ModelRoom> listFreeRooms);
    void displayCntFreeRooms(List<ModelRoom> listFreeRooms);
    void displaySumForRoom(int number, double sum,String guestName);
    void displayThreeGuest(List<Residence> history, int number);
    void displayPriceRoom(List<ModelRoom> listPrice);
    void displaySortedRoomsByPrice(List<ModelRoom> roomList);
    void displaySortedRoomsByCapacity(List<ModelRoom> roomList);
    void displaySortedRoomsByStars(List<ModelRoom> roomList);
    void displaySortedFreeRoomsByPrice(List<ModelRoom> roomList);
    void displaySortedFreeRoomsByCapacity(List<ModelRoom> roomList);
    void displaySortedFreeRoomsByStars(List<ModelRoom> roomList);
    void displayGuestByAplpha(List<ModelRoom> list);
    void displayGuestByTime(List<ModelRoom> list);
    void displayThreeLastGuests(List<Residence> history);
    void displayPricesForRoom(List<ModelRoom> list);

}
