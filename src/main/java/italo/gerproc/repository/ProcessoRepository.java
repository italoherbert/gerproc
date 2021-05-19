package italo.gerproc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.gerproc.model.Processo;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {

}
