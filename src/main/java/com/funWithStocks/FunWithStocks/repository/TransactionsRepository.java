package com.funWithStocks.FunWithStocks.repository;

import com.funWithStocks.FunWithStocks.entity.Transaction;
import com.funWithStocks.FunWithStocks.util.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByTransactionTypeOrderByTransactionTimeDesc(TransactionType transactionType);
    Optional<Transaction> findByOrderId(String order_id);
}
