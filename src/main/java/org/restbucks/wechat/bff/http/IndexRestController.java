package org.restbucks.wechat.bff.http;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexRestController {

    @RequestMapping(value = "/index", method = GET)
    public Resource index() {
        return new Resource<>("",
            linkTo(methodOn(IndexRestController.class).index()).withSelfRel());
    }

}
