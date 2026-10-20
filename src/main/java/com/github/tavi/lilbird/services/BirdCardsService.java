package com.github.tavi.lilbird.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.exception.NotFoundException;
import com.github.tavi.lilbird.models.dto.BirdCardDto;
import com.github.tavi.lilbird.models.entities.Bird;
import com.github.tavi.lilbird.models.entities.BirdName;
import com.github.tavi.lilbird.repositories.BirdNamesRepo;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.util.NameUtils;

import jakarta.validation.constraints.NotNull;


/**
 * Creates and reads bird cards.
 * A card's bird row stores only the latin name; every other name is a {@link BirdName}.
 */
@Service
@Validated
public class BirdCardsService {

    @Autowired
    private BirdsRepo birdsRepo;
    @Autowired
    private BirdNamesRepo namesRepo;


    /**
     * Stores a new bird and the names from the card.
     * The bird row receives only {@link BirdCardDto#getNameLatin()}.
     */
    @Transactional
    public Bird save(@NotNull BirdCardDto card) {
        Bird bird = birdsRepo.save(new Bird(card.getNameLatin()));

        for (String nameValue : card.getAlternativeNames()) {
            BirdName nameEntity = new BirdName(nameValue);
            nameEntity.setEntry(bird);
            nameEntity.setLatin(false);
            namesRepo.save(nameEntity);
        }

        return bird;
    }

    /** Creates a new bird row or updates the existing one. */
    public Bird save(@NotNull Bird bird) {
        return birdsRepo.save(bird);
    }

    /** Creates or updates one name row. */
    public BirdName save(@NotNull BirdName name) {
        return namesRepo.save(name);
    }


    /** Checks if a bird with this latin name exists. */
    public boolean birdExists(String latinName) {
        return birdsRepo.existsById(NameUtils.normalize(latinName));
    }

    /**
     * Finds the bird by its latin name.
     * The argument is normalized before the lookup.
     *
     * @throws NotFoundException    If no bird with this name is present.
     */
    public Bird get(String latinName) throws NotFoundException {
        return birdsRepo
                .findById(NameUtils.normalize(latinName))
                .orElseThrow(
                    () -> new NotFoundException(
                        "The bird entry does not exist: " + latinName
                    )
                );
    }


    /** Names stored for the bird with this latin name, in insertion order. */
    public List<BirdName> getNamesOf(@NotNull String latinName) {
        return namesRepo.findByReference(NameUtils.normalize(latinName));
    }

    /** Same as {@link #getNamesOf(String)} for a bird that is already loaded. */
    public List<BirdName> getNamesOf(@NotNull Bird entry) {
        return getNamesOf(entry.getNameNormal());
    }


    /** The same bird as {@link #get(String)}, assembled into a card. */
    public BirdCardDto getCard(String latinName) {
        Bird bird = get(latinName);
        BirdCardDto card = new BirdCardDto(bird.getNameLatin());
        List<String> alternativeNames = getNamesOf(bird)
            .stream()
            .filter(name -> !name.isLatin())
            .map(name -> name.getNameValue())
            .toList();
        card.setAlternativeNames(alternativeNames);
        return card;
    }


    /** Returns every bird row. */
    public List<Bird> getAllBirds() {
        return birdsRepo.findAll();
    }

}
