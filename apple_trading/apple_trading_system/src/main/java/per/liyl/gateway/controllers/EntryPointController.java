package per.liyl.gateway.controllers;

import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.liyl.enums.ApplicationExceptionEnum;
import per.liyl.gateway.request.AppleOrderReq;
import per.liyl.gateway.response.AppleOrderResp;
import per.liyl.gateway.response.Response;
import per.liyl.service.AppleService;

@RestController
@RequestMapping("/api")
public class EntryPointController {

    private static final Logger logger = LoggerFactory.getLogger(EntryPointController.class);

    @Autowired
    private AppleService appleService;

    @GetMapping("/myError")
    public String error(){
        throw ApplicationExceptionEnum.SYSTEM_ERROR.exception();
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello Liyl!";
    }

    @GetMapping("/orders")
    public AppleOrderResp findAll(AppleOrderReq req){
        logger.info("EntryPointController 收到 /orders 请求，请求参数为：【{}】", JSON.toJSONString(req));
        AppleOrderResp result = appleService.findAll(req);
        logger.info("EntryPointController 响应 /orders 请求，返回参数为：【{}】", JSON.toJSONString(result));
        return result;

    }

    @PostMapping("/add")
    public Response add(@RequestBody AppleOrderReq req){
        logger.info("EntryPointController 收到 /add 请求，请求参数为：【{}】", JSON.toJSONString(req));
        Response result = appleService.add(req);
        logger.info("EntryPointController 响应 /add 请求，返回参数为：【{}】", JSON.toJSONString(result));
        return result;
    }

    @PostMapping("/update")
    public Response update(@RequestBody AppleOrderReq req){
        logger.info("EntryPointController 收到 /update 请求，请求参数为：【{}】", JSON.toJSONString(req));
        Response result = appleService.update(req);
        logger.info("EntryPointController 响应 /update 请求，返回参数为：【{}】", JSON.toJSONString(result));
        return result;
    }

    @PostMapping("/delete")
    public Response delete(@RequestBody AppleOrderReq req){
        logger.info("EntryPointController 收到 /delete 请求，请求参数为：【{}】", JSON.toJSONString(req));
        Response result = appleService.delete(req);
        logger.info("EntryPointController 响应 /delete 请求，返回参数为：【{}】", JSON.toJSONString(result));
        return result;
    }
}
