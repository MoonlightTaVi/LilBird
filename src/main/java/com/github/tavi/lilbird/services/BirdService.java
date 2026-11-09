package com.github.tavi.lilbird.services;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.github.tavi.lilbird.api.models.BirdCard;
import com.github.tavi.lilbird.repositories.BirdNamesRepo;
import com.github.tavi.lilbird.repositories.BirdsRepo;
import com.github.tavi.lilbird.repositories.PrimaryNamesRepo;
import com.github.tavi.lilbird.repositories.models.BirdEntity;
import com.github.tavi.lilbird.repositories.models.NameEntity;
import com.github.tavi.lilbird.repositories.models.PrimaryNameEntity;
import com.github.tavi.lilbird.util.NameUtils;
import com.github.tavi.lilbird.util.exceptions.NotFoundException;

import jakarta.validation.constraints.NotNull;


/**
 * Creates and reads bird cards.
 * A card's bird row stores only the latin name; every other name is a {@link NameEntity}.
 */
@Service
@Validated
public class BirdService {

    @Autowired
    private BirdsRepo birdsRepo;
    @Autowired
    private BirdNamesRepo namesRepo;
    @Autowired
    private PrimaryNamesRepo primaryNamesRepo;


    /** 
     * Decomposes the card into a {@link BirdEntity} 
     * and its {@link NameEntity} instances, then saves them separately.
     */
    @Transactional
    public BirdEntity saveCard(@NotNull BirdCard card) {
        Locale locale = card.getLocale();
        BirdEntity birdEntity = getBirdOrCreate(card.getNameLatin());

        String primaryName = card.getNamePrimary();
        if (primaryName != null)
            primaryNamesRepo.save(new PrimaryNameEntity(birdEntity, locale, primaryName));
        for (String nameStr : card.getAlternativeNames())
            namesRepo.save(new NameEntity(nameStr, locale, birdEntity));

        return birdEntity;
    }

    /** The same bird as {@link #getBird(String)}, but assembled into a card. */
    public BirdCard getCard(String nameLatin) throws NotFoundException {
        Locale locale = Locale.ENGLISH; // TODO Using EN locale for now
        BirdEntity birdEntity = getBird(nameLatin);
        BirdCard birdCard = new BirdCard(birdEntity);

        PrimaryNameEntity primaryName = primaryNamesRepo
            .getOpt(
                birdEntity.getId(), 
                locale
            )
            .orElse(null);
        if (primaryName != null)
            birdCard.setNamePrimary(primaryName.getNameValue());
        for (NameEntity nameEntity : getNamesOf(nameLatin, locale))
            birdCard.addAltName(nameEntity);

        return birdCard;
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically). 
     * @throws NotFoundException    If no bird with this name is present.
     */
    public BirdEntity getBird(String nameLatin) throws NotFoundException {
        return birdsRepo.getByName(NameUtils.normalize(nameLatin));
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically).
     * @return  The {@link BirdEntity} with this name; 
     *          creates a new entity if not present. 
     */
    public BirdEntity getBirdOrCreate(String nameLatin) {
        return birdsRepo.save(new BirdEntity(nameLatin));
    }

    /** 
     * @param nameLatin             The primary latin name (normalized automatically).
     * @return {@link Optional} of the bird, if present; 
     *          {@link Optional#empty()} otherwise. 
     */
    public Optional<BirdEntity> getBirdSilently(String nameLatin) {
        return birdsRepo.findByName(NameUtils.normalize(nameLatin));
    }


    /** Names stored for the bird with this latin name, in insertion order. */
    public List<NameEntity> getNamesOf(@NotNull String latinName, @NotNull Locale locale) {
        return namesRepo.findNamesByPrimary(NameUtils.normalize(latinName), locale);
    }

    /** Same as {@link #getNamesOf(String)} for a bird that is already loaded. */
    public List<NameEntity> getNamesOf(@NotNull BirdEntity entry, @NotNull Locale locale) {
        return namesRepo.findNamesByReference(entry.getId(), locale);
    }

}
