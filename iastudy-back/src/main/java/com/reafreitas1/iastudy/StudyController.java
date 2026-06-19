package com.reafreitas1.iastudy;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study")
@CrossOrigin(origins = "http://localhost:4200") 
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @PostMapping("/analyze")
    public StudyResponse analyze(@RequestBody StudyRequest request) {
        return studyService.processText(request.text(), request.quantity());
    }

    // http://localhost:8080/api/study/test
    @GetMapping("/test")
    public String test() {
        return "The backend is working!";
    }
}

record StudyRequest(String text, int quantity) {}