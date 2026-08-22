package gh.edu.ug.dsaoptimizer;

import gh.edu.ug.dsaoptimizer.persistence.AlgorithmRunRepository;
import gh.edu.ug.dsaoptimizer.persistence.AuditEventRepository;
import gh.edu.ug.dsaoptimizer.persistence.Database;
import gh.edu.ug.dsaoptimizer.persistence.LocationRepository;
import gh.edu.ug.dsaoptimizer.persistence.ResourceRepository;
import gh.edu.ug.dsaoptimizer.persistence.RoadRepository;
import gh.edu.ug.dsaoptimizer.persistence.ServiceRequestRepository;
import gh.edu.ug.dsaoptimizer.ui.ConsoleMenu;

import java.nio.file.Path;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("Ghana Smart Campus Service Operations Optimizer");
        System.out.println("System setup is complete.");

        Path dbPath = Path.of("dsa_optimizer.db");
        try (Database db = Database.open(dbPath)) {
            db.applySchema(Path.of("database/schema.sql"));

            LocationRepository locationRepository = new LocationRepository(db.getConnection());
            RoadRepository roadRepository = new RoadRepository(db.getConnection());
            ResourceRepository resourceRepository = new ResourceRepository(db.getConnection());
            ServiceRequestRepository serviceRequestRepository = new ServiceRequestRepository(db.getConnection());
            AlgorithmRunRepository algorithmRunRepository = new AlgorithmRunRepository(db.getConnection());
            AuditEventRepository auditEventRepository = new AuditEventRepository(db.getConnection());

            if (locationRepository.findAll().isEmpty()) {
                System.out.println("Database is empty -- loading seed data from data/processed/...");
                locationRepository.loadFromCsv(Path.of("data/processed/locations.csv"));
                roadRepository.loadFromCsv(Path.of("data/processed/roads.csv"));
                resourceRepository.loadFromCsv(Path.of("data/processed/resources.csv"));
                serviceRequestRepository.loadFromCsv(Path.of("data/processed/service_requests.csv"));
                System.out.println("Seed data loaded.");
            }

            ConsoleMenu menu = new ConsoleMenu(new Scanner(System.in), locationRepository, roadRepository,
                    resourceRepository, serviceRequestRepository, algorithmRunRepository, auditEventRepository);
            menu.run();
        }
    }
}
