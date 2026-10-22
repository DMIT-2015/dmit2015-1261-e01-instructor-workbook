package dmit2015.view;

import dmit2015.model.MapLocation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Getter;

import java.util.List;

@Named
@RequestScoped
public class MapLocationView {

    @Getter
    private List<MapLocation> locations = List.of(
         new MapLocation("West Edmonton Mall",53.5230503,-113.6253218),
         new MapLocation("Premium Outlet Collection Edmonton International Airport",53.3022299,-113.5559982),
         new MapLocation("Southgate Centre",53.4855032,-113.5167203)
    );
}