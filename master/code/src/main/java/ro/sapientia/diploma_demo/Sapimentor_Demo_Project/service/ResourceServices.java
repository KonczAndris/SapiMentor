package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceServices {
    private final ResourcesRepository resourcesRepository;

    public ResourceServices(ResourcesRepository resourcesRepository) {
        this.resourcesRepository = resourcesRepository;
    }

    public List<Resources> getAllResources() {
        return resourcesRepository.findAll();
    }

    public Map<String, Integer> getLikeAndDislikeCounts(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        likeAndDislikeCounts.put("like", resource.getLike());
        likeAndDislikeCounts.put("dislike", resource.getDislike());

        return likeAndDislikeCounts;
    }

    public void likeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Növeljük a like számot
        resource.setLike(resource.getLike() + 1);

        // Mentjük az új értéket az adatbázisba
        resourcesRepository.save(resource);
    }

    public void dislikeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Növeljük a dislike számot
        resource.setDislike(resource.getDislike() + 1);

        // Mentjük az új értéket az adatbázisba
        resourcesRepository.save(resource);
    }
}
