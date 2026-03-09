package view;

import org.springframework.stereotype.Component;

@Component
public class ConsoleViewFactory implements ViewFactory{
    @Override
    public IRoomView createRoomView(){
        return new RoomView();
    }

    @Override
    public IServiceView createServiceView(){
        return new ServiceView();
    }

}
