package com.woola.woola.controller.restController;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @Autowired
    Private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
}
