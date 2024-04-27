package org.rayvivek881.megamart.services;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.rayvivek881.megamart.documents.Order;
import org.rayvivek881.megamart.documents.OrderElement;
import org.rayvivek881.megamart.documents.Product;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.repositories.OrderElementRepository;
import org.rayvivek881.megamart.repositories.OrderRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServices {

  private final OrderRepository orderRepository;
  private final MongoTemplate mongoTemplate;
  private final UserServices userServices;
  private final OrderElementRepository orderElementRepository;
  private final CartService cartServices;

  private Order makeOrderForSeller(String sellerId, String buyerId, String addressId, List<Document> cartElements) {
    double totalPrice = 0;
    for (Document cartElement : cartElements) {
      Document product = cartElement.get("product", Document.class);
      totalPrice += product.getDouble("price") * cartElement.getInteger("quantity")
            * (1 - product.getDouble("discount") / 100);
    }
    Order order = Order.builder()
        .buyerId(buyerId)
        .SellerId(sellerId)
        .status("Order Placed")
        .total(totalPrice)
        .addressId(addressId)
        .build();
    orderRepository.save(order);
    for (Document cartElement : cartElements) {
      Document product = cartElement.get("product", Document.class);
      OrderElement orderElement = OrderElement.builder()
          .orderId(order.get_id())
          .productId(product.getString("_id"))
          .quantity(cartElement.getInteger("quantity"))
          .discount(product.getDouble("discount"))
          .build();
      orderElementRepository.save(orderElement);
    }
    return order;
  }

  private Order getOrderObjectById(String orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new RuntimeException("Order not found"));
  }

  public List<Order> getOrderBySellerId(String sellerId, int skip, int limit) {
    Query query = new Query(Criteria.where("sellerId").is(sellerId));
    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip(skip).limit(limit);

    return mongoTemplate.find(query, Order.class);
  }

  public List<Order> getOrderByBuyerId(String BuyerId, int skip, int limit) {
    Query query = new Query(Criteria.where("BuyerId").is(BuyerId));

    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
    query.skip(skip).limit(limit);

    return mongoTemplate.find(query, Order.class);
  }

  public Document getOrderById(String orderId) {
    Order order = getOrderObjectById(orderId);

    AggregationOperation match = Aggregation.match(Criteria.where("orderId").is(orderId));
    AggregationOperation lookup = Aggregation.lookup("products", "productId", "_id", "products");
    Aggregation aggregation = Aggregation.newAggregation(match, lookup);

    List<Document> orderElements = mongoTemplate.aggregate(aggregation, "OrderElements", Document.class).getMappedResults();

    Document orderDocument = new Document();
    orderDocument.put("order", order);
    orderDocument.put("orderElements", orderElements);

    return orderDocument;
  }

  public void OrderProduct(String productId, int quantity, String addressId) {
    Product product = mongoTemplate.findById(productId, Product.class);
    if (product == null) {
      throw new RuntimeException("Product not found");
    }

    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    double totalPrice = product.getPrice() * quantity * (1 - product.getDiscount() / 100);

    Order order = Order.builder()
        .buyerId(user.get_id())
        .SellerId(product.getSellerId())
        .status("Order Placed")
        .total(totalPrice)
        .addressId(addressId)
        .build();
    orderRepository.save(order);

    OrderElement orderElement = OrderElement.builder()
        .orderId(order.get_id())
        .productId(productId)
        .quantity(quantity)
        .discount(product.getDiscount())
        .build();
    orderElementRepository.save(orderElement);
  }


  public List<Order> OrderByCart(String addressId) {
    List<Document> cart = cartServices.getCart();
    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    Map<String, List<Document>> sellerMap = new HashMap<>();
    if (cart.isEmpty()) {
      throw new RuntimeException("Cart is empty");
    }
    cart.forEach(currentCartElement -> {
      String sellerId = currentCartElement.get("product", Document.class)
          .getString("sellerId");

      sellerMap.putIfAbsent(sellerId, List.of());
      sellerMap.get(sellerId).add(currentCartElement);
    });
    List<Order> response = new ArrayList<>();
    sellerMap.forEach((sellerId, cartElements) -> {
      response.add(makeOrderForSeller(sellerId, user.get_id(), addressId, cartElements));
    });
    return response;
  }

  public void updateOrderStatus(String orderId, String status) {
    Order order = getOrderObjectById(orderId);
    order.setStatus(status);
    orderRepository.save(order);
  }

  public void updateDeliveryDate(String orderId, String deliveryDate) throws ParseException {
    Order order = getOrderObjectById(orderId);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    order.setDeliveryDate(dateFormat.parse(deliveryDate));
    orderRepository.save(order);
  }

  public List<String> pushTrackHistory(String orderId, String track) {
    Order order = getOrderObjectById(orderId);
    List<String> trackHistory = order.getTrackHistory();
    if (trackHistory == null) {
      trackHistory = new ArrayList<>();
    }
    trackHistory.add(track);
    order.setTrackHistory(trackHistory);
    orderRepository.save(order);
    return trackHistory;
  }
}
