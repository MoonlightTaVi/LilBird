package com.github.tavi.lilbird.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.BirdEntity;
import com.github.tavi.lilbird.models.entities.NameEntity;
import com.github.tavi.lilbird.repositories.BirdNamesRepo;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.validation.constraints.NotNull;


/**
 * Creates and reads bird cards.
 * A card's bird row stores only the latin name; every other name is a {@link NameEntity}.
 */
@Service
@Validated
public class BirdCardsService {

    @Autowired
    private BirdsRepo birdsRepo;
    @Autowired
    private BirdNamesRepo namesRepo;


    /** Creates a new bird row or updates the existing one. */
    public BirdEntity save(@NotNull BirdEntity bird) {
        return birdsRepo.save(bird);
    }

    /** Creates or updates one name row. */
    public NameEntity save(@NotNull NameEntity name) {
        return namesRepo.save(name);
    }

    /**
     * Stores a new bird and the names from the card.
     * The bird row receives only {@link BirdCardDto#getNameLatin()}.
     */
    @Transactional
    public BirdEntity save(@NotNull BirdCardDto card) {
        BirdEntity bird = new BirdEntity(card.getNameLatin());
        bird = birdsRepo.save(bird);

        for (String nameValue : card.getAlternativeNames()) {
            // TODO Using RU locale currently
            NameEntity nameEntity = new NameEntity(nameValue, "ru");
            nameEntity.setReference(bird);
            namesRepo.save(nameEntity);
        }

        return bird;
    }


    /** Checks if a bird with this latin name exists. */
    public boolean checkBirdExists(String nameLatin) {
        return !birdsRepo
                .findByName(NameUtils.normalize(nameLatin))
                .isEmpty();
    }

    /**
     * Finds the bird by its latin name.
     * The argument is normalized before the lookup.
     *
     * @throws NotFoundException    If no bird with this name is present.
     */
    public BirdEntity getBirdByName(String nameLatin) throws NotFoundException {
        return birdsRepo
                .findByName(NameUtils.normalize(nameLatin))
                .orElseThrow(
                    () -> new NotFoundException(
                        "The bird entry does not exist: " + nameLatin
                    )
                );
    }

    /** The same bird as {@link #getBirdByName(String)}, assembled into a card. */
    public BirdCardDto getCardByName(String nameLatin) {
        BirdEntity entity = getBirdByName(nameLatin);
        BirdCardDto card = new BirdCardDto(entity.getNameLatin());

        List<String> alternativeNames = new ArrayList<>();
        for (NameEntity nameEnt : getNamesOf(nameLatin))
            alternativeNames.add(nameEnt.getNameDisplayed());
        card.setAlternativeNames(alternativeNames);

        return card;
    }


    /** Names stored for the bird with this latin name, in insertion order. */
    public List<NameEntity> getNamesOf(@NotNull String latinName) {
        return namesRepo.findNamesByPrimary(NameUtils.normalize(latinName));
    }

    /** Same as {@link #getNamesOf(String)} for a bird that is already loaded. */
    public List<NameEntity> getNamesOf(@NotNull BirdEntity entry) {
        return namesRepo.findNamesByReference(entry.getId());
    }


    /** Returns every bird row. */
    public List<BirdEntity> getAllBirds() {
        return birdsRepo.findAll();
    }

}
