package dto;

public class RoomDTO {
    private int id;
    private int number;
    private double price;
    private String status;
    private int stars;
    private int capacity;

    public RoomDTO() {
    }


    public RoomDTO(int id, int number, double price, String status,int stars,int capacity) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.status = status;
        this.stars=stars;
        this.capacity=capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}