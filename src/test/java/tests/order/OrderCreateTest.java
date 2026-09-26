package tests.order;

import client.ServiceClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.TrackResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final String[] colors;

    private ServiceClient client;
    private int createdTrack;

    public OrderCreateTest(String[] colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getColorOptions() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        });
    }

    @Before
    public void setUp() {
        client = new ServiceClient();
        createdTrack = 0;
    }

    @After
    public void tearDown() {
        if (createdTrack != 0) {
            client.cancelOrder(createdTrack);
        }
    }

    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Варианты color: BLACK, GREY, оба цвета, без цвета — код 201 и track в теле")
    @Test
    public void createOrderWithColors() {
        Order order = new Order(
                "Иван", "Петров", "Москва, ул. Тестовая, 1", "4",
                "+7 800 355 35 35", 5,
                LocalDate.now().plusDays(5).toString(),
                "Заказ с проверкой цвета", colors);

        Response response = client.createOrder(order);

        assertEquals(201, response.statusCode());
        response.then().body("track", notNullValue());
        createdTrack = response.as(TrackResponse.class).getTrack();
    }
}

