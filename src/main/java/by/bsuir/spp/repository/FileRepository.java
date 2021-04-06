package by.bsuir.spp.repository;

import by.bsuir.spp.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {
}
