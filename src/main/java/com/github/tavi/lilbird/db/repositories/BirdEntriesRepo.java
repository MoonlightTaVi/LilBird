package com.github.tavi.lilbird.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.tavi.lilbird.db.entities.BirdEntryEntity;


/**
 * The base repository for birds (as unique entries) in the database.
 */
public interface BirdEntriesRepo extends JpaRepository<BirdEntryEntity, Long> {

}
