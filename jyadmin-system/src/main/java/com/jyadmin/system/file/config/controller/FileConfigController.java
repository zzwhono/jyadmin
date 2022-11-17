package com.jyadmin.system.file.config.controller;

import cn.hutool.core.bean.BeanUtil;
import com.jyadmin.domain.Result;
import com.jyadmin.log.annotation.Log;
import com.jyadmin.system.config.detail.domain.ConfigDetail;
import com.jyadmin.system.datadict.domain.SimpleDataDict;
import com.jyadmin.system.datadict.model.vo.SimpleDataDictCreateVO;
import com.jyadmin.system.datadict.model.vo.SimpleDataDictUpdateVO;
import com.jyadmin.system.file.config.domain.FileConfig;
import com.jyadmin.system.file.config.model.vo.FileConfigUpdateVO;
import com.jyadmin.system.file.config.service.FileConfigService;
import com.jyadmin.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author LGX_TvT <br>
 * @version 1.0 <br>
 * Create by 2022-11-16 23:02 <br>
 * @description: FileConfigController <br>
 */
@Slf4j
@Api(value = "系统附件配置", tags = {"系统：系统附件配置接口"})
@RequestMapping("/api/file-config")
@RestController
public class FileConfigController {

    private static final String FILE_CONFIG_ID = "1";

    @Resource
    private FileConfigService fileConfigService;

    @Log(title = "系统附件配置：更新通用数据字典", desc = "更新通用数据字典")
    @ApiOperation(value = "更新通用数据字典", notes = "")
    @PutMapping("/update")
    @PreAuthorize("@jy.check('file-config:update')")
    public Result<Object> doUpdate(@RequestBody @Valid FileConfigUpdateVO vo) {
        FileConfig fileConfig = fileConfigService.getById(FILE_CONFIG_ID);
        BeanUtil.copyProperties(vo, fileConfig);
        return ResultUtil.toResult(fileConfigService.updateById(fileConfig));
    }

    @ApiOperation(value = "查找系统附件配置", notes = "")
    @GetMapping("/query")
    @PreAuthorize("@jy.check('file-config:query')")
    public Result<Object> doQueryById() {
        return Result.ok(fileConfigService.getById(FILE_CONFIG_ID));
    }

    @ApiOperation(value = "查找系统模板配置列表", notes = "")
    @GetMapping("/query-config/{code}")
    @PreAuthorize("@jy.check('file-config:query-config')")
    public Result<List<ConfigDetail>> doQueryConfigListByCode(@PathVariable("code") String code) {
        List<ConfigDetail> configDetails = this.fileConfigService.getConfigListByCode(code);
        return Result.ok(configDetails);
    }
}
