package com.totok.springecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.totok.springecom.model.dto.OrderRequest;
import com.totok.springecom.model.dto.OrderResponse;
import com.totok.springecom.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/orders/place")
	public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest) {
		try {
			OrderResponse orderResponse = orderService.placeOrder(orderRequest);
			return ResponseEntity.ok(orderResponse);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error placing order: " + e.getMessage());
		}
	}

	@GetMapping("/orders")
	public ResponseEntity<?> getAllOrders() {
		try {
			List<OrderResponse> orderResponses = orderService.getAllOrderResponses();
			return ResponseEntity.ok(orderResponses);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error fetching orders: " + e.getMessage());
		}
	}

}
