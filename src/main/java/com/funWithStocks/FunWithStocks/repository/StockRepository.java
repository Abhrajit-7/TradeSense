package com.funWithStocks.FunWithStocks.repository;

import com.funWithStocks.FunWithStocks.dto.StockListDTO;
import com.funWithStocks.FunWithStocks.dto.WinnerDTO;
import com.funWithStocks.FunWithStocks.entity.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    //@Query("SELECT DISTINCT ad.username FROM Stock b JOIN User ad ON b.user.username = ad.username WHERE CAST(FIND_IN_SET(:selected_numbers, REPLACE(b.selected_numbers, ' ', '')) AS int) > 0")
    @Query(value = "SELECT new com.funWithStocks.FunWithStocks.dto.WinnerDTO(ad.username, sum(b.bet_amount) as total_invested, :selected_numbers)  " +
            "FROM Stock b " +
            "JOIN User ad " +
            "ON b.user.username = ad.username " +
            "WHERE b.slot = :slots " +
            "AND b.bet_time >= :start_time " +
            "AND b.bet_time <= :end_time " +
            "AND b.selected_stocks = :selected_numbers "+
            "group by ad.username")
    List<WinnerDTO> findUsernamesBySelectedNumber(@Param("selected_numbers") String selected_numbers, @Param("start_time") LocalDateTime start_time, @Param("end_time") LocalDateTime end_time, @Param("slots") String slots);


    BigDecimal findLatestPrice(String selected_numbers);
    // Below is the query for fetching the total amount invested by user:
    @Query(value = "SELECT new com.funWithStocks.FunWithStocks.dto.StockListDTO(b.id, b.selected_stocks, b.bet_amount) FROM Stock b " +
            "WHERE b.user.username=:username AND b.slot = :slots ORDER BY b.bet_time DESC")
    Page<StockListDTO> findLatestBet(Pageable pageable, @Param("username") String username, @Param("slots") String slots);
}
