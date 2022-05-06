package com.gnerv.examples.service;

import com.gnerv.plus.elasticsearch.annotation.ElasticsearchDB;
import org.springframework.stereotype.Service;

/**
 * @author ligen
 * @since 2022/4/18 09:39
 */
@Service
public class BookService {

    @ElasticsearchDB("es1")
    public void get(){

    }

    @ElasticsearchDB("es1")
    public void get(String name){

    }

}
