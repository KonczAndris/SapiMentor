package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.serviceTest.ResourceServicesTests.Integration;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServicesIntegrationTest {

//    @Test
//    public void testLogin() {
//        Integer a = 1;
//        Integer b = 1;
//        assert(a.equals(b));
//    }


    // Mockoljuk a ResourcesRepository-t
    // igy a teszt soran nem a valodi adatbazist hasznaljuk
    // hanem a mockolt adatbazist
    // @Mock
    @Autowired
    private ResourcesRepository resourcesRepository;

//    @InjectMocks
    @Autowired
    private ResourceServices resourceServices;

    @Before
    public void setUp() {
        resourcesRepository.deleteAll();

        // Inicializálja a tesztadatokat az adatbázisban
        Resources resource1 = new Resources(
                "Google link",
                "https://www.google.com/?client=safari",
                "Informatics",
                "Igen Igen",
                2,
                1);
        Resources resource2 = new Resources(
                "Youtube link",
                "https://www.youtube.com/",
                "Informatics",
                "Igen Nem",
                4,
                2);
        Resources resource3 = new Resources(
                "Google link",
                "https://www.google.com/?client=safari",
                "Informatics",
                "Igen Igen",
                2,
                1);
        Resources resource4 = new Resources(
                "Google link",
                "https://www.google.com/?client=safari",
                "Informatics",
                "Igen Igen",
                2,
                1);

        resourcesRepository.save(resource1);
        resourcesRepository.save(resource2);
        resourcesRepository.save(resource3);
        resourcesRepository.save(resource4);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a getAllResources fuggvenyhez /////////////////////////////////////////////
    // 1.) Teszt
    @Test
    public void testGetAllResources() {
        // Given
        List<Resources> expectedResources = new ArrayList<>();
        Resources resource1 = new Resources(
                "Google link",
                "https://www.google.com/?client=safari",
                "Informatics",
                "Igen Igen",
                2,
                1);
        Resources resource2 = new Resources(
                "Youtube link",
                "https://www.youtube.com/",
                "Informatics",
                "Igen Nem",
                4,
                2);
        Resources resource3 = new Resources(
                "Youtube link",
                "https://www.youtube.com/",
                "Informatics",
                "Igen Nem",
                4,
                2);
        Resources resource4 = new Resources(
                "Youtube link",
                "https://www.youtube.com/",
                "Informatics",
                "Igen Nem",
                4,
                2);

        expectedResources.add(resource1);
        expectedResources.add(resource2);
        expectedResources.add(resource3);
        expectedResources.add(resource4);

        // When
        //when(resourcesRepository.findAll()).thenReturn(expectedResources);
        List<Resources> actualResources = resourceServices.getAllResources();
        for (Resources resource : actualResources) {
            System.out.println("Name:" + resource.getName()
            + " Id: " + resource.getId());
        }
        //System.out.println("actualResources: " + actualResources);

        // Then
        assertThat(actualResources.size()).isEqualTo(expectedResources.size());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a getLikeAndDislikeCounts fuggvenyhez /////////////////////////////////////////////
    // 2.) Teszt
    @Test
    public void testGetLikeAndDislikeCounts() {
        // Given
        Long resourcesID = 1L;

        // When
        Map<String, Integer> likeAndDislikeCounts = resourceServices.getLikeAndDislikeCounts(resourcesID);

        // Then
        assertThat(likeAndDislikeCounts.get("like")).isEqualTo(2);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a likeResource fuggvenyhez /////////////////////////////////////////////
    // 3.) Teszt
    @Test
    public void testLikeResource() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.likeResource(resourceId);
        Map<String, Integer> likeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);
        System.out.println("LikeStatus:" + likeResourceStatus);

        // Then
        assertThat(likeResourceStatus.get("like")).isEqualTo(3);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a dislikeResource fuggvenyhez /////////////////////////////////////////////
    // 4.) Teszt
    @Test
    public void testDislikeResource() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.dislikeResource(resourceId);
        Map<String, Integer> dislikeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);

        // Then
        assertThat(dislikeResourceStatus.get("dislike")).isEqualTo(2);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a revokeLike fuggvenyhez /////////////////////////////////////////////
    // 5.) Teszt
    @Test
    public void testRevokeLikeResource() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.revokeLike(resourceId);
        Map<String, Integer> revokeLikeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);

        // Then
        assertThat(revokeLikeResourceStatus.get("like")).isEqualTo(1);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a revokeDislike fuggvenyhez /////////////////////////////////////////////
    // 6.) Teszt
    @Test
    public void testRevokeDislikeResource() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.revokeDislike(resourceId);
        Map<String, Integer> revokeDislikeResourceStatus = resourceServices.getLikeAndDislikeCounts(resourceId);

        // Then
        assertThat(revokeDislikeResourceStatus.get("dislike")).isEqualTo(0);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a likeResourceAndRevokeDislike fuggvenyhez /////////////////////////////////////////////
    // 7.) Teszt
    @Test
    public void testLikeResourceAndRevokeDislike() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.likeResourceAndRevokeDislike(resourceId);
        Map<String, Integer> likeResourceAndRevokeDislikeStatus = resourceServices.getLikeAndDislikeCounts(resourceId);

        // Then
        assertThat(likeResourceAndRevokeDislikeStatus.get("like")).isEqualTo(3);
        assertThat(likeResourceAndRevokeDislikeStatus.get("dislike")).isEqualTo(0);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a dislikeResourceAndRevokeLike fuggvenyhez /////////////////////////////////////////////
    // 8.) Teszt
    @Test
    public void testDislikeResourceAndRevokeLike() {
        // Given
        Long resourceId = 1L;

        // When
        resourceServices.dislikeResourceAndRevokeLike(resourceId);
        Map<String, Integer> dislikeResourceAndRevokeLikeStatus = resourceServices.getLikeAndDislikeCounts(resourceId);

        // Then
        assertThat(dislikeResourceAndRevokeLikeStatus.get("like")).isEqualTo(1);
        assertThat(dislikeResourceAndRevokeLikeStatus.get("dislike")).isEqualTo(2);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a isLinkAccessible fuggvenyhez /////////////////////////////////////////////
    // 9.) Teszt
    @Test
    public void testIsLinkAccessible_True() {
        // Given
        String link = "https://www.google.com";

        // When
        boolean isLinkAccessible = resourceServices.isLinkAccessible(link);

        // Then
        assertThat(isLinkAccessible).isEqualTo(true);
    }

    // 10.) Teszt
    @Test
    public void testIsLinkAccessible_False() {
        // Given
        String link = "https://www.goasfsgfsafogle.com";

        // When
        boolean isLinkAccessible = resourceServices.isLinkAccessible(link);

        // Then
        assertThat(isLinkAccessible).isEqualTo(false);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a containsMaliciousContent fuggvenyhez /////////////////////////////////////////////
    // 11.) Teszt
    @Test
    public void testContainsmaliciousContent_False() {
        // Given
        String url = "https://www.google.com";

        // When
        boolean containsMaliciousContent = resourceServices.containsMaliciousContent(url);

        // Then
        assertThat(containsMaliciousContent).isEqualTo(false);
    }

    // 12.) Teszt
    @Test
    public void testContainsmaliciousContent_True() {
        // Given
        String url = "https://www.online-filmek.ac";

        // When
        boolean containsMaliciousContent = resourceServices.containsMaliciousContent(url);

        // Then
        assertThat(containsMaliciousContent).isEqualTo(true);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a isURLSafe fuggvenyhez /////////////////////////////////////////////
    // 13.) Teszt
    @Test
    public void testIsURLSafe_True() {
        // Given
        String url = "https://www.google.com";

        // When
        boolean isURLSafe = resourceServices.isURLSafe(url);

        // Then
        assertThat(isURLSafe).isEqualTo(true);
    }

    // 14.) Teszt
    @Test
    public void testIsURLSafe_False() {
        // Given
        String url = "https://www.online-filmek.ac";

        // When
        boolean isURLSafe = resourceServices.isURLSafe(url);

        // Then
        assertThat(isURLSafe).isEqualTo(false);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// Tesztek a processAndSaveResources fuggvenyhez /////////////////////////////////////////////
    // 15.) Teszt
    @Test
    public void testProcessAndSaveResources_Success() {
        // Given
        String name = "Google link";
        String link = "https://www.google.com";
        String topic_name = "Informatics";
        String user_name = "";
        Integer like = 2;
        Integer dislike = 1;
        Resources[] resourcesDataItems = { new Resources(name, link,
                topic_name, user_name,  like, dislike)};

        String fullUserNameTestData = "Szotyori Csongor";

        // When
        String result = resourceServices.processAndSaveResources(resourcesDataItems, fullUserNameTestData);

        // Then
        assertThat(result).isEqualTo("Success");
    }

    // 16.) Teszt
    @Test
    public void testProcessAndSaveResources_IvalidLink() {
        // Given
        String name = "Google link";
        String link = "https://www.gooasbfdjhasjhfle.asd";
        String topic_name = "Informatics";
        String user_name = "";
        Integer like = 2;
        Integer dislike = 1;
        Resources[] resourcesDataItems = { new Resources(name, link,
                topic_name, user_name,  like, dislike)};

        String fullUserNameTestData = "Szotyori Csongor";

        // When
        String result = resourceServices.processAndSaveResources(resourcesDataItems, fullUserNameTestData);

        // Then
        assertThat(result).isEqualTo("InvalidLink");
    }

    // 17.) Teszt
    @Test
    public void testProcessAndSaveResources_NotSafe() {
        // Given
        String name = "Google link";
        String link = "https://www.online-filmek.ac";
        String topic_name = "Informatics";
        String user_name = "";
        Integer like = 2;
        Integer dislike = 1;
        Resources[] resourcesDataItems = { new Resources(name, link,
                topic_name, user_name,  like, dislike)};

        String fullUserNameTestData = "Szotyori Csongor";

        // When
        String result = resourceServices.processAndSaveResources(resourcesDataItems, fullUserNameTestData);

        // Then
        assertThat(result).isEqualTo("NotSafe");
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

}
