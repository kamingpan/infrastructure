package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.constant.RedisConstant;
import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.base.service.StringRedisService;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis controller
 *
 * @author kamingpan
 * @since 2019-04-24
 */
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController {

    @Autowired
    private StringRedisService redisService;

    /**
     * redis查询
     *
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(RedisConstant.Authentication.LIST)
    public ResponseData list() {
        log.debug("查询redis列表...");

        return ResponseData.build("");
    }

    /**
     * redis查询指定键的值
     *
     * @param key redis键
     * @return 响应数据
     */
    @GetMapping("/{key}")
    @PreAuthorize(RedisConstant.Authentication.GET)
    public ResponseData info(@PathVariable("key") final String key) {
        log.debug("查询redis“{}”的值...", key);

        if (null == key || key.isEmpty()) {
            return ResponseData.build(ResponseStatus.VALIDATE_ERROR);
        }
        return ResponseData.build(this.redisService.get(key));
    }

    /**
     * redis新增
     *
     * @param key   键
     * @param value 值
     * @return 响应数据
     */
    @PostMapping("/{key}")
    @PreAuthorize(RedisConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("key") String key, @RequestParam("value") String value) {
        log.debug("更新redis“{key: {}, value: {}}”...", key, value);

        this.redisService.set(key, value);
        return ResponseData.success();
    }

    /**
     * redis删除
     *
     * @param key 键
     * @return 响应数据
     */
    @DeleteMapping("/{key}")
    @PreAuthorize(RedisConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("key") String key) {
        log.debug("删除redis“{}”...", key);

        this.redisService.delete(key);
        return ResponseData.success();
    }

}
