package com.totok.springecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.totok.springecom.model.Product;
import com.totok.springecom.repo.ProductRepo;

@Service
public class ProductService {

	   @Autowired
	    private ProductRepo productRepo;
	   
	   public List<Product> getAllProducts() {
		   return productRepo.findAll();
	   }
	   	
	   public Product getProductById(int productId) {
		   return productRepo.findById(productId).orElseThrow();
	   }
}