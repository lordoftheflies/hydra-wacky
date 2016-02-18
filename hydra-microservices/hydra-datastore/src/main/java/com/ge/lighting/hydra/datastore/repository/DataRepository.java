/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.datastore.repository;

import com.ge.lighting.hydra.datastore.entity.DataKey;
import com.ge.lighting.hydra.datastore.entity.DataPointEntity;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for observing and saving data. CRUD repository for the RDBMS
 * implementation of the data-store
 *
 * @author Hegedűs László (212429780)
 */
public interface DataRepository extends PagingAndSortingRepository<DataPointEntity, DataKey> {

    /**
     * Select last values fors an asset.
     *
     * @param boxId Asset id.
     * @param keys Selected sensor data keys.
     * @return Segmented sensor data.
     */
    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType IN (:keys) ORDER BY e.id.ts DESC")
    List<DataPointEntity> findLastValues(@Param("boxId") UUID boxId, @Param("keys") List<String> keys);
    
    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType IN (:keys) ORDER BY e.id.ts DESC")
    Page<DataPointEntity> findSet(@Param("boxId") UUID boxId, @Param("keys") List<String> keys, Pageable pageable);

    /**
     * Select data volume for time-series.
     *
     * @param boxId Asset id.
     * @param key Selected sensor data key.
     * @param pageable Object for segment data.
     * @return Segmented sensor data for series.
     */
    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType = :key ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findSerie(@Param("boxId") UUID boxId, @Param("key") String key, Pageable pageable);

    /**
     * Select data volume for time-series.
     *
     * @param boxId Asset id.
     * @param keys Selected sensor keys.
     * @param ts Begin of the data volume.
     * @param te End of the data volume.
     * @param pageable Object fir segment data.
     * @return Segmented sensor data for series.
     */
    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType IN (:keys) AND :ts <= e.id.ts AND e.id.ts < :te ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findSerie(@Param("boxId") UUID boxId, @Param("keys") List<String> keys, @Param("ts") Date ts, @Param("te") Date te, Pageable pageable);

    /**
     * Select data volume for time-series.
     *
     * @param boxId Asset id.
     * @param key Selected sensor keys.
     * @param ts Begin of the data volume.
     * @param te End of the data volume.
     * @param pageable Object fir segment data.
     * @return Segmented sensor data for series.
     */
    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType = :key AND :ts <= e.id.ts AND e.id.ts < :te ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findSerie(@Param("boxId") UUID boxId, @Param("key") String key, @Param("ts") Date ts, @Param("te") Date te, Pageable pageable);

    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType = :key AND :ts <= e.id.ts ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findSerieFromBegin(@Param("boxId") UUID fromString, @Param("key") String key, @Param("ts") Date ts, Pageable pageable);

    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.asset = :boxId AND e.id.xType = :key AND e.id.ts < :te ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findSerieToEnd(@Param("boxId") UUID fromString, @Param("key") String key, @Param("te") Date te, Pageable pageable);

    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.xType = :key ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findVolume(@Param("key") String key, Pageable pageable);

    @Query(value = "SELECT e FROM DataPointEntity e WHERE e.id.xType = :key AND :ts <= e.id.ts AND e.id.ts < :te ORDER BY e.id.ts ASC")
    Page<DataPointEntity> findVolume(@Param("key") String key, @Param("ts") Date ts, @Param("te") Date te, Pageable pageable);

    @Query(value = "SELECT AVG(e.val) FROM DataPointEntity e "
            + "WHERE e.id.asset = :asset AND e.id.xType = :key AND :ts <= e.id.ts AND e.id.ts < :te "
            + "GROUP BY e.id.asset "
            + "ORDER BY e.id.ts ASC")
    double avgOfVolume(@Param("asset") UUID asset, @Param("key") String key, @Param("ts") Date ts, @Param("te") Date te);
}
