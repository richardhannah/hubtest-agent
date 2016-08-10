package com.richard.services;

import com.richard.models.batchprocess.OFItem;

/**
 * Created by richard on 10/08/2016.
 */
public class ItemService {

    public OFItem retrieveItem(){
        OFItem ofItem = new OFItem();
        ofItem.setItemDescription("487:EVG2610");
        ofItem.setItemNo("HUBIO10005");
        ofItem.setLineNo(1);
        ofItem.setQuantity("1");
        return ofItem;

    }


}
