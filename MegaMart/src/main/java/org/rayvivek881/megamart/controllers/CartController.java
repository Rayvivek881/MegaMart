package org.rayvivek881.megamart.controllers;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.rayvivek881.megamart.documents.CartElement;
import org.rayvivek881.megamart.services.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  @PostMapping("/add")
  public CartElement addProductToCart(@RequestBody CartElement cartElement) {
    return cartService.addProductToCart(cartElement);
  }

  @GetMapping("/get")
  public List<Document> getCart() {
    return cartService.getCart();
  }

  @DeleteMapping("/remove")
  public void removeProductFromCart(@RequestParam String productId) {
    cartService.removeProductFromCart(productId);
  }

  @DeleteMapping("/clear")
  public void clearCart() {
    cartService.clearCart();
  }

  @PutMapping("/update/quantity/{productId}")
  public CartElement updateQuantity(@PathVariable String productId, @RequestParam int quantity) {
    return cartService.updateQuantity(productId, quantity);
  }
}
