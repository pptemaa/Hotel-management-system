package view;

public interface ViewFactory {
    IRoomView createRoomView();
    IServiceView createServiceView();
}
