package org.restbucks.wechat.bff.http.assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.restbucks.wechat.bff.http.WeChatUserRestController;
import org.restbucks.wechat.bff.http.resource.WeChatUserProfileResource;
import org.restbucks.wechat.bff.wechat.oauth.WeChatUserProfile;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class WeChatUserProfileResourceAssembler extends
    ResourceAssemblerSupport<WeChatUserProfile, WeChatUserProfileResource> {


    public WeChatUserProfileResourceAssembler() {
        super(WeChatUserRestController.class, WeChatUserProfileResource.class);
    }

    @Override
    public WeChatUserProfileResource toResource(WeChatUserProfile entity) {
        WeChatUserProfileResource resource = new ModelMapper()
            .map(entity, WeChatUserProfileResource.class);
        resource
            .add(
                linkTo(methodOn(WeChatUserRestController.class)
                    .me(entity.getOpenId())).withSelfRel());
        return resource;
    }
}
