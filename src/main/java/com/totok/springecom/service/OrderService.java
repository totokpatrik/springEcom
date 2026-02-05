package com.totok.springecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totok.springecom.model.Order;
import com.totok.springecom.model.Product;
import com.totok.springecom.model.OrderItem;
import com.totok.springecom.model.dto.OrderItemRequest;
import com.totok.springecom.model.dto.OrderItemResponse;
import com.totok.springecom.model.dto.OrderRequest;
import com.totok.springecom.model.dto.OrderResponse;
import com.totok.springecom.repo.OrderRepo;
import com.totok.springecom.repo.ProductRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	private ProductRepo productRepo;

	public OrderResponse placeOrder(OrderRequest orderRequest) {

		Order order = new Order();
		String generatedOrderId = "ORD" + System.currentTimeMillis();
		order.setOrderId(generatedOrderId);
		order.setCustomerName(orderRequest.customerName());
		order.setEmail(orderRequest.email());
		order.setStatus("PLACED");
		order.setOrderDate(LocalDate.now());

		// create the list of order items
		List<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemRequest orderItemRequest : orderRequest.items()) {
			// Find the product if exists
			Product product = productRepo.findById(orderItemRequest.productId())
					.orElseThrow(() -> new RuntimeException("Product not found."));

			// Subtract the stock quantity
			product.setStockQuantity(product.getStockQuantity() - orderItemRequest.quantity());
			productRepo.save(product);

			OrderItem orderItem = OrderItem.builder().product(product).quantity(orderItemRequest.quantity())
					.totalPrice(product.getPrice().multiply(BigDecimal.valueOf(orderItemRequest.quantity())))
					.order(order).build();

			orderItems.add(orderItem);
		}

		order.setOrderItems(orderItems);

		Order savedOrder = orderRepo.save(order);
		List<OrderItemResponse> orderItemResponses = new ArrayList<OrderItemResponse>();
		for (OrderItem orderItem : savedOrder.getOrderItems()) {
			OrderItemResponse orderItemResponse = new OrderItemResponse(orderItem.getProduct().getName(),
					orderItem.getQuantity(), orderItem.getTotalPrice());

			orderItemResponses.add(orderItemResponse);
		}

		OrderResponse orderResponse = new OrderResponse(savedOrder.getOrderId(), savedOrder.getCustomerName(),
				savedOrder.getEmail(), savedOrder.getStatus(), savedOrder.getOrderDate(), orderItemResponses);

		return orderResponse;
	}

	public List<OrderResponse> getAllOrderResponses() {
		List<OrderResponse> orderResponses = new ArrayList<>();
		List<Order> orders = orderRepo.findAll();
		
		for (Order order : orders) {
			List<OrderItemResponse> orderItemResponses = new ArrayList<>();
			
			for (OrderItem orderItem : order.getOrderItems()) {
				OrderItemResponse orderItemResponse = new OrderItemResponse(
							orderItem.getProduct().getName(),
							orderItem.getQuantity(),
							orderItem.getTotalPrice()
						);
				
				orderItemResponses.add(orderItemResponse);
			}
			
			OrderResponse orderResponse = new OrderResponse(
					order.getOrderId(),
					order.getCustomerName(),
					order.getEmail(),
					order.getStatus(),
					order.getOrderDate(),
					orderItemResponses
					);
			
			orderResponses.add(orderResponse);
		}
		
		return orderResponses;
	}

}
