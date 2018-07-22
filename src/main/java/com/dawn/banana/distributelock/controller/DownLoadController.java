package com.dawn.banana.distributelock.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dawn on 2018/7/22.
 */
@Controller
public class DownLoadController {

    private ExecutorService pool = Executors.newFixedThreadPool(5);

    @GetMapping("events7")
    public ResponseEntity<ResponseBodyEmitter> handle7()throws IOException{

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        emitter.send("start");
        pool.execute(() ->{
            try {
                Thread.sleep((long)(3*64*1000));
                emitter.send("finish\r\n");
                emitter.complete();
            }catch (IOException | InterruptedException e){

            }

        });
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/octet-stream;charset=UTF-8");
        headers.set("Transfer-Encoding","chunked");
        headers.setContentDispositionFormData("attachment","test.csv" );
        return  new ResponseEntity<>(emitter,headers, HttpStatus.OK);
    }




}
