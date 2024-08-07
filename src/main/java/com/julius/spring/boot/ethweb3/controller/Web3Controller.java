package com.julius.spring.boot.ethweb3.controller;

import com.julius.spring.boot.ethweb3.model.TransferRequest;
import com.julius.spring.boot.ethweb3.service.Web3Service;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigInteger;
import java.util.List;

@RestController
public class Web3Controller {

    private static final Logger logger = LoggerFactory.getLogger(Web3Controller.class);
    private final Web3Service web3Service;

    public Web3Controller(Web3Service web3Service) {
        this.web3Service = web3Service;
    }

    @GetMapping(path = {"/api/eth/blockNumber", "/api/eth/BlockNumber"})
    public BigInteger getBlockNumber(){
        logger.info("called getBlockNumber");
        return web3Service.getEthBlockNumber().getBlockNumber();
    }

    @GetMapping(path = "/api/eth/accounts")
    public List<String> getAccounts(){
        logger.info("called getAccounts");
        return web3Service.getEthAccounts().getAccounts();
    }

    @GetMapping(path = "/api/eth/balance/{address}")
    public BigInteger getEthBalance(@PathVariable("address") String address){
        logger.info("called getEthBalance with address: {}", address);
        return web3Service.getEthBalance(address).getBalance();
    }

    @GetMapping(path = "/api/eth/trxCount/{address}")
    public BigInteger getEthTransactionCount(@PathVariable("address") String address){
        logger.info("called getEthTransactionCount with address: {}", address);
        return web3Service.getTransactionCount(address).getTransactionCount();
    }

    @PostMapping(path = "/api/eth/send")
    public String sendEth(@RequestBody @NotNull TransferRequest transferRequest){
        logger.info("called sendEth with: {}", transferRequest);
        return web3Service.sendTransaction(transferRequest);
    }
}
