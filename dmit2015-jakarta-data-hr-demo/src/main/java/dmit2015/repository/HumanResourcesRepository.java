package dmit2015.repository;

import dmit2015.entity.Region;
import jakarta.data.repository.*;

import java.util.List;
import java.util.Optional;

@Repository
public interface HumanResourcesRepository {

    @Insert
    Region createRegion(Region newRegion);

    @Update
    Region updateRegion(Region updatedRegion);

    @Delete
    //void deleteRegion(Region existingRegion);
    void deleteRegionById(Long id);

    @Find
    Optional<Region> getRegionById(Long id);

    @Find
    List<Region> getAllRegions();

    @Find
    @OrderBy("regionName")
    List<Region> findAllRegionsOrderByRegionName();

    @Query("select r from Region r join fetch r.countries")
    List<Region> findAllRegionsWithCountries();
}
