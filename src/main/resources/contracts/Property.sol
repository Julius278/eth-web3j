// SPDX-License-Identifier: UNLICENSED
pragma solidity ^0.8.0;

import {IProperty} from "./IProperty.sol";

contract Property is IProperty {

    string internal id;
    string internal name;
    int public value;

    constructor(string memory _name, int _value){
        name = _name;
        value = _value;
    }

    function setValue(int _value) public{
        value = _value;
    }

    function getValue() public view returns (int) {
        return value;
    }

    function setPropertyId(string memory _propertyId) public{
        id = _propertyId;
    }

    function getPropertyId() public view returns (string memory){
        return id;
    }

    function setName(string memory _name) public{
        name = _name;
    }

    function getName() public view returns (string memory){
        return name;
    }

}
