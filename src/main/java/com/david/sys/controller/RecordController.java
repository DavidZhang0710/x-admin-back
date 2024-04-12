package com.david.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.david.common.vo.Result;
import com.david.sys.entity.Record;
import com.david.sys.entity.User;
import com.david.sys.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author david
 * @since 2024-04-12
 */
@RestController
@RequestMapping("/sys/record")
public class RecordController {
    @Autowired
    IRecordService recordService;

    @PostMapping
    public Result<?> addRecord(@RequestBody Record record) {
        recordService.save(record);
        return Result.success("Add new record success!!!");
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> getRecordList(@RequestParam(value = "id", required = false) String id,
                                                   @RequestParam(value = "userId", required = false) String userId,
                                                   @RequestParam(value = "fileName", required = false) String fileName,
                                                   @RequestParam(value = "pageNo") Long pageNo,
                                                   @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Record> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(id), Record::getId, id);
        wrapper.eq(StringUtils.hasLength(userId), Record::getUserId, userId);
        wrapper.eq(StringUtils.hasLength(fileName), Record::getFileName, fileName);

        Page<Record> page = new Page<>(pageNo, pageSize);
        recordService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteRecord(@PathVariable("id") Integer id) {
        recordService.removeById(id);
        return Result.success("Delete success!!!");
    }
}
