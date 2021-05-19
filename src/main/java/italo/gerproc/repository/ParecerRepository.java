package italo.gerproc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.gerproc.model.Parecer;

public interface ParecerRepository extends JpaRepository<Parecer, Long> {

}
