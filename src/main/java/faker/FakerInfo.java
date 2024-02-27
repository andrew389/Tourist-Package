package faker;

import com.github.javafaker.Faker;

import tour.Tour;
import db.DBTours;

import java.util.Locale;

public class FakerInfo {
    private Faker faker;

    public FakerInfo() {
        faker = new Faker(new Locale("en"));
    }

    public void fakeTours() {

        for (int i = 0; i < 10; i++) {
            String name = faker.lorem().sentence();
            String type = faker.lorem().word();
            String transport = faker.lorem().word();
            String meal = faker.lorem().word();
            int duration = faker.number().numberBetween(1, 15);
            int price = faker.number().numberBetween(900, 2500);

            Tour fakeTour = new Tour(name, type, transport, meal, duration, price);
            DBTours.addTour(fakeTour);
        }
    }


}
