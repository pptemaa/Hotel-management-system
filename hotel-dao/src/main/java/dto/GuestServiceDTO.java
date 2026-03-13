package dto;

public class GuestServiceDTO {
    private String serviceName;
    private String date;
    private double price;

    public GuestServiceDTO() {
    }

    public GuestServiceDTO(String serviceName, String date, double price) {
        this.serviceName = serviceName;
        this.date = date;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

