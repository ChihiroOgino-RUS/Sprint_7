package client;

import constants.ApiConstants;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CourierCredentials;
import model.CourierModel;
import model.Order;
import model.OrderCancel;

import static io.restassured.RestAssured.given;

public class ServiceClient {

    @Step("Создание курьера")
    public Response createCourier(CourierModel courier) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(ApiConstants.BASE_URL + ApiConstants.COURIER_CREATE_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(credentials)
                .when()
                .post(ApiConstants.BASE_URL + ApiConstants.COURIER_LOGIN_PATH);
    }

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ApiConstants.BASE_URL + ApiConstants.ORDERS_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrders() {
        return given()
                .log().all()
                .when()
                .get(ApiConstants.BASE_URL + ApiConstants.ORDERS_PATH);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int id) {
        return given()
                .log().all()
                .pathParam("id", id)
                .when()
                .delete(ApiConstants.BASE_URL + ApiConstants.COURIER_DELETE_PATH);
    }

    @Step("Отмена заказа")
    public Response cancelOrder(int track) {
        return given()
                .log().all()
                .header("Content-Type", "application/json")
                .body(new OrderCancel(track))
                .when()
                .put(ApiConstants.BASE_URL + ApiConstants.ORDERS_CANCEL_PATH);
    }
}
