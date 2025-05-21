package az.informatik.msparser.controller;

import az.informatik.msparser.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping("/news")
    public void getNews() {
        mainService.getNews();
    }

}
