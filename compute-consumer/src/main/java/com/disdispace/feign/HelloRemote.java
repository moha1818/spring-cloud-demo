package com.disdispace.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name= "compute-service")
public interface HelloRemote {
     @RequestMapping(value = "/hello")
     String hello(@RequestParam(value = "name") String name);

}

