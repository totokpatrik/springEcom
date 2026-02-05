package com.totok.springecom.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.totok.springecom.model.Product;
import com.totok.springecom.repo.ProductRepo;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductRepo productRepo;

	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	public Product getProductById(int productId) {
		return productRepo.findById(productId).orElseThrow();
	}

	public Product addProduct(Product product, MultipartFile image) throws IOException {
		product.setImageName(image.getOriginalFilename());
		product.setImageType(image.getContentType());
		product.setImageData(image.getBytes());

		return productRepo.save(product);
	}

	public Product updateProduct(int productId, Product updatedProduct, MultipartFile image) throws IOException {
		Product existingProduct = productRepo.findById(productId).orElseThrow();

		existingProduct.setName(updatedProduct.getName());
		existingProduct.setDescription(updatedProduct.getDescription());
		existingProduct.setBrand(updatedProduct.getBrand());
		existingProduct.setPrice(updatedProduct.getPrice());
		existingProduct.setCategory(updatedProduct.getCategory());
		existingProduct.setReleaseDate(updatedProduct.getReleaseDate());
		existingProduct.setProductAvailable(updatedProduct.isProductAvailable());
		existingProduct.setStockQuantity(updatedProduct.getStockQuantity());

		existingProduct.setImageName(image.getOriginalFilename());
		existingProduct.setImageType(image.getContentType());
		existingProduct.setImageData(image.getBytes());
		
		return productRepo.save(existingProduct);
	}

	public void deleteProduct(int id) {
		productRepo.deleteById(id);
	}

	public List<Product> searchProducts(String keyword) {
		List<Product> products = productRepo.searchProducts(keyword);
		return products;
	}
}