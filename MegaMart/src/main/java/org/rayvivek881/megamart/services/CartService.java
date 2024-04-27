package org.rayvivek881.megamart.services;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.rayvivek881.megamart.documents.CartElement;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.repositories.CartElementRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

  private final CartElementRepository cartElementRepository;
  private final UserServices userServices;
  private final MongoTemplate mongoTemplate;

  private CartElement getCartElementById(String cartElementId) {
    return cartElementRepository.findById(cartElementId)
        .orElseThrow(() -> new RuntimeException("Cart Element not found"));
  }

  public CartElement addProductToCart(CartElement cartElement) {
    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    cartElement.setUserId(user.get_id());
    return cartElementRepository.save(cartElement);
  }

  public CartElement updateQuantity(String cartElementId, int quantity) {
    CartElement cartElement = getCartElementById(cartElementId);

    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    if (!cartElement.getUserId().equals(user.get_id())) {
      throw new RuntimeException("You are not authorized to perform this operation");
    }
    cartElement.setQuantity(quantity);
    return cartElementRepository.save(cartElement);
  }

  public void removeProductFromCart(String cartElementId) {
    CartElement cartElement = getCartElementById(cartElementId);

    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    if (!cartElement.getUserId().equals(user.get_id())) {
      throw new RuntimeException("You are not authorized to perform this operation");
    }
    cartElementRepository.deleteById(cartElementId);
  }

  public void clearCart() {
    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);

    cartElementRepository.deleteCartElementByUserId(user.get_id());
  }

  public List<Document> getCart() {
    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);

    AggregationOperation match = Aggregation.match(Criteria.where("userId").is(user.get_id()));
    AggregationOperation lookup = Aggregation.lookup("Products", "productId", "_id", "product");
    AggregationOperation unwind = Aggregation.unwind("$product");
    Aggregation aggregation = Aggregation.newAggregation(match, lookup, unwind);
    return mongoTemplate.aggregate(aggregation, "CartElements", Document.class).getMappedResults();
  }
}
