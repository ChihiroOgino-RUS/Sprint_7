package tests.courier;

import client.ServiceClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CourierCredentials;
import model.CourierModel;
import model.LoginResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestDataGenerator;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CourierLoginTest {

    private ServiceClient client;
    private CourierModel courier;
    private int courierId;

    @Before
    public void setUp() {
        client = new ServiceClient();
        courier = TestDataGenerator.createUniqueCourier();
        client.createCourier(courier);   // предусловие: курьер существует
        courierId = client.loginCourier(
                        new CourierCredentials(courier.getLogin(), courier.getPassword()))
                .as(LoginResponse.class).getId();
    }

    @After
    public void tearDown() {
        if (courierId != 0) {
            client.deleteCourier(courierId);
        }
    }

    @DisplayName("Успешная авторизация курьера")
    @Description("Верные логин и пароль: код 200, в теле id")
    @Test
    public void loginCourierSuccess() {
        Response response = client.loginCourier(
                new CourierCredentials(courier.getLogin(), courier.getPassword()));

        assertEquals(200, response.statusCode());
        response.then().body("id", notNullValue());   // id каждый раз новый — проверяем только наличие
    }

    @DisplayName("Авторизация с неверным паролем")
    @Description("Существующий логин, неверный пароль: код 404 и сообщение об ошибке")
    @Test
    public void loginCourierWithWrongPasswordFails() {
        Response response = client.loginCourier(
                new CourierCredentials(courier.getLogin(), courier.getPassword() + "wrong"));

        assertEquals(404, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Авторизация с неверным логином")
    @Description("Неверный логин, существующий пароль: код 404 и сообщение об ошибке")
    @Test
    public void loginCourierWithWrongLoginFails() {
        Response response = client.loginCourier(
                new CourierCredentials(courier.getLogin() + "wrong", courier.getPassword()));

        assertEquals(404, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Логин и пароль отсутствуют в базе: код 404 и сообщение об ошибке")
    @Test
    public void loginCourierWithNonexistentUserFails() {
        Response response = client.loginCourier(new CourierCredentials(
                "nonexistent" + System.currentTimeMillis(),
                "pass" + System.currentTimeMillis()));

        assertEquals(404, response.statusCode());
        response.then().body("message", equalTo("Учетная запись не найдена"));
    }

    @DisplayName("Авторизация без логина")
    @Description("Нет обязательного поля login: код 400 и сообщение об ошибке")
    @Test
    public void loginCourierWithoutLoginFails() {
        Response response = client.loginCourier(
                new CourierCredentials(null, courier.getPassword()));

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }

    // Баг API: фактически ручка отвечает 504 "Service unavailable" (проверено 27.09.2026),
    // поэтому тест красный. Ожидание по документации — 400, как и при отсутствии логина.
    @DisplayName("Авторизация без пароля")
    @Description("Нет обязательного поля password: код 400 и сообщение об ошибке")
    @Test
    public void loginCourierWithoutPasswordFails() {
        Response response = client.loginCourier(
                new CourierCredentials(courier.getLogin(), null));

        assertEquals(400, response.statusCode());
        response.then().body("message", equalTo("Недостаточно данных для входа"));
    }
}
