package tests.order;

import client.ServiceClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class OrdersListTest {

    private ServiceClient client;

    @Before
    public void setUp() {
        client = new ServiceClient();
    }

    @DisplayName("Получение списка заказов")
    @Description("Список заказов в теле ответа: код 200, orders непустой")
    @Test
    public void getOrdersReturnsNonEmptyList() {
        Response response = client.getOrders();

        assertEquals(200, response.statusCode());
        response.then().body("orders", not(empty()));
    }
}
