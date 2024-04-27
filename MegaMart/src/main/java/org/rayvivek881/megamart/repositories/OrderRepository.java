package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String>{
}
