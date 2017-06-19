package br.desafio.repos;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.desafio.model.Segmentation;

/**
 * Classe responsável por realizar as operação da segmentação com o banco de dados.
 */
@Repository
public interface SegmentationRepos extends CrudRepository<Segmentation, Serializable> {


}
