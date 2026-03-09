package dto;

import java.time.LocalDate;

public class InGuestDTO {
    private int number;
    private String name;
    private String checkInDate;
    private String checkOutDate;
    public InGuestDTO(int number,String name,String checkInDate, String checkOutDate){
        this.number=number;
        this.name = name;
        this.checkInDate=checkInDate;
        this.checkOutDate=checkOutDate;
    }
    public InGuestDTO(){}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }
}
