package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.CartElement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartElementRepository extends MongoRepository<CartElement, String> {
  public List<CartElement> getCartElementByUserId(String userId);

  public void deleteCartElementByUserId(String userId);
}
