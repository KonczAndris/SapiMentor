package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;

import java.util.List;

@Service
public class ResourceServices {
    private final ResourcesRepository resourcesRepository;

    public ResourceServices(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    public List<Resources> getAllResources() {
        return resourcesRepository.findAll();
    }
}
