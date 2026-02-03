package com.totok.springecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.totok.springecom.model.Product;
import com.totok.springecom.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts() {
		try {
			List<Product> products = productService.getAllProducts();
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id) {
		try {
			Product product = productService.getProductById(id);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}