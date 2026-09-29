package utils;

import model.CourierModel;

public class TestDataGenerator {
    public static CourierModel createUniqueCourier() {
        String uniquePart = String.valueOf(System.currentTimeMillis());
        String login = "courier" + uniquePart;
        String password = "password" + uniquePart;
        String firstName = "Курьер";

        return new CourierModel(login, password, firstName);
    }
}
