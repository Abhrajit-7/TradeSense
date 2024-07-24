package com.funWithStocks.FunWithStocks.controller;

import com.funWithStocks.FunWithStocks.dto.ConfirmationRequest;
import com.funWithStocks.FunWithStocks.dto.FetchTransactionsDTO;
import com.funWithStocks.FunWithStocks.entity.Transaction;
import com.funWithStocks.FunWithStocks.repository.TransactionsRepository;
import com.funWithStocks.FunWithStocks.services.TransactionService;
import com.funWithStocks.FunWithStocks.util.TransStatus;
import com.funWithStocks.FunWithStocks.dto.TxnDto;
import com.funWithStocks.FunWithStocks.dto.WithdrawDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransController {

    Logger logger= LoggerFactory.getLogger(TransController.class);
    @Autowired
    private TransactionService transactionService;

    @Autowired
    TransactionsRepository transactionsRepository;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TxnDto txnDto) {
        logger.info("Request JSON : {}", txnDto);
        Transaction transaction = transactionService.depositByUsername(txnDto.getUsername(), txnDto.getAmount());
        String paymentPageUrl = transactionService.getRazorpayPaymentPageUrl(transaction);
        return ResponseEntity.ok(paymentPageUrl);
    }
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload, @RequestHeader("X-Razorpay-Signature") String razorpaySignature) {
        logger.info("Inside webhook controller");
        try {
            logger.info("Inside try block");
            //String payloadJson = new ObjectMapper().writeValueAsString(payload);
            logger.info("Webhook payload: {}", payload);
            logger.info("Webhook key Validation successful");
            //String event = (String) payload.get("event");
            //Map<String, Object> payloadData = (Map<String, Object>) payload.get("payload");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode event = objectMapper.readTree(payload);

            String eventType = event.get("event").asText();
            if ("payment.captured".equals(eventType)) {
                logger.info("Inside validation statement for payment capture");
                //String orderId = (String) ((Map<String, Object>) payloadData.get("order")).get("id");
                String orderId = event.get("payload").get("payment").get("entity").get("order_id").asText();
                logger.info("Webhook orderID: {}", orderId);
                //String paymentId = (String) ((Map<String, Object>) payloadData.get("payment")).get("id");
                String paymentId = event.get("payload").get("payment").get("entity").get("id").asText();
                logger.info("Webhook payment ID: {}", paymentId);
                transactionService.handlePaymentSuccess(orderId, paymentId);
                //Optional<Transaction> transaction=transactionsRepository.findByOrderId(orderId);
                Transaction transaction = transactionsRepository.findByOrderId(orderId)
                        .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + orderId));
                String username = transaction.getUser().getUsername();
                double amount = transaction.getAmount();
                logger.info("Amount received is :{}", amount);
                if (transaction.getOrderId() != null && transaction.getTransStatus() == TransStatus.SUCCESS) {
                    try {
                        transactionService.updateTransactionStatus(orderId, username, amount);
                }catch(Exception ex){
                    throw new IllegalStateException("Already deposit is successful and balance is updated");
                }
            }


                } else if ("payment.failed".equals(eventType)) {
                    String orderId = event.get("payload").get("payment").get("entity").get("order_id").asText();
                    //String orderId = (String) ((Map<String, Object>) payloadData.get("order")).get("id");
                    transactionService.handlePaymentFailure(orderId);
                }
                return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/withdrawPage")
    public  WithdrawDto withdraw(@RequestBody TxnDto txnDto){
        return transactionService.withdrawByUsername(txnDto.getUsername(),txnDto.getAmount());
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<?> confirmWithdraw(@RequestBody ConfirmationRequest confirmationRequest){
        transactionService.withdrawalConfirmation(confirmationRequest.getTrans_id(),confirmationRequest.getAmount());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/withdrawals")
    public List<WithdrawDto> withdrawals(){
        return transactionService.getWithdrawalRequests();
    }
    @GetMapping("/{username}/fetchTransactions")
    public ResponseEntity<List<FetchTransactionsDTO>> getTxnByUsername(@PathVariable String username){
        List<FetchTransactionsDTO> transactions=transactionService.getTxnByUsername(username);
        return ResponseEntity.ok(transactions);
    }

}
