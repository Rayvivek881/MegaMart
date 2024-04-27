package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  public Optional<User> findByUsername(String username);
}
