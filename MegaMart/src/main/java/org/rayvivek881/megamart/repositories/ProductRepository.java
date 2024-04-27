package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

  public List<Product> getProductsBySellerIdOrderByCreatedAt(String sellerId);
}