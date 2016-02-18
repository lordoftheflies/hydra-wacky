package com.ge.lighting.hydra.asset.model.repository;

import org.springframework.stereotype.Repository;

import com.ge.lighting.hydra.asset.model.entity.MetricEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface MetricEntityRepository extends
		PagingAndSortingRepository<MetricEntity, String> {
	/**
	 * Find by id
	 *
	 * @param id
	 *            Reference to the hospital’s id
	 * @return HospitalEntity object
	 */
	@Override
	MetricEntity findOne(String id);

	@Override
	Iterable<MetricEntity> findAll();
}
