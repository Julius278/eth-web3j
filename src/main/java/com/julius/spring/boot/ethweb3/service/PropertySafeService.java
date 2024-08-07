package com.julius.spring.boot.ethweb3.service;

import com.julius.spring.boot.ethweb3.Property;
import com.julius.spring.boot.ethweb3.PropertySafe;
import com.julius.spring.boot.ethweb3.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.exceptions.ContractCallException;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S2629")
@Service
public class PropertySafeService {

    private static final Logger logger = LoggerFactory.getLogger(PropertySafeService.class);

    private final Web3j web3client;
    private final ContractGasProvider gasProvider;
    private final TransactionManager manager;
    private final PropertySafe propertySafe;

    public PropertySafeService(Web3j web3client, ContractGasProvider gasProvider, TransactionManager manager, PropertySafe propertySafe) {
        this.web3client = web3client;
        this.gasProvider = gasProvider;
		this.manager = manager;
		this.propertySafe = propertySafe;
    }

    /**
     *
     * @return contract value
     */
    public BigInteger deployPropertyToSafe(String externalPropertyId, String propertyName, int propertyValue) {
        try {
            logger.info("deployPropertyToSafe for id: {}", externalPropertyId);
            Property property = Property.deploy(web3client, manager, gasProvider, propertyName, BigInteger.valueOf(propertyValue)).send();
            property.setPropertyId(externalPropertyId).send();
            logger.info("contractAddress of deployed property: {}", property.getContractAddress());
            propertySafe.addProperty(externalPropertyId, property.getContractAddress()).send();

            BigInteger value = property.value().send();
            logger.info("value of deployed property: {}", value);

            return value;
        } catch (Exception e) {
            logger.error("failed to deploy Property contract");
            throw new ContractCallException("failed to deploy Property contract, " + e);
        }
    }


    /**
     *
     * @return contract value
     */
    public String deployPropertySafe() {
        try {
            PropertySafe deployedPropertySafe = PropertySafe.deploy(web3client, manager, gasProvider).send();

            return deployedPropertySafe.getContractAddress();
        } catch (Exception e) {
            logger.error("failed to deploy PropertySafe contract");
            throw new ContractCallException("failed to deploy Property contract, " + e);
        }
    }

    public List<PropertyModel> listPropertySafe() {
        try {
            List<PropertyModel> properties = new ArrayList<>();
            List<String> propertyList = propertySafe.getProperties().send();
            for(String addr : propertyList){
                Property p = Property.load(addr, web3client, manager, gasProvider);

                String id = p.getPropertyId().send();
                String name = p.getName().send();
                BigInteger value = p.getValue().send();

                PropertyModel pm = new PropertyModel();

                pm.setId(id);
                pm.setName(name);
                pm.setValue(value);

                properties.add(pm);
            }
            return properties;
        } catch (Exception e) {
            logger.error("failed to get list of Properties in PropertySafe contract");
            throw new ContractCallException("failed to deploy Property contract, " + e);
        }
    }
}
