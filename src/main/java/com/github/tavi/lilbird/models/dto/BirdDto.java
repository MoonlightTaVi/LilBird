package com.github.tavi.lilbird.models.dto;

import com.github.tavi.lilbird.models.Bird;

import lombok.Data;


/** A basic bird entry for data transfering. */
@Data
@Deprecated
public class BirdDto implements Bird {

    private String nameLatin;
    private String nameMain;

}
