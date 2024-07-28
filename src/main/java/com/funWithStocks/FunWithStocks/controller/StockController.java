package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.ExceptionHandling.UserNotFoundException;
import com.funWithStocks.FunWithStocks.dto.WinnerDTO;
import com.funWithStocks.FunWithStocks.dto.StocksDTO;
import com.funWithStocks.FunWithStocks.repository.UserRepository;
import com.funWithStocks.FunWithStocks.services.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class StockController {

    private String messageStorageSlot1;
    private String messageStorageSlot2;
    Logger logger= LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(path = "/submit/slot1/{username}")
    public List<String> submitBetSlot1(@RequestBody StocksDTO stocksDTO, @PathVariable String username) {
        logger.info("Request JSON: {}", stocksDTO);
        // Delegate the request to the service layer
        return stockService.submitBet(stocksDTO, username);
    }
    @PostMapping("/submit/slot2/{username}")
    public List<String> submitBetSlot2(@RequestBody StocksDTO stocksDTO, @PathVariable String username) {
        logger.info("Request JSON: {}", stocksDTO);
        // Delegate the request to the service layer
        return stockService.submitBet(stocksDTO, username);
    }

    @PostMapping("/winner")
    //To be used by admin
    public List<WinnerDTO> getUsernameBySelectedNumbers(@RequestParam("winner") String number, @RequestParam("slot") String slots) {
        return stockService.getUsernameBySelectedNumbers(number,slots);
    }

    @GetMapping("/lists/{username}")
    public List<List<String>> getList(@PathVariable String username, @RequestParam("slot") String slot){
        logger.info("Inside getList controller method");
        if(userRepository.findByUsername(username)!=null) {
            logger.info("Username validated");
            return stockService.getLatestRecords(username, slot);
        }else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    // Mapped as /app/application
    @MessageMapping("/application")
    @SendTo("/all/messages")
    public String send(String message) throws Exception {
        messageStorageSlot1=message;
        return message;
    }
    @MessageMapping("/applicationSlot2")
    @SendTo("/all/slot2")
    public String sendForSlot2(String slot2Message) throws Exception{
        messageStorageSlot2=slot2Message;
        return slot2Message;
    }
    @GetMapping("/last-message/slot1")
    public String fetchSlot1(){
        return messageStorageSlot1;
    }
    @GetMapping("/last-message/slot2")
    public String fetchSlot2(){
        return messageStorageSlot2;
    }
}
