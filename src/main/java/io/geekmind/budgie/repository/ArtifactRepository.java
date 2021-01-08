package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Integer> {
}
