package com.funWithStocks.FunWithStocks.services;

import com.funWithStocks.FunWithStocks.dto.StocksDTO;
import com.funWithStocks.FunWithStocks.dto.WinnerDTO;
import com.funWithStocks.FunWithStocks.entity.Stock;
import com.funWithStocks.FunWithStocks.entity.User;
import com.funWithStocks.FunWithStocks.repository.StockRepository;
import com.funWithStocks.FunWithStocks.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StockService {

    private static final Logger logger = LoggerFactory.getLogger(StockService.class);
    private static final int MAX_DEPTH = 2;
    private static final double PERCENTAGE_TO_PARENT = 0.04;
    private static final double PERCENTAGE_TO_PARENT_OF_PARENT = 0.02;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<String> submitBet(StocksDTO stocksDTO, String username) {
        logger.info("Request JSON: {}", stocksDTO);

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found: " + username);
        }

        double betAmount = stocksDTO.getBet_amount();
        Stock stock = new Stock();
        stock.setBet_number(stocksDTO.getBet_number());
        stock.setUser(user);
        stock.setBet_amount(betAmount);
        stock.setSelected_stocks(stocksDTO.getSelected_stock_nse_codes());
        stock.setSlot(stocksDTO.getSlot());

        double updatedBal = user.getAccountBalance() - (betAmount * stocksDTO.getNumberCount());
        user.setAccountBalance(updatedBal);
        userRepository.save(user);

        Stock stockSaved = stockRepository.save(stock);

        String parentUsername = user.getParent() != null ? user.getParent().getUsername() : null;
        if (parentUsername != null) {
            updateParentAccountBalance(parentUsername, betAmount * PERCENTAGE_TO_PARENT);

            String parentOfParentUsername = getParentUsername(parentUsername);
            if (parentOfParentUsername != null) {
                updateParentOfParentAccountBalance(parentOfParentUsername, betAmount * PERCENTAGE_TO_PARENT_OF_PARENT);
            }
        }

        List<String> responseList = List.of(
                "ID: " + stock.getBet_number(),
                "Stocks: " + stock.getSelected_stocks(),
                "Amount: " + stock.getBet_amount()
        );
        logger.info("Response: {}", responseList);

        return responseList;
    }

    private void updateParentOfParentAccountBalance(String parentUsername, double amount) {
        updateParentOfParentAccountBalanceRecursive(parentUsername, amount, MAX_DEPTH);
    }

    private void updateParentOfParentAccountBalanceRecursive(String parentUsername, double amount, int depth) {
        if (parentUsername != null && depth > 0) {
            try {
                User parentAccount = userRepository.findByUsername(parentUsername);
                if (parentAccount == null) {
                    throw new EntityNotFoundException("Parent user not found: " + parentUsername);
                }
                parentAccount.setAccountBalance(parentAccount.getAccountBalance() + amount);
                userRepository.save(parentAccount);
                logger.info("Updated balance for parent of parent: {}", parentAccount.getUsername());
                updateParentOfParentAccountBalanceRecursive(parentAccount.getParent().getUsername(), amount, depth - 1);
            } catch (EntityNotFoundException ex) {
                logger.warn("No more parents after {}", parentUsername);
            }
        }
    }

    private void updateParentAccountBalance(String parentUsername, double amount) {
        if (parentUsername != null) {
            User parentAccount = getUserAccountByUsername(parentUsername);
            parentAccount.setAccountBalance(parentAccount.getAccountBalance() + amount);
            userRepository.save(parentAccount);
        }
    }

    private String getParentUsername(String username) {
        User userAccount = getUserAccountByUsername(username);
        return userAccount.getParent() != null ? userAccount.getParent().getUsername() : null;
    }

    private User getUserAccountByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("Account not found for username: " + username);
        }
        return user;
    }

    public List<WinnerDTO> getUsernameBySelectedNumbers(String selectedNumber, String slots) {
        updateWinnerBalance(selectedNumber, slots);

        LocalDateTime startTime = LocalDateTime.now().minusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);
        String formattedEndTime = endTime.format(formatter);

        logger.info("Start Time: {}", formattedStartTime);
        logger.info("End Time: {}", formattedEndTime);

        return stockRepository.findUsernamesBySelectedNumber(selectedNumber, startTime, endTime, slots);
    }

    @Transactional
    public void updateWinnerBalance(String selectedNumber, String slots) {
        LocalDateTime startTime = LocalDateTime.now().minusDays(1).withHour(16).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = LocalDateTime.now();

        List<WinnerDTO> winnerList = stockRepository.findUsernamesBySelectedNumber(selectedNumber, startTime, endTime, slots);
        for (WinnerDTO winnerDTO : winnerList) {
            User user = userRepository.findByUsername(winnerDTO.getUsername());
            if (user == null) {
                logger.warn("User not found: {}", winnerDTO.getUsername());
                continue;
            }

            double currentBalance = user.getAccountBalance();
            double betAmount = winnerDTO.getTotalInvested();
            double updatedBalance = currentBalance;

            if (Objects.equals(slots, "Slot-1")) {
                updatedBalance += betAmount * 80;
            } else if (Objects.equals(slots, "Slot-2")) {
                updatedBalance += betAmount * 60;
            }

            user.setAccountBalance(updatedBalance);
            logger.info("Updated balance for user {}: {}", user.getUsername(), updatedBalance);
            userRepository.save(user);
        }
    }

    public List<List<String>> getLatestRecords(String username, String slot) {
        logger.info("Inside getLatestRecords service method");
        Pageable pageable = PageRequest.of(0, 10);
        return stockRepository.findLatestBet(pageable, username, slot).getContent().stream()
                .map(bet -> List.of(
                        "ID: " + bet.getId(),
                        "Stocks: " + bet.getSelected_stocks(),
                        "Amount: " + bet.getAmount()
                ))
                .collect(Collectors.toList());
    }
}
