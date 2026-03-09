package view;

import model.Service;
import model.ServiceRecord;

import java.util.List;

public class ServiceView implements IServiceView {
    @Override
    public void displayAllService(List<Service> serviceList){
        System.out.println("Список всех услуг");
        if (!serviceList.isEmpty()){
            for (Service service:serviceList){
                System.out.println("Название " + service.getName()+" цена " + service.getPrice());
            }
        }
    }
    @Override
    public void displayGuestServiceByPrice(List<ServiceRecord> list){
        System.out.println("Отсортированныые услуги гостя по цене");
        for (ServiceRecord serviceRecord:list){
            System.out.println("Услуга " + serviceRecord.getName());
        }
    }
    @Override
    public void displayGuestServiceByDate(List<ServiceRecord> list){
        System.out.println("Отсортированныые услуги гостя по дате");
        for (ServiceRecord serviceRecord:list){
            System.out.println("Услуга " + serviceRecord.getName());
        }
    }
    @Override
    public void displayPricesForService(List<Service> list){
        if (list.isEmpty()){
            System.out.println("Услуги не найдены");
        }else{
            System.out.println("Цены на все услуги");
            for (Service service:list){
                System.out.println("Услуга " + service.getName() + " , цена " + service.getPrice());
            }
        }
    }

}
