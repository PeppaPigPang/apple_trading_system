package per.liyl.service;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import per.liyl.database.entities.AppleOrder;
import per.liyl.database.repositories.AppleRepository;
import per.liyl.enums.AppleUnitEnum;
import per.liyl.enums.ApplicationExceptionEnum;
import per.liyl.gateway.dtos.AppleOrderDTO;
import per.liyl.gateway.request.AppleOrderReq;
import per.liyl.gateway.response.AppleOrderResp;
import per.liyl.gateway.response.Response;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppleService {

    private static final Logger logger = LoggerFactory.getLogger(AppleService.class);

    @Autowired
    public AppleRepository appleRepository;

    public Cache<Long, AppleOrder> appleOrderCache;

    @Autowired
    public void setAppleOrderCache(Cache<Long, AppleOrder> appleOrderCache){
        this.appleOrderCache = appleOrderCache;
    }

    /**
     * 分页查询所有的订单信息
     *
     * @param req
     * @return
     */
    public AppleOrderResp findAll(AppleOrderReq req){
        logger.info("查询所有的订单信息，查询开始！");
        int pageNum = req.getH2PageNum();
        int pageSize = req.getPageSize();
        Pageable pageable = PageRequest.of(
                pageNum,
                pageSize,
                Sort.by("orderId")
        );
        logger.info("查询所有的订单信息，分页信息为：pageNum=【{}】，pageSize=【{}】", pageNum, pageSize);
        Page<AppleOrder> appleOrders = appleRepository.findAll(pageable);
        logger.info("查询所有的订单信息，本次分页查询到数据量：【{}】", appleOrders.getContent().size());
        List<AppleOrderDTO> contents = new ArrayList<>();
        Optional.ofNullable(appleOrders)
                .filter((ordersPage) -> !ObjectUtils.isEmpty(ordersPage))
                .map((ordersPage) -> ordersPage.getContent())
                .filter((orders) -> !CollectionUtils.isEmpty(orders))
                .ifPresent((orders) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    orders.forEach((order) -> {
                        AppleOrderDTO appleOrderDTO = new AppleOrderDTO();
                        appleOrderDTO.setOrderId(order.getOrderId());
                        appleOrderDTO.setOrderName(order.getOrderName());
                        appleOrderDTO.setQuantity(order.getQuantity());
                        appleOrderDTO.setUnit(order.getUnit());
                        appleOrderDTO.setUnitPrice(order.getUnitPrice());
                        appleOrderDTO.setCreateTime(sdf.format(order.getCreateTime()));
                        appleOrderDTO.setUpdateTime(sdf.format(order.getUpdateTime()));
                        contents.add(appleOrderDTO);
                    });
                });
        AppleOrderResp appleOrderResp = new AppleOrderResp();
        appleOrderResp.setContents(contents);
        appleOrderResp.setTotalNum(appleOrders.getTotalElements());
        appleOrderResp.setPageNum(req.getPageNum());
        appleOrderResp.setPageSize(req.getPageSize());
        logger.info("查询所有的订单信息，查询结束！");
        return appleOrderResp;
    }

    /**
     * 根据 ID 查询订单信息
     *
     * @param id
     * @return
     */
    public AppleOrder findById(Long id){
        logger.info("查询订单号为：【{}】的信息，开始！", id);
        AppleOrder cachedOrder = null;
        // 读取缓存
        try {
            cachedOrder = appleOrderCache.get(id, () -> {
                // 缓存不存在时
                logger.info("查询订单号为：【{}】的信息，缓存不存在，开始查询数据库！", id);
                Optional<AppleOrder> appleRepositoryById = appleRepository.findById(id);
                return appleRepositoryById
                        .filter((o) -> !ObjectUtils.isEmpty(o))
                        .map((o) -> {
                            appleOrderCache.put(o.getOrderId(), o);
                            return o;
                        })
                        .orElse(null);
            });
        } catch (Exception e) {
            logger.error("查询订单号为：【{}】的信息，缓存查询报错！", id);
            logger.error("报错明细：", e);
        }
        logger.info("查询订单号为：【{}】的信息，获取到订单信息：【{}】！", id, JSON.toJSONString(cachedOrder));
        logger.info("查询订单号为：【{}】的信息，结束！", id);
        return cachedOrder;
    }

    /**
     * 新增订单
     *
     * @param req
     * @return
     */
    public Response add(AppleOrderReq req){
        logger.info("新增订单，开始！");
        String unit = req.getUnit();
        Optional.ofNullable(AppleUnitEnum.findKeyByValue(req.getUnit()))
                .filter((u) -> !ObjectUtils.isEmpty(u))
                .orElseThrow(() -> ApplicationExceptionEnum.ORDER_UNIT_ERROR.exception());

        // 查看数据是否已存在，已存在则报错
        Optional.ofNullable(req.getOrderId())
                .filter((id) -> !ObjectUtils.isEmpty(id))
                .ifPresent((id) -> {
                    if(!ObjectUtils.isEmpty(this.findById(id))){
                        throw ApplicationExceptionEnum.ORDER_EXIST_ERROR.exception();
                    }
                });

        Long orderId = Optional.ofNullable(req.getOrderId())
                .filter((id) -> !ObjectUtils.isEmpty(id))
                .orElseGet(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
                    return Long.valueOf(now.format(formatter));
                });
        AppleOrder appleOrder = new AppleOrder();
        appleOrder.setOrderId(orderId);
        appleOrder.setOrderName(req.getOrderName());
        appleOrder.setQuantity(req.getQuantity());
        appleOrder.setUnit(unit);
        appleOrder.setUnitPrice(req.getUnitPrice());
        appleOrder.setCreateTime(new Date());
        appleOrder.setUpdateTime(new Date());
        logger.info("新增订单，新增前的信息为：【{}】！", JSON.toJSONString(appleOrder));
        AppleOrder saved = appleRepository.save(appleOrder);
        Optional.ofNullable(saved)
                .filter((o) -> !ObjectUtils.isEmpty(o))
                .orElseThrow(() -> ApplicationExceptionEnum.ORDER_ADD_ERROR.exception());
        // 加入缓存
        appleOrderCache.put(orderId, saved);
        logger.info("新增订单，新增后的信息为：【{}】！", JSON.toJSONString(saved));
        logger.info("新增订单，结束！");
        return Response.success();
    }

    /**
     * 更新订单
     *
     * @param req
     * @return
     */
    public Response update(AppleOrderReq req){
        Long orderId =
                Optional.ofNullable(req.getOrderId())
                        .filter((id) -> !ObjectUtils.isEmpty(id))
                        .orElseThrow(() -> ApplicationExceptionEnum.ORDER_ID_ERROR.exception());
        logger.info("更新订单号：【{}】，开始！", orderId);
        String unit = req.getUnit();
        Optional.ofNullable(AppleUnitEnum.findKeyByValue(unit))
                .filter((u) -> !ObjectUtils.isEmpty(u))
                .orElseThrow(() -> ApplicationExceptionEnum.ORDER_UNIT_ERROR.exception());
        // 查看数据是否已存在，不存在则报错
        AppleOrder appleOrder = Optional.ofNullable(req.getOrderId())
                .filter((id) -> !ObjectUtils.isEmpty(id))
                .map((id) -> this.findById(id))
                .filter((o) -> !ObjectUtils.isEmpty(o))
                .orElseThrow(() ->ApplicationExceptionEnum.ORDER_NOT_EXIST_ERROR.exception());

        appleOrder.setOrderId(orderId);
        appleOrder.setOrderName(req.getOrderName());
        appleOrder.setQuantity(req.getQuantity());
        appleOrder.setUnit(unit);
        appleOrder.setUnitPrice(req.getUnitPrice());
        appleOrder.setUpdateTime(new Date());
        logger.info("更新订单号：【{}】，更新前的信息为：【{}】！", orderId, JSON.toJSONString(appleOrder));
        AppleOrder saved = appleRepository.save(appleOrder);
        Optional.ofNullable(saved)
                .filter((o) -> !ObjectUtils.isEmpty(o))
                .orElseThrow(() -> ApplicationExceptionEnum.ORDER_UPDATE_ERROR.exception());

        // 加入缓存
        appleOrderCache.put(orderId, saved);
        logger.info("更新订单号：【{}】，更新后的信息为：【{}】！", orderId, JSON.toJSONString(saved));
        logger.info("更新订单号：【{}】，结束！", orderId);
        return Response.success();
    }

    /**
     * 删除订单
     *
     * @param req
     * @return
     */
    public Response delete(AppleOrderReq req){
        Long orderId =
                Optional.ofNullable(req.getOrderId())
                        .filter((id) -> !ObjectUtils.isEmpty(id))
                        .orElseThrow(() -> ApplicationExceptionEnum.ORDER_ID_ERROR.exception());
        logger.info("删除订单号：【{}】，开始！", orderId);

        // 查看数据是否已存在，不存在则报错
        Optional.ofNullable(req.getOrderId())
                .filter((id) -> !ObjectUtils.isEmpty(id))
                .map((id) -> this.findById(id))
                .filter((o) -> !ObjectUtils.isEmpty(o))
                .orElseThrow(() ->ApplicationExceptionEnum.ORDER_NOT_EXIST_ERROR.exception());

        appleRepository.deleteById(orderId);
        // 删除缓存
        appleOrderCache.invalidate(orderId);
        logger.info("删除订单号：【{}】，结束！", orderId);
        return Response.success();
    }

}
