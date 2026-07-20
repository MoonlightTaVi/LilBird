package com.github.tavi.lilbird.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.tavi.lilbird.models.entities.BirdEntity;


/** The base repository for birds (as unique entries) in the database. */
public interface BirdsRepo extends JpaRepository<BirdEntity,String> {

}
