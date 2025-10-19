package per.liyl.test;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import per.liyl.database.entities.AppleOrder;
import per.liyl.database.repositories.AppleRepository;
import per.liyl.enums.AppleUnitEnum;
import per.liyl.gateway.request.AppleOrderReq;
import per.liyl.gateway.response.AppleOrderResp;
import per.liyl.gateway.response.Response;
import per.liyl.service.AppleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class AppleServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AppleServiceTest.class);

    @Mock
    private AppleRepository appleRepository;

    @InjectMocks
    private AppleService appleService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this); // 初始化Mockito注解

        // 初始化Guava Cache
        Cache<Long, AppleOrder> appleOrderCache = CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();

        // 注入到AppleService中
        appleService.setAppleOrderCache(appleOrderCache);
    }

    @Test
    public void findAll_Exists(){
        logger.info("测试【{}】方法，开始！", "findAll_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("orderId")
        );
        List<AppleOrder> contents = new ArrayList<>();
        contents.add(new AppleOrder(1L));
        contents.add(new AppleOrder(2L));
        contents.add(new AppleOrder(3L));
        contents.add(new AppleOrder(4L));
        contents.add(new AppleOrder(5L));
        contents.add(new AppleOrder(6L));
        contents.add(new AppleOrder(7L));
        Page<AppleOrder> appleOrders = new PageImpl<>(contents, pageable, 7);

        // 2. 定义方法
        Mockito.when(appleRepository.findAll(pageable)).thenReturn(appleOrders);

        //3. 执行
        AppleOrderResp appleOrderResp = appleService.findAll(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "findAll_Exists", JSON.toJSONString(appleOrderResp));

        // 4. 检查
        Assertions.assertThat(appleOrderResp).isNotNull();
        Assertions.assertThat(appleOrderResp.getContents()).isNotNull();
        Assertions.assertThat(appleOrderResp.getContents().size()).isEqualTo(7);
        logger.info("测试【{}】方法，结束！", "findAll_Exists");
    }

    @Test
    public void findAll_Not_Exists(){
        logger.info("测试【{}】方法，开始！", "findAll_Not_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("orderId")
        );
        List<AppleOrder> contents = new ArrayList<>();
        Page<AppleOrder> appleOrders = new PageImpl<>(contents, pageable, 0);

        // 2. 定义方法
        Mockito.when(appleRepository.findAll(pageable)).thenReturn(appleOrders);

        //3. 执行
        AppleOrderResp appleOrderResp = appleService.findAll(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "findAll_Not_Exists", JSON.toJSONString(appleOrderResp));

        // 4. 检查
        Assertions.assertThat(appleOrderResp).isNotNull();
        Assertions.assertThat(appleOrderResp.getContents().size()).isEqualTo(0);
        logger.info("测试【{}】方法，结束！", "findAll_Not_Exists");
    }

    @Test
    public void findById_Test(){
        logger.info("测试【{}】方法，开始！", "findById_Test");
        // 1. 准备数据
        Long orderId = 1L;

        // 2. 定义方法
        Mockito.when(appleRepository.findById(orderId)).thenReturn(Optional.of(new AppleOrder(1L)));

        //3. 执行
        AppleOrder appleOrder = appleService.findById(orderId);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "findById_Test", JSON.toJSONString(appleOrder));

        // 4. 检查
        Assertions.assertThat(appleOrder).isNotNull();
        Assertions.assertThat(appleOrder.getOrderId()).isEqualTo(orderId);
        logger.info("测试【{}】方法，结束！", "findById_Test");
    }

    @Test
    public void add_Not_Exists(){
        logger.info("测试【{}】方法，开始！", "add_Not_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();
        appleOrderReq.setOrderId(1L);
        appleOrderReq.setOrderName("test1");
        appleOrderReq.setQuantity(2);
        appleOrderReq.setUnit(AppleUnitEnum.JIN.getValue());
        appleOrderReq.setUnitPrice(6.03);

        AppleOrder appleOrder = new AppleOrder(1L);

        // 2. 定义方法
        Mockito.when(appleRepository.save(Mockito.any(AppleOrder.class))).thenReturn(appleOrder);

        //3. 执行
        Response response = appleService.add(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "add_Not_Exists", JSON.toJSONString(response));

        // 4. 检查
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.isSuccess()).isEqualTo(Boolean.TRUE.booleanValue());
        logger.info("测试【{}】方法，结束！", "add_Not_Exists");
    }

    @Test
    public void update_Exists(){
        logger.info("测试【{}】方法，开始！", "update_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();
        appleOrderReq.setOrderId(20251018120303L);
        appleOrderReq.setOrderName("test——UUU");
        appleOrderReq.setQuantity(2);
        appleOrderReq.setUnit(AppleUnitEnum.JIN.getValue());
        appleOrderReq.setUnitPrice(6.03);

        AppleOrder appleOrder = new AppleOrder(1L);

        // 2. 定义方法
        Mockito.when(appleRepository.save(Mockito.any(AppleOrder.class))).thenReturn(appleOrder);
        Mockito.when(appleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appleOrder));

        //3. 执行
        Response response = appleService.update(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "update_Exists", JSON.toJSONString(response));

        // 4. 检查
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.isSuccess()).isEqualTo(Boolean.TRUE.booleanValue());
        logger.info("测试【{}】方法，结束！", "update_Exists");
    }

    @Test
    public void update_Not_Exists(){
        logger.info("测试【{}】方法，开始！", "update_Not_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();
        appleOrderReq.setOrderId(20251018120303L);
        appleOrderReq.setOrderName("test——UUU");
        appleOrderReq.setQuantity(2);
        appleOrderReq.setUnit(AppleUnitEnum.JIN.getValue());
        appleOrderReq.setUnitPrice(6.03);

        AppleOrder appleOrder = new AppleOrder(1L);

        // 2. 定义方法
        Mockito.when(appleRepository.save(Mockito.any(AppleOrder.class))).thenReturn(appleOrder);
        Mockito.when(appleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        //3. 执行
        Response response = appleService.update(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "update_Not_Exists", JSON.toJSONString(response));

        // 4. 检查
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.isSuccess()).isEqualTo(Boolean.TRUE.booleanValue());
        logger.info("测试【{}】方法，结束！", "update_Not_Exists");
    }

    @Test
    public void delete_Exists(){
        logger.info("测试【{}】方法，开始！", "delete_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();
        appleOrderReq.setOrderId(20251018120303L);
        appleOrderReq.setOrderName("test——UUU");
        appleOrderReq.setQuantity(2);
        appleOrderReq.setUnit(AppleUnitEnum.JIN.getValue());
        appleOrderReq.setUnitPrice(6.03);

        AppleOrder appleOrder = new AppleOrder(1L);

        // 2. 定义方法
        Mockito.when(appleRepository.save(Mockito.any(AppleOrder.class))).thenReturn(appleOrder);
        Mockito.when(appleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(appleOrder));

        //3. 执行
        Response response = appleService.update(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "delete_Exists", JSON.toJSONString(response));

        // 4. 检查
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.isSuccess()).isEqualTo(Boolean.TRUE.booleanValue());
        logger.info("测试【{}】方法，结束！", "delete_Exists");
    }

    @Test
    public void delete_Not_Exists(){
        logger.info("测试【{}】方法，开始！", "delete_Not_Exists");
        // 1. 准备数据
        AppleOrderReq appleOrderReq = new AppleOrderReq();
        appleOrderReq.setOrderId(20251018120303L);
        appleOrderReq.setOrderName("test——UUU");
        appleOrderReq.setQuantity(2);
        appleOrderReq.setUnit(AppleUnitEnum.JIN.getValue());
        appleOrderReq.setUnitPrice(6.03);

        AppleOrder appleOrder = new AppleOrder(1L);

        // 2. 定义方法
        Mockito.when(appleRepository.save(Mockito.any(AppleOrder.class))).thenReturn(appleOrder);
        Mockito.when(appleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        //3. 执行
        Response response = appleService.update(appleOrderReq);
        logger.info("测试【{}】方法，获取到数据结果：【{}】！",
                "delete_Not_Exists", JSON.toJSONString(response));

        // 4. 检查
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.isSuccess()).isEqualTo(Boolean.TRUE.booleanValue());
        logger.info("测试【{}】方法，结束！", "delete_Not_Exists");
    }

}
