package view;

import model.Service;
import model.ServiceRecord;

import java.util.List;

public interface IServiceView {
    void displayAllService(List<Service> serviceList);
    void displayGuestServiceByPrice(List<ServiceRecord> list);
    void displayGuestServiceByDate(List<ServiceRecord> list);
    void displayPricesForService(List<Service> list);


}
