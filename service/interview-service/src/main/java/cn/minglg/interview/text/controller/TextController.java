package cn.minglg.interview.text.controller;

import cn.minglg.interview.text.service.TextService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:TextController
 * Package:cn.minglg.interview.text.controller
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/interview/text")
public class TextController {
    private final TextService textService;

    @GetMapping("/testFeignClient/{jobId}")
    public ResponseEntity<?> testFeignClient(@PathVariable("jobId") Long jobId) {
        return textService.testFeignClient(jobId);
    }
}
