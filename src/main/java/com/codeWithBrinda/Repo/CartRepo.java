package com.codeWithBrinda.Repo;

import com.codeWithBrinda.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {

  public Cart findByProductIdAndUserUserid (int productId, int UserId);

  public int countByUserUserid(int UserId);

  List<Cart> findByUserUserid(int userId);

    void deleteByUserUserid(int uid);
}

