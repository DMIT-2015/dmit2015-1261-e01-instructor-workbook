package dmit2015.repository;

import dmit2015.config.ApplicationConfig;
import dmit2015.entity.Region;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import net.datafaker.Faker;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Transactional(TransactionMode.ROLLBACK)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ArquillianTest
public class RegionJpaServiceIT { // The class must be declared as public

    static Faker faker = new Faker();

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("net.datafaker:datafaker").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.h2database:h2").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc17").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.postgresql:postgresql").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.mysql:mysql-connector-j").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("org.mariadb.jdbc:mariadb-java-client").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson").withTransitivity().asFile())
                .addPackages(true,
                        "dmit2015.config",
                        "dmit2015.entity",
                        "dmit2015.repository"
                )
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private HumanResourcesRepository regionService;

    @Test
    @Order(1)
    void shouldInjectRegionJpaService() {
        assertThat(regionService).isNotNull();
    }

    @Order(2)
    @Test
    void givenNewRegion_whenAddRegion_thenRegionIsAdded() {
        // Arrange
        Region newRegion = new Region();
        newRegion.setId(60L);
        newRegion.setRegionName("New Alberta");

        // Act
        regionService.createRegion(newRegion);

        // Assert
        Optional<Region> createdRegion = regionService.getRegionById(newRegion.getId());
        assertThat(createdRegion.isPresent()).isTrue();

    }

    @Order(3)
    @Test
    void givenExistingId_whenFindById_thenReturnEntity() {
        // Arrange
        final Long existingRegionId = 30L;

        // Act
        Optional<Region> optionalRegion = regionService.getRegionById(existingRegionId);

        // Assert
        assertThat(optionalRegion.isPresent())
                .isTrue();
        Region foundRegion = optionalRegion.orElseThrow();
        assertThat(foundRegion.getRegionName()).isEqualTo("Asia");

    }

    @Transactional(TransactionMode.DISABLED)
    @Order(4)
    @Test
    void givenExistingEntity_whenUpdatedRegion_thenRegionIsUpdated() {
        // Arrange
        final Long existingRegionId = 20L;
        Optional<Region> optionalRegion = regionService.getRegionById(existingRegionId);
        Region existingRegion = optionalRegion.orElseThrow();

        // Act
        existingRegion.setRegionName("Canadas");
        Region updatedRegion = regionService.updateRegion(existingRegion);

        // Assert
        Optional<Region> optionalUpdatedRegion = regionService.getRegionById(updatedRegion.getId());
        assertThat(optionalUpdatedRegion.isPresent())
                .isTrue();
        var existingUpdatedRegion = optionalRegion.orElseThrow();
        assertThat(existingUpdatedRegion.getRegionName()).isEqualTo("Canadas");

    }

    @Order(5)
    @Test
    void givenExistingId_whenDeleteRegion_thenRegionIsDeleted() {
        Region newRegion = new Region();
        newRegion.setId(60L);
        newRegion.setRegionName("New Alberta");

        // Act
        regionService.createRegion(newRegion);
        // Act
        regionService.deleteRegionById(newRegion.getId());
        // Assert
        Optional<Region> optionalRegion = regionService.getRegionById(newRegion.getId());
        assertThat(optionalRegion.isPresent())
                .isFalse();

    }

    @Order(6)
//    @ParameterizedTest
//    @CsvSource({"10"})
    @Test
    void givenMultipleEntity_whenFindAll_thenReturnEntityList() {
        // Arrange: Set up the initial state
        // Act: Perform the action to be tested
        List<Region> regionList = regionService.getAllRegions();

        // Assert: Verify the expected outcome
        assertThat(regionList.size())
                .isEqualTo(5);

    }

}