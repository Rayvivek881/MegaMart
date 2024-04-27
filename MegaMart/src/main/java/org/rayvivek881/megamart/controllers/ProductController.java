package org.rayvivek881.megamart.controllers;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.documents.Product;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.services.ProductService;
import org.rayvivek881.megamart.services.UserServices;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;
  private final UserServices userService;

  @PostMapping("/create")
  public Product create(@RequestBody Product product) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userService.getUserByUsername(username);

    if (user == null || !user.getRoles().contains("SELLER")) {
      throw new RuntimeException("You are not authorized to create a product");
    }

    product.setSellerId(user.get_id());
    return productService.save(product);
  }

  @GetMapping("/{productId}")
  public Product getProductById(@PathVariable String productId) {
    return productService.getProductById(productId);
  }

  @GetMapping("/seller/{sellerId}")
  public List<Product> getProductBySellerId(@PathVariable String sellerId, @RequestParam(defaultValue = "0") int skip, @RequestParam(defaultValue = "10") int limit) {
    return productService.getProductBySellerId(sellerId, skip, limit);
  }

  @GetMapping("/search")
  public List<Product> searchByKeyword(@RequestParam String text) {
    return productService.searchByKeyword(text);
  }
}
