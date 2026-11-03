package com.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/room")
    public String room() {
        return "room";
    }

    @GetMapping("/room-type")
    public String roomType() {
        return "room-type";
    }

    @GetMapping("/booking")
    public String booking() {
        return "booking";
    }

    @GetMapping("/checkin")
    public String checkin() {
        return "checkin";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @GetMapping("/bill")
    public String bill() {
        return "bill";
    }

    @GetMapping("/customer")
    public String customer() {
        return "customer";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }
}
