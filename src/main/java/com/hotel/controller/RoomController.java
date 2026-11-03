package com.hotel.controller;

import com.hotel.entity.Room;
import com.hotel.entity.RoomType;
import com.hotel.service.RoomService;
import com.hotel.service.RoomTypeService;
import com.hotel.vo.Result;
import com.hotel.vo.RoomStatusVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 房间控制器类，处理房间相关的HTTP请求
 * 提供房间列表查询、状态统计、详情获取、保存、删除等功能
 * 同时也提供房间类型的增删查功能
 */
@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;    // 房间服务，处理房间相关的业务逻辑
    private final RoomTypeService roomTypeService;  // 房间类型服务，处理房间类型相关的业务逻辑

    /**
     * 构造函数，通过依赖注入方式初始化服务
     * @param roomService 房间服务实例
     * @param roomTypeService 房间类型服务实例
     */
    public RoomController(RoomService roomService, RoomTypeService roomTypeService) {
        this.roomService = roomService;
        this.roomTypeService = roomTypeService;
    }

    /**
     * 获取房间列表
     * @return 返回房间列表的响应结果
     */
    @GetMapping("/list")
    public Result<List<Room>> list() {
        return Result.success(roomService.listRooms());
    }

    /**
     * 获取房间状态统计信息
     * @return 返回房间状态统计的响应结果
     */
    @GetMapping("/stats")
    public Result<List<RoomStatusVO>> stats() {
        return Result.success(roomService.getStatusStats());
    }

    /**
     * 根据ID获取房间详情
     * @param id 房间ID
     * @return 返回房间详情的响应结果
     */
    @GetMapping("/detail/{id}")
    public Result<Room> detail(@PathVariable Long id) {
        return Result.success(roomService.getRoomDetail(id));
    }

    /**
     * 保存或更新房间信息
     * @param room 房间对象
     * @return 返回操作结果的响应结果
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Room room) {
        return Result.success(roomService.saveOrUpdate(room));
    }

    /**
     * 根据ID删除房间
     * @param id 房间ID
     * @return 返回操作结果的响应结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(roomService.removeById(id));
    }

    /**
     * 更新房间状态
     * @param id 房间ID
     * @param status 新的状态值
     * @return 返回操作结果的响应结果
     */
    @PostMapping("/status/{id}/{status}")
    public Result<Boolean> status(@PathVariable Long id, @PathVariable Integer status) {
        Room room = roomService.getById(id);
        if (room == null) {
            return Result.error("房间不存在");
        }
        room.setStatus(status);
        return Result.success(roomService.updateById(room));
    }

    /**
     * 获取房间类型列表
     * @return 返回房间类型列表的响应结果
     */
    @GetMapping("/type/list")
    public Result<List<RoomType>> typeList() {
        return Result.success(roomTypeService.list());
    }

    /**
     * 保存或更新房间类型
     * @param roomType 房间类型对象
     * @return 返回操作结果的响应结果
     */
    @PostMapping("/type/save")
    public Result<Boolean> saveType(@RequestBody RoomType roomType) {
        return Result.success(roomTypeService.saveOrUpdate(roomType));
    }

    /**
     * 根据ID删除房间类型
     * @param id 房间类型ID
     * @return 返回操作结果的响应结果
     */
    @DeleteMapping("/type/delete/{id}")
    public Result<Boolean> deleteType(@PathVariable Long id) {
        return Result.success(roomTypeService.removeById(id));
    }
}
