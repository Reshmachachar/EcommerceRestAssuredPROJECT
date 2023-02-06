package Tests;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import POJO.CreateOrderPOJO;
import POJO.CreateOrderResPOJO;
import POJO.LoginResponsePOJO;
import POJO.createProductResponsePOJO;
import POJO.loginPOJO;
import POJO.orderPOJO;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Basic {
			public static void main(String[] args) {
				
			RequestSpecification req=new RequestSpecBuilder()
					.setBaseUri("https://rahulshettyacademy.com")
					.setContentType(ContentType.JSON)
					.build();
			
			ResponseSpecification res=new ResponseSpecBuilder()
					.expectStatusCode(200)
					.expectContentType(ContentType.JSON)
					.build();
			
			ResponseSpecification response=new ResponseSpecBuilder()
					.expectStatusCode(201)
					.expectContentType(ContentType.JSON)
					.build();
			
		//RestAssured.baseURI = "https://rahulshettyacademy.com/";
		loginPOJO l=new loginPOJO();
		l.setUserEmail("manali.kulkarni@cogniwize.com");
		l.setUserPassword("Manali@123");

		//login
		LoginResponsePOJO lrp=given()
				.log().all()
			    .spec(req)
				.body(l)
				.when()
				.post("api/ecom/auth/login")
				.then()
				.log().all()
				.spec(res)
				.extract().response().as(LoginResponsePOJO.class);
		String token =lrp.getToken();
		String userID=lrp.getUserId();


		//Create product
		createProductResponsePOJO crProduct=given()
				.log().all()
				.spec(req)
				.header("Content-Type","multipart/form-data")
				.header("Authorization",token)
				.formParam("productName","qwerty")
				.formParam("productAddedBy",userID)
				.formParam("productCategory","fashion")
				.formParam("productSubCategory","shirts")
				.formParam("productPrice","11500")
				.formParam("productDescription","Addias Originals")
				.formParam("productFor","women")
				.multiPart("productImage", new File("C:\\Users\\RESHMA\\Pictures\\Cogniwize.jpg"))
				.when()
				.post("api/ecom/product/add-product")
				.then()
				.log().all()
				.spec(response)
				.extract().response().as(createProductResponsePOJO.class);
		String productID=crProduct.getProductId();
		String messege=crProduct.getMessage();
		System.out.println("product id ="+productID);

		//Create order
		orderPOJO op=new orderPOJO();
		op.setCountry("India");
		op.setProductOrderedId(productID);

		List<orderPOJO> orderList = new ArrayList<orderPOJO>();
		orderList.add(op);	
		CreateOrderPOJO orders = new CreateOrderPOJO();
		orders.setOrders(orderList);

		CreateOrderResPOJO crOrder=given()
				.log().all()
				.spec(req)
				.header("Authorization",token)
				.body(orders)
				.when()
				.post("api/ecom/order/create-order")
				.then()
				.log().all()
				  .spec(response)
				.extract().response().as(CreateOrderResPOJO.class);

		List<String> a=new ArrayList<String>();
		a.addAll(crOrder.getOrders()) ;
		System.out.println("order id is"+a);

		//view order
		given()
		.log().all()
		.spec(req)
		.queryParam("id",a)
		.header("Authorization",token)
		.when()
		.get("api/ecom/order/get-orders-details")
		.then()
		.log().all()
		.spec(res);

		//Delete order
		given()
		.log().all()
		.spec(req)
		.header("Authorization",token)
		.pathParam("productId",productID)
		.when()
		.delete("api/ecom/product/delete-product/{productId}")
		.then()
		.log().all()
		 .spec(res)
		.extract().response().asString();
	}

	}
























//		RestAssured.baseURI = "https://rahulshettyacademy.com/";
//		loginPOJO l=new loginPOJO();
//		l.setUserEmail("manali.kulkarni@cogniwize.com");
//		l.setUserPassword("Manali@123");
//
//		//login
//		LoginResponsePOJO lrp=given()
//				.log().all()
//				.header("Content-Type","application/json")
//				.body(l)
//				.when()
//				.post("api/ecom/auth/login")
//				.then()
//				.log().all()
//				.assertThat().statusCode(200)
//				.extract().response().as(LoginResponsePOJO.class);
//		String token =lrp.getToken();
//		String userID=lrp.getUserId();
//
//
//		//Create product
//		createProductResponsePOJO crProduct=given()
//				.log().all()
//				.header("Content-Type","multipart/form-data")
//				.header("Authorization",token)
//				.formParam("productName","qwerty")
//				.formParam("productAddedBy",userID)
//				.formParam("productCategory","fashion")
//				.formParam("productSubCategory","shirts")
//				.formParam("productPrice","11500")
//				.formParam("productDescription","Addias Originals")
//				.formParam("productFor","women")
//				.multiPart("productImage", new File("C:\\Users\\RESHMA\\Pictures\\Cogniwize.jpg"))
//				.when()
//				.post("api/ecom/product/add-product")
//				.then()
//				.log().all()
//				.assertThat().statusCode(201)
//				.extract().response().as(createProductResponsePOJO.class);
//		String productID=crProduct.getProductId();
//		String messege=crProduct.getMessage();
//		System.out.println("product id ="+productID);
//
//		//Create Product
//		orderPOJO op=new orderPOJO();
//		op.setCountry("India");
//		op.setProductOrderedId(productID);
//
//		List<orderPOJO> orderList = new ArrayList<orderPOJO>();
//		orderList.add(op);	
//		CreateOrderPOJO orders = new CreateOrderPOJO();
//		orders.setOrders(orderList);
//
//		CreateOrderResPOJO crOrder=given()
//				.log().all()
//				.header("Content-Type","application/json")
//				.header("Authorization",token)
//				.body(orders)
//				.when()
//				.post("api/ecom/order/create-order")
//				.then()
//				.log().all()
//				.assertThat().statusCode(201)
//				.extract().response().as(CreateOrderResPOJO.class);
//
//		List<String> a=new ArrayList<String>();
//		a.addAll(crOrder.getOrders()) ;
//		System.out.println("order id is"+a);
//
//		//view order
//		given()
//		.log().all()
//		.queryParam("id",a)
//		.header("Authorization",token)
//		.when()
//		.get("api/ecom/order/get-orders-details")
//		.then()
//		.log().all()
//		.assertThat().statusCode(200);
//
//		//Delete order
//		given()
//		.log().all()
//		.header("Authorization",token)
//		.pathParam("productId",productID)
//		.when()
//		.delete("api/ecom/product/delete-product/{productId}")
//		.then()
//		.log().all()
//		.assertThat().statusCode(200)
//		.extract().response().asString();
//	}


