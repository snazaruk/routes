package de.nazaruk.routes.service.impl;

import de.nazaruk.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Component
public class BusRoutesConfiguration {

    /**
     * Represents the map of all bus routes.
     * Key - route Id
     * Value - set of all station Ids in this rute
     */
    private Map<Integer, Set<Integer>> busRoutes;

    @Autowired
    private FileSource fileSource;

    @PostConstruct
    void loadDirectRoutes() throws IOException {
        String fileName = fileSource.getFileName();

        Stream<String> stream = Files.lines(Paths.get(fileName));
        stream.forEach(e -> {
            if (busRoutes == null) {
                busRoutes = new HashMap<>(Integer.parseInt(e));
            } else {
                loadAvailableRoutes(e);
            }
        });
    }

    private void loadAvailableRoutes(String line) {
        String[] stringIds = line.split("\\s+");
        if (stringIds.length > 2) {
            int[] ids = Arrays.stream(stringIds).mapToInt(Integer::parseInt).toArray();
            Set<Integer> stationIds = new HashSet<>();
            for (int i = 1; i < ids.length; i++) {
                stationIds.add(ids[i]);
            }
            busRoutes.put(ids[0], stationIds);
        }
    }

    public Map<Integer, Set<Integer>> getBusRoutes() {
        return busRoutes;
    }

}
