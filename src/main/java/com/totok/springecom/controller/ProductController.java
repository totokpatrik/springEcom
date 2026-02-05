package com.totok.springecom.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable int id) {
		try {
			Product product = productService.getProductById(id);
			return ResponseEntity.ok(product);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/product/{productId}/image")
	public ResponseEntity<byte[]> getProductImage(@PathVariable int productId) {
		try {
			Product product = productService.getProductById(productId);
			byte[] imageData = product.getImageData();
			String imageType = product.getImageType();

			return ResponseEntity.ok().header("Content-Type", imageType).body(imageData);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/products/search")
	public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
		try {
			List<Product> products = productService.searchProducts(keyword);
			return ResponseEntity.ok(products);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
		try {
			Product savedProduct = productService.addProduct(product, imageFile);
			return ResponseEntity.ok(savedProduct);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}

	@PutMapping("/product/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestPart Product product,
			@RequestPart MultipartFile imageFile) {
		try {
			Product updatedProduct = productService.updateProduct(id, product, imageFile);
			return ResponseEntity.ok(updatedProduct);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/product/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable int id) {
		try {
			productService.getProductById(id);
		} catch (Exception e) {
			if (e instanceof java.util.NoSuchElementException)
				return ResponseEntity.notFound().build();
		}

		try {
			productService.deleteProduct(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}