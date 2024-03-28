package com.julius.spring.boot.ethweb3.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.crypto.exception.CipherException;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Getter
@Configuration
public class Web3Config {

    private static final Logger logger = LoggerFactory.getLogger(Web3Config.class);

    @Value("${eth.node.address}")
    private String ethServerAddress;

    @Value("${web3.keyfile}")
    private String keyFile;

    @Value("${web3.keyfile.password}")
    private String keyFilePassword;

    BigInteger GAS_LIMIT = BigInteger.valueOf(9_000_000);
    BigInteger GAS_PRICE = BigInteger.valueOf(4_100_000_000L);

    @Bean
    public ContractGasProvider staticGasProvider() {
        //return new StaticGasProvider(BigInteger.valueOf(1L), BigInteger.valueOf(60_000L));
        //return new StaticGasProvider(GAS_PRICE, GAS_LIMIT);
        return new DefaultGasProvider();
    }

    @Bean
    public Web3j web3jClient() {
        return Web3j.build(new HttpService(ethServerAddress));
    }

    @Bean
    public Credentials credentials() {
        Credentials credentials = null;
        try {
            File key = ResourceUtils.getFile(keyFile);
            logger.info("keyfile path: {}", key.getPath());
            credentials = WalletUtils.loadCredentials(keyFilePassword, key);
            logger.info("successfully created credentials, address: {}", credentials.getAddress());
        } catch (IOException | CipherException e) {
            logger.error("Failed to load credentials", e);
        }
        return credentials;
    }

}
