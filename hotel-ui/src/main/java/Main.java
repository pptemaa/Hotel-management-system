import Service.Admin;
import controller.HotelController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        Admin admin = ctx.getBean(Admin.class);
        try {
            admin.initFromDb();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить данные из БД: " + e.getMessage());
        }
        HotelController hotelController = ctx.getBean(HotelController.class);
        hotelController.start();
    }
}