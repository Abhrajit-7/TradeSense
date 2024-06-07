package com.arrow.Arrow.services;

import com.arrow.Arrow.dto.WinnerDTO;
import com.arrow.Arrow.entity.User;
import com.arrow.Arrow.repository.BetRepository;
import com.arrow.Arrow.repository.UserRepository;
import com.arrow.Arrow.dto.BetsDTO;
import com.arrow.Arrow.entity.Bet;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class BetService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BetRepository betRepository;
    Logger logger=LoggerFactory.getLogger(BetService.class);

    public List<String> submitBet(BetsDTO betsDTO, String username) {
        logger.info("Request JSON: {}", betsDTO);
        // Implementation
        User user = userRepository.findByUsername(username);
        double bet_amount = betsDTO.getBet_amount();
        Bet bet =new Bet();
        bet.setBet_number(betsDTO.getBet_number());
        bet.setUser(user);
        bet.setBet_amount(betsDTO.getBet_amount());
        bet.setSelected_numbers(betsDTO.getSelected_numbers());
        //Slot is added
        bet.setSlot(betsDTO.getSlot());
        double currentBal=user.getAccountBalance();
        logger.info("Updated bal "+ currentBal);
        // Updating balance subtracting the number by the selected amount
        double updatedBal=currentBal - bet_amount;
        logger.info("Updated bal "+ updatedBal);
        // Saving the updated balance in the account_details DB
        user.setAccountBalance(updatedBal);
        userRepository.save(user);
        Bet betSaved=betRepository.save(bet);

        String parentUsername=user.getParent().getUsername();
        logger.info("Parent user is: {} ", parentUsername);

        // Calculate commission percentage amounts
        double betAmount = betSaved.getBet_amount();
        // Take 4% from immediate child to parent
        double percentageToParent = 0.04;
        double percentageAmountToParent = betAmount * percentageToParent;
        // Take 2% from immediate child to parent of parent
        double percentageToParentOfParent = 0.02;
        double percentageAmountToParentOfParent = betAmount * percentageToParentOfParent;

        // Update parent account balance
        updateParentAccountBalance(parentUsername, percentageAmountToParent);

        // Update parent of parent account balance
        String parentOfParentUsername = getParentUsername(parentUsername);
        logger.info("Parent of parent: {}", parentOfParentUsername);
        if(parentOfParentUsername!=null) {
            // Update the parent of parent account balance recursively
            updateParentOfParentAccountBalance(parentOfParentUsername, percentageAmountToParentOfParent);
        }

        //Send back the BetID, Numbers and the corresponding amount
        List<String> responseList = List.of( "ID: " + bet.getBet_number() ,"Numbers: " + bet.getSelected_numbers().toString(), "Amount: " + bet.getBet_amount());
        logger.info("Response: {}" ,responseList);
        return responseList;
    }

    private static final int MAX_DEPTH = 2;
    private void updateParentOfParentAccountBalance(String parentUsername, double amount) {
        updateParentOfParentAccountBalanceRecursive(parentUsername, amount, MAX_DEPTH);
    }

    private void updateParentOfParentAccountBalanceRecursive(String parentUsername, double amount, int depth){
        if (parentUsername != null && depth > 0 ) {
            try {
                User parentAccount = userRepository.findByUsername(parentUsername);
                parentAccount.setAccountBalance(parentAccount.getAccountBalance()+amount);
                userRepository.save(parentAccount);
                logger.info("Saved for Parent of parent: {}", parentAccount.getUsername());
                updateParentOfParentAccountBalanceRecursive(parentAccount.getParent().getUsername(), amount, depth - 1);
            }catch (Exception ex){
                //throw new EntityNotFoundException("Parent user not found for :"+ parentUsername);
                logger.warn("No more parents after {}", parentUsername);
            }
        }
    }

    private void updateParentAccountBalance(String parentUsername, double amount) {
        if (parentUsername != null) {
            User parentAccount = getUserAccountByUsername(parentUsername);
            parentAccount.setAccountBalance(parentAccount.getAccountBalance()+amount);
            userRepository.save(parentAccount);
        }
    }

    private String getParentUsername(String username) {
        User userAccount = getUserAccountByUsername(username);
            if(userAccount.getParent()!=null) {
                return userAccount.getParent().getUsername();
            }
            return null;
    }

    private User getUserAccountByUsername (String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw  new EntityNotFoundException("Account not found for username: " + username);
        }
    }

    private void updateAccountBalance(User userAccount, double amount) {
        userAccount.setAccountBalance(userAccount.getAccountBalance()-amount);
        userRepository.save(userAccount);
    }

    public List<WinnerDTO> getUsernameBySelectedNumbers(String selectedNumber, String slots) {
        updateWinnerBalance(selectedNumber, slots);
        return betRepository.findUsernamesBySelectedNumber(selectedNumber, slots);
    }

    @Transactional
    public void updateWinnerBalance(String selectedNumber, String slots){
        List<WinnerDTO> winnerList=betRepository.findUsernamesBySelectedNumber(selectedNumber, slots);
        for(WinnerDTO winnerDTO: winnerList) {
            User user=userRepository.findByUsername(winnerDTO.getUsername());
            logger.info("User: {}",user.getUsername());

            double currentBalance = user.getAccountBalance();
            logger.info("Current Balance: {}",currentBalance);

            double betAmount = winnerDTO.getTotalInvested(); // Assuming this retrieves the total bet amount for the user
            logger.info("Total invested Balance: {}",betAmount);
            double updatedBalance = currentBalance + (betAmount * 80);
            logger.info("Updated Balance: {}",updatedBalance);

            user.setAccountBalance(updatedBalance);
            logger.debug("User account balannce is updated to : {}",updatedBalance);
            //user.setAccountBalance(winnerAmount);
            //Need to work on it
            //user.setAccountBalance(user.getAccountBalance()+winnerAmount);
            userRepository.save(user);
        }
    }
}
