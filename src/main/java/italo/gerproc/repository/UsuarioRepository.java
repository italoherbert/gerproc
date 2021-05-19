package italo.gerproc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import italo.gerproc.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Optional<Usuario> findByNomeUsuario( String nomeUsuario );
	
}
