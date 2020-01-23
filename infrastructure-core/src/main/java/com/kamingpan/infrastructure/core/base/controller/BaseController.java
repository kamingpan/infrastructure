package com.kamingpan.infrastructure.core.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller基础类
 *
 * @author kamingpan
 * @since 2016-03-1
 */
public class BaseController {

    protected final ObjectMapper objectMapper = new ObjectMapper();
}
