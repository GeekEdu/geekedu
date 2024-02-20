package com.zch.ask.controller;

import com.zch.ask.service.ICommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final ICommentsService commentsService;

}
