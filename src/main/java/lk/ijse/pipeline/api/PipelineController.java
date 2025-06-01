package lk.ijse.pipeline.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * --------------------------------------------
 * Author: Shamodha Sahan
 * GitHub: https://github.com/shamodhas
 * Website: https://shamodha.com
 * --------------------------------------------
 * Created: 6/1/2025 8:15 AM
 * Project: ci-cd-k8s-pipeline
 * --------------------------------------------
 **/

@RestController
@RequestMapping("/api/v1/app")
public class PipelineController {

    @GetMapping("/pipeline")
    public String get() {
        return "Pipeline working";
    }

    @GetMapping("/test")
    public String getTest() {
        return "Pipeline test working";
    }
}
