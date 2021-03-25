package com.company.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * 如果具有add的权限就能使用add的方法功能
     * @return
     */
    @PreAuthorize("hasAuthority('add.do')")
    @RequestMapping("/add")
    public String add(){
        return "add";
    }


    @PreAuthorize("hasAuthority('delete.do')")
    @RequestMapping("/delete")
    public String delete(){
        return "delete";
    }

    @PreAuthorize("hasAuthority('query.do')")
    @RequestMapping("/query")
    public String query(){
        return "delete";
    }



    @PreAuthorize("hasAuthority('edit.do')")
    @RequestMapping("/edit")
    public String edit(){
        return "edit";
    }
}
