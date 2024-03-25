package com.zch.oss.controller;

import com.zch.api.vo.resources.AttachVO;
import com.zch.common.mvc.result.Response;
import com.zch.oss.service.IAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@RestController
@RequestMapping("/api/attach")
@RequiredArgsConstructor
public class AttachController {

    private final IAttachService attachService;

    /**
     * 获取课程附件列表
     * @param courseId
     * @return
     */
    @GetMapping("/list")
    public Response<List<AttachVO>> queryAttachList(@RequestParam("id") Integer courseId) {
        return Response.success(attachService.queryAttachList(courseId));
    }

    /**
     * 上传附件
     * @param file
     * @param name
     * @param courseId
     * @return
     */
    @PostMapping("/upload")
    public Response<Boolean> uploadAttach(@RequestParam("file")MultipartFile file, @RequestParam("name") String name,
                                          @RequestParam("courseId") Integer courseId) {
        return Response.success(attachService.uploadAttach(file, name, courseId));
    }

    /**
     * 下载文件
     * @param id
     * @param response
     * @return
     */
    @GetMapping("/{id}/download/{token}")
    public Response<Void> downloadAttach(@PathVariable("id") Integer id,
                                         @PathVariable("token") String token,
                                         HttpServletResponse response) {
        attachService.downloadAttach(id, token, response);
        return Response.success();
    }

    /**
     * 删除附件
     * @param id
     * @return
     */
    @PostMapping("/{id}/delete")
    public Response<Boolean> deleteAttach(@PathVariable("id") Integer id) {
        return Response.success(attachService.deleteAttach(id));
    }

}
