package com.puscas.authentication.model;

import lombok.Data;

@Data
public class UserPlaceScheduler {
    private String idOfSchedule; //year+month+day+hour
    private Long userIdentifier;
    private String placeIdentifier;
    private String timeCreated;
}
