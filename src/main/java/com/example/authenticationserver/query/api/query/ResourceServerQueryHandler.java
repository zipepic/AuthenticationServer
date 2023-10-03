package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.ResourceServerEntity;
import com.example.authenticationserver.query.api.data.ResourceServerRepository;
import com.example.authenticationserver.query.api.dto.ResourceServerDTO;
import com.project.core.queries.FetchResourceServersQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ResourceServerQueryHandler {
  private final ResourceServerRepository resourceServerRepository;
  @Autowired
  public ResourceServerQueryHandler(ResourceServerRepository resourceServerRepository) {
    this.resourceServerRepository = resourceServerRepository;
  }

  @QueryHandler
  public List on(FetchResourceServersQuery query){
    return resourceServerRepository.findAll().stream()
      .map(this::convertResourceServerToResourceServerDTO)
      .collect(Collectors.toList());
  }
  private ResourceServerDTO convertResourceServerToResourceServerDTO(ResourceServerEntity resourceServerEntity){
    ResourceServerDTO resourceServerDTO = new ResourceServerDTO();
    BeanUtils.copyProperties(resourceServerEntity,resourceServerDTO);
    return resourceServerDTO;
  }
}
