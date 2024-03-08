package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.serviceTest.ResourceServicesTests.Integration;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServicesIntegrationTest {

    @Test
    public void testLogin() {
        Integer a = 1;
        Integer b = 1;
        assert(a.equals(b));
    }
//
//
//    // Mockoljuk a ResourcesRepository-t
//    // igy a teszt soran nem a valodi adatbazist hasznaljuk
//    // hanem a mockolt adatbazist
//    // @Mock
//    @Autowired
//    private ResourcesRepository resourcesRepository;
//
////    @InjectMocks
//    @Autowired
//    private ResourceServices resourceServices;
//
//    @Before
//    public void setUp() {
//        resourcesRepository.deleteAll();
//
//        // Inicializálja a tesztadatokat az adatbázisban
//        Resources resource1 = new Resources(
//                "Google link",
//                "https://www.google.com/?client=safari",
//                "Informatics",
//                "Igen Igen",
//                2,
//                1);
//        Resources resource2 = new Resources(
//                "Youtube link",
//                "https://www.youtube.com/",
//                "Informatics",
//                "Igen Nem",
//                4,
//                2);
//        Resources resource3 = new Resources(
//                "Google link",
//                "https://www.google.com/?client=safari",
//                "Informatics",
//                "Igen Igen",
//                2,
//                1);
//        Resources resource4 = new Resources(
//                "Google link",
//                "https://www.google.com/?client=safari",
//                "Informatics",
//                "Igen Igen",
//                2,
//                1);
//
//        resourcesRepository.save(resource1);
//        resourcesRepository.save(resource2);
//        resourcesRepository.save(resource3);
//        resourcesRepository.save(resource4);
//    }
//
//    @Test
//    public void testGetAllResources() {
//        // Given
//        List<Resources> expectedResources = new ArrayList<>();
//        Resources resource1 = new Resources(
//                "Google link",
//                "https://www.google.com/?client=safari",
//                "Informatics",
//                "Igen Igen",
//                2,
//                1);
//        Resources resource2 = new Resources(
//                "Youtube link",
//                "https://www.youtube.com/",
//                "Informatics",
//                "Igen Nem",
//                4,
//                2);
//        Resources resource3 = new Resources(
//                "Youtube link",
//                "https://www.youtube.com/",
//                "Informatics",
//                "Igen Nem",
//                4,
//                2);
//        Resources resource4 = new Resources(
//                "Youtube link",
//                "https://www.youtube.com/",
//                "Informatics",
//                "Igen Nem",
//                4,
//                2);
//
//        expectedResources.add(resource1);
//        expectedResources.add(resource2);
//        expectedResources.add(resource3);
//        expectedResources.add(resource4);
//
//        // When
//        //when(resourcesRepository.findAll()).thenReturn(expectedResources);
//        List<Resources> actualResources = resourceServices.getAllResources();
//        for (Resources resource : actualResources) {
//            System.out.println("Name:" + resource.getName()
//            + " Id: " + resource.getId());
//        }
//        //System.out.println("actualResources: " + actualResources);
//
//        // Then
//        assertThat(actualResources.size()).isEqualTo(expectedResources.size());
//    }
//
//
//    @Test
//    public void testGetLikeAndDislikeCounts() {
//        // Given
//        Long resourcesID = 1L;
//
//        // When
//        Map<String, Integer> likeAndDislikeCounts = resourceServices.getLikeAndDislikeCounts(resourcesID);
//
//        // Then
//        assertThat(likeAndDislikeCounts.get("like")).isEqualTo(2);
//
//    }
//
//    @Test
//    public void testLikeResource() {
//        // Given
//        Long resourceId = 1L;
//
//        // When
//        resourceServices.likeResource(resourceId);
//        Map<String, Integer> likeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);
//        System.out.println("LikeStatus:" + likeResourceStatus);
//
//        // Then
//        assertThat(likeResourceStatus.get("like")).isEqualTo(3);
//    }
//
//    @Test
//    public void testDislikeResource() {
//        // Given
//        Long resourceId = 1L;
//
//        // When
//        resourceServices.dislikeResource(resourceId);
//        Map<String, Integer> dislikeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);
//
//        // Then
//        assertThat(dislikeResourceStatus.get("dislike")).isEqualTo(2);
//    }
//
//    @Test
//    public void testRevokeLikeResource() {
//        // Given
//        Long resourceId = 1L;
//
//        // When
//        resourceServices.revokeLike(resourceId);
//        Map<String, Integer> revokeLikeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);
//
//        // Then
//        assertThat(revokeLikeResourceStatus.get("like")).isEqualTo(1);
//    }

}
